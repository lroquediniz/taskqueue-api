package br.com.sysmap.taskqueue.socket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.sysmap.taskqueue.business.LoteProcessamentoService;
import br.com.sysmap.taskqueue.dto.Execucao;
/**
 * Web Socket para monitoramento de atividades em processamento.
 * @author Luan Roque.
 *
 */
@ServerEndpoint("/process")
public class ProcessWebSocket {

	private static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

	private static Set<Session> sessoes;

	@Inject
	private LoteProcessamentoService service;

	/**
	 * Recebe uma conexão de cliente criando um schedule 
	 * com tempo de 1 segundo para recuperar informações do processamento.
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session) {
		sessoes = session.getOpenSessions();
		if (sessoes.size() == 1) {
			timer.scheduleAtFixedRate(() -> enviarAtualizacoesProcessamentoParaClientes(session), 0, 1, TimeUnit.SECONDS);
		}
	}
	/**
	 * Envia atulização de um processamento para todos os cliente conectados.
	 * @param session
	 */
	private void enviarAtualizacoesProcessamentoParaClientes(Session session) {
		Execucao execucao = this.service.getExecucao();
		if (execucao != null) {
			Gson gson = new GsonBuilder().create();
			String execucaoStr = gson.toJson(execucao);
			sessoes = session.getOpenSessions();
			for (Session sess : sessoes) {
				try {
					sess.getBasicRemote().sendText(execucaoStr);
				} catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			}
		}
	}

	@OnMessage
	public String onMessage(String message) {
		return null;
	}

}
