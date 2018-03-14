/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Reply;
import entity.Thread;
import entity.User;
import entity.VoteReply;
import entity.VoteThread;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface CommunitySessionBeanLocal {

    public boolean createThread(String username, String content, String title, String tag);
    public boolean createReply(Long threadId, String username, String content);
    public boolean createVoteThread(Long threadId, String username, boolean voteType);
    public boolean createVoteReply(Long replyId, String username, boolean voteType);

    public void editThread(Long threadId, String content, String title, String tag);
    public void editReply(Long replyId, String content);

    public boolean deleteThread(String username, Long threadId);
    public boolean deleteReply(String username, Long replyId);
    public boolean deleteVoteThread(String username, Long threadId, Long voteId);
    public boolean deleteVoteReply(String username, Long replyId, Long voteId);

    public boolean checkExistingThread(Long threadId);
    public boolean checkExistingReply(Long replyId);

    public List<Thread> getAllThreads();
    public List<VoteThread> getAllVoteThreads();
    public List<VoteReply> getAllVoteReplies();

    public List<Thread> getThreadsFromUser(String username);
    public List<Reply> getRepliesFromUser(String username);
    public List<Reply> getRepliesFromThread(Long threadId);

    public List<Thread> searchThreadByTitle(String searchTitle);
    public List<Thread> getThreadsByTag(String tag);

    public List<VoteThread> getVotesFromThread(Long threadId);
    public List<VoteReply> getVotesFromReply(Long replyId);
    
    public User findUser(String username);
    
    public VoteThread findVoteByUserThread(Long userId, Long threadId);
    public VoteReply findVoteByUserReply(Long userId, Long replyId);
            
}
