/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
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
    
    public AccessControlBean() {
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
    
    public void approveUser() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(uacbl.approveUser(username) == true){
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Approved.", username + " has been approved.");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminUserList.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to approved.", " Failed to approve " + username + ".");
            context.addMessage(null, fmsg);
        }
    }
    
    //Note: the method itself does not do anything but to only send a reject email
    public void rejectUser() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        userEntity = csbl.findUser(username);
        
        if(userEntity != null){
            rejectEmail(username, userEntity.getEmail());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rejection email sent.", "");
            FacesContext.getCurrentInstance().addMessage(null, fmsg);
            context.getExternalContext().redirect("adminUserList.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to reject " + username + ".", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void rejectEmail(String username, String email){
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

        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("[PRISM] Rejection for Approval of Account");
            message.setText("The account " + username + " was rejected for approval. Please contact the administrator for further enquiries.");

            Transport.send(message);
            System.out.println("Reset password email sent.");
        }
        catch (MessagingException mex) {
             mex.printStackTrace();
        }
    }
    
    public void removeUser() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(uacbl.deleteUser(username) == true){
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, username + " has been deleted.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminUserList.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete " + username + ".", "");
            context.addMessage(null, fmsg);
        }
    }
    
}
