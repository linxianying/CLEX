/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Thread;
import entity.Reply;
import entity.Student;
import entity.User;
import entity.Vote;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.CommunitySessionBeanLocal;

/**
 *
 * @author lin
 */
@ManagedBean
@SessionScoped
public class CommunityBean {

    @EJB
    private CommunitySessionBeanLocal cmsbl;

    //For displaying 
    private Student studentEntity;
    private Lecturer lecturerEntity;
    private String faculty;
    private String major;
    private String dateTimeCompare;
    private String dayDisplay; //for thread

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

    //Reply
    private List<Reply> replies;
    private Reply replyEntity;
    private Long rId;
    private String rContent;

    //Vote
    private Vote voteEntity;
    private boolean voteFor; //false - reply, true - thread
    private boolean voteType; //false - downvote, true - upvote

    FacesContext context;
    HttpSession session;

    public CommunityBean() {
    }

    public void init() {
        refresh();
    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        threadEntity = (Thread) session.getAttribute("thread");
        System.out.println(threadEntity.getUser().getName());
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        check();
        replies = cmsbl.getRepliesFromThread(threadEntity.getId());        
    }

    public void check() {
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeCompare = format.format(current);
        if (threadEntity.getDateTime().substring(0, 10).equals(dateTimeCompare)) {
            dayDisplay = "Today, " + threadEntity.getDateTime().substring(10);
        } else {
            dayDisplay = threadEntity.getDateTime();
        }
        if (threadEntity.getUser().getUserType().equals("Student")) {
            studentEntity = (Student) threadEntity.getUser();
            faculty = studentEntity.getFaculty();
            major = studentEntity.getMajor();
        } else if (threadEntity.getUser().getUserType().equals("Lecturer")) {
            lecturerEntity = (Lecturer) threadEntity.getUser();
            faculty = lecturerEntity.getFaculty();
            major = "";
        } else {
            faculty = "";
            major = "";
        }
    }

    public String getReplyFaculty(User userEntity) {
        String faculty1;
        System.out.println("USER: " + userEntity.getName());
        if (userEntity.getUserType().equals("Student")) {
            studentEntity = (Student) userEntity;
            faculty1 = studentEntity.getFaculty();
        } else if (userEntity.getUserType().equals("Lecturer")) {
            lecturerEntity = (Lecturer) userEntity;
            faculty1 = lecturerEntity.getFaculty();
        } else {
            faculty1 = "";
        }
        System.out.println("FACULTY: " + faculty1);
        return faculty1;
    }

    public String getReplyMajor(User userEntity) {
        String major1;
        if (userEntity.getUserType().equals("Student")) {
            studentEntity = (Student) userEntity;
            major1 = studentEntity.getMajor();
        } else {
            major1 = "";
        }
        System.out.println("MAJOR: " + major1);
        return major1;
    }

    //Use this for creating, editing and deleting, use forumlistbean for other functions
    public void enterReply() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");

