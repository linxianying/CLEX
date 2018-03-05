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
import javax.persistence.ManyToOne;

/**
 *
 * @author lin
 */
@Entity
public class Ledger implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //in this transaction, how much this student should pay 
    private double ascCost;
    //in this transaction, how much this student actually paid 
    private double pay;
    
    @ManyToOne
    private Student student = new Student();
    
    @ManyToOne
    private Transaction transaction = new Transaction();
 
    public void createLedger(Student student, double ascCost, double pay, Transaction transaction){
        this.student = student;
        this.ascCost = ascCost;
        this.pay = pay;
        this.transaction = transaction;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAscCost() {
        return ascCost;
    }

    public void setAscCost(double ascCost) {
        this.ascCost = ascCost;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
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
        if (!(object instanceof Ledger)) {
            return false;
        }
        Ledger other = (Ledger) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Ledger[ id=" + id + " ]";
    }
    
}
