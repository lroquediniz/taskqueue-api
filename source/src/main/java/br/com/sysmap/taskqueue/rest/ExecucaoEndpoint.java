package br.com.sysmap.taskqueue.rest;


import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.sysmap.taskqueue.business.LoteProcessamentoService;
import br.com.sysmap.taskqueue.message.MessageApplication;
import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.tipos.OperacaoTempo;

/**
 * Endpoind EJB Atividade. Endpoint inteligente para servico de cadastro de
 * atividades.
 */
@Path("/execucao")
public class ExecucaoEndpoint {

	@Inject
	private LoteProcessamentoService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById() {
		Response resposta = null;
		try {
			this.service.iniciarProcessamento();
			resposta = Response.ok().build();
		} catch (IllegalArgumentException e) {
			resposta = Response.status(Response.Status.NO_CONTENT).entity(new MessageApplication("msg.nenhuma.atividade.cadastrada")).build();
		}
		return resposta;
	}
	
	@GET
	@Path("/recuperaAtividadesPendentes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Atividade> recuperaAtividadesPendentes() {
		return service.recuperaListaAtividadePendentes();
	}
	
	@GET
	@Path("/acelerarTempo/{operacao}")
	public void acelerarTempo(@PathParam("operacao") OperacaoTempo operacao){
		service.atualizarTempo(operacao);
	}

}
