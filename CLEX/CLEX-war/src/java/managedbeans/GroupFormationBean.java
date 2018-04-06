/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.GroupFormationSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@Named(value = "groupFormationBean")
@RequestScoped
public class GroupFormationBean {

    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectSessionBeanLocal psbl;
    @EJB
    private GroupFormationSessionBeanLocal gfsbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private Student student;
    private ProjectGroup projectGroup;
    private Module module;
    private SuperGroup superGroup;
    private ArrayList<ProjectGroup> projectGroups;
    
    private String currentYear;
    private String currentSem;
    private int minStudentNum;
    private int maxStudentNum;
    
    
    public GroupFormationBean() {
    }
    
    @PostConstruct
    public void init() {
        
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        student = (Student) session.getAttribute("user");
        username = student.getUsername();
        currentYear = (String) session.getAttribute("currentYear");
        currentSem = (String) session.getAttribute("currentSem");
        module = (Module) session.getAttribute("module");
        superGroup = module.getSuperGroup();
        minStudentNum = superGroup.getMinStudentNum();
        maxStudentNum = superGroup.getMaxStudentNum();
        if (superGroup != null)
            projectGroups = gfsbl.getAllProjectGroups(superGroup.getId());
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public GroupFormationSessionBeanLocal getGfsbl() {
        return gfsbl;
    }

    public void setGfsbl(GroupFormationSessionBeanLocal gfsbl) {
        this.gfsbl = gfsbl;
    }

    public ArrayList<ProjectGroup> getProjectGroups() {
        System.out.println("groupFormationBean: projectGroups" +  projectGroups.size());
        System.out.println( projectGroups);
        return projectGroups;
    }

    public void setProjectGroups(ArrayList<ProjectGroup> projectGroups) {
        this.projectGroups = projectGroups;
    }

    public SuperGroup getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(SuperGroup superGroup) {
        this.superGroup = superGroup;
    }

    public int getMinStudentNum() {
        return minStudentNum;
    }

    public void setMinStudentNum(int minStudentNum) {
        this.minStudentNum = minStudentNum;
    }

    public int getMaxStudentNum() {
        return maxStudentNum;
    }

    public void setMaxStudentNum(int maxStudentNum) {
        this.maxStudentNum = maxStudentNum;
    }
    
    public void joinGroup(Long id) throws IOException{
        FacesMessage fmsg = new FacesMessage();
        System.out.println("first step start");
        projectGroup = gfsbl.findProjectGroup(id);
        System.out.println("first step finish");
        // if the group is full ,refresh the page
        if (!gfsbl.joinGroup(student,projectGroup)) {
            context.getExternalContext().redirect("groupFormation.xhtml");
        }
        //if sucessfully join the group, redeirect to the project page
        else {
            fmsg = new FacesMessage("Successful", "You have join the project group " 
                    + projectGroup.getName() + " for module " 
                    + projectGroup.getSuperGroup().getModule().getCourse().getModuleCode());
            context.addMessage(null, fmsg);
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect("project.xhtml");
        }
    }
    
}
