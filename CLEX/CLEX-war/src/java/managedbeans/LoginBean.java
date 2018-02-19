
package managedbeans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import entity.User;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import session.ClexSessionBeanLocal;

/**
 *
 * @author eeren
 */
@RequestScoped
@ManagedBean
public class LoginBean implements Serializable{
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    private String username;
    private String password;
    private String userType;

    public LoginBean(){
        
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

    public void doLogin() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        //Login based on usertype first, then check username, then password
        if(userType.equals("1")){ //Student
            userEntity = csbl.findStudent(username);
            if(userEntity == null){
               fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User '" + username + "' does not exists.", "");
               FacesContext.getCurrentInstance().addMessage(null, fmsg);
            }
            else{
                if(csbl.checkPassword(username, password)){
                    FacesContext.getCurrentInstance().getExternalContext().redirect("studentMain.xhtml");
                }
                else{
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password.", "");
                    FacesContext.getCurrentInstance().addMessage(null, fmsg);
                }
            }
        }
        else if(userType.equals("2")){ //Lecturer
            userEntity = csbl.findLecturer(username);
            if(userEntity == null){
               
            }
            else{
                
            }
        }
        else if(userType.equals("3")){ //Admin
            userEntity = csbl.findAdmin(username);
            if(userEntity == null){
               
            }
            else{
                
            }
        }
        else{ //Guest
            userEntity = csbl.findGuest(username);
            if(userEntity == null){
               
            }
            else{
                
            }
        }
    }
}