        if (cmsbl.checkExistingThread(threadEntity.getId())) {
            if (this.rContent.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reply contents needed.", "Please fill up the content field.");
            } else {
                if (cmsbl.createReply(threadEntity.getId(), username, rContent)) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Replied to thread.");
                    refresh();
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create reply.", "Please ensure you are logged in.");
                }
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create reply.", "Thread does not exists.");
        }
        context.addMessage(null, fmsg);
        this.rContent = "";
    }

    public void quoteText(String quotedText, String quoteWho) {
        rContent = quoteWho + " said:\n''" + quotedText + "'' ----- My Reply: ";
        refresh();
    }

    public void voteThread(Long id, boolean voteType) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        voteEntity = (Vote) cmsbl.findVoteByUserThread(userEntity.getId(), id);
        if (userEntity != null) {
            if (voteEntity != null) {
                if (voteEntity.isVoteType() == voteType) { //if voted before, check if vote is same, if yes, error
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "You have made the same vote before.");
                } else { //if vote is different but have already voted before, remove vote then create
                    cmsbl.deleteVoteThread(userEntity.getUsername(), id, voteEntity.getId());
                    cmsbl.createVoteThread(id, userEntity.getUsername(), voteType);
                    if (voteType) {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread upvoted.");
                    } else {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread downvoted.");
                    }
                }
            } else { //if never vote before, create vote
                cmsbl.createVoteThread(id, userEntity.getUsername(), voteType);
                if (voteType) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread upvoted.");
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread downvoted.");
                }
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to vote.", "Please ensure you are logged in.");
        }

        context.addMessage(null, fmsg);
    }

    public void voteReply(Long id, boolean voteType) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        voteEntity = (Vote) cmsbl.findVoteByUserReply(userEntity.getId(), id);
        if (userEntity != null) {
            if (voteEntity != null) {
                if (voteEntity.isVoteType() == voteType) { //if voted before, check if vote is same, if yes, error
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "You have made the same vote before.");
                } else { //if vote is different but have already voted before, remove vote then create
                    cmsbl.deleteVoteReply(userEntity.getUsername(), id, voteEntity.getId());
                    cmsbl.createVoteReply(id, userEntity.getUsername(), voteType);
                    if (voteType) {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply upvoted.");
                    } else {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply downvoted.");
                    }
                }
            } else { //if never vote before, create vote
                cmsbl.createVoteReply(id, userEntity.getUsername(), voteType);
                if (voteType) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply upvoted.");
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply downvoted.");
                }
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to vote.", "Please ensure you are logged in.");
        }

        context.addMessage(null, fmsg);
    }

    public void removeVoteThread(Long id) {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        voteEntity = (Vote) cmsbl.findVoteByUserThread(userEntity.getId(), id);
        if (userEntity != null) {
            if (voteEntity != null) {
                cmsbl.deleteVoteThread(userEntity.getUsername(), id, voteEntity.getId());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Vote removed.");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "You have not voted for this thread before.");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to unvote.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }

    public void removeVoteReply(Long id) {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        voteEntity = (Vote) cmsbl.findVoteByUserReply(userEntity.getId(), id);
        if (userEntity != null) {
            if (voteEntity != null) {
                cmsbl.deleteVoteReply(userEntity.getUsername(), id, voteEntity.getId());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Vote removed.");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "You have not voted for this reply before.");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to unvote.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }

    public void modifyThread() {
        FacesMessage fmsg = new FacesMessage();
        cmsbl.editThread(threadEntity.getId(), tContent, tTitle, tTag);
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread edited.");
        refresh();
        context.addMessage(null, fmsg);
        tContent = "";
        tTitle = "";
        tTag = "";
    }

    public void modifyReply() {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        cmsbl.editReply(rId, rContent);
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply edited.");
        refresh();
        context.addMessage(null, fmsg);
        rContent = "";
    }

    public void forModifyReply(String content, Long id) {
        rContent = content;
        rId = id;
        refresh();

    }

    public void forModifyThread(String title, String content, String tag, Long id) {
        tContent = content;
        tTitle = title;
        tTag = tag;
        tId = id;
        refresh();
    }

    public void removeReply(Long id) {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");

        if (userEntity != null) {
            if (cmsbl.checkExistingReply(id)) {
                if (cmsbl.deleteReply(username, id)) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply removed.");
                    refresh();
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete reply.", "");
                }
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete reply.", "Reply does not exists.");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete reply.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }

    public void removeThread() {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = cmsbl.findUser(username);

        if (userEntity != null) {
            if (cmsbl.checkExistingThread(tId)) {
                if (cmsbl.deleteThread(username, tId)) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thread removed.", "");
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete thread.", "");
                }
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete thread.", "Thread does not exists.");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete thread.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }

    //For debugging entities
    public void sort() {
        List<Thread> threadList = cmsbl.getAllThreads();
        List<Thread> sortedListByUpvote = cmsbl.sortThreadByUpvote(cmsbl.getAllThreads()); //highest vote first
        List<Thread> sortedListByDate = cmsbl.sortThreadByDate(cmsbl.getAllThreads()); //latest first
        for (int i = 0; i < threadList.size(); i++) {
            System.out.println("TestBeforeSort" + i + ": " + threadList.get(i).getDateTime());
        }
        for (int i = 0; i < sortedListByUpvote.size(); i++) {
            System.out.println("TestAfterSortByUpvote" + i + ": " + sortedListByUpvote.get(i).getDateTime());
        }
        for (int i = 0; i < sortedListByDate.size(); i++) {
            System.out.println("TestAfterSortByDate" + i + ": " + sortedListByDate.get(i).getDateTime());
        }
    }

    public void test() {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = cmsbl.findUser(username);

        List<Thread> allthreads = (List) userEntity.getThreads();
        List<Reply> allreplies = (List) userEntity.getReplys();
        List<Vote> allvotes = (List) userEntity.getVotes();
        List<Reply> threadreplies;
        List<Vote> threadvotes;
        List<Vote> replyvotes;

        for (int i = 0; i < allthreads.size(); i++) {
            System.out.println("TestprintUserThread" + i + ": " + allthreads.get(i).getDateTime());
            threadreplies = (List) allthreads.get(i).getReplies();
            threadvotes = (List) allthreads.get(i).getVoteThreads();

            for (int j = 0; j < threadreplies.size(); j++) {
                System.out.println("TestprintThreadReply" + j + ": " + threadreplies.get(j).getDateTime());
            }

            for (int k = 0; k < threadvotes.size(); k++) {
                System.out.println("TestprintThreadVote" + k + ": " + threadvotes.get(k).getDateTime());
            }
        }

        for (int i = 0; i < allreplies.size(); i++) {
            System.out.println("TestprintUserReply" + i + ": " + allreplies.get(i).getDateTime());
            replyvotes = (List) allreplies.get(i).getVoteReplies();

            for (int j = 0; j < replyvotes.size(); j++) {
                System.out.println("TestprintReplyVote" + j + ": " + replyvotes.get(j).getDateTime());
            }
        }

        for (int i = 0; i < allvotes.size(); i++) {
            System.out.println("TestprintUserVote" + i + ": " + allvotes.get(i).getDateTime());
        }
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

    public Thread getThreadEntity() {
        return threadEntity;
    }

    public void setThreadEntity(Thread threadEntity) {
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

    public Student getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(Student studentEntity) {
        this.studentEntity = studentEntity;
    }

    public Lecturer getLecturerEntity() {
        return lecturerEntity;
    }

    public void setLecturerEntity(Lecturer lecturerEntity) {
        this.lecturerEntity = lecturerEntity;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDateTimeCompare() {
        return dateTimeCompare;
    }

    public void setDateTimeCompare(String dateTimeCompare) {
        this.dateTimeCompare = dateTimeCompare;
    }

    public String getDayDisplay() {
        return dayDisplay;
    }

    public void setDayDisplay(String dayDisplay) {
        this.dayDisplay = dayDisplay;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
