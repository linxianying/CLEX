/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.GroupTask;
import entity.Guest;
import entity.Lecturer;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import entity.Task;
import entity.Timeslot;
import entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author lin
 */
@Stateless
public class ToDoListSessionBean implements ToDoListSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext
    EntityManager em;
    private User userEntity;
    private Student studentEntity;
    private Lecturer lecturerEntity;
    private Guest guestEntity;
    private Course courseEntity;
    private Timeslot timeslotEntity;
    private Task taskEntity;
    private GroupTask groupTaskEntity;
    private ProjectGroup projectGroupEntity;
    private SuperGroup superGroupEntity;
    
    
    @Override
    public void setTaskUrgency(Long TaskId, String urgency){
        taskEntity = null;
        taskEntity = findTask(TaskId);
        taskEntity.setUrgency(urgency);
        em.merge(taskEntity);
        em.flush();
    }
    
    @Override
    public Task findTask(Long taskId){
        Task t = new Task();
        t = null;
        try{
            Query q = em.createQuery("SELECT t FROM Task t WHERE t.taskId=:taskId");
            q.setParameter("taskId", taskId);
            t = (Task) q.getSingleResult();
            System.out.println("Task " + taskId + " found.");
        }
        catch(NoResultException e){
            System.out.println("Task " + taskId + " does not exist.");
            t = null;
        }
        return t;
    }
    
    @Override
    public String removeTask(Long taskId) {
        Task task = findTask(taskId);
        if (task==null) {
            return "Task not found!\n";
        }
        else{
            task = em.find(Task.class, taskId);
            em.remove(task);
            em.flush();
            em.clear();
        }
        return "Tutorial is sucessfully deleted!\n";
    }
    @Override
    public void createTask(String date, String deadline, String title,String details, String status){
        taskEntity = new Task();
        taskEntity.createTask(date, deadline, title, details, status);
        em.persist(taskEntity);
        em.flush();
    
    }
    
    @Override
    public void updateTask(String date, String deadline, String title,String details, String status, String urgency){
        taskEntity.setDate(date);
        taskEntity.setDeadline(deadline);
        taskEntity.setDetails(details);
        taskEntity.setStatus(status);
        taskEntity.setTitle(title);
        taskEntity.setUrgency(urgency);
        em.persist(taskEntity);
        em.flush();
    }
}
