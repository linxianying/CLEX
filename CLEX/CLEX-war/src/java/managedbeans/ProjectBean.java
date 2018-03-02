/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.User;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Student student;
    private ArrayList<Module> takingModules;
    private boolean hasProjectGroup;
    private ProjectGroup projectGroup;
    private String currentYear;
    private String currentSem;
    
    //for test
    private boolean check;
    private Module module;
    
    public ProjectBean() {
    }
    
    @PostConstruct
    public void init() {
        //for test purpose
        check = false;
        //module = csbl.findModule("PS2240", "2017", "2");
        //for test purpose only
        this.username="namename";
        this.student = csbl.findStudent(username);
        takingModules = psbl.getTakingModules(username);
        this.setCurrentYearSem();
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    public void setCurrentYearSem() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = "2";
            year--;
        }
        else {
            currentSem = "1";
        }
        currentYear = Integer.toString(year);
        System.out.println("projectBean: Current Year:" + currentYear + ", current sem:" + currentSem);
    }
    
    public boolean hasProjectGroup(String moduleCode) {
        module = csbl.findModule(moduleCode, currentYear, currentSem);
        return psbl.checkStudentProjectGroup(student, module);
    }
    
    public String getStudentProjectGroup(String moduleCode) {
        module = csbl.findModule(moduleCode, currentYear, currentSem);
        projectGroup = psbl.getStudentProjectGroup(student, module);
        return projectGroup.getName();
    }
    
}
