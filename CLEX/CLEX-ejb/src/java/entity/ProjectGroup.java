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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author lin
 */
@Entity
public class ProjectGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 32, nullable = false)
    private double cost;
    
    @Column(length = 32, nullable = false)
    private String name;
    
    @ManyToOne
    private SuperGroup superGroup;
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    private Collection<Student> groupMembers = new ArrayList<Student>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="projectGroup")
    private Collection<GroupTimeslot> groupTimeslots = new ArrayList<GroupTimeslot>();
    
    @OneToMany(cascade={CascadeType.ALL})
    private Collection<GroupTask> groupTasks = new ArrayList<GroupTask>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="projectGroup")
    private Collection<Transaction> transactions = new ArrayList<Transaction>();

    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "projectGroup")
    private Collection<PeerReviewAnswer> answers = new ArrayList<PeerReviewAnswer>();
    
    public void createProjectGroup(SuperGroup superGroup, String name, double cost){
        this.superGroup = superGroup;
        this.cost = cost;
        this.name = name;
    }

    public Collection<GroupTimeslot> getGroupTimeslots() {
        return groupTimeslots;
    }

    public void setGroupTimeslots(Collection<GroupTimeslot> groupTimeslots) {
        this.groupTimeslots = groupTimeslots;
    }

    public Collection<GroupTask> getGroupTasks() {
        return groupTasks;
    }

    public void setGroupTasks(Collection<GroupTask> groupTasks) {
        this.groupTasks = groupTasks;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Collection<Student> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(Collection<Student> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public SuperGroup getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(SuperGroup superGroup) {
        this.superGroup = superGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<PeerReviewAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<PeerReviewAnswer> answers) {
        this.answers = answers;
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
        if (!(object instanceof ProjectGroup)) {
            return false;
        }
        ProjectGroup other = (ProjectGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProjectGroup[ id=" + id + " ]";
    }
    
}
