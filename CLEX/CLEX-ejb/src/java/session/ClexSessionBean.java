/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Student;
import entity.Module;
import entity.Course;
import entity.Lecturer;
import java.util.Collection;
import java.util.Set;
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
    public void createStudent(String username, String password, String name, String email, String school, Long contactNum, 
                String faculty, String major, String matricYear, String matricSem, String currentYear, double cap){
        studentEntity = new Student();
        studentEntity.createStudent(username, password, name, email, school, contactNum, 
                 faculty, major, matricYear, matricSem, currentYear, cap);
        em.persist(studentEntity);
        em.flush();
    }
    
    @Override
    public boolean checkNewUser(String username) {
        if(findStudent(username) == null&&findAdmin(username) == null&&findLecturer(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewAdmin(String username) {
        if(findAdmin(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewStudent(String username) {
        if(findStudent(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewLecturer(String username) {
        if(findLecturer(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewModule(String moduleCode) {
        if(findModule(moduleCode) == null){
            return true;
        }
        return false;
    }
    
    private Student findStudent(String username){
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
    
    private Lecturer findLecturer(String username){
        Lecturer l = new Lecturer();
        l = null;
        try{
            Query q = em.createQuery("SELECT l FROM Lecturer l WHERE l.username=:username");
            q.setParameter("username", username);
            l = (Lecturer) q.getSingleResult();
            System.out.println("Lecturer " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Lecturer " + username + " does not exist.");
            l = null;
        }
        return l;
    }
    
    private Admin findAdmin(String username){
        Admin a = new Admin();
        a = null;
        try{
            Query q = em.createQuery("SELECT a FROM Admin a WHERE a.username=:username");
            q.setParameter("username", username);
            a = (Admin) q.getSingleResult();
            System.out.println("Admin " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Admin " + username + " does not exist.");
            a = null;
        }
        return a;
    }
    
    private Module findModule(String moduleCode){
        Module m = new Module();
        m = null;
        try{
            Query q = em.createQuery("SELECT m FROM Module m WHERE m.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            m = (Module) q.getSingleResult();
            System.out.println("Module " + moduleCode + " found.");
        }
        catch(NoResultException e){
            System.out.println("Module " + moduleCode + " does not exist.");
            m = null;
        }
        return m;
    }
    
    private Course findCourse(String moduleCode){
        Course c = new Course();
        c = null;
        try{
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            c = (Course) q.getSingleResult();
            System.out.println("Course " + moduleCode + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            c = null;
        }
        return c;
    }

    @Override
    public String viewModule(String moduleCode) {
        String result;
        Module module = findModule(moduleCode);
        if (module == null) {
            return "Module not found!\n";
        } else {
            result = "Module Code: " + module.getCourse().getModuleCode() + "\n";
            result = result + "Modular Credits: " + module.getModularCredits() + "\n";
            result = result + "Workload: " + module.getWorkload() + "\n";
            result = result + "Registered Students: \n";
            Collection<Student> students = module.getStudents();
            if (students.isEmpty()) {
                result = result + "No registered students!";
            }
            for (Student student : students) {
                result = result + student.getUsername() + " " + student.getName() + " " + student.getEmail() + "\n";
            }
            return result;
        }
    }


}
