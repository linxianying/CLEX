/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Task;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ToDoListSessionBeanLocal {
    public void setTaskUrgency(Long TaskId, String urgency);
    public void createTask(String date, String deadline, String title,String details, String status);
    public String removeTask(Long taskId);
    public Task findTask(Long taskId);
    public void updateTask(String date, String deadline, String title,String details, String status, String urgency);
    
}
