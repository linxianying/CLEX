/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Course;
import entity.GroupTask;
import entity.Guest;
import entity.Lecturer;
import entity.Lesson;
import entity.Module;
import entity.Poll;
import entity.ProjectGroup;
import entity.Reply;
import entity.Student;
import entity.SuperGroup;
import entity.Task;
import entity.Timeslot;
import entity.User;
import entity.Vote;
import entity.VoteReply;
import entity.VoteThread;
import entity.Thread;
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
public class CommunitySessionBean implements CommunitySessionBeanLocal {
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
    private Reply replyEntity;

    @Override
    public void createThread(String username, String content, String title){
        userEntity = findUser(username);
        Thread thread = new Thread();
        
        thread.createThread(username, content, title);
        thread.setUser(userEntity);
        userEntity.getThreads().add(thread);
        
        em.merge(userEntity);
        em.persist(thread);
        em.flush();
    }
    
    @Override
    public void createReply(Long threadId, String content, String username){
        userEntity = findUser(username);
        threadEntity = findThread(threadId);
        Reply reply = new Reply();
        
        reply.createReply(threadId, content);
        reply.setUser(userEntity);
        reply.setThread(threadEntity);
        
        userEntity.getReplys().add(reply);
        threadEntity.getReplies().add(reply);
        
        em.merge(userEntity);
        em.merge(threadEntity);
        em.persist(reply);
        em.flush();     
    }
    
    public User findUser(String username) {
        userEntity = null;
        try {
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    
    public Thread findThread(Long id) {
        threadEntity = null;
        try {
            Query q = em.createQuery("SELECT t FROM Thread t WHERE t.id = :id");
            q.setParameter("id", id);
            threadEntity = (Thread) q.getSingleResult();
            System.out.println("Thread " + id + " found.");
        } catch (NoResultException e) {
            System.out.println("Thread " + id + " does not exist.");
            threadEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return threadEntity;
    }
}
