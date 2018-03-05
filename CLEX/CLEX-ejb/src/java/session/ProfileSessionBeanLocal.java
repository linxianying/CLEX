/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Local;

/**
 *
 * @author eeren
 */
@Local
public interface ProfileSessionBeanLocal {

    public void editStudent(String username, String name, String email, Long contactNum, String faculty, String major, String matricYear, String matricSem);
    public void editLecturer(String username, String name, String email, String school, Long contactNum, String faculty);
    public void editGuest(String username, String name, String email, String school, Long contactNum);
    public void editAdmin(String username, String name, String email, String school, Long contactNum);
    
    public void changePassword(String username, String newPassword);
    public boolean checkPassword(String username, String password);
}
