/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author lin
 */
@Entity
public class Module implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String moduleCode;
    private String takenYear;
    private String takenSem;
    private String prerequisite;
    private String preclusions;
    private int modularCredits;
    private int workload;
    private Long superGroupId;
    private Course course = new Course();

    @ManyToMany(cascade={CascadeType.ALL}, mappedBy="modules")
    @JoinTable(name="Lecturer_Module")
    private Set<Lecturer> lecturers = new HashSet<Lecturer>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="module")
    private Collection<Lesson> lessons = new ArrayList<Lesson>();
    
    @OneToOne(mappedBy="module")
    private SuperGroup supergroup;
    
    public void createModule(String moduleCode, int modularCredits, int workload, String takenYear, 
                String takenSem,String prerequisite, String preclusions, Long superGroupId){
        this.moduleCode = moduleCode;
        this.modularCredits = modularCredits;
        this.moduleCode = moduleCode;
        this.takenSem = takenSem;
        this.takenYear = takenYear;
        this.workload = workload;
        this.prerequisite = prerequisite;
        this.preclusions = preclusions;
        this.superGroupId = superGroupId;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SuperGroup getSupergroup() {
        return supergroup;
    }

    public void setSupergroup(SuperGroup supergroup) {
        this.supergroup = supergroup;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Set<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
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

    public int getModularCredits() {
        return modularCredits;
    }

    public void setModularCredits(int modularCredits) {
        this.modularCredits = modularCredits;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public Long getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(Long superGroupId) {
        this.superGroupId = superGroupId;
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
