/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Grade;
import entity.Module;
import entity.Student;
import entity.StudyPlan;
import java.util.ArrayList;
import java.util.Collection;
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
    private Module module;
    private StudyPlan studyPlan;
    private Student student;
    private Course course;

    private ArrayList<Course> takenCourses;
    private Collection<StudyPlan> studyPlans;
    private double calculatedCap; 


     @Override
    public Student findStudent(String username) {
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
        return student;
    }

    @Override
    public Course findCourse(String moduleCode) {
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
        return course;
    }
    
    @Override
    public Module findModule(String takenYear, String takenSem, String moduleCode) {
        Module m = new Module();
        m = null;
        try{
            Query q = em.createQuery("SELECT m FROM SchoolModule m WHERE "
                    + "m.takenYear:=takenYear AND m.takenSem:=takenSem AND "
                    + "m.course.moduleCode:=moduleCode");
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            q.setParameter("moduleCode", moduleCode);
            m = (Module) q.getSingleResult();
            System.out.println("Module " + moduleCode + ", takenYear="+takenYear+
                    ", takenSem=" + takenSem +" found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            m = null;
        }
        return m;
    }
    
    // find all courses taken by the user
    @Override
    public ArrayList<Course> getTakenModules(String username) {
        findStudent(username);
        Collection<Module> modules = this.student.getModules();
        for (Module m: modules) {
            this.takenCourses.add(m.getCourse());
        }
        return takenCourses;
    }
    
    // find all studyPlan the user has
    @Override
    public Collection<StudyPlan> getAllStudyPlans(String username) {
        student = findStudent(username);
        this.studyPlans = student.getStudyPlan();
        return studyPlans;
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
            Query q = em.createQuery("SELECT s FROM StudyPlan s WHERE s.student.id =:studentId AND s.course.id =:courseId");
            q.setParameter("studentId", studentId);
            q.setParameter("courseId", courseId);
            s = (StudyPlan)q.getSingleResult();
            System.out.println("StudyPlan " + "with user:" + username + ", course:" + moduleCode + " found.");
        }
        catch (NoResultException e) {
            System.out.println("StudyPlanSessionBean: findStudyPlan method: No result");
            return false;
        }
        catch(Exception e){ 
            System.out.println("StudyPlanSessionBean: findStudyPlan method: exception");
            e.printStackTrace();
        }
        
        this.studyPlan = s;
        return true;
    }
    
    //the actual create studyPlan enity adn set relationship
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

    //check whether it's in DB or not, if not, create one by calling method createStudyPlan.
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
    public void changeStudyPlan() {
        this.studyPlan.setPickSem(this.pickSem);
        this.studyPlan.setPickYear(this.pickYear);
        em.persist(this.studyPlan);
        em.flush();
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

    
    private double capCulculator(Student student){
        double cap = 0.0;
        int num = 0;
        Collection<Grade> grades = student.getGrades();
        for (Grade grade : grades) {
            String tempGrade = grade.getModuleGrade();
            if(tempGrade.equals("A")||tempGrade.equals("A+")){
                cap = cap + 5;
                num++;
            }else if(tempGrade.equals("A-")){
                cap = cap + 4.5;
                num++;
            }else if(tempGrade.equals("B+")){
                cap = cap + 4;
                num++;
            }else if(tempGrade.equals("B")){
                cap = cap + 3.5;
                num++;
            }else if(tempGrade.equals("B-")){
                cap = cap + 3;
                num++;
            }else if(tempGrade.equals("C+")){
                cap = cap + 2.5;
                num++;
            }else if(tempGrade.equals("C")){
                cap = cap + 2;
                num++;
            }else if(tempGrade.equals("C-")){
                cap = cap + 1.5;
                num++;
            }else if(tempGrade.equals("D+")){
                cap = cap + 1.0;
                num++;
            }else if(tempGrade.equals("D")){
                cap = cap + 0.5;
                num++;
            }else{
                num++;
            }
        }
        student.setCap((cap/num));
        return (cap/num); 
    }
    
    
    @Override
    public void capCalculator(String username) {
        findStudent(username);
        double cap = capCulculator(this.student);
        em.persist(this.student);
        em.flush();
    }

    @Override
    public void viewStudyPlan(String username) {
        this.getTakenModules(username);
        this.getAllStudyPlans(username);
    }

    @Override
    public void setStudentTakenModules(String username, String moduleCode, String takenYear, String takenSem) {
        student = this.findStudent(username);
        module = this.findModule(takenYear, takenSem, moduleCode);
        student.getModules().add(module);
        em.persist(student);
    }
    
    
    //-------------------------------------------------------------------------
    //for test urpose only
    
    
    //just to randomly create a list of courses, should call gettakenCourses instead
    @Override
    public ArrayList<Course> testViewTakenModules() {
        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(this.findCourse("IS3106"));
        courses.add(this.findCourse("IS2103"));
        courses.add(this.findCourse("IS4100"));
        courses.add(this.findCourse("IS2102"));
        System.out.println(courses);
        System.out.println("sp session bean: testViewTakenModules finish ");
        return courses;
    }
    
    

    @Override
    public void testAddTakenModules() {
        
    }

   

    

    

    
    
    

}