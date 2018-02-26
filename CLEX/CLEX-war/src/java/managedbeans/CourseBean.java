/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Module;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class CourseBean implements Serializable{

    @EJB
    CourseMgmtBeanLocal cmbl;
    
    //Course
    Course courseEntity;
    private String moduleCode;
    private String moduleName;
    private String moduleInfo;
    private boolean discontinuedBool;
    private String discountinuedYear;
    private String discountinuedSem;
    private String offeredSem;
    private String school;
    private String modularCredits;    
    private String workload;

    
    //Module
    Module moduleEntity;
    private String takenYear;
    private String takenSem;
    private String prerequisite;
    private String preclusions;
    
    //Lesson
    private String day;
    private String timeFrom;
    private String timeEnd;
    private String type;
    private String venue;
    
    //Others
    private String lecturerUser;
    
    public CourseBean() {
        
    }
    
    public void enterCourse() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkNewCourse(moduleCode) == true){
            cmbl.createCourse(moduleCode, moduleName, moduleInfo, false, "",  "",  offeredSem,  school,  modularCredits,  workload);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, moduleCode + " has been created.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Course '" + moduleCode + "' already exists.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void enterModule() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance(); 
        if(cmbl.checkNewModule(moduleCode, takenYear, takenSem) == true){
            cmbl.createModule(takenYear, takenSem, prerequisite, preclusions, moduleCode);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been created.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating module.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void enterLesson() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkNewLesson(moduleCode, takenYear, takenSem, day, timeFrom, timeEnd) == true){
            cmbl.createLesson(day, timeFrom, timeEnd, type, venue, moduleCode, takenYear, takenSem);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been created.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating lesson.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    //All edits cannot change primary key (Course: moduleCode, Module: takenYear, takenSem, Lesson: day, timeFrom, timeEnd)
    public void editCourse() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkExistingCourse(moduleCode) == true){
            cmbl.editCourse(moduleCode, moduleName, moduleInfo, discontinuedBool, discountinuedYear,  discountinuedSem, 
                    offeredSem, school, modularCredits, workload);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, moduleCode + " has been edited.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Course '" + moduleCode + "' does not exists.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void editModule() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance(); 
        if(cmbl.checkExistingModule(moduleCode, takenYear, takenSem) == true){
            cmbl.editModule(takenYear, takenSem, prerequisite, preclusions, moduleCode);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been edited.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error editing module.", "");
            context.addMessage(null, fmsg);
        }
    }
    public void editLesson() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkExistingLesson(moduleCode, takenYear, takenSem, day, timeFrom, timeEnd) == true){
            cmbl.editLesson(day, timeFrom, timeEnd, type, venue, moduleCode, takenYear, takenSem);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been edited.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error editing lesson.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void removeCourse() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkExistingCourse(moduleCode) == true){
            if(cmbl.deleteCourse(moduleCode) == true){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, moduleCode + " has been deleted.", "");
                System.out.println("yes");
                context.addMessage(null, fmsg);
                context.getExternalContext().redirect("adminCourse.xhtml");
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete course.", "");
                context.addMessage(null, fmsg);
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Course not found.", "");
            context.addMessage(null, fmsg);
        }
    }
    public void removeModule() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkExistingModule(moduleCode, takenYear, takenSem) == true){
            if(cmbl.deleteModule(moduleCode, takenYear, takenSem) == true){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been deleted.", "");
                context.addMessage(null, fmsg);
                context.getExternalContext().redirect("adminCourse.xhtml");
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete module.", "");
                context.addMessage(null, fmsg);
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Module not found.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void removeLesson() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if(cmbl.checkExistingLesson(moduleCode, takenYear, takenSem, day, timeFrom, timeEnd) == true){
            if(cmbl.deleteLesson(day, timeFrom, timeEnd, moduleCode, takenYear, takenSem) == true){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been deleted.", "");
                context.addMessage(null, fmsg);
                context.getExternalContext().redirect("adminCourse.xhtml");
            }
            else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete lesson.", "");
                context.addMessage(null, fmsg);
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lesson not found.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public void assignLecturer() throws IOException{
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance(); 
        if(cmbl.checkExistingModule(moduleCode, takenYear, takenSem) == true){
            if(cmbl.linkLecturerToModule(moduleCode, takenYear, takenSem, lecturerUser) == true){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lecturer " + lecturerUser + " assigned to module.", "");
                context.addMessage(null, fmsg);
                context.getExternalContext().redirect("adminCourse.xhtml");
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to assign lecturer.", "");
                context.addMessage(null, fmsg);
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Module does not exists", "");
            context.addMessage(null, fmsg);
        }
    }
    
    public Course getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(Course courseEntity) {
        this.courseEntity = courseEntity;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public boolean isDiscontinuedBool() {
        return discontinuedBool;
    }

    public void setDiscontinuedBool(boolean discontinuedBool) {
        this.discontinuedBool = discontinuedBool;
    }

    public String getDiscountinuedYear() {
        return discountinuedYear;
    }

    public void setDiscountinuedYear(String discountinuedYear) {
        this.discountinuedYear = discountinuedYear;
    }

    public String getDiscountinuedSem() {
        return discountinuedSem;
    }

    public void setDiscountinuedSem(String discountinuedSem) {
        this.discountinuedSem = discountinuedSem;
    }

    public String getOfferedSem() {
        return offeredSem;
    }

    public void setOfferedSem(String offeredSem) {
        this.offeredSem = offeredSem;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getModularCredits() {
        return modularCredits;
    }

    public void setModularCredits(String modularCredits) {
        this.modularCredits = modularCredits;
    }

    public Module getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(Module moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public String getTakenYear() {
        return takenYear;
    }

    public void setTakenYear(String takenYear) {
        this.takenYear = takenYear;
    }

    public String getTakenSem() {
        return takenSem;
    }

    public void setTakenSem(String takenSem) {
        this.takenSem = takenSem;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getPreclusions() {
        return preclusions;
    }

    public void setPreclusions(String preclusions) {
        this.preclusions = preclusions;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLecturerUser() {
        return lecturerUser;
    }

    public void setLecturerUser(String lecturerUser) {
        this.lecturerUser = lecturerUser;
    }
    
}
