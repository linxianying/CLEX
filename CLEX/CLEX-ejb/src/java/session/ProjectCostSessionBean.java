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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javaClass.ComparableTransaction;
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
    private ComparableTransaction comTransaction;
    private ArrayList<ComparableTransaction> sortedTransactions;
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
        //System.out.println("pc session bean: addTransaction");
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
    
    //sort all transactions of a group according to date
    public ArrayList<ComparableTransaction> getSortedTransactions(ProjectGroup group) {
        try{    
            if (group.getTransactions().isEmpty())
                return null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            sortedTransactions = new ArrayList<ComparableTransaction>();
            transactions = new ArrayList<Transaction>();
            Collection<Transaction> all = group.getTransactions();
            for(Transaction t: all){
                transactions.add(t);
            }
            for (Transaction t: transactions) {
                comTransaction = new ComparableTransaction(t.getCost(), formatter.parse(t.getDate()), 
                t.getActivity(), t.getProjectGroup(), t.getLedgers());
                sortedTransactions.add(comTransaction);
            }
            Collections.sort(sortedTransactions);
            System.out.println(sortedTransactions.size());
            System.out.println(sortedTransactions);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return sortedTransactions;
    }
}
