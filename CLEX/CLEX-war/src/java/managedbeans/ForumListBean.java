/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Thread;
import entity.Reply;
import entity.User;
import entity.Vote;
import entity.VoteReply;
import entity.VoteThread;
import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import session.CommunitySessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@RequestScoped
public class ForumListBean {

    @EJB
    private CommunitySessionBeanLocal cmsbl;

    private ArrayList<Thread> threads;
    private ArrayList<Reply> replies;
    private ArrayList<Vote> votes;
    private ArrayList<VoteThread> voteThreads;
    private ArrayList<VoteReply> voteReplies;

    //User
    private User userEntity;
    private String username;
    private String userType;

    //Thread
    private Thread threadEntity;
    private Long tId;
    private String tContent;
    private String tTitle;
    private String tTag;
    private Thread selectedThread;

    //Reply
    private Reply replyEntity;
    private Long rId;
    private String rContent;

    //Vote
    private Vote voteEntity;
    private VoteThread voteThread;
    private VoteReply voteReply;
    private boolean voteFor; //false - reply, true - thread
    private boolean voteType; //false - downvote, true - upvote

    FacesContext context;
    HttpSession session;

    public ForumListBean() {
    }

    //Use this to search, collect and display, use communitybean for other functions
    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
    }

    public void startThread() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");

        if (this.tTitle.equals("")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thread title needed.", "Please fill up the title field.");
        } else if (this.tContent.equals("")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thread contents needed.", "Please fill up the content field.");
        } else {
            if (cmsbl.createThread(username, tContent, tTitle, tTag, userEntity.getSchool())) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread created.");
                refresh();

            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create thread.", "Please ensure you are logged in.");
            }
        }
        context.addMessage(null, fmsg);
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println(selectedThread.getId());
        try {
            session.setAttribute("thread", selectedThread);
            context.getExternalContext().redirect("viewThread.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listRepliesForThread(Long tId) {
        this.tId = tId;
        replies = (ArrayList) cmsbl.getRepliesFromThread(tId);
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<Thread> threads) {
        this.threads = threads;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    public ArrayList<VoteThread> getVoteThreads() {
        return voteThreads;
    }

    public void setVoteThreads(ArrayList<VoteThread> voteThreads) {
        this.voteThreads = voteThreads;
    }

    public ArrayList<VoteReply> getVoteReplies() {
        return voteReplies;
    }

    public void setVoteReplies(ArrayList<VoteReply> voteReplies) {
        this.voteReplies = voteReplies;
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public entity.Thread getThreadEntity() {
        return threadEntity;
    }

    public void setThreadEntity(entity.Thread threadEntity) {
        this.threadEntity = threadEntity;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public String gettContent() {
        return tContent;
    }

    public void settContent(String tContent) {
        this.tContent = tContent;
    }

    public String gettTitle() {
        return tTitle;
    }

    public void settTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    public String gettTag() {
        return tTag;
    }

    public void settTag(String tTag) {
        this.tTag = tTag;
    }

    public Reply getReplyEntity() {
        return replyEntity;
    }

    public void setReplyEntity(Reply replyEntity) {
        this.replyEntity = replyEntity;
    }

    public Long getrId() {
        return rId;
    }

    public void setrId(Long rId) {
        this.rId = rId;
    }

    public String getrContent() {
        return rContent;
    }

    public void setrContent(String rContent) {
        this.rContent = rContent;
    }

    public Vote getVoteEntity() {
        return voteEntity;
    }

    public void setVoteEntity(Vote voteEntity) {
        this.voteEntity = voteEntity;
    }

    public VoteThread getVoteThread() {
        return voteThread;
    }

    public void setVoteThread(VoteThread voteThread) {
        this.voteThread = voteThread;
    }

    public VoteReply getVoteReply() {
        return voteReply;
    }

    public void setVoteReply(VoteReply voteReply) {
        this.voteReply = voteReply;
    }

    public boolean isVoteFor() {
        return voteFor;
    }

    public void setVoteFor(boolean voteFor) {
        this.voteFor = voteFor;
    }

    public boolean isVoteType() {
        return voteType;
    }

    public void setVoteType(boolean voteType) {
        this.voteType = voteType;
    }

    public Thread getSelectedThread() {
        return selectedThread;
    }

    public void setSelectedThread(Thread selectedThread) {
        this.selectedThread = selectedThread;
    }
}
