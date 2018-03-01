/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.User;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import session.ClexSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author lin
 */
@ManagedBean(name="projectBean")
@RequestScoped
public class ProjectBean {

    /**
     * Creates a new instance of ProjectBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectSessionBeanLocal psbl;
    
    private User user;
    private String username;
    private ArrayList<Module> takingModules;
    private boolean hasProjectGroup;
    
    //for test
    private boolean check;
    private Module module;
    
    public ProjectBean() {
    }
    
    @PostConstruct
    public void init() {
        //for test purpose
        check = false;
        module = csbl.findModule("PS2240", "2017", "2");
        //for test purpose only
        this.username="namename";
        takingModules = psbl.getTakingModules(username);
        hasProjectGroup = false;
    }

    
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    public ProjectSessionBeanLocal getPsbl() {
        return psbl;
    }

    public void setPsbl(ProjectSessionBeanLocal psbl) {
        this.psbl = psbl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Module> getTakingModules() {
        return takingModules;
    }

    public void setTakingModules(ArrayList<Module> takingModules) {
        this.takingModules = takingModules;
    }

    public boolean isHasProjectGroup() {
        return hasProjectGroup;
    }

    public void setHasProjectGroup(boolean hasProjectGroup) {
        this.hasProjectGroup = hasProjectGroup;
    }
    
    

    
}
