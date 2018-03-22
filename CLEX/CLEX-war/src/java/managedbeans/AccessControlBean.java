/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.primefaces.context.RequestContext;
import session.ClexSessionBeanLocal;
import session.UserAccessControlBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class AccessControlBean implements Serializable {

    @EJB
    private UserAccessControlBeanLocal uacbl;
    @EJB
    private ClexSessionBeanLocal csbl;

    private User userEntity;
    private String username;

    private List<User> users;
    private List<User> filteredUsers;
    private List<String> allUserTypes;

    public AccessControlBean() {
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        users = uacbl.getAllUsers();
        allUserTypes = uacbl.createUserTypes();
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

    public void approveUser() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        userEntity = csbl.findUser(username);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (userEntity != null) {
            if (uacbl.approveUser(username) == true) {
                approveEmail(username, userEntity.getEmail());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success.", username + " has been approved.");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.update("userForm:userlist");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", username + " has already been approved.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", username + " not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void approveUser(String username) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        userEntity = csbl.findUser(username);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (userEntity != null) {
            if (uacbl.approveUser(username) == true) {
                approveEmail(username, userEntity.getEmail());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success.", username + " has been approved.");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.update("userForm:userlist");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", username + " has already been approved.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", username + " not found.");
            context.addMessage(null, fmsg);
        }
    }

    //Note: the method itself does not do anything but to only send a reject email
    public void rejectUser(User selectedUser) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempUser = selectedUser.getUsername();
        userEntity = csbl.findUser(tempUser);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (userEntity != null) {
            if (!userEntity.isApproval()) {
                rejectEmail(tempUser, userEntity.getEmail());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rejection email sent.", "");
                FacesContext.getCurrentInstance().addMessage(null, fmsg);
                refresh();
                reqcontext.update("userForm:userlist");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", tempUser + " cannot be rejected.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", tempUser + " not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void approveEmail(String username, String email) {
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

            message.setSubject("[PRISM] Notification for Approval of Account");
            message.setText("Thank you for using PRISM. The account " + username + " has been approved.");

            Transport.send(message);
            System.out.println("Approve email sent.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void rejectEmail(String username, String email) {
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

            message.setSubject("[PRISM] Rejection for Approval of Account");
            message.setText("The account " + username + " was rejected for approval. Please contact the administrator for further enquiries.");

            Transport.send(message);
            System.out.println("Reject email sent.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void removeUser(User user) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (uacbl.deleteUser(user.getUsername()) == true) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUsername() + " has been deleted.", "");
            context.addMessage(null, fmsg);
            refresh();
            reqcontext.execute("PF('usersTable').clearFilters()");
            reqcontext.update("userForm:userlist");
        } else if (uacbl.deleteUser(user.getUsername()) == false){
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete " + user.getUsername() + ".", "");
            context.addMessage(null, fmsg);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<String> getAllUserTypes() {
        return allUserTypes;
    }

    public void setAllUserTypes(List<String> allUserTypes) {
        this.allUserTypes = allUserTypes;
    }
}
