/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eeren
 */
@Stateless
public class UserAccessControlBean implements UserAccessControlBeanLocal {

    @PersistenceContext
    EntityManager em;
    User userEntity;
    
    @Override
    public List getAllUsers(){
        List<User> users = new ArrayList<User>();
        Query q = em.createQuery("Select u FROM BasicUser u");
        for(Object o: q.getResultList()){
            userEntity = (User) o;
            if(!userEntity.getUserType().equals("Admin")){
                users.add(userEntity);
            }
        }
        return users;
    }
    
    @Override
    public List<String> createUserTypes(){
        String[] allUserTypes = new String[3];
        allUserTypes[0] = "Student";
        allUserTypes[1] = "Lecturer";
        allUserTypes[2] = "Guest";
        return Arrays.asList(allUserTypes);
    }
    
    @Override
    public boolean approveUser(String username){
        userEntity = findUser(username);
        if(!userEntity.isApproval()){
            userEntity.setApproval(true);
            em.merge(userEntity);
            em.flush();
            em.clear();
            System.out.println("User " + username + " approved.");
            return true;
        }
        System.out.println("Failed to approve " + username + ".");
        return false;
    }
    
    @Override
    public boolean deleteUser(String username){
        userEntity = findUser(username);
        if(userEntity != null){
            //If you are not approved in the first place, you cant access any feature so everything is empty
            if(userEntity.isApproval()){
                System.out.println("Failed to delete " + username + ".");
                return false;
            }
            em.remove(userEntity);
            em.flush();
            em.clear();
            System.out.println("User " + username + " deleted.");
            return true;
        }
        System.out.println(username + " not found.");
        return false;
    }    

    public User findUser(String username){
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        }
        return userEntity;
    }
    
}
