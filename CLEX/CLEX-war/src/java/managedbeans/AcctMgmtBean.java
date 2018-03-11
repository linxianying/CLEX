/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.*;  
import javax.activation.*;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import session.ClexSessionBeanLocal;

/**
 *
 * @author eeren
 */
@SessionScoped
@ManagedBean
public class AcctMgmtBean implements Serializable{
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    private String username;
    private String password;
    private String userType;
    private String email;
    private String newPassword1;
    private String newPassword2;
    
    FacesContext context;
    HttpSession session;
    
    public AcctMgmtBean(){
        
    }

    public void logout() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
    
    public void editProfile() throws IOException{
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        userEntity = (User) session.getAttribute("user");
        username = userEntity.getUsername();
        userType = userEntity.getUserType();
        
        if(userType.equals("Student")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
        }
        else if (userType.equals("Lecturer")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("lecturerProfile.xhtml");
        }
        else if (userType.equals("Guest")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("guestProfile.xhtml");
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminProfile.xhtml");
        }
        //student profile page: profile.xhtml
        //lecturer profile page: lecturerProfile.xhtml
        //admin profile page: adminProfile.xhtml
        //guest profile page: guest.xhtml
    }
    
    public void changePassword() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        userEntity = csbl.findUser(username);
        
        if(userEntity != null){
            if(userEntity.getEmail().equals(email)){
                if(newPassword1.equals(newPassword2)){
                    csbl.changePassword(username, newPassword2);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password has been successfully reset.", ""));
                    FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                }    
                else{
                    System.out.println("Passwords Mismatch");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect new passwords.", ""));
                }
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The entered email is not registered with the user.", "");
                FacesContext.getCurrentInstance().addMessage(null, fmsg);
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User '" + username + "' does not exists.", "");
            FacesContext.getCurrentInstance().addMessage(null, fmsg);
            
            
        }
    }
    
    //Only for those who cannot log in, don't use this for editing profile
    public void resetPassword() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        userEntity = csbl.findUser(username);
        
        if(userEntity != null){
            //Old method for resetting password
            //String newPass = csbl.resetPassword(username);
            /*if(newPass != null && !newPass.isEmpty()){
                resetEmail(newPass, userEntity.getEmail());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User " + username + "'s password has been reset. Please check your email.", "");
                FacesContext.getCurrentInstance().addMessage(null, fmsg);
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to reset password. Please check with the administrator.", "");
                FacesContext.getCurrentInstance().addMessage(null, fmsg);
            }
            */
            resetEmail(userEntity.getEmail());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "The reset instructions have been sent to " + username + "'s email.", "");
            FacesContext.getCurrentInstance().addMessage(null, fmsg);
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User '" + username + "' does not exists.", "");
            FacesContext.getCurrentInstance().addMessage(null, fmsg);
        }
    }
    
    public void resetEmail(String email){
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

            message.setSubject("[PRISM] Password Reset");
            //message.setText("Your account has been reseted. Please enter " + newPass + "as your password on your next log in.");
            message.setText("Dear " + username + ",\n"
                    + "To reset the password, please use the link below and provide the necessary details for authentication."
                    + "\n\nhttp://localhost:8080/CLEX-war/reset.xhtml \n\n"
                    + "Best regards,\nPRISM Organization");
            Transport.send(message);
            System.out.println("Reset password email sent.");
        }
        catch (MessagingException mex) {
             mex.printStackTrace();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    
}
