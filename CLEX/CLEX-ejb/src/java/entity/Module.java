/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author lin
 */
@Entity(name="SchoolModule")
public class Module implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String takenYear;
    private String takenSem;
    private String prerequisite;
    private String preclusions;

    
    @OneToOne(cascade={CascadeType.ALL})
    private SuperGroup superGroup;

    @ManyToOne
    private Course course = new Course();
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    private Collection<Student> students = new ArrayList<Student>();

    @ManyToMany(cascade={CascadeType.PERSIST}, mappedBy="modules")
    private Collection<Lecturer> lecturers = new ArrayList<Lecturer>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="module")
    private Collection<Lesson> lessons = new ArrayList<Lesson>();
    
    
    @OneToMany(cascade={CascadeType.ALL})  
    private Collection<Poll> polls = new ArrayList<Poll>();
    
    public void createModule(String takenYear, 
                String takenSem,String prerequisite, String preclusions, Course course){
        this.takenSem = takenSem;
        this.takenYear = takenYear;

        this.prerequisite = prerequisite;
        this.preclusions = preclusions;
        this.course = course;
    }
    
    public SuperGroup getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(SuperGroup superGroup) {
        this.superGroup = superGroup;
    }
    
    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Poll> getPolls() {
        return polls;
    }

    public void setPolls(Collection<Poll> polls) {
        this.polls = polls;
    }



    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Collection<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Collection<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }


    public String getTakenYear() {
        return takenYear;
    }

    public void setTakenYear(String takenYear) {
        this.takenYear = takenYear;
    }

    public String getTakenSem() {
        return takenSem;
    }

    public void setTakenSem(String takenSem) {
        this.takenSem = takenSem;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getPreclusions() {
        return preclusions;
    }

    public void setPreclusions(String preclusions) {
        this.preclusions = preclusions;
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
        if (!(object instanceof Module)) {
            return false;
        }
        Module other = (Module) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Module[ id=" + id + " ]";
    }
    
}
