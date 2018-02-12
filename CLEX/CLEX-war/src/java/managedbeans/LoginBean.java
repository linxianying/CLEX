
package managedbeans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import entity.User;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import session.ClexSessionBeanLocal;

/**
 *
 * @author eeren
 */
@Named(value = "loginBean")
public class LoginBean {
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    private String username;
    private String password;

    public void login() throws IOException{
       
        if(userEntity == null){
            
        }
    }
}
