/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Thread;
import entity.Reply;
import entity.User;
import entity.Vote;
import entity.VoteReply;
import entity.VoteThread;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import session.CommunitySessionBeanLocal;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@RequestScoped
public class ForumListBean {

    @EJB
    private CommunitySessionBeanLocal cmsbl;

    @EJB
    private CourseMgmtBeanLocal cmbl;

    private ArrayList<Thread> threads;
    private ArrayList<Reply> replies;
    private ArrayList<Vote> votes;
    private ArrayList<VoteThread> voteThreads;
    private ArrayList<VoteReply> voteReplies;

    private ArrayList<Thread> reviewThreads;
    private ArrayList<Thread> bazaar;
    private ArrayList<Thread> important;

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
    private String dateTimeCompare;
    private String dayDisplay;

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

    //search
    private String searchContent;
    private String searchTitle;
    private String searchTag;

    private String buysell;
    private String bazaarprice;
    private String bazaaritem;
    private String bazaarcondition;

    //search review
    private String searchModuleCode;
    private List<Course> courses;

    FacesContext context;
    HttpSession session;

    public ForumListBean() {
    }

    //Use this to search, collect and display, use communitybean for other functions
    @PostConstruct
    public void init() {
        courses = cmbl.getAllCourses();
        refresh();
    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
        threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
        threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
        threads = (ArrayList) cmsbl.filterNonStickied(threads);
        important = (ArrayList) cmsbl.getThreadsStickiedByAdmin(userEntity.getSchool());
        reviewThreads = (ArrayList) cmsbl.getThreadsByTag("Course Review", userEntity.getSchool());
        tTag = "";
        if (!userEntity.getUserType().equals("Guest")) {
            bazaar = (ArrayList) cmsbl.getThreadsByTag("Bazaar", userEntity.getSchool());
            buysell = "sell";
        }
    }

