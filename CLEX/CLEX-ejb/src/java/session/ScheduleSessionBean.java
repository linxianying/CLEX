/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Course;
import entity.GroupTask;
import entity.GroupTimeslot;
import entity.Guest;
import entity.Lecturer;
import entity.Lesson;
import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import entity.Task;
import entity.Timeslot;
import entity.User;
import java.awt.Component;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javaClass.IcsReader;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import jdk.nashorn.internal.runtime.ParserException;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;

/**
 *
 * @author lin
 */
@Stateless
public class ScheduleSessionBean implements ScheduleSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext
    EntityManager em;
    private User userEntity;
    private Admin adminEntity;
    private Student studentEntity;
    private Module moduleEntity;
    private Lecturer lecturerEntity;
    private Guest guestEntity;
    private Course courseEntity;
    private Timeslot timeslotEntity;
    private Task taskEntity;
    private GroupTask groupTaskEntity;
    private ProjectGroup projectGroupEntity;
    private SuperGroup superGroupEntity;
    private Lesson lessonEntity;
    private GroupTimeslot groupTimeslotEntity;
    private ArrayList<Timeslot> timeslots;
    
    

    @Override
    public boolean loadIcsFile() {
        IcsReader ics = new IcsReader();
        ics.test();
        return false;
    }
    @Override
    public Timeslot createTimeslot(String username, String title, String startDate, 
            String endDate, String details, String venue){
        userEntity = findUser(username);
        if(userEntity==null){
            return null;
        }
        timeslotEntity = new Timeslot();
        timeslotEntity.createTimeslot(title, startDate, endDate, details, venue);
        em.persist(timeslotEntity);
        userEntity.getTimeslots().add(timeslotEntity);
        em.merge(userEntity);
        em.flush();
        System.out.print("timeslot " + timeslotEntity.getId()+" created for user " + username);
        return timeslotEntity;
    }
    

    public Student findStudent(String username){
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
        catch(Exception e) {
            e.printStackTrace();
        }
        return studentEntity;
    }
    
    public User findUser(String username){
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    
    public User findUser(Long id){
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.id = :id");
            q.setParameter("id", id);
            userEntity = (User) q.getSingleResult();
            System.out.println("User id" + id + " found.");
        }
        catch(NoResultException e){
            System.out.println("User id" + id + " does not exist.");
            userEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    
    @Override
    public void deleteTimeslot(Long id, User user) {
        
        timeslotEntity = findTimeslot(id);
        userEntity = user;
        if(userEntity!=null&&userEntity!=null){
            userEntity.getTimeslots().remove(timeslotEntity);
            em.remove(timeslotEntity);
            em.flush();
        }else{
            System.out.println("timeslot not found or user not found");
        }
    }
    
    public void deleteTimeslot(Long id) {
        
        timeslotEntity = findTimeslot(id);
        userEntity = null;
        Long userId;
        if(timeslotEntity != null){
            try{
                Query q = em.createQuery("SELECT s.User_id FROM BasicUser_Timeslot s WHERE s.Timeslot_id = :id");
                q.setParameter("Timeslot_id", id);
                //q.getParameter("User_id", userId);
                String user = (String) q.getSingleResult();
                System.out.println(user);
                userId = Long.parseLong(user);
                userEntity = findUser(userId);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            if(userEntity!=null)
                userEntity.getTimeslots().remove(timeslotEntity);
            em.remove(timeslotEntity);
            em.flush();
        }else{
            System.out.println("Timeslot "+ id +" not found");
        }
    }
    
    @Override
    public void updateTimeslot(Long id, String title, String startDate, String endDate, 
            String details, String venue) {
        timeslotEntity = findTimeslot(id);
        if(timeslotEntity != null){
            timeslotEntity.setDetails(details);
            timeslotEntity.setStartDate(startDate);
            timeslotEntity.setEndDate(endDate);
            timeslotEntity.setTitle(title);
            timeslotEntity.setVenue(venue);
            em.merge(timeslotEntity);
            em.flush();
        }else{
            System.out.println("Timeslot "+ id +" not found");
        }
    }
    
    @Override
    public Timeslot findTimeslot(Long id){
        timeslotEntity = null;
        try{
            Query q = em.createQuery("SELECT s FROM Timeslot s WHERE s.id = :id");
            q.setParameter("id", id);
            timeslotEntity = (Timeslot) q.getSingleResult();
            System.out.println("Timeslot " + id + " found.");
        }
        catch(NoResultException e){
            System.out.println("Timeslot " + id + " does not exist.");
            timeslotEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return timeslotEntity;
    }
    
        @Override
    public ArrayList<Timeslot> getAllTimeslots(User userentitity) {
        Collection<Timeslot> all = new ArrayList<Timeslot>();
        timeslots = new ArrayList<Timeslot>();
        all = userentitity.getTimeslots();
        for (Timeslot t: all) {
            timeslots.add(t);
        }
        return timeslots;
    }
    
    @Override
    public void createGroupTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup projectGroup) {
        groupTimeslotEntity = new GroupTimeslot();
        groupTimeslotEntity.createGroupTimeslot(date, timeFrom, timeEnd,title, details, venue, projectGroup);
        em.persist(groupTimeslotEntity);
        em.flush();
    }

    @Override
    public void updateGroupTimeslot(Long id, String date, String timeFrom, String timeEnd, String title, String details, String venue, ProjectGroup pojectGroup) {
         //To change body of generated methods, choose Tools | Templates.
        groupTimeslotEntity = findGroupTimeslot(id);
        if(groupTimeslotEntity != null){
            groupTimeslotEntity.setDate(date);
            groupTimeslotEntity.setDetails(details);
            groupTimeslotEntity.setTimeFrom(timeFrom);
            groupTimeslotEntity.setTimeEnd(timeEnd);
            groupTimeslotEntity.setTitle(title);
            groupTimeslotEntity.setVenue(venue);
            groupTimeslotEntity.setProjectGroup(pojectGroup);
            em.merge(groupTimeslotEntity);
            em.flush();
        }else{
            System.out.println("Group Timeslot "+ id +" not found");
        }
    }
    
    @Override
    public GroupTimeslot findGroupTimeslot(Long id){
        groupTimeslotEntity = null;
        try{
            Query q = em.createQuery("SELECT s FROM GroupTimeslot s WHERE s.id = :id");
            q.setParameter("id", id);
            groupTimeslotEntity = (GroupTimeslot) q.getSingleResult();
            System.out.println("Group Timeslot " + id + " found.");
        }
        catch(NoResultException e){
            System.out.println("Group Timeslot " + id + " does not exist.");
            groupTimeslotEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return groupTimeslotEntity;
    }
}
