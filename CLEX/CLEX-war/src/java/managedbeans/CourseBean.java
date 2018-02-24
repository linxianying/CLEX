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
 * @author Gwee
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
    private String workload;
    private String modularCredits;
    
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
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Existing module already exist.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    //WIP
    public void enterLesson(){
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
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
