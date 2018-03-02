/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.AnnouncementSessionBeanLocal;
import session.CourseMgmtBeanLocal;

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

    private User userEntity;
    private String username;
    private int userType;

    private Long anncId;
    private String title;
    private String message;
    private String type; //Input "admin": admin announcement, "<modulecode>": lecturer announcement
    private String audience; //Input "1": all, "2": students only, "3": lecturers only, "4": guests only, "5": 2&3 (admin can use any, lecturer's default use 2)

    FacesContext context;
    HttpSession session;

    public AnnouncementBean() {
    }

    public void enterAnnouncement() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        username = (String) session.getAttribute("username");
        userType = (int) session.getAttribute("userType");

        System.out.println("\ntitle :" + title + "\nmsg :" + message + "\ntype :" + type + "\naudience :" + audience);

        if (userType == 2) {
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
        } else if (userType == 3) {
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
}
