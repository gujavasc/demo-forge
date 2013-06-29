package org.gujavasc.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gujavasc.app.model.SessionReview;
import org.gujavasc.app.model.Session;

/**
 * Backing bean for SessionReview entities.
 * <p>
 * This class provides CRUD functionality for all SessionReview entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SessionReviewBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving SessionReview entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private SessionReview sessionReview;

   public SessionReview getSessionReview()
   {
      return this.sessionReview;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.sessionReview = this.example;
      }
      else
      {
         this.sessionReview = findById(getId());
      }
   }

   public SessionReview findById(Long id)
   {

      return this.entityManager.find(SessionReview.class, id);
   }

   /*
    * Support updating and deleting SessionReview entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.sessionReview);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.sessionReview);
            return "view?faces-redirect=true&id=" + this.sessionReview.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         this.entityManager.remove(findById(getId()));
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching SessionReview entities with pagination
    */

   private int page;
   private long count;
   private List<SessionReview> pageItems;

   private SessionReview example = new SessionReview();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public SessionReview getExample()
   {
      return this.example;
   }

   public void setExample(SessionReview example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<SessionReview> root = countCriteria.from(SessionReview.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<SessionReview> criteria = builder.createQuery(SessionReview.class);
      root = criteria.from(SessionReview.class);
      TypedQuery<SessionReview> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<SessionReview> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Session session = this.example.getSession();
      if (session != null)
      {
         predicatesList.add(builder.equal(root.get("session"), session));
      }
      String reviewerName = this.example.getReviewerName();
      if (reviewerName != null && !"".equals(reviewerName))
      {
         predicatesList.add(builder.like(root.<String> get("reviewerName"), '%' + reviewerName + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<SessionReview> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back SessionReview entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SessionReview> getAll()
   {

      CriteriaQuery<SessionReview> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SessionReview.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(SessionReview.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SessionReviewBean ejbProxy = this.sessionContext.getBusinessObject(SessionReviewBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((SessionReview) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private SessionReview add = new SessionReview();

   public SessionReview getAdd()
   {
      return this.add;
   }

   public SessionReview getAdded()
   {
      SessionReview added = this.add;
      this.add = new SessionReview();
      return added;
   }
}