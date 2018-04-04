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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
    public boolean removeTask(Long taskId, User user) {
        Task task = findTask(taskId);
        if (task==null||user==null) {
            return false;
        }
        else{
            //task = em.find(Task.class, taskId);
            user.getTasks().remove(task);
            em.merge(user);
            em.remove(task);
            em.flush();

        }
        return true;
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
    public Task createTask(String username, String date, String deadline, String title,String details, String status){
        
        studentEntity = findStudent(username);
        if(studentEntity==null){
            System.out.println("Student " + username + " does not exist. Create Task failed.");
            taskEntity = null;
        }else{
            taskEntity = new Task();
            taskEntity.createTask(date, deadline, title, details, status);
            taskEntity.setUser(studentEntity);
            studentEntity.getTasks().add(taskEntity);
            em.merge(studentEntity);
            em.persist(taskEntity);
            em.flush();
            
        }
        return taskEntity;
    }
    
    public void linkIndividualTaskStudent(IndividualGroupTask indGroupTaskEntity, String username) {
        studentEntity = findStudent(username);
        if(studentEntity==null){
            System.out.println("Student " + username + " does not exist.");
        }else if(indGroupTaskEntity==null){
            System.out.println("IndividualGroupTask does not exist.");
        }else{
            indGroupTaskEntity.setStudent(studentEntity);
            studentEntity.getIndividualGroupTasks().add(indGroupTaskEntity);
            System.out.println("linkIndividualTaskStudent: " + indGroupTaskEntity.getId());
            em.merge(indGroupTaskEntity);
            em.merge(studentEntity);
            em.flush();
        }
    }
    
    @Override
    public IndividualGroupTask createIndividualGroupTask(String username, String date, String deadline, String title,String details, String status){
        indGroupTaskEntity = new IndividualGroupTask();
        indGroupTaskEntity.createIndividualGroupTask(date, deadline, title, details, status);
        em.persist(indGroupTaskEntity);
        em.flush();
        linkIndividualTaskStudent(indGroupTaskEntity, username);
        
        System.out.println("createIndividualGroupTask: " + indGroupTaskEntity.getId());
        return indGroupTaskEntity;
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
    public void unfinishTask(Long taskId){
        taskEntity = findTask(taskId);
        taskEntity.setStatus("unfinished");
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
    public void finishIndGroupTask(Long taskId){
        indGroupTaskEntity = findIndGroupTask(taskId);
        indGroupTaskEntity.setStatus("finished");
        em.merge(indGroupTaskEntity);
        em.flush();
    }
    
    @Override
    public void unfinishIndGroupTask(Long taskId){
        indGroupTaskEntity = findIndGroupTask(taskId);
        indGroupTaskEntity.setStatus("unfinished");
        em.merge(indGroupTaskEntity);
        em.flush();
    }
    
    @Override
    public void unfinishGroupTask(Long taskId){
        groupTaskEntity = findGroupTask(taskId);
        groupTaskEntity.setStatus("unfinished");
        em.merge(groupTaskEntity);
        em.flush();
    }
    
    @Override
    public GroupTask findGroupTask(Long id){
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
    public IndividualGroupTask findIndGroupTask(Long id){
        indGroupTaskEntity = null;
        try{
            Query q = em.createQuery("SELECT t FROM IndividualGroupTask t WHERE t.id=:id");
            q.setParameter("id", id);
            indGroupTaskEntity = (IndividualGroupTask) q.getSingleResult();
            System.out.println("IndividualGroupTask " + id + " found.");
        }
        catch(NoResultException e){
            System.out.println("IndividualGroupTask " + id + " does not exist.");
            indGroupTaskEntity = null;
        }
        return indGroupTaskEntity;
        
    }
    
    @Override
    public void createGroupTask(String date, String deadline, String title,
        String details, String status, ProjectGroup projectGroup, String[] users){
        groupTaskEntity = new GroupTask();
        groupTaskEntity.createGroupTask(date, deadline, title,details, status, projectGroup);
        em.persist(groupTaskEntity);
        em.flush();
        for(int i=0;i<users.length;i++){
            indGroupTaskEntity = createIndividualGroupTask(users[i], date, deadline, title, details+"("+projectGroup.getId()+")", status);
            indGroupTaskEntity.setGroupTask(groupTaskEntity);
            em.persist(indGroupTaskEntity);
            System.out.println("createGroupTask: individual" + indGroupTaskEntity.getId());
        }
        System.out.println("createGroupTask: group" + groupTaskEntity.getId());
        
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
            taskEntity.setUser(studentEntity);
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
    
    @Override
    public int calculateTaskOverview(Student student){
        double uncomplete = 0;
        double total = 0;
        studentEntity = student;
        Collection<Task> tasks = studentEntity.getTasks();
        Iterator itr = tasks.iterator();
        while (itr.hasNext()) {
          Task t = (Task) itr.next();
          total++;
          if(t.getStatus().equals("unfinished")){
              uncomplete++;
          }
        }
        System.out.println("The tasks overview is: " + (uncomplete/total));
        return  (int)(uncomplete/total*100);
    }

    
    @Override
    public int calculateDailyTaskOverview(Student student){
        double uncomplete = 0;
        double total = 0;
        studentEntity = student;
        Collection<Task> tasks = studentEntity.getTasks();
        Iterator itr = tasks.iterator();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        //2018-01-17
        while (itr.hasNext()) {
          Task t = (Task) itr.next();
          if(t.getDeadline().equals(fmt.format(date))){
            total++;
            if(t.getStatus().equals("unfinished")){
                uncomplete++;
            }
          }
        }
        System.out.println("The tasks overview is: " + (uncomplete/total));
        return  (int)(uncomplete/total*100);
    }
}