    public void searchThread() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        threads.clear();
        if (searchTitle.equals("") && searchContent.equals("") && searchTag.equals(" ")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error!", "Please enter a Title or Content to start searching!");
            threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
            threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
            threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
            context.addMessage(null, fmsg);
        } else {
            if (!searchTitle.equals("") && !searchContent.equals("") && !searchTag.equals(" ")) {
                threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
                threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
                threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
                threads = (ArrayList) cmsbl.filterThreadByTitle(threads, searchTitle);
                threads = (ArrayList) cmsbl.filterThreadByContent(threads, searchContent);
                threads = (ArrayList) cmsbl.filterThreadByTag(threads, searchTag);
            } else if (!searchTitle.equals("") && searchContent.equals("") && searchTag.equals(" ")) {
                threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
                threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
                threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
                threads = (ArrayList) cmsbl.filterThreadByTitle(threads, searchTitle);
            } else if (searchTitle.equals("") && !searchContent.equals("") && searchTag.equals(" ")) {
                threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
                threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
                threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
                threads = (ArrayList) cmsbl.filterThreadByContent(threads, searchContent);
            } else if (searchTitle.equals("") && searchContent.equals("") && !searchTag.equals(" ")) {
                threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
                threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
                threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
                threads = (ArrayList) cmsbl.filterThreadByTag(threads, searchTag);
            }
            if (threads.isEmpty()) {
                threads = (ArrayList) cmsbl.getAllThreadsBySchool(userEntity.getSchool());
                threads = (ArrayList) cmsbl.filterNonTagCourseReview(threads);
                threads = (ArrayList) cmsbl.filterNonTagMarketplace(threads);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Threads found!", "Try refining your search terms.");
                context.addMessage(null, fmsg);
            } else {
                int searchcount = threads.size();
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", searchcount + " Thread(s) found.");
                context.addMessage(null, fmsg);
            }
        }
        searchTitle = "";
        searchContent = "";
        searchTag = "";
    }

    public void searchReview() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        if (!searchModuleCode.equals("")) {
            reviewThreads = (ArrayList) cmsbl.searchThreadByTitle(searchModuleCode, userEntity.getSchool());
            reviewThreads = (ArrayList) cmsbl.filterNonTagCourseReview(reviewThreads);
            for (int i = 0; i < reviewThreads.size(); i++) {
                System.out.println(i + ": " + reviewThreads.get(i).getTitle());
            }
            if (reviewThreads.isEmpty()) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No reviews found!", "");
            } else {
                int searchcount = reviewThreads.size();
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", searchcount + " Review(s) found.");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please fill in the module code field.");
        }
        searchModuleCode = "";
        context.addMessage(null, fmsg);
    }

    public void startThread() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        if (tTitle.equals("")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thread title needed.", "Please fill up the title field.");
            context.addMessage(null, fmsg);
        } else if (tContent.equals("")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thread contents needed.", "Please fill up the content field.");
            context.addMessage(null, fmsg);
        } else {
            if (cmsbl.createThread(username, tContent, tTitle, tTag, userEntity.getSchool())) {
                refresh();
                tTitle = "";
                tContent = "";
                tTag = "";
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failed to create thread.", "Please ensure you are logged in.");
                context.addMessage(null, fmsg);
            }
        }
    }

    public void startSales() throws IOException {

        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");

        if (buysell.equals("buy")) {
            tTitle = "WTB: " + bazaaritem;
            String otherdetails = tContent;
            if (tContent.equals("")) {
                otherdetails = "NIL";
            }
            tContent = "Looking to buy " + bazaaritem + " at <strong>SGD$" + bazaarprice + "</strong> with a <strong>" + bazaarcondition.toUpperCase() + "</strong> condition"
                    + "!" + " </p><p>" + "Other details: " + "</p><p>" + otherdetails;
        } else if (buysell.equals("sell")) {
            tTitle = "WTS: " + bazaaritem;
            tContent = "Selling " + bazaaritem + " at <strong>SGD$" + bazaarprice + "</strong> !"
                    + "</p><p>" + "Other details about the item: " + "</p><p>" + "Condition is <strong>" + bazaarcondition.toUpperCase() + "</strong></p><p>" + tContent;
        }
        if (cmsbl.createThread(username, tContent, tTitle, "Bazaar", userEntity.getSchool())) {
            refresh();
            tTitle = "";
            tContent = "";
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create thread.", "Please ensure you are logged in.");
            context.addMessage(null, fmsg);
        }

    }

    public void searchSales() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        bazaar.clear();
        if (searchTitle.equals("") && searchContent.equals("")) {
            if (buysell.equals("buy")) {
                searchTitle = "WTB: " + searchTitle;
            } else if (buysell.equals("sell")) {
                searchTitle = "WTS: " + searchTitle;
            }
            bazaar = (ArrayList) cmsbl.getThreadsByTag("Bazaar", userEntity.getSchool());
            bazaar = (ArrayList) cmsbl.filterThreadByTitle(bazaar, searchTitle);
            bazaar = (ArrayList) cmsbl.filterThreadByContent(bazaar, searchContent);
        } else {
            if (!searchTitle.equals("") && !searchContent.equals("")) {
                if (buysell.equals("buy")) {
                    searchTitle = "WTB: " + searchTitle;
                } else if (buysell.equals("sell")) {
                    searchTitle = "WTS: " + searchTitle;
                }
                bazaar = (ArrayList) cmsbl.getThreadsByTag("Bazaar", userEntity.getSchool());
                bazaar = (ArrayList) cmsbl.filterThreadByTitle(bazaar, searchTitle);
                bazaar = (ArrayList) cmsbl.filterThreadByContent(bazaar, searchContent);
            } else if (!searchTitle.equals("") && searchContent.equals("")) {
                if (buysell.equals("buy")) {
                    searchTitle = "WTB: " + searchTitle;
                } else if (buysell.equals("sell")) {
                    searchTitle = "WTS: " + searchTitle;
                }
                bazaar = (ArrayList) cmsbl.getThreadsByTag("Bazaar", userEntity.getSchool());
                bazaar = (ArrayList) cmsbl.filterThreadByTitle(bazaar, searchTitle);
            } else if (searchTitle.equals("") && !searchContent.equals("")) {
                if (buysell.equals("buy")) {
                    searchTitle = "WTB: ";
                } else if (buysell.equals("sell")) {
                    searchTitle = "WTS: ";
                }
                bazaar = (ArrayList) cmsbl.getThreadsByTag("Bazaar", userEntity.getSchool());
                bazaar = (ArrayList) cmsbl.filterThreadByContent(bazaar, searchContent);
                bazaar = (ArrayList) cmsbl.filterThreadByTitle(bazaar, searchTitle);
            }

        }
        if (bazaar.isEmpty()) {
            bazaar = (ArrayList) cmsbl.getThreadsByTag("Bazaar", userEntity.getSchool());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Threads found!", "Try refining your search terms.");
        } else {
            int searchcount = bazaar.size();
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", searchcount + " Thread(s) found.");
        }

        searchTitle = "";
        searchContent = "";
        context.addMessage(null, fmsg);
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("Selected Thread ID: " + selectedThread.getId());
        try {
            int usertype = (int) session.getAttribute("userType");
            session.setAttribute("id", selectedThread.getId());
            if (usertype == 1) { //Student
                context.getExternalContext().redirect("viewThread.xhtml");
            } else if (usertype == 2) { //Lecturer
                context.getExternalContext().redirect("viewThreadL.xhtml");
            } else if (usertype == 3) { //Admin
                context.getExternalContext().redirect("viewThreadA.xhtml");
            } else if (usertype == 4) { //Guest
                context.getExternalContext().redirect("viewThreadG.xhtml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String dayTime(Thread threadEntity) {
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeCompare = format.format(current);
        if (threadEntity.getDateTime().substring(0, 10).equals(dateTimeCompare)) {
            dayDisplay = "Today, " + threadEntity.getDateTime().substring(10);
        } else {
            dayDisplay = threadEntity.getDateTime();
        }
        return dayDisplay;
    }

    public String latestReplyDayTime(String dayTime) {
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeCompare = format.format(current);
        if (dayTime.substring(0, 10).equals(dateTimeCompare)) {
            dayDisplay = "Today, " + dayTime.substring(10);
        } else {
            dayDisplay = dayTime;
        }
        return dayDisplay;
    }

    public ArrayList<Thread> getImportant() {
        return important;
    }

    public void setImportant(ArrayList<Thread> important) {
        this.important = important;
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

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }

    public CommunitySessionBeanLocal getCmsbl() {
        return cmsbl;
    }

    public void setCmsbl(CommunitySessionBeanLocal cmsbl) {
        this.cmsbl = cmsbl;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public ArrayList<Thread> getReviewThreads() {
        return reviewThreads;
    }

    public void setReviewThreads(ArrayList<Thread> reviewThreads) {
        this.reviewThreads = reviewThreads;
    }

    public String getSearchModuleCode() {
        return searchModuleCode;
    }

    public void setSearchModuleCode(String searchModuleCode) {
        this.searchModuleCode = searchModuleCode;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Thread> getBazaar() {
        return bazaar;
    }

    public void setBazaar(ArrayList<Thread> bazaar) {
        this.bazaar = bazaar;
    }

    public String getBuysell() {
        return buysell;
    }

    public void setBuysell(String buysell) {
        this.buysell = buysell;
    }

    public String getBazaarprice() {
        return bazaarprice;
    }

    public void setBazaarprice(String bazaarprice) {
        this.bazaarprice = bazaarprice;
    }

    public String getBazaaritem() {
        return bazaaritem;
    }

    public void setBazaaritem(String bazaaritem) {
        this.bazaaritem = bazaaritem;
    }

    public String getBazaarcondition() {
        return bazaarcondition;
    }

    public void setBazaarcondition(String bazaarcondition) {
        this.bazaarcondition = bazaarcondition;
    }

}
