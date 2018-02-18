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
public class ScheduleBean {

    /**
     * Creates a new instance of ScheduleBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    private String username;
    private String userType;
    
    public ScheduleBean() {
    }
    
}
