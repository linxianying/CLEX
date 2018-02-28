/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import session.ClexSessionBeanLocal;

/**
 *
 * @author lin
 */
@ManagedBean
@RequestScoped
public class ProjectBean {

    /**
     * Creates a new instance of ProjectBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User user;
    private String username;
    
    public ProjectBean() {
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public User getUserEntity() {
        return user;
    }

    public void setUserEntity(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
}
