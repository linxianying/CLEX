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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private Student student;
    private ArrayList<Module> takingModules;
    private boolean hasProjectGroup;
    private ProjectGroup projectGroup;
    private String currentYear;
    private String currentSem;
    private Module module;
    
    //for test
    private boolean check;
    
    public ProjectBean() {
    }
    
    @PostConstruct
    public void init() {
        //for test purpose
        //check = false;
        //module = csbl.findModule("PS2240", "2017", "2");
        //for test purpose only
        //this.username="namename";
        //this.student = csbl.findStudent(username);
        System.out.println("ProjectBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        username = (String) session.getAttribute("username");
        student = csbl.findStudent(username);
        
        takingModules = psbl.getTakingModules(student);
        this.setCurrentYearSem();
        System.out.println("ProjectBean Finish initialization");
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

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        this.projectGroup = projectGroup;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getCurrentSem() {
        return currentSem;
    }

    public void setCurrentSem(String currentSem) {
        this.currentSem = currentSem;
    }
    
    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
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
        //System.out.println("projectBean: Current Year:" + currentYear + ", current sem:" + currentSem);
    }
    
    public boolean hasProjectGroup(String moduleCode) {
        module = csbl.findModule(moduleCode, currentYear, currentSem);
        return psbl.checkStudentProjectGroup(student, module);
    }
    
    public ProjectGroup getStudentProjectGroup(String moduleCode) {
        module = csbl.findModule(moduleCode, currentYear, currentSem);
        projectGroup = psbl.getStudentProjectGroup(student, module);
        return projectGroup;
    }
    
     public String getStudentProjectGroupName(String moduleCode) {
        module = csbl.findModule(moduleCode, currentYear, currentSem);
        projectGroup = psbl.getStudentProjectGroup(student, module);
        return projectGroup.getName();
    }
    
    public void goFormGroup(String moduleCode) {
        try {
            module = csbl.findModule(moduleCode, currentYear, currentSem);
            session.setAttribute("module", module);
            session.setAttribute("currentYear", currentYear);
            session.setAttribute("currentSem", currentSem);
            context.getExternalContext().redirect("groupFormation.xhtml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void goModuleInfoPage (String moduleCode){
        try {
            module = csbl.findModule(moduleCode, currentYear, currentSem);
            session.setAttribute("module", module);
            session.setAttribute("currentYear", currentYear);
            session.setAttribute("currentSem", currentSem);
            context.getExternalContext().redirect("projectDetails.xhtml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void GoGroupPage(String moduleCode) {
        try {
            module = csbl.findModule(moduleCode, currentYear, currentSem);
            session.setAttribute("module", module);
            projectGroup = psbl.getStudentProjectGroup(student, module);
            session.setAttribute("projectGroup", projectGroup);
            context.getExternalContext().redirect("viewProjectCost.xhtml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void GoGroupSchedulePage(String moduleCode) {
        try {
            module = csbl.findModule(moduleCode, currentYear, currentSem);
            session.setAttribute("module", module);
            projectGroup = psbl.getStudentProjectGroup(student, module);
            session.setAttribute("projectGroup", projectGroup);
            context.getExternalContext().redirect("viewProjectSchedule.xhtml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void GoPRPage(String moduleCode){
    try {
            module = csbl.findModule(moduleCode, currentYear, currentSem);
            session.setAttribute("module", module);
            projectGroup = psbl.getStudentProjectGroup(student, module);
            session.setAttribute("projectGroup", projectGroup);
            context.getExternalContext().redirect("studentPeerReview.xhtml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
