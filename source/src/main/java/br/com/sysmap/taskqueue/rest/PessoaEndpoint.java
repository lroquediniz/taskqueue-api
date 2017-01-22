package br.com.sysmap.taskqueue.rest;

import java.util.List;

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

import br.com.sysmap.taskqueue.model.Pessoa;
import br.com.sysmap.taskqueue.model.Pessoa.ConstantePessoa;
import br.com.sysmap.taskqueue.util.Constantes;

/**
 * Endpoind EJB Pessoa.
 * Endpoint inteligente para servico de cadastro de atividades.
 * @author Luan Roque.
 */
@Stateless
@Path("/pessoas")
public class PessoaEndpoint {
	/**
	 * Gerenciador de entidades JPA.
	 */
	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	/**
	 * Grava entidade Pessoa.
	 * @param entity - {@link Pessoa} a ser gravas
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@POST
	@Consumes("application/json")
	public Response create(Pessoa entity) {
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(PessoaEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	/**
	 * Remove uma {@link Pessoa} cadastrada pelo idenficador.
	 * @param id - {@link Long} identificador.
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Pessoa entity = em.find(Pessoa.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	/**
	 * Recupera uma {@link Pessoa} pelo idenficador.
	 * @param id - {@link Long} identificador.
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Pessoa> findByIdQuery = em
				.createNamedQuery(ConstantePessoa.BUSCAR_PESSOA_POR_ID_KEY, Pessoa.class);
		findByIdQuery.setParameter(ConstantePessoa.ID_FIELD, id);
		Pessoa entity;
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
	 * Recupera uma List<{@link Pessoa}> paginados.
	 * @param startPosition - {@link Integer} posicao de inicio da consulta.
	 * @param maxResult - {@link Integer} posicao final da consulta.
	 * @return  List<{@link Pessoa}> - Lista de Pessoas.
	 */
	@GET
	@Produces("application/json")
	public List<Pessoa> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<Pessoa> findAllQuery = em.createNamedQuery(ConstantePessoa.BUSCAR_TODAS_KEY, Pessoa.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Pessoa> results = findAllQuery.getResultList();
		return results;
	}

	/**
	 * Atualiza uma {@link Pessoa} por id e entidade com atualização.
	 * @param id - {@link Long} identificador da {@link Pessoa}
	 * @param entity - {@link Pessoa} com valores atualizados.
	 * @return Response - {@link Response} objeto de resposta http.
	 */
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Pessoa entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (em.find(Pessoa.class, id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
