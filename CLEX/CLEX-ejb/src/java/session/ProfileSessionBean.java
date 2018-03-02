/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Guest;
import entity.Lecturer;
import entity.Student;
import entity.User;
import java.security.MessageDigest;
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
public class ProfileSessionBean implements ProfileSessionBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    private User userEntity;
    private Student studentEntity;
    private Lecturer lecturerEntity;
    private Guest guestEntity;
    
    @Override
    public void editStudent(String username, String name, String email, Long contactNum, String faculty, String major, String matricYear, String matricSem){
        studentEntity = findStudent(username);
        
        studentEntity.setName(name);
        studentEntity.setEmail(email);
        studentEntity.setContactNum(contactNum);
        studentEntity.setFaculty(faculty);
        studentEntity.setMajor(major);
        studentEntity.setMatricYear(matricYear);
        studentEntity.setMatricSem(matricSem);
        
        em.merge(studentEntity);
        em.flush();
    }

    @Override
    public void editLecturer(String username, String name, String email, String school, Long contactNum, String faculty){
        lecturerEntity = findLecturer(username);
        
        lecturerEntity.setName(name);
        lecturerEntity.setEmail(email);
        lecturerEntity.setContactNum(contactNum);
        lecturerEntity.setFaculty(faculty);
        
        em.merge(lecturerEntity);
        em.flush();
    }

    @Override
    public void editGuest(String username, String name, String email, String school, Long contactNum){
        guestEntity = findGuest(username);
        
        guestEntity.setName(name);
        guestEntity.setEmail(email);
        guestEntity.setContactNum(contactNum);
        
        em.merge(guestEntity);
        em.flush();
    }
    
    

    @Override
    public void changePassword(String username, String newPassword){
        Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
        q.setParameter("username", username);
        userEntity = (User) q.getSingleResult();
        userEntity.setPassword(hashPassword(newPassword, userEntity.getSalt()));
        System.out.println("Password of " + username + " has been changed.");
        em.merge(userEntity);
        em.flush();
    }
    
    @Override
    public boolean checkPassword(String username, String password){
        Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
        q.setParameter("username", username);
        userEntity = (User) q.getSingleResult();
        if(userEntity.getPassword().equals(hashPassword(password, userEntity.getSalt()))){
            System.out.println("Password of " + username + " is correct.");
            return true;
        }
        else{
            System.out.println("Password of " + username + " is wrong.");
        }
        return false;
    }
    
    private String hashPassword(String password, String salt){
        String genPass = null;
 
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pass = (password+salt).getBytes();
            md.update(pass);
            byte[] temp = md.digest(pass);
            
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < temp.length; i++){
                sb.append(Integer.toString((temp[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPass = sb.toString();
            
        }
        catch(Exception e){
            System.out.println("Failed to hash password.");
        }
        return genPass;
    }
    
    public Student findStudent(String username){
        studentEntity = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            studentEntity = (Student) q.getSingleResult();
            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            studentEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return studentEntity;
    }

    public Lecturer findLecturer(String username){
        lecturerEntity = null;
        try{
            Query q = em.createQuery("SELECT l FROM Lecturer l WHERE l.username=:username");
            q.setParameter("username", username);
            lecturerEntity = (Lecturer) q.getSingleResult();
            System.out.println("Lecturer " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Lecturer " + username + " does not exist.");
            lecturerEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return lecturerEntity;
    }
    
    public Guest findGuest(String username){
        guestEntity = null;
        try{
            Query q = em.createQuery("SELECT g FROM Guest g WHERE g.username=:username");
            q.setParameter("username", username);
            guestEntity = (Guest) q.getSingleResult();
            System.out.println("Guest " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Guest " + username + " does not exist.");
            guestEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return guestEntity;
    }
}
