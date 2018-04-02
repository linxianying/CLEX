/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ProjectGroup;
import entity.GroupTask;
import entity.Student;
import entity.Task;
import entity.User;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ToDoListSessionBeanLocal {
    
    public Task createTask(String username, String date, String deadline, 
            String title,String details, String status);
    public void createGroupTask(String date, String deadline, String title, 
            String details, String status, ProjectGroup projectGroup, 
            String[] users);
    public void createIndividualGroupTask(String username, String date, 
            String deadline, String title,String details, String status);
    
    public void linkTaskStudent(Long taskId, String name);
    
    public void updateTask(Long taskId, String date, String deadline, 
            String title,String details, String status, String urgency);
    public void updateGroupTask(Long taskId, String date, String deadline, 
            String title,String details, String status, String urgency);
    public void setTaskUrgency(Long TaskId, String urgency);
    public void setGroupTaskUrgency(Long TaskId, String urgency);
    
    public Task findTask(Long taskId);
    public GroupTask findGroupTask(Long taskId);
    
    public void finishTask(Long taskId);
    public void unfinishTask(Long taskId);
    public void finishGroupTask(Long taskId);
    public void unfinishGroupTask(Long taskId);
    public Student findStudent(String username);
    
    public boolean removeTask(Long taskId, User user);
    public String removeGroupTask(Long taskId, ProjectGroup projectGroup) ;
    
    public int calculateTaskOverview(Student student);

    public int calculateDailyTaskOverview(Student student);

}
