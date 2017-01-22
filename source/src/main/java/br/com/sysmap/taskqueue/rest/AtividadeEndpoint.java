package br.com.sysmap.taskqueue.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.Atividade.ConstanteAtividade;
import br.com.sysmap.taskqueue.model.Pessoa;
import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;
import br.com.sysmap.taskqueue.util.Constantes;

/**
 * Endpoind EJB Atividade.
 * Endpoint inteligente para servico de cadastro de atividades.
 */
@Stateless
@Path("/atividades")
public class AtividadeEndpoint {
	
	/**
	 * Gerenciador de entidades JPA.
	 */
	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	/**
	 * Grava entidade Atividade.
	 * @param entity - {@link Atividade} a ser gravas
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@POST
	@Consumes("application/json")
	public Response create(Atividade entity) {
		calcularTempoExecucao(entity);
		entity.setPessoa(em.getReference(Pessoa.class, entity.getPessoa().getId()));
		entity.setStatus(StatusProcessamento.PENDENTE);
		em.persist(entity);
		return Response
				.created(UriBuilder.fromResource(AtividadeEndpoint.class).path(String.valueOf(entity.getId())).build())
				.build();
	}

	/**
	 * Calcula tempo real de execução. Regra de Negócio O esforço da atividade
	 * indica quantas horas são necessárias para concluir a tarefa. Exemplo: Se
	 * uma atividade leva 1 hora para ser realizada por uma pessoa com 10% de
	 * indice de produtividade, então a tarefa termina em 54 minutos. Formula
	 * para calculo em base de minutos: ((60 * esforco)-((produtividade/100) * (60 * esforco))).
	 * @param entity
	 */
	public void calcularTempoExecucao(Atividade entity) {
		BigDecimal esforco = new BigDecimal(entity.getEsforco());
		BigDecimal produtividade = new BigDecimal(entity.getPessoa().getProdutividade());
		BigDecimal baseMinutos = new BigDecimal(Constantes.Params.BASE_MINUTOS);
		BigDecimal basePorcentagem = new BigDecimal(Constantes.Params.BASE_PORCENTAGEM);

		BigDecimal tempoExecucao = baseMinutos.multiply(esforco)
				.subtract(produtividade.divide(basePorcentagem).multiply(baseMinutos.multiply(esforco)));

		entity.setTempoExecucao(tempoExecucao.intValue());
	}
	/**
	 * Remove uma {@link Atividade} cadastrada pelo idenficador.
	 * @param id - {@link Long} identificador.
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Atividade entity = em.find(Atividade.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}
	
	/**
	 * Recupera uma {@link Atividade} pelo idenficador.
	 * @param id - {@link Long} identificador.
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Atividade> findByIdQuery = em.createNamedQuery(ConstanteAtividade.BUSCAR_ATIVIDADE_POR_ID_KEY, Atividade.class);
		findByIdQuery.setParameter(ConstanteAtividade.ID_FIELD, id);
		Atividade entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	/**
	 * Recupera uma List<{@link Atividade}> paginados.
	 * @param startPosition - {@link Integer} posicao de inicio da consulta.
	 * @param maxResult - {@link Integer} posicao final da consulta.
	 * @return  List<{@link Atividade}> - Lista de {@link Atividade}.
	 */
	@GET
	@Produces("application/json")
	@PermitAll
	public List<Atividade> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		TypedQuery<Atividade> findAllQuery = em.createNamedQuery(ConstanteAtividade.BUSCAR_TODOS_KEY, Atividade.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Atividade> results = findAllQuery.getResultList();
		return results;
	}
	
	/**
	 * Atualiza uma {@link Atividade} por id e entidade com atualização.
	 * @param id - {@link Long} identificador da {@link Atividade}
	 * @param entity - {@link Atividade} com valores atualizados.
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Atividade entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (em.find(Atividade.class, id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

}
