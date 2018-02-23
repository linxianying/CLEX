/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author eeren
 */
@Local
public interface UserAccessControlBeanLocal {
    public List getAllUsers();
    public List<String> createUserTypes();
    public boolean approveUser(String username);
}
