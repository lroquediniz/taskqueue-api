package br.com.sysmap.taskqueue.rest;


import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.sysmap.taskqueue.business.LoteProcessamentoService;
import br.com.sysmap.taskqueue.exception.NenhumaAtividadeException;
import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.tipos.OperacaoTempo;

/**
 * Endpoind Execuções de atividades.
 * @author Luan Roque.
 * 
 */
@Path("/execucao")
public class ExecucaoEndpoint {

	/**
	 * Servico de processamento injetado por CDI.
	 */
	@Inject
	private LoteProcessamentoService service;

	/**
	 * Recupera processamento por id;
	 * @return {@link Response}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response inicicarProcessamento() {
		Response resposta = null;
		try {
			this.service.iniciarProcessamento();
			resposta = Response.ok().build();
		} catch (NenhumaAtividadeException e) {
			resposta = Response.status(Response.Status.NO_CONTENT).build();
		}
		return resposta;
	}
	
	/**
	 * Recupera lista de atividades pendentes.
	 * @return List<{@link Atividade}> - atividades.
	 */
	@GET
	@Path("/recuperaAtividadesPendentes")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<Atividade> recuperaAtividadesPendentes() {
		return service.recuperaListaAtividadePendentes();
	}
	
	/**
	 * Recupera lista de atividades Processadas.
	 * @return List<{@link Atividade}> - atividades.
	 */
	@GET
	@Path("/recuperaAtividadesProcessadas")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<Atividade> recuperaAtividadesProcessadas() {
		return service.recuperaListaAtividadeProcessadas();
	}
	/**
	 * Altera o tempo de processamento das atividades.
	 * @param {@link OperacaoTempo} operacao
	 */
	@GET
	@Path("/alterarTempo/{operacao}")
	@PermitAll
	public void alterarTempo(@PathParam("operacao") OperacaoTempo operacao){
		service.atualizarTempo(operacao);
	}

}
