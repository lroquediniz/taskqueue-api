package br.com.sysmap.taskqueue.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import br.com.sysmap.taskqueue.ProcessMethod;
import br.com.sysmap.taskqueue.business.LoteProcessamentoService;
import br.com.sysmap.taskqueue.exception.ProcessamentoException;
import br.com.sysmap.taskqueue.message.MessageApplication;

/**
 * Filtro para requisicoes REST.
 * Valicao de processamento de lotes.
 * @author Luan Roque
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class LockFilter implements ContainerRequestFilter {

	/**
	 * Classe de Servico EJB 
	 * Injecao de dependencia CDI.
	 */
	@Inject
	private LoteProcessamentoService service;

	/**
	 * Methodos de requisição
	 */
	@Context
	private ResourceInfo resourceInfo;
	
	/**
	 * Resposta de requisicao.
	 */
	private static final Response CONFLICT;
	/**
	 * Inicializador de atributos estaticos.
	 */
	static {
		CONFLICT = Response.status(Response.Status.CONFLICT).entity(new MessageApplication("msg.lotes.em.processamento")).build();
	}
	/**
	 * Metodo do filtro.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Method method = resourceInfo.getResourceMethod();
		if (!method.isAnnotationPresent(ProcessMethod.class)) {
			try {
				this.service.validarLoteExecucao();
			} catch (ProcessamentoException e) {
				requestContext.abortWith(CONFLICT);
			}
		}
		
	}
}
