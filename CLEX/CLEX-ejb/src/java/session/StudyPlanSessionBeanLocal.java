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
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface StudyPlanSessionBeanLocal {

    public void createStudyPlan(String pickYear, String pickSem, Course course, Student student);

    public Student findStudent(String username);

    public Course findCourse(String moduleCode);

    public void addStudyPlan(String pickYear, String pickSem, String moduleCode, String username);

    public boolean checkStudyPlan(String username, String moduleCode);

    public void updateStudyPlan(String username, String moduleCode, String pickYear, String pickSem);

    public void changeStudyPlan();

    public void removeStudyPlan(String username, String moduleCode);
    
    public void capCalculator(String username);

    public void viewStudyPlan(String usrname);

    public Collection<Module> getTakenModules(String username);

    public ArrayList<StudyPlan> getAllStudyPlans(String username);

    public ArrayList<Course> testViewTakenModules();

    public Module findModule(String takenYear, String takenSem, String moduleCode);

    public void setStudentTakenModules(String username, String moduleCode, String takenYear, String takenSem);

    public int checkNumOfSemTaken(String username);

    public ArrayList<Course> getTakenCourses(String username);

    public ArrayList<ArrayList<Course>> getTakenModulesInOrder(String username);

    public void updateAllStudyPlans(String username);

    public ArrayList<ArrayList<StudyPlan>> getStudyPlanInOrder(String username);

    public boolean checkStudentModule(String username, String moduleCode);

    public boolean checkCourseExistance(String moduleCode);

    public ArrayList<Grade> getAllGrades(String username);

    public ArrayList<ArrayList<Grade>> getAllGradesInOrder(String username);

    public ArrayList<Module> getCurrentModules(String username);

    public StudyPlan findStudyPlan(String username, String moduleCode);
    
    
}
