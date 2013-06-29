package org.gujavasc.app.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import org.gujavasc.app.model.Conference;
import javax.persistence.ManyToOne;
import java.util.Set;
import java.util.HashSet;
import org.gujavasc.app.model.SessionReview;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import org.gujavasc.app.model.Speaker;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Session implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String title;

   @ManyToOne
   private Conference conference;

   @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
   private Set<SessionReview> reviews = new HashSet<SessionReview>();

   @ManyToOne
   private Speaker speaker;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Session) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getTitle()
   {
      return this.title;
   }

   public void setTitle(final String title)
   {
      this.title = title;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (title != null && !title.trim().isEmpty())
         result += "title: " + title;
      return result;
   }

   public Conference getConference()
   {
      return this.conference;
   }

   public void setConference(final Conference conference)
   {
      this.conference = conference;
   }

   public Set<SessionReview> getReviews()
   {
      return this.reviews;
   }

   public void setReviews(final Set<SessionReview> reviews)
   {
      this.reviews = reviews;
   }

   public Speaker getSpeaker()
   {
      return this.speaker;
   }

   public void setSpeaker(final Speaker speaker)
   {
      this.speaker = speaker;
   }
}