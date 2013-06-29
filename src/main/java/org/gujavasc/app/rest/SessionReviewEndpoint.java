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
import org.gujavasc.app.model.SessionReview;

/**
 * 
 */
@Stateless
@Path("/sessionreviews")
public class SessionReviewEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(SessionReview entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(SessionReviewEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      SessionReview entity = em.find(SessionReview.class, id);
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
      TypedQuery<SessionReview> findByIdQuery = em.createQuery("SELECT s FROM SessionReview s LEFT JOIN FETCH s.session WHERE s.id = :entityId", SessionReview.class);
      findByIdQuery.setParameter("entityId", id);
      SessionReview entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces("application/json")
   public List<SessionReview> listAll()
   {
      final List<SessionReview> results = em.createQuery("SELECT s FROM SessionReview s LEFT JOIN FETCH s.session", SessionReview.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, SessionReview entity)
   {
      entity.setId(id);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}