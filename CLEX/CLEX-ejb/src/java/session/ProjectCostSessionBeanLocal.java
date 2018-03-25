/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Ledger;
import entity.ProjectGroup;
import entity.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javaClass.ComparableTransaction;
import javaClass.StudentBalance;
import javaClass.StudentCost;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface ProjectCostSessionBeanLocal {

    public void addTransaction(ArrayList<StudentCost> all, String activity, double totalCost, ProjectGroup group, Date date);

//    public ArrayList<ComparableTransaction> getSortedTransactions(ProjectGroup group);

    public ArrayList<StudentBalance> getAllStudentBalance(ProjectGroup group);

    public Transaction findTransactionById(Long id);

    public void deleteTransaction(Long deletedTransactionId, ProjectGroup group, Transaction t);

    public Ledger findLedgerById(Long id);

    public ArrayList<Transaction> getALLSortedTransactions(ProjectGroup group);

}
