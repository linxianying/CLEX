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
import javax.persistence.PersistenceContext;

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
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Thread createThread(String username, String content, String dateTime, String title, 
                int upVote, int downVote, User user){
        threadEntity = new Thread();
        threadEntity.createThread(username, content, dateTime, title, upVote, downVote, user);
        em.persist(threadEntity);
        em.flush();
        return threadEntity;
    }
    
    @Override
    public Reply createReply(Long threadId, String dateTime, 
                String content,int upVote, int downVote, User user){
        replyEntity = new Reply();
        replyEntity.createReply(threadId, dateTime, content, upVote, downVote, user);
        em.persist(replyEntity);
        em.flush();
        return replyEntity;
        
    }
    
    
}
