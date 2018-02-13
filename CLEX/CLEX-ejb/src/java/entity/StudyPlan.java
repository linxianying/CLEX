/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author caoyu
 */
@Entity
public class StudyPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade={CascadeType.PERSIST})
    private Course course = new Course();

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getPickUpYear() {
        return pickUpYear;
    }

    public void setPickUpYear(String pickUpYear) {
        this.pickUpYear = pickUpYear;
    }

    public String getPickUpSem() {
        return pickUpSem;
    }

    public void setPickUpSem(String pickUpSem) {
        this.pickUpSem = pickUpSem;
    }
    
    private String pickUpYear;
    private String pickUpSem;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudyPlan)) {
            return false;
        }
        StudyPlan other = (StudyPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StudyPlan[ id=" + id + " ]";
    }
    
}
