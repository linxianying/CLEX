/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author Joseph
 */
@Named(value = "projectDetailsBean")
@Dependent
public class ProjectDetailsBean {

    /**
     * Creates a new instance of ProjectDetailsBean
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
    
    //for test
    private boolean check;
    
    public ProjectDetailsBean() {
    }
    
    @PostConstruct
    public void init() {
        //for test purpose
        //check = false;
        //module = csbl.findModule("PS2240", "2017", "2");
        //for test purpose only
        //this.username="namename";
        //this.student = csbl.findStudent(username);
        System.out.println("ProjectDetailsBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        setUsername((String) session.getAttribute("username"));
        setStudent(csbl.findStudent(getUsername()));
        
        module = (Module) session.getAttribute("module");

        System.out.println("ProjectDetailsBean Finish initialization");
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the module
     */
    public Module getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(Module module) {
        this.module = module;
    }
}
