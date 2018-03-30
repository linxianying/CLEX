/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.Poll;
import entity.Student;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "pollInfoBean")
@SessionScoped
public class PollInfoBean implements Serializable {

    /**
     * Creates a new instance of PollInfoBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectSessionBeanLocal psbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private Student student;
    private Module module;
    private Collection<Poll> polls;
    private Collection<Poll> filteredPolls;
   
    public PollInfoBean() {
    }
    
    @PostConstruct
    public void init() {
        System.out.println("PollingBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        student = (Student) session.getAttribute("user");
        username = student.getUsername();
        module = (Module) session.getAttribute("module");
        polls = (Collection<Poll>) module.getPolls();
               
    }  

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ProjectSessionBeanLocal getPsbl() {
        return psbl;
    }

    public void setPsbl(ProjectSessionBeanLocal psbl) {
        this.psbl = psbl;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Collection<Poll> getPolls() {
        return polls;
    }

    public void setPolls(Collection<Poll> polls) {
        this.polls = polls;
    }

    public Collection<Poll> getFilteredPolls() {
        return filteredPolls;
    }

    public void setFilteredPolls(Collection<Poll> filteredPolls) {
        this.filteredPolls = filteredPolls;
    }
    
    
}
