/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Student;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author lin
 */
@Stateless
public class ClexSessionBean implements ClexSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private Student studentEntity;
    
    @Override
    public void createStudent(String username, String password, String name, 
                String email, String school, Long contactNum, 
                String faculty, String major, String matricYear, String matricSem, 
                String currentYear, double cap){
        studentEntity = new Student();
        studentEntity.createStudent(username, password, name, email, school, contactNum, 
                 faculty, major, matricYear, matricSem, currentYear, cap);
        em.persist(studentEntity);
        em.flush();
    }
    
    @Override
    public boolean checkNewUser(String username) {
        if(findUser(username) == null){
            return true;
        }
        return false;
    }
    
    private Student findUser(String username){
        Student u = new Student();
        u = null;
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username=:username");
            q.setParameter("username", username);
            u = (Student) q.getSingleResult();
            System.out.println("Username " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Username " + username + " does not exist.");
            u = null;
        }
        return u;
    }

}
