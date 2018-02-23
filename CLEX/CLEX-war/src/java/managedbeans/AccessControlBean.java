/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
    
}
