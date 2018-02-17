/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Student;
import entity.StudyPlan;
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
public class StudyPlanSessionBean implements StudyPlanSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    private String pickYear;
    private String pickSem;
    private String username;
    private String moduleCode;
    
    private StudyPlan studyPlan;
    private Student student;
    private Course course;

    
    
    
    @Override
    public void createStudyPlan() {
        try {
        //create new studyPlan entity
        studyPlan = new StudyPlan();
        studyPlan.createStudyPlan(pickYear, pickSem, course, student);
        //set relationship between StudyPlan and Student
        this.student.getStudyPlan().add(studyPlan);
        //set relationship between StudyPlan and course
        studyPlan.setCourse(course);
        
        em.persist(studyPlan);
        }
        catch(Exception e){
            System.out.println("StudyPlanSessionBean: createStudyPlan method:");
            e.printStackTrace();
        }
    }

    
    
    @Override
    public void findStudent(String username) {
        Student u = new Student();
        u = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            u = (Student) q.getSingleResult();
            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            u = null;
        }
        this.student = u;
    }

    @Override
    public void findCourse(String moduleCode) {
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
        this.course = c;
    }

    @Override
    public void addStudyPlan(String username, String moduleCode, String pickYear, String pickSem) {
        this.findStudent(username);
        this.findCourse(moduleCode);
        this.pickSem = pickSem;
        this.pickYear = pickYear;
        this.createStudyPlan();
    }
    
    
    
    
    
}