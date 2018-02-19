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
        this.username = username;
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
        this.moduleCode = moduleCode;
        this.course = c;
    }
    
    //find whether this studyplan exits, if not, set this.studyPlan to it
    @Override
    public boolean findStudyPlan(String username, String moduleCode) {
        this.findStudent(username);
        this.findCourse(moduleCode);
        Long studentId = this.student.getId();
        Long courseId = this.course.getId();
        
        StudyPlan s = new StudyPlan();
        s = null;
        try {
            Query q = em.createQuery("SELECT s FROM STUDYPLAN S WHERE "
                    + "S.STUDENT_ID=:studentId AND S.COURSE_ID=:courseId");
            q.setParameter("studentId", studentId);
            q.setParameter("courseId", courseId);
            s = (StudyPlan)q.getSingleResult();
            System.out.println("StudyPlan " + "with user:" + username + 
                    ", course:" + moduleCode + " found.");
        }
        catch (NoResultException e) {
            System.out.println("StudyPlanSessionBean: findStudyPlan method: No result");
            return false;
        }
        catch(Exception e){ 
            System.out.println("StudyPlanSessionBean: findStudyPlan method:");
            e.printStackTrace();
        }
        
        this.studyPlan = s;
        return true;
    }
    
    @Override
    public void changeStudyPlan() {
        this.studyPlan.setPickSem(this.pickSem);
        this.studyPlan.setPickYear(this.pickYear);
        em.persist(this.studyPlan);
        em.flush();
    }
    
    
    
    @Override
    public void addStudyPlan(String username, String moduleCode, String pickYear, String pickSem) {
        //if the studyplan is not found
        if (!findStudyPlan(username, moduleCode)) {
            this.pickSem = pickSem;
            this.pickYear = pickYear;
            this.createStudyPlan();
        }
        //if the studyPlan is in DB already
        else {
            System.out.println("StudyPlanSessionBean: addStudyPlan method: "
                    + "studyplan with user:" + username + ", moduleCode:" 
                    + moduleCode + "alrady exists");
        }
    }
  
    
    @Override
    public void updateStudyPlan(String username, String moduleCode, String pickYear, String pickSem) {
        //if the studyplan is found
        if (findStudyPlan(username, moduleCode)) {
            this.pickSem = pickSem;
            this.pickYear = pickYear;
            this.changeStudyPlan();
        }
        //if the studyPlan is not in DB, create one
        else {
            addStudyPlan(username, moduleCode, pickYear, pickSem);
        }
    }

    @Override
    public void removeStudyPlan(String username, String moduleCode) {
        findStudyPlan(username, moduleCode);
        em.remove(this.studyPlan);
        em.flush();
    }

    
    
    
    
    

    

    
    
    
    
    
    
    
    
}
