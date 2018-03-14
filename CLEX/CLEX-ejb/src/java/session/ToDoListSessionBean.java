/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.GroupTask;
import entity.Guest;
import entity.IndividualGroupTask;
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
    private IndividualGroupTask indGroupTaskEntity;
    
    
    @Override
    public void setTaskUrgency(Long TaskId, String urgency){
        taskEntity = null;
        taskEntity = findTask(TaskId);
        taskEntity.setUrgency(urgency);
        em.merge(taskEntity);
        em.flush();
    }
    
    @Override
    public Task findTask(Long id){
        taskEntity = null;
        try{
            Query q = em.createQuery("SELECT t FROM Task t WHERE t.id=:id");
            q.setParameter("id", id);
            taskEntity = (Task) q.getSingleResult();
            System.out.println("Task " + id + " found.");
        }
        catch(NoResultException e){
            System.out.println("Task " + id + " does not exist.");
            taskEntity = null;
        }
        return taskEntity;
    }
    
    @Override
    public String removeTask(Long taskId, User user) {
        Task task = findTask(taskId);
        if (task==null||user==null) {
            return "Task not found or user not found!\n";
        }
        else{
            //task = em.find(Task.class, taskId);
            user.getTasks().remove(task);
            em.merge(user);
            em.remove(task);
            em.flush();

        }
        return "Task is sucessfully deleted!\n";
    }
    
    @Override
    public String removeGroupTask(Long taskId, ProjectGroup projectGroup) {
        groupTaskEntity = findGroupTask(taskId);
        if (groupTaskEntity==null||projectGroup==null) {
            return "GroupTask not found or projectGroup not found!\n";
        }
        else{
            //task = em.find(Task.class, taskId);
            projectGroup.getGroupTasks().remove(groupTaskEntity);
            em.merge(projectGroup);
            em.remove(groupTaskEntity);
            em.flush();

        }
        return "Group Task is sucessfully deleted!\n";
    }
    
    @Override
    public void createTask(String username, String date, String deadline, String title,String details, String status){
        
        studentEntity = findStudent(username);
        if(studentEntity==null){
            System.out.println("Student " + username + " does not exist. Create Task failed.");
        }else{
            taskEntity = new Task();
            taskEntity.createTask(date, deadline, title, details, status);
            taskEntity.setStudent(studentEntity);
            studentEntity.getTasks().add(taskEntity);
            em.persist(taskEntity);
            em.merge(studentEntity);
            em.flush();
        }
    
    }
    
    public void linkIndividualTaskStudent(IndividualGroupTask indGroupTaskEntity, String username) {
    //To change body of generated methods, choose Tools | Templates.
        studentEntity = findStudent(username);
        if(studentEntity==null){
            System.out.println("Student " + username + " does not exist.");
        }else if(taskEntity==null){
            System.out.println("Task does not exist.");
        }else{
            indGroupTaskEntity.setStudent(studentEntity);
            studentEntity.getIndividualGroupTasks().add(indGroupTaskEntity);
            em.merge(indGroupTaskEntity);
            em.merge(studentEntity);
            em.flush();
        }
    }
    
    @Override
    public void createIndividualGroupTask(String username, String date, String deadline, String title,String details, String status){
        indGroupTaskEntity = new IndividualGroupTask();
        indGroupTaskEntity.createIndividualGroupTask(date, deadline, title, details, status);
        linkIndividualTaskStudent(indGroupTaskEntity, username);
        em.persist(indGroupTaskEntity);
        em.flush();
    
    }
    
    @Override
    public void updateGroupTask(Long taskId, String date, String deadline, String title,String details, String status, String urgency){
        groupTaskEntity = findGroupTask(taskId);
        if(groupTaskEntity!=null){
            if(!date.equals(""))
                groupTaskEntity.setDate(date);
            if(!deadline.equals(""))
                groupTaskEntity.setDeadline(deadline);
            if(!details.equals(""))
                groupTaskEntity.setDetails(details);
            if(!status.equals(""))
                groupTaskEntity.setStatus(status);
            if(!title.equals(""))
                groupTaskEntity.setTitle(title);
            if(!urgency.equals(""))
                groupTaskEntity.setUrgency(urgency);
            em.merge(groupTaskEntity);
            em.flush();
            System.out.println("ToDoList Session Bean updateTask: Group Task found! Updation performed");
        }else{
            System.out.println("ToDoList Session Bean updateTask: Group Task not found! ");
        }
    }
    
    @Override
    public void updateTask(Long taskId, String date, String deadline, String title,String details, String status, String urgency){
        taskEntity = findTask(taskId);
        if(taskEntity!=null){
            if(!date.equals(""))
                taskEntity.setDate(date);
            if(!deadline.equals(""))
                taskEntity.setDeadline(deadline);
            if(!details.equals(""))
                taskEntity.setDetails(details);
            if(!status.equals(""))
                taskEntity.setStatus(status);
            if(!title.equals(""))
                taskEntity.setTitle(title);
            if(!urgency.equals(""))
                 taskEntity.setUrgency(urgency);
            em.merge(taskEntity);
            em.flush();
            System.out.println("ToDoList Session Bean updateTask: Task found! Updation performed");
        }else{
            System.out.println("ToDoList Session Bean updateTask: Task not found! ");
        }
    }
    
    @Override
    public void finishTask(Long taskId){
        taskEntity = findTask(taskId);
        taskEntity.setStatus("finished");
        em.merge(taskEntity);
        em.flush();
    }
    
    @Override
    public void finishGroupTask(Long taskId){
        groupTaskEntity = findGroupTask(taskId);
        groupTaskEntity.setStatus("finished");
        em.merge(groupTaskEntity);
        em.flush();
    }
    
    @Override
    public GroupTask findGroupTask(Long id){
        groupTaskEntity = new GroupTask();
        groupTaskEntity = null;
        try{
            Query q = em.createQuery("SELECT t FROM GroupTask t WHERE t.id=:id");
            q.setParameter("id", id);
            groupTaskEntity = (GroupTask) q.getSingleResult();
            System.out.println("GroupTask " + id + " found.");
        }
        catch(NoResultException e){
            System.out.println("GroupTask " + id + " does not exist.");
            groupTaskEntity = null;
        }
        return groupTaskEntity;
        
    }
    
    @Override
    public void createGroupTask(String date, String deadline, String title,
        String details, String status, ProjectGroup projectGroup, String[] users){
        groupTaskEntity = new GroupTask();
        groupTaskEntity.createGroupTask(date, deadline, title,details, status, projectGroup);
        for(int i=0;i<users.length;i++){
            createIndividualGroupTask(users[i], date, deadline, title, details+"("+projectGroup.getId()+")", status);
        }
        em.persist(groupTaskEntity);
        em.flush();
    }

    @Override
    public void setGroupTaskUrgency(Long TaskId, String urgency) {
         //To change body of generated methods, choose Tools | Templates.
        groupTaskEntity = findGroupTask(TaskId);
        groupTaskEntity.setUrgency(urgency);
        em.merge(groupTaskEntity);
        em.flush();
    }

    @Override
    public void linkTaskStudent(Long taskId, String username) {
    //To change body of generated methods, choose Tools | Templates.
        taskEntity = findTask(taskId);
        studentEntity = findStudent(username);
        if(studentEntity==null){
            System.out.println("Student " + username + " does not exist.");
        }else if(taskEntity==null){
            System.out.println("Task " + taskId + " does not exist.");
        }else{
            taskEntity.setStudent(studentEntity);
            studentEntity.getTasks().add(taskEntity);
            em.merge(taskEntity);
            em.merge(studentEntity);
            em.flush();
        }
    }
    
    @Override
    public Student findStudent(String username) {
        studentEntity = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            studentEntity = (Student) q.getSingleResult();
            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            studentEntity = null;
        }
        return studentEntity;
    }

}
