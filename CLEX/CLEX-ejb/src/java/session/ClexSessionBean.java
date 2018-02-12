/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.User;
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
    private User userEntity;
    
    @Override
    public void createUser(String username, String password, String name, String email, String userType, String school, Long contactNum){
        userEntity = new User();
        userEntity.createUser(username, password, name, email, userType, school, contactNum);
        em.persist(userEntity);
        em.flush();
    }
    
    @Override
    public boolean checkNewUser(String username) {
        if(findUser(username) == null){
            return true;
        }
        return false;
    }
    
    private User findUser(String username){
        User u = new User();
        u = null;
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username=:username");
            q.setParameter("username", username);
            u = (User) q.getSingleResult();
            System.out.println("Username " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Username " + username + " does not exist.");
            u = null;
        }
        return u;
    }

}
