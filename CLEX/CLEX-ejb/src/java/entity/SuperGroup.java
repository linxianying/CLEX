/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;  
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author lin
 */
@Entity
public class SuperGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 32, nullable = false)
    private int numOfGroups;
    
    @Column(length = 32, nullable = false)
    private int avgStudentNum;
    
    @Column(length = 32)
    private int minStudentNum;
    
    @Column(length = 32)
    private int maxStudentNum;
    
    //if the group formation ended, the lecturer can close the group formation and confirm=true;
    private boolean confirm;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deadline;
    
    @OneToOne(mappedBy="superGroup")
    private Module module;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="superGroup")
    private Collection<ProjectGroup> projectGroups = new ArrayList<ProjectGroup>();
    
    public void createSuperGroup(int numOfGroups, int avgStudentNum, Module module){
        this.numOfGroups = numOfGroups;
        this.avgStudentNum = avgStudentNum;
        this.module = module;
        confirm = false;
    }
    
//    //set min and max as well
//    public void createSuperGroup(int numOfGroups, int avgStudentNum, int minStudentNum, int maxStudentNum, Module module){
//        this.numOfGroups = numOfGroups;
//        this.avgStudentNum = avgStudentNum;
//        this.minStudentNum = minStudentNum;
//        this.maxStudentNum = maxStudentNum;
//        this.module = module;
//        confirm = false;
//    }
//    
//    //set max only
//    public void createSuperGroupWithMax(int numOfGroups, int avgStudentNum,  int maxStudentNum, Module module){
//        this.numOfGroups = numOfGroups;
//        this.avgStudentNum = avgStudentNum;
//        this.maxStudentNum = maxStudentNum;
//        this.module = module;
//        confirm = false;
////    }
//    
//    //set min only
//    public void createSuperGroupWithMin(int numOfGroups, int avgStudentNum, int minStudentNum,  Module module){
//        this.numOfGroups = numOfGroups;
//        this.avgStudentNum = avgStudentNum;
//        this.minStudentNum = minStudentNum;
//        this.module = module;
//        confirm = false;
//    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<ProjectGroup> getProjectGroups() {
        return projectGroups;
    }

    public void setProjectGroups(Collection<ProjectGroup> projectGroups) {
        this.projectGroups = projectGroups;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getNumOfGroups() {
        return numOfGroups;
    }

    public void setNumOfGroups(int numOfGroups) {
        this.numOfGroups = numOfGroups;
    }

    public int getMinStudentNum() {
        return minStudentNum;
    }

    public void setMinStudentNum(int minStudentNum) {
        this.minStudentNum = minStudentNum;
    }

    public int getMaxStudentNum() {
        return maxStudentNum;
    }

    public void setMaxStudentNum(int maxStudentNum) {
        this.maxStudentNum = maxStudentNum;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public int getAvgStudentNum() {
        return avgStudentNum;
    }

    public void setAvgStudentNum(int avgStudentNum) {
        this.avgStudentNum = avgStudentNum;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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
        if (!(object instanceof SuperGroup)) {
            return false;
        }
        SuperGroup other = (SuperGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SuperGroup[ id=" + id + " ]";
    }
    
}
