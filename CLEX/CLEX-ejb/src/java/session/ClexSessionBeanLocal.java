/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.User;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ClexSessionBeanLocal {

    public void createUser(String username, String password, String name, String email, String userType, String school, Long contactNum);
    public boolean checkNewUser(String username);
    
}
