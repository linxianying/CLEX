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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long superGroupId;
    private List<Long> listOfStudentId;
    private double cost;
    
    @OneToMany(cascade={CascadeType.PERSIST})
    private Collection<Transaction> transactions = new ArrayList<Transaction>();
    
    @ManyToOne
    private SuperGroup superGroup;
    
    
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

    public Long getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(Long superGroupId) {
        this.superGroupId = superGroupId;
    }

    public List<Long> getListOfStudentId() {
        return listOfStudentId;
    }

    public void setListOfStudentId(List<Long> listOfStudentId) {
        this.listOfStudentId = listOfStudentId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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
