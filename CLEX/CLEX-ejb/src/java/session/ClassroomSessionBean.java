/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;
import entity.Admin;
import entity.Student;
import entity.Module;
import entity.Course;
import entity.Grade;
import entity.GroupTask;
import entity.Guest;
import entity.Lecturer;
import entity.Lesson;
import entity.Poll;
import entity.ProjectGroup;
import entity.StudyPlan;
import entity.SuperGroup;
import entity.Task;
import entity.Timeslot;
import entity.User;
import entity.Vote;
import entity.VoteReply;
import entity.VoteThread;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Random;
import javaClass.JsonReader;
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
public class ClassroomSessionBean implements ClassroomSessionBeanLocal {
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
    private Vote voteEntity;
    private VoteReply voteReplyEntity;
    private VoteThread voteThreadEntity;
    private Poll pollEntity;
    private Thread threadEntity;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Poll createPoll(String moduleCode, String takenYear, String takenSem, 
            String datetime, String topic, int count, String type, String content){
        moduleEntity = null;
        pollEntity = null;
        moduleEntity = findModule(moduleCode, takenYear, takenSem);
        if(moduleEntity==null){
            System.out.println("Module " + moduleCode + " does not exist!");
            return null;
        }else{
            System.out.println("Module " + moduleCode + " is found");
            pollEntity = new Poll();
            pollEntity.setContent(content);
            pollEntity.setCount(count);
            pollEntity.setDatetime(datetime);
            pollEntity.setTopic(topic);
            pollEntity.setType(type);
            em.persist(pollEntity);
            em.flush();
        }
        return pollEntity;
    }
    
    @Override
    public Module findModule(String moduleCode, String takenYear, String takenSem){
        moduleEntity = null;
        courseEntity = null;
        Long courseId;
        try{
            courseEntity = this.findCourse(moduleCode);
            courseId = courseEntity.getId();
            Query q = em.createQuery("SELECT m FROM SchoolModule m "
                    + "WHERE m.course.id=:courseId AND m.takenYear=:takenYear "
                    + "AND m.takenSem=:takenSem");
            q.setParameter("courseId", courseId);
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            moduleEntity = (Module) q.getSingleResult();
            System.out.println("Module: " + moduleCode + "with takenYear=" + 
                    takenYear + ", takenSem= " + takenSem + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            moduleEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return moduleEntity;
    }
    
    public Course findCourse(String moduleCode){
        courseEntity = null;
        try{
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            courseEntity = (Course) q.getSingleResult();
            System.out.println("Course " + moduleCode + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            courseEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return courseEntity;
    }
}
