package org.gujavasc.app.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.gujavasc.app.model.Session;

/**
 * 
 */
@Stateless
@Path("/sessions")
public class SessionEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(Session entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(SessionEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Session entity = em.find(Session.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Session> findByIdQuery = em.createQuery("SELECT s FROM Session s LEFT JOIN FETCH s.conference LEFT JOIN FETCH s.reviews LEFT JOIN FETCH s.speaker WHERE s.id = :entityId", Session.class);
      findByIdQuery.setParameter("entityId", id);
      Session entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces("application/json")
   public List<Session> listAll()
   {
      final List<Session> results = em.createQuery("SELECT s FROM Session s LEFT JOIN FETCH s.conference LEFT JOIN FETCH s.reviews LEFT JOIN FETCH s.speaker", Session.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, Session entity)
   {
      entity.setId(id);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}