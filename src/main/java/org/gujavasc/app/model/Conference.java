package org.gujavasc.app.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Set;
import java.util.HashSet;
import org.gujavasc.app.model.Session;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Conference implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String name;

   @Temporal(TemporalType.DATE)
   private Date conferenceDate;

   @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL)
   private Set<Session> sessions = new HashSet<Session>();

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
         return id.equals(((Conference) that).id);
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

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public Date getConferenceDate()
   {
      return this.conferenceDate;
   }

   public void setConferenceDate(final Date conferenceDate)
   {
      this.conferenceDate = conferenceDate;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (name != null && !name.trim().isEmpty())
         result += "name: " + name;
      return result;
   }

   public Set<Session> getSessions()
   {
      return this.sessions;
   }

   public void setSessions(final Set<Session> sessions)
   {
      this.sessions = sessions;
   }
}