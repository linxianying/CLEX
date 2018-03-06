/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 32, nullable = false)
    private String pickYear;
    
    @Column(length = 32, nullable = false)
    private String pickSem;
    
    @ManyToOne
    private Course course = new Course();
    
    @ManyToOne
    private Student student = new Student();

    public void createStudyPlan(String pickYear, String pickSem, Course course, Student student){
        this.pickYear = pickYear;
        this.pickSem = pickSem;
        this.course = course;
        this.student = student;
    }

    public String getPickYear() {
        return pickYear;
    }

    public void setPickYear(String pickYear) {
        this.pickYear = pickYear;
    }

    public String getPickSem() {
        return pickSem;
    }

    public void setPickSem(String pickSem) {
        this.pickSem = pickSem;
    }
    
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    
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
