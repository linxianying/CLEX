/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Announcement;
import entity.Module;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.AnnouncementSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.StudyPlanSessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@RequestScoped
public class AnnouncementBean {

    @EJB
    AnnouncementSessionBeanLocal asbl;

    @EJB
    CourseMgmtBeanLocal cmbl;

    @EJB
    StudyPlanSessionBeanLocal spsbl;

    private User userEntity;
    private String username;
    private int userType;

    private Long anncId;
    private String title;
    private String message;
    private String type; //Input "admin": admin announcement, "<modulecode>": lecturer announcement
    private String audience; //Input "1": all, "2": students only, "3": lecturers only, "4": guests only, "5": 2&3 (admin can use any, lecturer's default use 2)

    private ArrayList<Announcement> announcements;
    private ArrayList<Announcement> announcements2;

    private ArrayList<Module> takingModules;

    FacesContext context;
    HttpSession session;

    public AnnouncementBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        username = (String) session.getAttribute("username");
        userType = (int) session.getAttribute("userType");
        if (userType == 1) {
            takingModules = spsbl.getCurrentModules(username);
            announcements = getAnnouncementByAdminForStudent();
        } else if (userType == 2) {
            announcements = getAnnouncementsSelf(username);
            announcements2 = getAnnouncementByAdminForLecturer();
        } else if (userType == 3) {
            announcements = getAnnouncementsSelf(username);
            //get all announcements then remove those by admin
            announcements2 = getAllAnnouncements();
            announcements2.removeAll(announcements);
        } else if (userType == 4) {
            announcements = getAnnouncementByAdminForGuests();
        }
    }

    public void enterAnnouncement() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        username = (String) session.getAttribute("username");
        userType = (int) session.getAttribute("userType");

        if (userType == 2) { // for lecturers
            if (this.title.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Announcement title needed", "Up to 80 characters");
            } else if (this.message.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Announcement details needed.", "Up to 300 characters.");
            } else if (this.type == null) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Intended recipients needed", "Please select from the list.");
            } else {
                asbl.createLecturerAnnc(username, title, message, type);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement created.", "");
                title = "";
                message = "";
                type = "";
                audience = "";
                //context.getExternalContext().redirect("lecturerMain.xhtml"); //redirect will not show success message
            }
        } else if (userType == 3) { //for admin
            if (this.title.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Announcement title needed", "Up to 80 characters");
            } else if (this.message.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Announcement details needed.", "Up to 300 characters.");
            } else if (this.audience == null) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Intended recipients needed", "Please select from the list.");
            } else {
                asbl.createAdminAnnc(username, title, message, audience);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement created.", "");
                //context.getExternalContext().redirect("adminMain.xhtml"); //redirect will not show success message
                title = "";
                message = "";
                type = "";
                audience = "";
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating announcement.", "Missing fields");
        }
        context.addMessage(null, fmsg);
    }

    public ArrayList<Announcement> getAnnouncementOneModule(String moduleCode) {
        ArrayList<Announcement> tempAllAnnouncements2 = new ArrayList<Announcement>();
        tempAllAnnouncements2 = (ArrayList<Announcement>) asbl.getAnncByModule(moduleCode);
        return tempAllAnnouncements2;
    }
//Input "1": all, "2": students only, "3": lecturers only, "4": guests only, "5" Lecturer and Student)

    public ArrayList<Announcement> getAnnouncementByAdminForStudent() {
        ArrayList<Announcement> tempAllAnnouncements1;
        ArrayList<Announcement> tempAllAnnouncements2;
        ArrayList<Announcement> tempAllAnnouncements3;
        tempAllAnnouncements1 = (ArrayList<Announcement>) asbl.getAnncByAudience("1");
        tempAllAnnouncements2 = (ArrayList<Announcement>) asbl.getAnncByAudience("2");
        tempAllAnnouncements3 = (ArrayList<Announcement>) asbl.getAnncByAudience("5");
        for (int i = 0; i < tempAllAnnouncements2.size(); i++) {
            tempAllAnnouncements1.add(tempAllAnnouncements2.get(i));
        }
        for (int i = 0; i < tempAllAnnouncements3.size(); i++) {
            tempAllAnnouncements1.add(tempAllAnnouncements3.get(i));
        }
        return tempAllAnnouncements1;
    }
    
        public ArrayList<Announcement> getAnnouncementByAdminForGuests() {
        ArrayList<Announcement> tempAllAnnouncements1;
        ArrayList<Announcement> tempAllAnnouncements2;
        tempAllAnnouncements1 = (ArrayList<Announcement>) asbl.getAnncByAudience("1");
        tempAllAnnouncements2 = (ArrayList<Announcement>) asbl.getAnncByAudience("4");
        for (int i = 0; i < tempAllAnnouncements2.size(); i++) {
            tempAllAnnouncements1.add(tempAllAnnouncements2.get(i));
        }
        return tempAllAnnouncements1;
    }

    public ArrayList<Announcement> getAnnouncementByAdminForLecturer() {
        ArrayList<Announcement> tempAllAnnouncements1;
        ArrayList<Announcement> tempAllAnnouncements2;
        ArrayList<Announcement> tempAllAnnouncements3;
        tempAllAnnouncements1 = (ArrayList<Announcement>) asbl.getAnncByAudience("1");
        tempAllAnnouncements2 = (ArrayList<Announcement>) asbl.getAnncByAudience("3");
        tempAllAnnouncements3 = (ArrayList<Announcement>) asbl.getAnncByAudience("5");
        for (int i = 0; i < tempAllAnnouncements2.size(); i++) {
            tempAllAnnouncements1.add(tempAllAnnouncements2.get(i));
        }
        for (int i = 0; i < tempAllAnnouncements3.size(); i++) {
            tempAllAnnouncements1.add(tempAllAnnouncements3.get(i));
        }
        return tempAllAnnouncements1;
    }

    public ArrayList<Announcement> getAnnouncementsSelf(String username) {
        ArrayList<Announcement> tempAllAnnouncements4 = (ArrayList<Announcement>) asbl.getAnncByUser(username);
        return tempAllAnnouncements4;
    }

    public ArrayList<Announcement> getAllAnnouncements() {
        ArrayList<Announcement> tempAllAnnouncements5 = (ArrayList<Announcement>) asbl.getAllAnnc();
        return tempAllAnnouncements5;
    }

    public void updateAnnouncement(String title, String message, Long Id) {
        asbl.editAnnc(title, message, Id);
    }

    public void deleteAnnouncement(String username, Long Id) {
        asbl.deleteAnnc(username, Id);
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public Long getAnncId() {
        return anncId;
    }

    public void setAnncId(Long anncId) {
        this.anncId = anncId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    public ArrayList<Module> getTakingModules() {
        return takingModules;
    }

    public void setTakingModules(ArrayList<Module> takingModules) {
        this.takingModules = takingModules;
    }

    public ArrayList<Announcement> getAnnouncements2() {
        return announcements2;
    }

    public void setAnnouncements2(ArrayList<Announcement> announcements2) {
        this.announcements2 = announcements2;
    }
}
