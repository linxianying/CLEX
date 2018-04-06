/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Course;
import entity.Guest;
import entity.Lecturer;
import entity.Lesson;
import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import entity.Task;
import entity.Timeslot;
import entity.Transaction;
import entity.User;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ClexSessionBeanLocal {

    public void createStudent(String username, String password, String name, String studentId,
            String email, String school, Long contactNum, String salt,
            String faculty, String major, String matricYear, String matricSem, double cap);

    public void createLecturer(String username, String password, String name,
            String email, String school, Long contactNum, String salt,
            String faculty);

    public void createGuest(String username, String password, String name,
            String email, String school, Long contactNum, String salt);

    public void createAdmin(String username, String password, String name,
            String email, String school, Long contactNum, String salt);

    public void createCourse(String moduleCode, String moduleName, String moduleInfo,
            boolean discontinuedBool, String discountinuedYear, String discountinuedSem,
            String offeredSem, String school, String moduleCredit, String workload);

    public void createProjectGroup(SuperGroup superGroup, String name, double cost);

    public void createProjectGroupTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup projectGroup);
    
    public void createModule(String takenYear, String takenSem, String prerequisite,
            String preclusions, Course course);

    public void createStudyPlan(String pickYear, String pickSem, Course course, Student student);

    public void createLesson(String day, String timeFrom, String timeEnd,
            String type, String venue, Module module);

    public Timeslot createTimeslot(String title, String startDate, String endDate,
            String details, String venue);

//    public void createGrade(String moduleGrade, Module module, Student student);

    public boolean checkNewUser(String username);

    public boolean checkNewCourse(String moduleCode);

    public boolean checkNewAdmin(String username);

    public boolean checkNewLecturer(String username);

    public boolean checkNewStudent(String username);

    public boolean checkPassword(String username, String password);

    /*public String viewModule(String moduleCode); */
    public boolean updateStudentEmail(String username, String newEmail);

    public boolean updateStudentFaculty(String username, String faculty);

    public boolean updateStudentContact(String username, Long contactNum);

    public User findUser(String username);

    public Student findStudent(String username);

    public Lecturer findLecturer(String username);

    public Admin findAdmin(String username);

    public Guest findGuest(String username);

    public Module findModule(String moduleCode, String takenYear, String takenSem);

    public Course findCourse(String moduleCode);

    public Lesson findLesson(String day, String timeFrom, String type, Module module);

    public void dragAllNusMods(String url);

    public void getTimetable(String moduleCode);

    public void changePassword(String username, String newPassword);
    
    public String resetPassword(String username);

    public void setStudentTakenModules(Student student, Module module, String grade);

    public void setStudentLesson(Student student, Lesson lesson);

    public void linkLecturerModule(String username, String moduleCode, String takenYear, String takenSem);

    public void linkStudentModule(String username, String moduleCode, String takenYear, String takenSem);

    public ProjectGroup findProjectgroup(String name, Module module);

    public void linkStudentGroup(Student student, ProjectGroup projectGroup);

    public Transaction createTransaction(double cost, String date, String activity, ProjectGroup group);

    public void createLedger(Student student, double ascCost, double pay, Transaction transaction);

    public void confirmGroupFormation(Module module);

    public void setStudentTakingModules(Student student, Module module);
    
    public void createSuperGroup(int numOfGroups, int avgStudentNum, int minStudentNum, int maxStudentNum, Module module);

    public void createSuperGroup(int numOfGroups, int avgStudentNum, Module module);

    public void createSuperGroupWithMax(int numOfGroups, int avgStudentNum, int maxStudentNum, Module module);

    public void createSuperGroupWithMin(int numOfGroups, int avgStudentNum, int minStudentNum, Module module);
    
}
