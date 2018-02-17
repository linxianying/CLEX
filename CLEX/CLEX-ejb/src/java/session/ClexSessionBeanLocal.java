/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Course;
import entity.Lecturer;
import entity.Module;
import entity.Student;
import entity.Task;
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
    public boolean checkNewUser(String username);
    public boolean checkNewModule(String moduleCode);
    public boolean checkNewAdmin(String username);
    public boolean checkNewLecturer(String username);
    public boolean checkNewStudent(String username);
    public String viewModule(String moduleCode);
    public boolean updateStudentEmail(String username, String newEmail);
    public boolean updateStudentFaculty(String username, String faculty);
    public boolean checkPassword(String username, String password);
    public String removeTask(Long taskId);
    public boolean updateStudentContact(String username, Long contactNum);
    public Student findStudent(String username);
    public Lecturer findLecturer(String username);
    public Admin findAdmin(String username);
    public Module findModule(String moduleCode);
    public Course findCourse(String moduleCode);
    public Task findTask(Long taskId);
}
