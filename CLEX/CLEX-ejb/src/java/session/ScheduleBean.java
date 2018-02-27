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
import java.util.Date;
import java.util.Iterator;
import javaClass.IcsReader;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jdk.nashorn.internal.runtime.ParserException;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;

/**
 *
 * @author lin
 */
@Stateless
public class ScheduleBean implements ScheduleBeanLocal {

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
    

    @Override
    public boolean loadIcsFile() {
        IcsReader ics = new IcsReader();
        ics.test();
        return false;
    }
    
    @Override
    public void createTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue){
        timeslotEntity = new Timeslot();
        timeslotEntity.createTimeslot(date, timeFrom, timeEnd,title, details, venue);
        em.persist(timeslotEntity);
        em.flush();
    }

    @Override
    public void deleteTimeslot(Long id) {
        timeslotEntity = findTimeslot(id);
        if(timeslotEntity != null){
            em.remove(timeslotEntity);
            em.flush();
        }else{
            System.out.println("Timeslot "+ id +" not found");
        }
    }
    
    @Override
    public void updateTimeslot(Long id, String date, String timeFrom, String timeEnd, 
                String title, String details, String venue) {
        timeslotEntity = findTimeslot(id);
        if(timeslotEntity != null){
            timeslotEntity.setDate(date);
            timeslotEntity.setDetails(details);
            timeslotEntity.setTimeFrom(timeFrom);
            timeslotEntity.setTimeEnd(timeEnd);
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
            groupTimeslotEntity.setPojectGroup(pojectGroup);
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
