/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ProjectGroup;
import entity.Student;
import entity.Transaction;
import java.util.ArrayList;
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
    
    //get all transactions of a projectgroup sorted according to date
    public ArrayList<Transaction> getAllTransaction(ProjectGroup group) {
        return transactions;
    }
    
    //get all transactions of a certain student in a projectgroup sorted according to date
    public ArrayList<Transaction> getAllTransaction(Student student) {
        return transactions;
    }
    
    public void addTransaction( ) {
        
    }
    
    
}
