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
    
    public AcctMgmtBean(){
        
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

    //eeren: Im not sure how but I need to research on how to store variables when you visit other pages
    public void logout() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
    
    //Only for those who cannot log in, don't use this for editing profile
    public void resetPassword() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        userEntity = csbl.findUser(username);
        
        if(userEntity != null){
            String newPass = csbl.resetPassword(username);
            
            if(newPass != null && !newPass.isEmpty()){
                resetEmail(newPass, userEntity.getEmail());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User " + username + "'s password has been reset. Please check your email.", "");
                FacesContext.getCurrentInstance().addMessage(null, fmsg);
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to reset password. Please check with the administrator.", "");
                FacesContext.getCurrentInstance().addMessage(null, fmsg);
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User '" + username + "' does not exists.", "");
            FacesContext.getCurrentInstance().addMessage(null, fmsg);
        }
    }
    
    public void resetEmail(String newPass, String email){
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
            message.setText("Your new password is " + newPass + ".");

            Transport.send(message);
            System.out.println("Reset password email sent.");
        }
        catch (MessagingException mex) {
             mex.printStackTrace();
        }
    
    }
    
}
