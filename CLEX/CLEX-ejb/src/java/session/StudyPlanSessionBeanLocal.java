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
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface StudyPlanSessionBeanLocal {

    public void createStudyPlan(String pickYear, String pickSem, String moduleCode, Student student);

    public Student findStudent(String username);

    public Course findCourse(String moduleCode);

    public boolean checkStudyPlan(String username, String moduleCode);

    public void updateStudyPlan(String pickYear, String pickSem, String moduleCode, String username);

    public void changeStudyPlan();

    public void removeStudyPlan(String username, String moduleCode);
    
    public void capCalculator(String username);

    public Collection<Module> getTakenModules(String username);

    public ArrayList<StudyPlan> getAllStudyPlans(Student student);

    public Module findModule(String takenYear, String takenSem, String moduleCode);

    public void setStudentTakenModules(String username, String moduleCode, String takenYear, String takenSem);

    public int checkNumOfSemTaken(String username);

    public ArrayList<ArrayList<Course>> getTakenModulesInOrder(String username);

    public void updateAllStudyPlans(String username);

    public ArrayList<ArrayList<StudyPlan>> getStudyPlanInOrder(Student s);

    public ArrayList<ArrayList<Grade>> getAllGradesInOrder(Student s);

    public StudyPlan findStudyPlan(String username, String moduleCode);

    public int getNumOfCredits(ArrayList<Grade> grades);

    public double updateExpectedCapTwo(int allCredits, double cap, int newModuleCredit, String newModuleGrade);

    public double updateExpectedCapOne(int allCredits, double cap, int newModuleCredit, String oldGrade);

    public double updateExpectedCapFour(int allCredits, double cap, int newModuleCredit, String newModuleGrade, String oldGrade);

    public HashMap<String, String> getExpectedCourseGrade(String username);

    public List<Course> getAllCourses();

    public ArrayList<Grade> getAllGrades(Student student);

    public ArrayList<Module> getCurrentModules(Student student);

    public Grade findGrade(Long id);

    public Module findModuleById(Long id);

    public StudyPlan findStudyPlanById(Long id);

    public void updateGradeYearSem(Long id, String moduleCode, int newYear, int newSem);

    public void updateStudyPlanYearSem(Long id, int newYear, int newSem);

    public void addTakingModule(String pickYear, String pickSem, String moduleCode, Student student);

    public void removeModule(Student student, Module module);

    public void removeGrade(Student student, Grade grade);

    public void addGrade(String pickYear, String pickSem, String moduleCode, Student student, String moduelGrade);

    public boolean checkInSP(ArrayList<StudyPlan> all, String moduleCode);

    public boolean checkInCM(ArrayList<Module> all, String moduleCode);

    public boolean checkInGrade(ArrayList<Grade> all, String moduleCode);
    
    
}
