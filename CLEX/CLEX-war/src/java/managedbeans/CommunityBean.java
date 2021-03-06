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
    private Long Id;
    private String content;
    private String title;
    private String tag;

    //Reply
    private List<Reply> replies;
    private Reply replyEntity;
    private Long replyId;
    private String replyContent;
    private String dayDisplay2; //for reply

    private String forumtype;
    //Vote
    private Vote voteEntity;
    private boolean voteFor; //false - reply, true - thread
    private boolean voteType; //false - downvote, true - upvote
    private boolean editThread = false;
    private boolean editReply = false;
    private boolean newReply = false;

    FacesContext context;
    HttpSession session;

    public CommunityBean() {
    }

    public void init() {
        refresh();
        editThread = false;
        forumtype = threadEntity.getTag();
        if (forumtype.equals("Bazaar")) {
            forumtype = "BAZAAR CORNER";
        } else if (forumtype.equals("Course Review")) {
            forumtype = "MODULE REVIEW";
        } else {
            forumtype = "COMMUNITY FORUMS";
        }

    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        Long threadID = (Long) session.getAttribute("id");
        threadEntity = cmsbl.findThread(threadID);
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

    public String dayTime(Reply replyEntity) {
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeCompare = format.format(current);
        if (replyEntity.getDateTime().substring(0, 10).equals(dateTimeCompare)) {
            dayDisplay2 = "Today, " + replyEntity.getDateTime().substring(10);
        } else {
            dayDisplay2 = replyEntity.getDateTime();
        }
        return dayDisplay2;
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
        if (cmsbl.checkExistingThread(threadEntity.getId())) {
            if (replyContent.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reply contents needed.", "Please fill up the content field.");
            } else {
                if (cmsbl.createReply(threadEntity.getId(), username, replyContent)) {
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
        replyContent = "";
        newReply = false;
    }

    public void quoteText(Long id, String checker) {
        String name;
        String content;
        if (checker.equals("thread")) {
            Thread thread = cmsbl.findThread(id);
            name = thread.getUser().getName();
            content = thread.getContent();
        } else {
            Reply reply = cmsbl.findReply(id);
            name = reply.getUser().getName();
            content = reply.getContent();
        }
        replyContent = name + " said: </p><p> <em> " + content + " <em>" + "</p><p>"
                + "---------------------------------------------------------------------------------------------------------------------------------"
                + "</p><p><br></p><p><br></p><p><br>";
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
        refresh();
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
        refresh();
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
        session.setAttribute("thread", threadEntity);
        refresh();
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
        refresh();
        context.addMessage(null, fmsg);
    }
    
    public void createNewReply(){
        newReply = true;
    }
    
    public void cancelModify(){
        editThread = false;
        editReply = false;
        newReply = false;
    }

    public void modifyThread() {
        System.out.println("Edit thread");
        System.out.println(threadEntity.getId() + " " + title + " " + content + " " + tag);
//        FacesMessage fmsg = new FacesMessage();
        cmsbl.editThread(threadEntity.getId(), content, title, tag);
        System.out.println("Edited");
//        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Thread edited.");
        threadEntity.setContent(content);
        threadEntity.setTitle(title);
        threadEntity.setTag(tag);
        session.setAttribute("thread", threadEntity);
        refresh();
//        context.addMessage(null, fmsg);
        content = "";
        title = "";
        tag = "";
        editThread = false;
    }

    public void modifyReply() {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        cmsbl.editReply(replyId, replyContent);
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Reply edited.");
        refresh();
        context.addMessage(null, fmsg);
        replyContent = "";
        editReply = false;
    }

    public void forModifyReply(String content, Long id) {
        replyContent = content;
        replyId = id;
        refresh();
        editReply = true;

    }

    public void forModifyThread(Thread threadEntity) {
        content = threadEntity.getContent();
        title = threadEntity.getTitle();
        tag = threadEntity.getTag();
        Id = threadEntity.getId();
        refresh();
        editThread = true;
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

    public void removeThread(Long id) {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = threadEntity.getUsername();
        userEntity = cmsbl.findUser(username);

        if (userEntity != null) {
            if (cmsbl.checkExistingThread(id)) {
                if (cmsbl.deleteThread(username, id)) {
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
        try {
            context.getExternalContext().redirect("adminCommunity.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public Reply getReplyEntity() {
        return replyEntity;
    }

    public void setReplyEntity(Reply replyEntity) {
        this.replyEntity = replyEntity;
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

    public String getDayDisplay2() {
        return dayDisplay2;
    }

    public void setDayDisplay2(String dayDisplay2) {
        this.dayDisplay2 = dayDisplay2;
    }

    public String getForumtype() {
        return forumtype;
    }

    public void setForumtype(String forumtype) {
        this.forumtype = forumtype;
    }

    /**
     * @return the editThread
     */
    public boolean isEditThread() {
        return editThread;
    }

    /**
     * @param editThread the editThread to set
     */
    public void setEditThread(boolean editThread) {
        this.editThread = editThread;
    }

    /**
     * @return the editReply
     */
    public boolean isEditReply() {
        return editReply;
    }

    /**
     * @param editReply the editReply to set
     */
    public void setEditReply(boolean editReply) {
        this.editReply = editReply;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    /**
     * @return the newReply
     */
    public boolean isNewReply() {
        return newReply;
    }

    /**
     * @param newReply the newReply to set
     */
    public void setNewReply(boolean newReply) {
        this.newReply = newReply;
    }

}
