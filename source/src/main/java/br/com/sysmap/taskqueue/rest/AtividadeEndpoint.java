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
import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.Pessoa;
import br.com.sysmap.taskqueue.util.Constantes;

/**
 * 
 */
@Stateless
@Path("/atividades")
public class AtividadeEndpoint {
	
	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(Atividade entity) {
		entity.setPessoa(em.getReference(Pessoa.class, entity.getPessoa().getId()));
		em.persist(entity);
		return Response.created(UriBuilder.fromResource(AtividadeEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

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

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Atividade> findByIdQuery = em.createQuery(
				"SELECT DISTINCT a FROM Atividade a LEFT JOIN FETCH a.pessoa WHERE a.id = :entityId ORDER BY a.id",
				Atividade.class);
		findByIdQuery.setParameter("entityId", id);
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

	@GET
	@Produces("application/json")
	public List<Atividade> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		TypedQuery<Atividade> findAllQuery = em.createQuery(
				"SELECT DISTINCT a FROM Atividade a LEFT JOIN FETCH a.pessoa ORDER BY a.id", Atividade.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Atividade> results = findAllQuery.getResultList();
		return results;
	}

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