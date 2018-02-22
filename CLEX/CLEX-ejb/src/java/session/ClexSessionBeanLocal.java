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
import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import entity.Task;
import entity.User;
import java.util.List;
import java.util.Vector;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ClexSessionBeanLocal {

    public void createStudent(String username, String password, String name, String email, String school, Long contactNum, String salt,
                String faculty, String major, String matricYear, String matricSem, String currentYear, double cap);
    public void createLecturer(String username, String password, String name, String email, String school, Long contactNum, String salt,
                String faculty);
    public void createGuest(String username, String password, String name, String email, String school, Long contactNum, String salt);
    public void createAdmin(String username, String password, String name, String email, String school, Long contactNum, String salt);
    
    public boolean checkNewUser(String username);
    public boolean checkNewCourse(String moduleCode);
    public boolean checkNewAdmin(String username);
    public boolean checkNewLecturer(String username);
    public boolean checkNewStudent(String username);
    /*public String viewModule(String moduleCode); */
    public boolean updateStudentEmail(String username, String newEmail);
    public boolean updateStudentFaculty(String username, String faculty);
    public boolean updateStudentContact(String username, Long contactNum);
    
    public boolean checkPassword(String username, String password);
    public String removeTask(Long taskId);
    public String resetPassword(String username);
    
    public User findUser(String username);
    public Student findStudent(String username);
    public Lecturer findLecturer(String username);
    public Admin findAdmin(String username);
    public Guest findGuest(String username);
    public Module findModule(String moduleCode, String takenYear, String takenSem);
    public Course findCourse(String moduleCode);
    public Task findTask(Long taskId);


    public void createCourse(String moduleCode, String moduleName, String moduleInfo ,boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload);
        
    public void createTimeslot(String date, String timeFrom, String timeEnd, String title, String details, String venue);
    public void createTask(String date, String deadline, String title,String details, String status);
    public void dragAllNusMods(String url);
    public void getTimetable(String moduleCode);
    public void createGroupTask(String date, String deadline, String title,
            String details, String status, ProjectGroup pojectGroup);
    public void createSuperGroup(int numOfGroups, int minStudentNum, int maxStudentNum, Module module);
    public void createProjectGroup(SuperGroup superGroup, double cost);

    void createModule(String takenYear, String takenSem, String prerequisite, String preclusions, Course course);

    public void createStudyPlan(String pickYear, String pickSem, Course course, Student student);
    
}
