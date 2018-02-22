/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import session.UserAccessControlBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@SessionScoped
public class AccessControlBean implements Serializable {
    
    @EJB
    private UserAccessControlBeanLocal uacbl;
    private User userEntity;
    private String username;
    
    public AccessControlBean() {
    }
    
    
    
    
}
