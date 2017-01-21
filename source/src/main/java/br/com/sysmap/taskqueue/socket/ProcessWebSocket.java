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

@ServerEndpoint("/process")
public class ProcessWebSocket {

	private static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

	private static Set<Session> sessoes;

	@Inject
	private LoteProcessamentoService service;


	@OnOpen
	public void onOpen(Session session) {
		sessoes = session.getOpenSessions();
		if (sessoes.size() == 1) {
			timer.scheduleAtFixedRate(() -> sendTimeToAll(session), 0, 1, TimeUnit.SECONDS);
		}
	}
	
	private void sendTimeToAll(Session session) {
		Execucao execucao = this.service.getExecucao();
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

	@OnMessage
	public String onMessage(String message) {
		return null;
	}

}
