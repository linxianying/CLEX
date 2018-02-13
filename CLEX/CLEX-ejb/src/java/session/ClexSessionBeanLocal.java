/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ClexSessionBeanLocal {

    public void createStudent(String username, String password, String name, String email, String school, Long contactNum, 
                String faculty, String major, String matricYear, String matricSem, String currentYear, double cap);
    public boolean checkNewUser(String username);
    
}
