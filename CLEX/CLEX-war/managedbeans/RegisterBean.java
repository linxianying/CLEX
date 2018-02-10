
package managedbeans;

import entity.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import session.ClexSessionBeanLocal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eeren
 */
@ManagedBean
@SessionScoped
public class RegisterBean implements Serializable{
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    private String username;
    private String password;

    public RegisterBean(){
        
    }
    
    public void register(ActionEvent evt){
        userEntity = csbl.getUser(username);
        if(userEntity == null){
            csbl.createUser(username, password);
        }
    }
}