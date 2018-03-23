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
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import session.AnnouncementSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.StudyPlanSessionBeanLocal;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.conf.Configuration;
//import facebook4j.conf.ConfigurationBuilder;

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
    private int anncSize;
    private int latestCount;

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
            userEntity = asbl.findUser(username);
            takingModules = spsbl.getCurrentModules(username);
            announcements = getAnnouncementByAdminForStudent();
            announcements2 = getLatestAnnouncementForAllModules();
            announcements.addAll(announcements2);
            announcements = (ArrayList<Announcement>) asbl.sortAnncByDate(announcements);
            setAnncSize(announcements.size());
            setLatestCount(announcements.size() - userEntity.getViewAnncCount());
        } else if (userType == 2) {
            userEntity = asbl.findUser(username);
            announcements = getAnnouncementsSelf(username);
            announcements2 = getAnnouncementByAdminForLecturer();
            announcements.addAll(announcements2);
            announcements = (ArrayList<Announcement>) asbl.sortAnncByDate(announcements);
            setAnncSize(announcements.size());
            setLatestCount(announcements.size() - userEntity.getViewAnncCount());
        } else if (userType == 3) {
            userEntity = asbl.findUser(username);
            announcements = getAnnouncementsSelf(username);
            setAnncSize(announcements.size());
            setLatestCount(announcements.size() - userEntity.getViewAnncCount());
        } else if (userType == 4) {
            userEntity = asbl.findUser(username);
            announcements = getAnnouncementByAdminForGuests();
            setAnncSize(announcements.size());
            setLatestCount(announcements.size() - userEntity.getViewAnncCount());
        }
    }

    public void enterAnnouncement() throws IOException, TwitterException, FacebookException {
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
                System.out.println("Audience == " + audience);
                
                if (audience.equals("1")) {
                    postToTwitter(title, message);
                    postToFacebook(title,message);
                }
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
        System.out.println("temp size = " + tempAllAnnouncements2.size());
        return tempAllAnnouncements2;
    }

    public ArrayList<Announcement> getLatestAnnouncementForAllModules() {
        ArrayList<Announcement> allAnncList = new ArrayList<Announcement>();
        ArrayList<Announcement> tempList = new ArrayList<Announcement>();

        for (int i = 0; i < takingModules.size(); i++) {
            tempList = (ArrayList<Announcement>) asbl.getAnncByModule(takingModules.get(i).getCourse().getModuleCode());
            allAnncList.addAll(tempList);
        }

        allAnncList = (ArrayList<Announcement>) asbl.sortAnncByDate(allAnncList);

        return allAnncList;
    }

    //updates the counter above the notification icon upon closing it
    public void setAnncViewCount() {
        System.out.println("annc size = " + getAnncSize());
        asbl.setViewAnncCount(userEntity.getUsername(), getAnncSize());
        userEntity.setViewAnncCount(getAnncSize());
        System.out.println("view Count= " + userEntity.getViewAnncCount());
        setLatestCount(anncSize - userEntity.getViewAnncCount());
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

        tempAllAnnouncements1 = (ArrayList<Announcement>) asbl.sortAnncByDate(tempAllAnnouncements1);
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
        tempAllAnnouncements4 = (ArrayList<Announcement>) asbl.sortAnncByDate(tempAllAnnouncements4);
        return tempAllAnnouncements4;
    }

    public ArrayList<Announcement> getAllAnnouncements() {
        ArrayList<Announcement> tempAllAnnouncements5 = (ArrayList<Announcement>) asbl.getAllAnnc();
        return tempAllAnnouncements5;
    }

    public void updateAnnouncement(String title, String message, Long id) {
        asbl.editAnnc(title, message, id);
    }

    public void deleteAnnouncement(String username, Long id) {
        Announcement tempAnnc = asbl.findAnnc(id);
        asbl.deleteAnnc(username, id);
        //If admin delete lecturer only
        if (asbl.findUser(username).getUserType().equals("Lecturer") && userType == 3) {
            deleteNotify(username, asbl.findUser(username).getEmail(), tempAnnc);
        }
    }

    public void deleteNotify(String username, String email, Announcement tempAnnc) {
        String to = email;
        String from = "iamprism@gmail.com";

        Properties props = System.getProperties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("prismeduc@gmail.com", "fvgbhnjm"); //don't change this
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("[PRISM] Notification for Deletion of Announcement");
            message.setText("The following announcement (ID: " + tempAnnc.getId() + ") has been deleted by the admin.\n\nTitle: " + tempAnnc.getTitle()
                    + "\n\nMessage: " + tempAnnc.getMessage());

            Transport.send(message);
            System.out.println("Notification email sent.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void postToTwitter(String title, String message) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("e8esm6lm48ESJjZGbPIz7y6oo")
                .setOAuthConsumerSecret("4fNFt2CUQPYGHDy5yRh7v14HwBJ43Q5yKHwPye3m9uEsvuF4qH")
                .setOAuthAccessToken("975955155492335616-0sJTAKwjK4CKgDpW8b9eZ61r7uXbE5N")
                .setOAuthAccessTokenSecret("RrKBCgq7dJzIsI6SP1jpanqKM41Gc9m8E7MFx9fFmAcmT");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        String messages = title + "\n\n" + message;
        Status status = twitter.updateStatus(messages);

        System.out.println("Updated Twitter status to " + status.getText());
    }

    public void postToFacebook(String title, String message) throws FacebookException {
        //ConfigurationBuilder cb = new ConfigurationBuilder();
        
        //FacebookFactory ff = new FacebookFactory((Configuration) cb.build());
        
        
        facebook4j.conf.ConfigurationBuilder fac = new facebook4j.conf.ConfigurationBuilder();
        FacebookFactory ff = new FacebookFactory(fac.build());
        Facebook facebook = ff.getInstance();
        String accessTokenString = "EAACEdEose0cBAONR4wnoJwBJq342hZA5bp3iNZACmzd7iVI06zpwzySQqdQcNXUxR7ahFErQ2pcZCjyMexh0MQGtmMmWj8lJaZBZAUdTnKPmN03dqWbdKKByyWwwk2j4E6AkLZCvLBFdaT6kddsxR2FIR37t02pdqmw3YXIzXEsXHU01Tc2eTj9x3vFmEO36xZBVb6c10UROTZBegVmtBmUc";
        AccessToken at = new AccessToken(accessTokenString);

        facebook.setOAuthAppId("157056298269296", "a48488cd453b77ff317270433fbd16b2");
        facebook.setOAuthAccessToken(at);
        facebook.setOAuthPermissions("email,publish_stream,publish_actions,manage_pages,publish_pages");

        String messages = title + "\n\n" + message;
        facebook.postStatusMessage(messages);

        System.out.println("Updated Facebook status to " + messages);
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

    public int getAnncSize() {
        return anncSize;
    }

    public void setAnncSize(int anncSize) {
        this.anncSize = anncSize;
    }

    public int getLatestCount() {
        return latestCount;
    }

    public void setLatestCount(int latestCount) {
        this.latestCount = latestCount;
    }
}
