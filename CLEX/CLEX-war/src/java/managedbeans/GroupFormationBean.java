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
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.GroupFormationSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "groupFormationBean")
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
    private ProjectGroup currentProjectGroup;
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
        
        String username = (String) session.getAttribute("username");
        student = csbl.findStudent(username);
        currentYear = (String) session.getAttribute("currentYear");
        currentSem = (String) session.getAttribute("currentSem");
        module = (Module) session.getAttribute("module");
        superGroup = module.getSuperGroup();
        minStudentNum = superGroup.getMinStudentNum();
        maxStudentNum = superGroup.getMaxStudentNum();
        projectGroups = gfsbl.getAllProjectGroups(superGroup.getId());
        //student current project group, can be null
        if (gfsbl.checkStudentGroupId(student.getId(), superGroup.getId()) != null)
            currentProjectGroup = gfsbl.findProjectGroup(gfsbl.checkStudentGroupId(student.getId(), superGroup.getId()));
        else
            currentProjectGroup = null;
    }
    
    public void refresh(){
        superGroup = gfsbl.findSuperGroup(superGroup.getId());
        projectGroups = gfsbl.getAllProjectGroups(superGroup.getId());
        minStudentNum = superGroup.getMinStudentNum();
        maxStudentNum = superGroup.getMaxStudentNum();
        //student current project group, can be null
        if (gfsbl.checkStudentGroupId(student.getId(), superGroup.getId()) != null)
            currentProjectGroup = gfsbl.findProjectGroup(gfsbl.checkStudentGroupId(student.getId(), superGroup.getId()));
        else
            currentProjectGroup = null;
        
        projectGroups = gfsbl.getAllProjectGroups(superGroup.getId());
    }
    
    //student already have a group; leave a group and join another group
    public void joinGroup(Long id) throws IOException{
        System.out.println("Current group " + currentProjectGroup + ", supergroup " +this.superGroup.getId());
        FacesMessage fmsg = new FacesMessage();
//        System.out.println("first step start");
        projectGroup = gfsbl.findProjectGroup(id);
//        System.out.println("first step finish");
        if (currentProjectGroup != null) {
            boolean full = !gfsbl.joinGroup(student.getId(),id,currentProjectGroup.getId());
            // if the group is full ,refresh the page
            if (full) {
                //            context.getExternalContext().redirect("groupFormation.xhtml");
                fmsg = new FacesMessage("Error", "The group "+ projectGroup.getName() + " is full!");
                context.addMessage(null, fmsg);
            }
            else {
                fmsg = new FacesMessage("Successful", "You have changed to the project group "
                        + projectGroup.getName() + " for module "
                        + projectGroup.getSuperGroup().getModule().getCourse().getModuleCode());
                context.addMessage(null, fmsg);
                context.getExternalContext().getFlash().setKeepMessages(true);
                context.getExternalContext().redirect("project.xhtml");
                this.refresh();
            }
        }
        else if (currentProjectGroup == null) {
            this.joinNewGroup(id);
            this.refresh();
        }
    }
    
    //student does not have group yet, join a group
    public void joinNewGroup(Long id) throws IOException{
        FacesMessage fmsg = new FacesMessage();
        projectGroup = gfsbl.findProjectGroup(id);
        // if the group is full ,refresh the page
        boolean full = !gfsbl.joinGroup(student.getId(),id,null);
        if (full) {
            System.out.println("first step start");
            fmsg = new FacesMessage("Error", "The group "+ projectGroup.getName() + " is full!");
            context.addMessage(null, fmsg);
//            context.getExternalContext().redirect("groupFormation.xhtml");
        }
        else {
            //if sucessfully join the group
            System.out.println("join new group ");
            fmsg = new FacesMessage("Successful", "You have changed to the project group "
                    + projectGroup.getName() + " for module "
                    + projectGroup.getSuperGroup().getModule().getCourse().getModuleCode());
            context.addMessage(null, fmsg);
            context.getExternalContext().getFlash().setKeepMessages(true);
//            context.getExternalContext().redirect("project.xhtml");
        }
    }
    
    public void leaveGroup(Long id) throws IOException{
        FacesMessage fmsg = new FacesMessage();
        gfsbl.leaveGroup(student.getId(), id);
        fmsg = new FacesMessage("Successful", "You have leave the project group "+ currentProjectGroup.getName());
        context.addMessage(null, fmsg);
        this.refresh();
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
//        System.out.println("groupFormationBean: projectGroups" +  projectGroups.size());
//        System.out.println( projectGroups);
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
    
    public ProjectGroup getCurrentProjectGroup() {
        return currentProjectGroup;
    }
    
    public void setCurrentProjectGroup(ProjectGroup currentProjectGroup) {
        this.currentProjectGroup = currentProjectGroup;
    }
    
    
    
}
