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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    public boolean createThread(String username, String content, String title, String tag, String school) {
        userEntity = findUser(username);

        if (userEntity == null) {
            System.out.println("User not logged in.");
            return false;
        }

        Thread thread = new Thread();

        System.out.println("Id: " + thread.getId());
        thread.createThread(username, content, title, tag, school);
        thread.setUser(userEntity);
        userEntity.getThreads().add(thread);

        em.merge(userEntity);
        em.persist(thread);
        em.flush();

        System.out.println("Thread " + thread.getId() + " created.");
        return true;
    }

    @Override
    public boolean createReply(Long threadId, String username, String content) {
        userEntity = findUser(username);
        threadEntity = findThread(threadId);

        if (userEntity == null) {
            System.out.println("User not logged in.");
            return false;
        }

        Reply reply = new Reply();
        threadEntity.setLatestReplyDateTime(genDateTime());

        reply.createReply(threadId, content);
        reply.setUser(userEntity);
        reply.setThread(threadEntity);

        userEntity.getReplys().add(reply);
        threadEntity.getReplies().add(reply);

        em.merge(userEntity);
        em.merge(threadEntity);
        em.persist(reply);
        em.flush();

        System.out.println("Reply " + reply.getId() + " created.");
        return true;
    }

    @Override
    public boolean createVoteThread(Long threadId, String username, boolean voteType) {
        userEntity = findUser(username);
        threadEntity = findThread(threadId);

        if (checkUserVotedThread(userEntity, threadEntity)) {
            System.out.println("User " + username + " has already voted thread.");
            return false;
        }

        VoteThread voteThread = new VoteThread();

        voteThread.createVoteThread(genDateTime(), voteType, userEntity, threadEntity);

        userEntity.getVotes().add(voteThread);
        threadEntity.getVoteThreads().add(voteThread);
        if (voteType == false) { //false = downvote, true = upvote
            threadEntity.setDownVote((threadEntity.getDownVote() + 1));
        } else {
            threadEntity.setUpVote((threadEntity.getUpVote() + 1));
        }

        em.merge(userEntity);
        em.merge(threadEntity);
        em.persist(voteThread);
        em.flush();
        return true;
    }

    @Override
    public boolean createVoteReply(Long replyId, String username, boolean voteType) {
        userEntity = findUser(username);
        replyEntity = findReply(replyId);

        if (checkUserVotedReply(userEntity, replyEntity)) {
            System.out.println("User " + username + " has already voted reply.");
            return false;
        }

        VoteReply voteReply = new VoteReply();

        voteReply.createVoteReply(genDateTime(), voteType, userEntity, replyEntity);

        userEntity.getVotes().add(voteReply);
        replyEntity.getVoteReplies().add(voteReply);
        if (voteType == false) { //false = downvote, true = upvote
            replyEntity.setDownVote((replyEntity.getDownVote() + 1));
        } else {
            replyEntity.setUpVote((replyEntity.getUpVote() + 1));
        }

        em.merge(userEntity);
        em.merge(replyEntity);
        em.persist(voteReply);
        em.flush();
        return true;
    }

    @Override
    public void editThread(Long threadId, String content, String title, String tag) {
        threadEntity = findThread(threadId);

        threadEntity.setContent(content);
        threadEntity.setTitle(title);
        threadEntity.setTag(tag);
        threadEntity.setEdited(true);
        threadEntity.setEditDateTime(genDateTime());

        em.merge(threadEntity);
        em.flush();
    }

    @Override
    public void editReply(Long replyId, String content) {
        replyEntity = findReply(replyId);

        replyEntity.setContent(content);
        replyEntity.setEdited(true);
        replyEntity.setEditDateTime(genDateTime());

        em.merge(replyEntity);
        em.flush();
    }

    @Override
    public boolean deleteThread(String username, Long threadId) {
        userEntity = findUser(username);
        threadEntity = findThread(threadId);

        if (userEntity.getThreads().remove(threadEntity)) {
            em.merge(userEntity);
            em.remove(threadEntity);
            em.flush();
            em.clear();
            System.out.println("Thread " + threadId + " deleted.");
            return true;
        }

        System.out.println("Failed to delete thread.");
        return false;
    }

    @Override
    public boolean deleteReply(String username, Long replyId) {
        userEntity = findUser(username);
        replyEntity = findReply(replyId);
        threadEntity = replyEntity.getThread();

        if (userEntity.getReplys().remove(replyEntity) && threadEntity.getReplies().remove(replyEntity)) {
            em.merge(userEntity);
            em.merge(threadEntity);
            em.remove(replyEntity);
            em.flush();
            em.clear();
            System.out.println("Reply " + replyId + " deleted.");
            return true;
        }

        System.out.println("Failed to delete reply.");
        return false;
    }

    @Override
    public boolean deleteVoteThread(String username, Long threadId, Long voteId) {
        userEntity = findUser(username);
        threadEntity = findThread(threadId);
        voteEntity = findVote(voteId);

        if (userEntity.getVotes().remove(voteEntity) && threadEntity.getVoteThreads().remove((VoteThread) voteEntity)) {

            if (voteEntity.isVoteType()) {
                threadEntity.setUpVote((threadEntity.getUpVote() - 1));
            } else {
                threadEntity.setDownVote((threadEntity.getDownVote() - 1));
            }

            em.remove(voteEntity);
            em.merge(userEntity);
            em.merge(threadEntity);
            em.flush();
            em.clear();
            System.out.println("Vote " + voteId + " deleted.");
            return true;
        }

        System.out.println("Failed to delete thread vote.");
        return false;
    }

    @Override
    public boolean deleteVoteReply(String username, Long replyId, Long voteId) {
        userEntity = findUser(username);
        replyEntity = findReply(replyId);
        voteEntity = findVote(voteId);

        if (userEntity.getVotes().remove(voteEntity) && replyEntity.getVoteReplies().remove((VoteReply) voteEntity)) {

            if (voteEntity.isVoteType()) {
                replyEntity.setUpVote((replyEntity.getUpVote() - 1));
            } else {
                replyEntity.setDownVote((replyEntity.getDownVote() - 1));
            }

            em.merge(userEntity);
            em.merge(replyEntity);
            em.remove(voteEntity);
            em.flush();
            em.clear();
            System.out.println("Vote " + voteId + " deleted.");
            return true;
        }

        System.out.println("Failed to delete reply vote.");
        return false;
    }

    @Override
    public boolean checkExistingThread(Long threadId) {
        threadEntity = findThread(threadId);
        if (threadEntity == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkExistingReply(Long replyId) {
        replyEntity = findReply(replyId);
        if (replyEntity == null) {
            return false;
        }
        return true;
    }

    public boolean checkUserVotedThread(User userEntity, Thread threadEntity) {
        List<Vote> votes = (List) userEntity.getVotes();
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).isVoteFor()) { //if true (thread), else ignore
                if (threadEntity.getVoteThreads().contains((VoteThread) votes.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkUserVotedReply(User userEntity, Reply replyEntity) {
        List<Vote> votes = (List) userEntity.getVotes();
        for (int i = 0; i < votes.size(); i++) {
            if (!votes.get(i).isVoteFor()) { //if false (reply), else ignore
                if (replyEntity.getVoteReplies().contains((VoteReply) votes.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Thread> getAllThreads() {
        List<Thread> threads = new ArrayList<Thread>();
        Query q = em.createQuery("Select t FROM Thread t");
        for (Object o : q.getResultList()) {
            threadEntity = (Thread) o;
            threads.add(threadEntity);
        }
        return threads;
    }

    @Override
    public List<Thread> getAllThreadsBySchool(String school) {
        List<Thread> threads = new ArrayList<Thread>();
        Query q = em.createQuery("Select t FROM Thread t");
        for (Object o : q.getResultList()) {
            threadEntity = (Thread) o;
            if (threadEntity.getSchool().equals(school)) {
                threads.add(threadEntity);
            }
        }
        threads = this.sortThreadByLatestReply(threads);
        return threads;
    }

    @Override
    public List<VoteThread> getAllVoteThreads() {
        List<VoteThread> voteThreads = new ArrayList<VoteThread>();
        Query q = em.createQuery("Select v FROM VoteThread v");
        for (Object o : q.getResultList()) {
            voteThreadEntity = (VoteThread) o;
            voteThreads.add(voteThreadEntity);
        }
        return voteThreads;
    }

    @Override
    public List<VoteReply> getAllVoteReplies() {
        List<VoteReply> voteReplies = new ArrayList<VoteReply>();
        Query q = em.createQuery("Select v FROM VoteReply v");
        for (Object o : q.getResultList()) {
            voteReplyEntity = (VoteReply) o;
            voteReplies.add(voteReplyEntity);
        }
        return voteReplies;
    }

    @Override
    public List<Thread> getThreadsFromUser(String username) {
        userEntity = findUser(username);
        List<Thread> threads = (List) userEntity.getThreads();
        return threads;
    }

    @Override
    public List<Reply> getRepliesFromUser(String username) {
        userEntity = findUser(username);
        List<Reply> replies = (List) userEntity.getReplys();
        return replies;
    }

    @Override
    public List<Reply> getRepliesFromThread(Long threadId) {
        threadEntity = findThread(threadId);
        List<Reply> replies = (List) threadEntity.getReplies();
        return replies;
    }

    @Override
    public List<Thread> searchThreadByTitle(String searchTitle, String schoolname) {
        List<Thread> threadList = new ArrayList<Thread>();
        List<Thread> threads = getAllThreadsBySchool(schoolname);
        for (int i = 0; i < threads.size(); i++) {
            threadEntity = threads.get(i);
            if (threadEntity.getTitle().contains(searchTitle)) {
                threadList.add(threads.get(i));
            }
        }
        threadList = sortThreadByLatestReply(threadList);
        return threadList;
    }

    @Override
    public List<Thread> searchThreadByContent(String searchContent, String schoolname) {
        List<Thread> threadList = new ArrayList<Thread>();
        List<Thread> threads = getAllThreadsBySchool(schoolname);
        for (int i = 0; i < threads.size(); i++) {
            threadEntity = threads.get(i);
            if (threadEntity.getContent().contains(searchContent)) {
                threadList.add(threads.get(i));
            }
        }
        threadList = sortThreadByLatestReply(threadList);
        return threadList;
    }

    @Override
    public List<Thread> getThreadsByTag(String tag, String schoolname) {
        List<Thread> threadList = new ArrayList<Thread>();
        List<Thread> threads = getAllThreadsBySchool(schoolname);
        for (int i = 0; i < threads.size(); i++) {
            threadEntity = threads.get(i);
            if (threadEntity.getTag().equals(tag)) {
                threadList.add(threads.get(i));
            }
        }
        threadList = sortThreadByLatestReply(threadList);
        return threadList;
    }

    @Override
    public List<VoteThread> getVotesFromThread(Long threadId) {
        threadEntity = findThread(threadId);
        List<VoteThread> voteThreads = (List) threadEntity.getVoteThreads();
        return voteThreads;
    }

    @Override
    public List<VoteReply> getVotesFromReply(Long replyId) {
        replyEntity = findReply(replyId);
        List<VoteReply> voteReplies = (List) replyEntity.getVoteReplies();
        return voteReplies;
    }

    public String genDateTime() {
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.format(current);
    }

    @Override
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

    @Override
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

    @Override
    public Reply findReply(Long id) {
        replyEntity = null;
        try {
            Query q = em.createQuery("SELECT r FROM Reply r WHERE r.id = :id");
            q.setParameter("id", id);
            replyEntity = (Reply) q.getSingleResult();
            System.out.println("Reply " + id + " found.");
        } catch (NoResultException e) {
            System.out.println("Reply " + id + " does not exist.");
            replyEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replyEntity;
    }

    public Vote findVote(Long id) {
        voteEntity = null;
        try {
            Query q = em.createQuery("SELECT v FROM Vote v WHERE v.id = :id");
            q.setParameter("id", id);
            voteEntity = (Vote) q.getSingleResult();
            System.out.println("Vote " + id + " found.");
        } catch (NoResultException e) {
            System.out.println("Vote " + id + " does not exist.");
            voteEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voteEntity;
    }

    @Override
    public VoteThread findVoteByUserThread(Long userId, Long threadId) {
        voteThreadEntity = null;
        try {
            Query q = em.createQuery("SELECT v FROM VoteThread v WHERE v.user.id = :userid AND v.thread.id = :threadid");
            q.setParameter("userid", userId);
            q.setParameter("threadid", threadId);
            voteThreadEntity = (VoteThread) q.getSingleResult();
            System.out.println("Vote found.");
        } catch (NoResultException e) {
            System.out.println("Vote does not exist.");
            voteThreadEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voteThreadEntity;
    }

    @Override
    public VoteReply findVoteByUserReply(Long userId, Long replyId) {
        voteReplyEntity = null;
        try {
            Query q = em.createQuery("SELECT v FROM VoteReply v WHERE v.user.id = :userid AND v.reply.id = :replyid");
            q.setParameter("userid", userId);
            q.setParameter("replyid", replyId);
            voteReplyEntity = (VoteReply) q.getSingleResult();
            System.out.println("Vote found.");
        } catch (NoResultException e) {
            System.out.println("Vote does not exist.");
            voteReplyEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voteReplyEntity;
    }

    @Override
    public List<Thread> sortThreadByUpvote(List<Thread> threadList) {
        //Descending order (Highest vote to lowest)
        Collections.sort(threadList, new Comparator<Thread>() {
            public int compare(Thread t1, Thread t2) {
                return t2.getUpVote() - t1.getUpVote();
            }
        });
        return threadList;
    }

    @Override
    public List<Thread> sortThreadByDate(List<Thread> threadList) {
        //Descending order (Latest to oldest)
        Collections.sort(threadList, new Comparator<Thread>() {
            public int compare(Thread t1, Thread t2) {
                return t2.getDateTime().compareTo(t1.getDateTime());
            }
        });
        return threadList;
    }

    @Override
    public List<Thread> sortThreadByLatestReply(List<Thread> threadList) {
        //Descending order (Latest to oldest)
        Collections.sort(threadList, new Comparator<Thread>() {
            public int compare(Thread t1, Thread t2) {
                return t2.getLatestReplyDateTime().compareTo(t1.getLatestReplyDateTime());
            }
        });
        return threadList;
    }




}
