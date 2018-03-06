/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Ledger;
import entity.ProjectGroup;
import entity.Student;
import entity.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javaClass.StudentCost;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author caoyu
 */
@Stateless
public class ProjectCostSessionBean implements ProjectCostSessionBeanLocal {
    @PersistenceContext
    EntityManager em;
    private Student student;
    private String username;
    private ProjectGroup group;
    private ArrayList<Transaction> transactions;
    
    private Transaction transaction;
    private Ledger ledger;
    private double cost;
    private double pay;
    
    
    //get all transactions of a projectgroup sorted according to date
    public ArrayList<Transaction> getAllTransaction(ProjectGroup group) {
        return transactions;
    }
    
    //get all transactions of a certain student in a projectgroup sorted according to date
    public ArrayList<Transaction> getAllTransaction(Student student) {
        return transactions;
    }
    

    @Override
    public void addTransaction(ArrayList<StudentCost> all, String activity, double totalCost, ProjectGroup group) {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //first create the transaction
        transaction = new Transaction();
        transaction.createTransaction(totalCost, date, activity, group);
        em.persist(transaction);
        em.flush();
        System.out.println("pc session bean: addTransaction");
        for(StudentCost sc: all) {
            student = sc.getStudent();
            cost = sc.getCost();
            pay = sc.getPay();
            ledger = new Ledger();
            ledger.createLedger(student, cost, pay, transaction);
            this.setLedger(ledger, student, transaction);
        }
    }
    
    private void setLedger(Ledger ledger, Student student, Transaction transaction) {
        student.getLedgers().add(ledger);
        transaction.getLedgers().add(ledger);
        em.persist(ledger);
        em.merge(student);
        em.merge(transaction);
        em.flush();
    }
    
}
