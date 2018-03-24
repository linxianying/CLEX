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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaClass.ComparableTransaction;
import javaClass.StudentBalance;
import javaClass.StudentCost;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    private ArrayList<Transaction> sortedTransactions;
    private ArrayList<StudentBalance> balances;
    private ArrayList<StudentBalance> zeroBalances;
    private Transaction transaction;
    private StudentBalance studentBalance;
    private Ledger ledger;
    private double cost;
    private double pay;
    private double assCost;
    
    
    //get all transactions of a projectgroup sorted according to date
    public ArrayList<Transaction> getAllTransaction(ProjectGroup group) {
        return transactions;
    }
    
    //get all transactions of a certain student in a projectgroup sorted according to date
    public ArrayList<Transaction> getAllTransaction(Student student) {
        return transactions;
    }
    
    @Override
    public Ledger findLedgerById(Long id){
        Ledger l = null;
        try {
            Query q = em.createQuery("Select l From Ledger l Where l.id=:id");
            q.setParameter("id", id);
            l = (Ledger) q.getSingleResult();
        }
        catch (NoResultException e){
            System.out.println("pcsb: findLedgerById: no ledger found");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return l;
    }
    
    @Override
    public Transaction findTransactionById(Long id){
        Transaction t = null;
        try {
            Query q = em.createQuery("Select t From GroupTransaction t Where t.id=:id");
            q.setParameter("id", id);
            t = (Transaction) q.getSingleResult();
        }
        catch (NoResultException e){
            System.out.println("pcsb: findTransactionById: no transaction found");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return t;
    }

    @Override
    public void addTransaction(ArrayList<StudentCost> all, String activity, double totalCost, ProjectGroup group) {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //first create the transaction
        transaction = new Transaction();
        transaction.createTransaction(totalCost, date, activity, group);
        em.persist(transaction);
        group.getTransactions().add(transaction);
        em.merge(group);
        em.flush();
        System.out.println("pc session bean: addTransaction: New Transaction " + transaction.getId());
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
    
//    //sort all transactions of a group according to date
//    @Override
//    public ArrayList<ComparableTransaction> getSortedTransactions(ProjectGroup group) {
//        try{    
//            if (group.getTransactions().isEmpty())
//                return null;
//            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//            sortedTransactions = new ArrayList<ComparableTransaction>();
//            transactions = new ArrayList<Transaction>();
//            Collection<Transaction> all = group.getTransactions();
//            for(Transaction t: all){
//                transactions.add(t);
//            }
//            for (Transaction t: transactions) {
//                comTransaction = new ComparableTransaction(t.getId(), t.getCost(), formatter.parse(t.getDate()), 
//                t.getActivity(), t.getProjectGroup(), t.getLedgers());
//                sortedTransactions.add(comTransaction);
//            }
//            Collections.sort(sortedTransactions);
//            //System.out.println(sortedTransactions.size());
//            //System.out.println(sortedTransactions);
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        return sortedTransactions;
//    }
    
    //caculated the asscociated cost of a Student's all ledgers in all Transactions of the projectGroup
    //which means in total, th student needs to get how much back (if assCost>0) or owes how much (if assCost<0)
    private double calculateAssCost(Student student, ProjectGroup group) {
        try{
            Query q = em.createQuery("SELECT l FROM Ledger l WHERE l.student.id=:studentId AND l.transaction.projectGroup.id=:groupId");
//            if (q == null)
//                System.out.println("query is null");
//            else if (student == null)
//                System.out.println("student is null");
//            else if (student.getId() == null)
//                System.out.println("student id is null");
            q.setParameter("studentId", student.getId());
            q.setParameter("groupId", group.getId());
            List<Ledger> all = q.getResultList();
            assCost = 0;
            for (Ledger l: all) {
                assCost += (l.getPay()-l.getAscCost());
                //System.out.println("For " + student.getName() + " pay " + l.getPay() + ", spend " + l.getAscCost());
            }
            //System.out.println("balance for " + student.getName() + " in group " + group.getName() + " is " + assCost);
        }
        catch(NoResultException e){
            System.out.println("pc session bean: calculateAssCost: no ledger for " + student.getName() + " in group " + group.getName());
            return 0.0;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return assCost;
    }
    
    @Override
    public ArrayList<StudentBalance> getAllStudentBalance(ProjectGroup group) {
        Collection<Student> allStudents = group.getGroupMembers();
        zeroBalances = new ArrayList<StudentBalance>();
        balances = new ArrayList<StudentBalance>();
        for (Student s: allStudents) {
            cost = calculateAssCost(s, group);
            studentBalance = new StudentBalance(s, cost);
            //for sorting purposes, add students with zero balances to another arraylist first
            if (cost == 0.0)
                zeroBalances.add(studentBalance);
            else 
                balances.add(studentBalance);
        }
        
        //sort the studentBalance by totalAmount
        Collections.sort(balances);
//        System.out.println("balances: " + balances.size());
//        System.out.println(balances);
//        
        if (!balances.isEmpty()){
        //set each student's balance
        int payerIndex = 0;
        int payeeIndex = balances.size()-1;
        StudentBalance payerStudentBalance = balances.get(payerIndex);
        StudentBalance payeeStudentBalance = balances.get(payeeIndex);;
        double payerBalance = Math.abs(payerStudentBalance.getTotalAmount());
        double payeeBalance = Math.abs(payeeStudentBalance.getTotalAmount());
        double amountPaid = 0.0;
        while(payerIndex < payeeIndex) {
//            payerStudentBalance = balances.get(payerIndex);
//            payeeStudentBalance = balances.get(payeeIndex);
//            payerBalance = Math.abs(payerStudentBalance.getTotalAmount());
//            payeeBalance = Math.abs(payeeStudentBalance.getTotalAmount());
            
            // if the payer owes money less than the payee lends, let the payer pays all the amount the payer owes to the payee
            if (payerBalance <= payeeBalance) {
                amountPaid = payerBalance;
                payerBalance = 0.0;
                payeeBalance -= payerBalance;
                //set StudentBalance of the payer
                student = payeeStudentBalance.getStudent();
                payerStudentBalance.getPayees().put(student, amountPaid);
                //set StudentBalance of the payee
                student = payerStudentBalance.getStudent();
                payeeStudentBalance.getPayees().put(student, amountPaid);
//                System.out.println(payerStudentBalance.getStudent().getName() + 
//                        " pays $" + amountPaid + " to " + payeeStudentBalance.getStudent().getName());
            }
            // if the payer owes money more than the payee lends, let the payer pays all the amount the payee lends to the payee
            else {
                amountPaid = payeeBalance;
                payeeBalance = 0.0;
                payerBalance -= payeeBalance;
                //set StudentBalance of the payer
                student = payeeStudentBalance.getStudent();
                payerStudentBalance.getPayees().put(student, amountPaid);
                //set StudentBalance of the payee
                student = payerStudentBalance.getStudent();
                payeeStudentBalance.getPayees().put(student, amountPaid);
//                System.out.println(payerStudentBalance.getStudent().getName() + 
//                        " pays $" + amountPaid + " to " + payeeStudentBalance.getStudent().getName());
            }
            //move the iterator if the balance is zero
            if (payerBalance == 0.0)
                payerIndex++;
            if (payeeBalance == 0.0)
                payeeIndex--;
            //in case of index out of bound
            //reset the values
            if (payerIndex < balances.size() && payeeIndex >= 0) {
                if (payerBalance == 0.0) {
                    payerStudentBalance = balances.get(payerIndex);
                    payerBalance = Math.abs(payerStudentBalance.getTotalAmount());
                }
                if (payeeBalance == 0.0) {
                    payeeStudentBalance = balances.get(payeeIndex);
                    payeeBalance = Math.abs(payeeStudentBalance.getTotalAmount());
                }
            }
        }
            
            //test print out
//            for (StudentBalance sb: balances) {
//                System.out.println("------------For " + sb.getStudent().getName() + "------------");
//                HashMap<Student, Double> all = sb.getPayees();
//                for (Student s: all.keySet()){
//                    String key = s.getName();
//                    String value = Double.toString(all.get(s));  
//                    System.out.println(key + " " + value);  
//                } 
//            }
        }
        
        //add the zeroBalances arraylist (zeroBalances, payees is empty)
        balances.addAll(zeroBalances); 
        return balances;
    }
    
    //called by deleteTransaction method, delete a ledger and its relationship with student
    private void deleteLedger(Long ledgerId, Student student, Transaction deletetransaction) {
//        student.getLedgers().remove(ledger);
//        em.merge(student);
        //deletetransaction.getLedgers().remove(ledger);
//        em.remove(ledger);
//        System.out.println("remove Ledger");
//        System.out.println("Check inside student still have or not: " + student.getLedgers().contains(ledger));
//        em.merge(student);
//        em.flush();
        Ledger deleteLedger = this.findLedgerById(ledgerId);
        deletetransaction.getLedgers().remove(ledger);
//        em.merge(deletetransaction);
        student.getLedgers().remove(ledger);
        em.merge(student);
        em.remove(deleteLedger); 
        em.flush();
        System.out.println("remove Ledger");
    }
    
    
    //delete a transaction need to delete it in databse, projectGroup, delete all ledger 

    /**
     *
     * @param deletedTransactionId
     * @param group
     */
        @Override
    public void deleteTransaction(Long deletedTransactionId, ProjectGroup group, Transaction t) {
        Transaction deletetransaction = this.findTransactionById(deletedTransactionId);
//        group.getTransactions().remove(deletetransaction);
//        em.merge(group);
        for (Ledger l: t.getLedgers()) {
            this.deleteLedger(l.getId(), l.getStudent(), deletetransaction);
        }
        group.getTransactions().remove(deletetransaction);
        em.merge(group);
        //em.merge(deletetransaction);
        em.remove(deletetransaction);
        em.flush();
    }
    
    /**
     *
     * @param group
     * @return
     */
    @Override
    public ArrayList<Transaction> getALLSortedTransactions(ProjectGroup group) {
        try{    
            if (group.getTransactions().isEmpty())
                return null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            sortedTransactions = new ArrayList<Transaction>();
            //transactions = new ArrayList<Transaction>();
            Collection<Transaction> all = group.getTransactions();
            for(Transaction t: all){
                sortedTransactions.add(t);
            }

            Collections.sort(sortedTransactions, new Comparator<Transaction>() {
                public int compare (Transaction t1, Transaction t2) {
                    try {
                        return formatter.parse(t1.getDate()).compareTo(formatter.parse(t2.getDate()));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("pcsb: getSortedTransactions: compare error");
                    return 0;
                }
            });
            //System.out.println(sortedTransactions.size());
            //System.out.println(sortedTransactions);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return sortedTransactions;
    }
}
