/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Lecturer;
import entity.Lesson;
import entity.Module;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class CourseBean implements Serializable {

    @EJB
    CourseMgmtBeanLocal cmbl;

    //Course
    Course courseEntity;
    private Course selectedCourse;

    private Module selectedModule;
    private Lesson selectedLesson;
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
    private List<String> modularCreditList;

    private List<String> timelist;

    private List<String> schoollist;
    private ArrayList<User> lecturerlist;
    private ArrayList<Module> modulelist;
    private ArrayList<Course> courselist;

    private ArrayList<Lesson> lessonlist; //list of all lesson entity
    private List<String> daylist;

    public CourseBean() {

    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        schoollist = (List) cmbl.getAllSchools();
        courselist = (ArrayList<Course>) cmbl.getAllCourses();
        modulelist = (ArrayList<Module>) cmbl.getAllModules();
        timelist = (List) cmbl.getAllTimings();
        daylist = (List) cmbl.getAllDays();
        lecturerlist = (ArrayList<User>) cmbl.getLecturerName();
        lessonlist = (ArrayList<Lesson>) cmbl.getAllLessons();
        modularCreditList = (List) cmbl.getAllModularCredits();
    }

    public Collection<Lecturer> getAssignedLecturer(Module moduleEntity) {
        List<Lecturer> users = new ArrayList<Lecturer>();
        List<Lecturer> lecturers = (List) moduleEntity.getLecturers();
        for (int i = 0; i < lecturers.size(); i++) {
            users.add(lecturers.get(i));
        }
        return users;
    }

    public void enterCourse() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkNewCourse(moduleCode) == true) {
            cmbl.createCourse(moduleCode, moduleName, moduleInfo, false, "", "", offeredSem, school, modularCredits, workload);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, moduleCode + " has been created.", "");
            context.addMessage(null, fmsg);
            //context.getExternalContext().redirect("adminCourse.xhtml");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Course '" + moduleCode + "' already exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void enterModule(String moduleCode) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if (cmbl.checkNewModule(moduleCode, takenYear, takenSem) == true) {
            cmbl.createModule(takenYear, takenSem, prerequisite, preclusions, moduleCode);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been created.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module already exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void enterLesson() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if (cmbl.checkNewLesson(moduleCode, takenYear, takenSem, day, timeFrom, timeEnd) == true) {
            cmbl.createLesson(day, timeFrom, timeEnd, type, venue, moduleCode, takenYear, takenSem);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been created.", "");
            context.addMessage(null, fmsg);
            context.getExternalContext().redirect("adminCourse.xhtml");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson already exists.");
            context.addMessage(null, fmsg);
        }
    }

    //All edits cannot change primary key (Course: moduleCode | Module: takenYear, takenSem | Lesson: day, timeFrom, timeEnd)
    public void editCourse(Course courseEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String tempModCode = courseEntity.getModuleCode();
        String tempModName = courseEntity.getModuleName();
        String tempModInfo = courseEntity.getModuleInfo();
        boolean tempBool = courseEntity.isDiscontinuedBool();
        String tempYear = courseEntity.getDiscountinuedYear();
        String tempDisSem = courseEntity.getDiscountinuedSem();
        String tempOffSem = courseEntity.getOfferedSem();
        String tempSchool = courseEntity.getSchool();
        String tempModCredits = courseEntity.getModularCredits();
        String tempWorkload = courseEntity.getWorkload();
        if (cmbl.checkExistingCourse(tempModCode) == true) {
            cmbl.editCourse(tempModCode, tempModName, tempModInfo, tempBool, tempYear, tempDisSem,
                    tempOffSem, tempSchool, tempModCredits, tempWorkload);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, tempModCode + " has been edited.", "");
            reqcontext.update("courselist");
            context.addMessage(null, fmsg);
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Course '" + tempModCode + "' does not exists.");
            context.addMessage(null, fmsg);
        }

    }

    public void editModule(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempModCode = moduleEntity.getCourse().getModuleCode();
        String tempTakenYear = moduleEntity.getTakenYear();
        String tempTakenSem = moduleEntity.getTakenSem();
        String tempPrerequisite = moduleEntity.getPrerequisite();
        String tempPreclusions = moduleEntity.getPreclusions();
        if (cmbl.checkExistingModule(tempModCode, tempTakenYear, tempTakenSem) == true) {
            cmbl.editModule(tempTakenYear, tempTakenSem, tempPrerequisite, tempPreclusions, tempModCode);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been edited.", "");
            context.addMessage(null, fmsg);
            //context.getExternalContext().redirect("adminCourse.xhtml");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void editLesson(Lesson lessonEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempModCode = lessonEntity.getModule().getCourse().getModuleCode();
        String tempTakenYear = lessonEntity.getModule().getTakenYear();
        String tempTakenSem = lessonEntity.getModule().getTakenSem();
        String tempDay = lessonEntity.getDay();
        String tempTimeStart = lessonEntity.getTimeFrom();
        String tempTimeEnd = lessonEntity.getTimeEnd();
        String tempType = lessonEntity.getType();
        String tempVenue = lessonEntity.getVenue();
        if (cmbl.checkExistingLesson(tempModCode, tempTakenYear, tempTakenSem, tempDay, tempTimeStart, tempTimeEnd) == true) {
            cmbl.editLesson(tempDay, tempTimeStart, tempTimeEnd, tempType, tempVenue, tempModCode, tempTakenYear, tempTakenSem);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been edited.", "");
            context.addMessage(null, fmsg);
            //context.getExternalContext().redirect("adminCourse.xhtml");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson does not exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void lectEditModule(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        lecturerUser = (String) session.getAttribute("username");
        String tempModCode = moduleEntity.getCourse().getModuleCode();
        String tempTakenYear = moduleEntity.getTakenYear();
        String tempTakenSem = moduleEntity.getTakenSem();
        String tempPrerequisite = moduleEntity.getPrerequisite();
        String tempPreclusions = moduleEntity.getPreclusions();
        if (cmbl.checkExistingModule(tempModCode, tempTakenYear, tempTakenSem) == true) {
            if (cmbl.checkLectTeachModule(lecturerUser, tempModCode, tempTakenYear, tempTakenSem)) {
                cmbl.editModule(tempTakenYear, tempTakenSem, tempPrerequisite, tempPreclusions, tempModCode);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been edited.", "");
                context.addMessage(null, fmsg);
                //context.getExternalContext().redirect("lecturerModule.xhtml");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not authorised to edit this module.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exist.");
            context.addMessage(null, fmsg);
        }
    }

    public void lectEditLesson(Lesson lessonEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        lecturerUser = (String) session.getAttribute("username");
        String tempModCode = lessonEntity.getModule().getCourse().getModuleCode();
        String tempTakenYear = lessonEntity.getModule().getTakenYear();
        String tempTakenSem = lessonEntity.getModule().getTakenSem();
        String tempDay = lessonEntity.getDay();
        String tempTimeStart = lessonEntity.getTimeFrom();
        String tempTimeEnd = lessonEntity.getTimeEnd();
        String tempType = lessonEntity.getType();
        String tempVenue = lessonEntity.getVenue();
        if (cmbl.checkExistingLesson(tempModCode, tempTakenYear, tempTakenSem, tempDay, tempTimeStart, tempTimeEnd) == true) {
            if (cmbl.checkLectTeachModule(lecturerUser, tempModCode, tempTakenYear, tempTakenSem)) {
                cmbl.editLesson(tempDay, tempTimeStart, tempTimeEnd, tempType, tempVenue, tempModCode, tempTakenYear, tempTakenSem);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been edited.", "");
                context.addMessage(null, fmsg);
                //context.getExternalContext().redirect("lecturerModule.xhtml");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not authorised to edit this lesson.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson does not exist.");
            context.addMessage(null, fmsg);
        }
    }

    public void removeCourse() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String modCode = selectedCourse.getModuleCode();
        if (cmbl.checkExistingCourse(modCode) == true) {
            if (cmbl.deleteCourse(modCode) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, modCode + " has been deleted.", "Please refresh the page");
                System.out.println("yes");
                context.addMessage(null, fmsg);
                reqcontext.update("courselist");
                //context.getExternalContext().redirect("adminCourse.xhtml");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete course.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Course " + modCode + " not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void removeModule() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String modCode = selectedModule.getCourse().getModuleCode();
        String tempTakenYear = selectedModule.getTakenYear();
        String tempTakenSem = selectedModule.getTakenSem();
        if (cmbl.checkExistingModule(modCode, tempTakenYear, tempTakenSem) == true) {
            if (cmbl.deleteModule(modCode, tempTakenYear, tempTakenSem) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Module has been deleted.", "Please refresh the page");
                context.addMessage(null, fmsg);
                //context.getExternalContext().redirect("adminCourse.xhtml");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete module.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void removeLesson() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempDay = selectedLesson.getDay();
        System.out.println("Day: " + tempDay);
        String tempTimeStart = selectedLesson.getTimeFrom();
        System.out.println("start: " + tempTimeStart);
        String tempTimeEnd = selectedLesson.getTimeEnd();
        System.out.println("end: " + tempTimeEnd);
        String tempModCode = selectedLesson.getModule().getCourse().getModuleCode();
        System.out.println("modcode: " + tempModCode);
        String tempTakenYear = selectedLesson.getModule().getTakenYear();
        System.out.println("year: " + tempTakenYear);
        String tempTakenSem = selectedLesson.getModule().getTakenSem();
        System.out.println("sem: " + tempTakenSem);

        if (cmbl.checkExistingLesson(tempModCode, tempTakenYear, tempTakenSem, tempDay, tempTimeStart, tempTimeEnd) == true) {
            if (cmbl.deleteLesson(tempDay, tempTimeStart, tempTimeEnd, tempModCode, tempTakenYear, tempTakenSem) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lesson has been deleted.", "Please refresh the page");
                context.addMessage(null, fmsg);
                RequestContext.getCurrentInstance().update("panel:courselist");
                //context.getExternalContext().redirect("adminCourse.xhtml");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete lesson.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void assignLecturer(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(lecturerUser);
        System.out.println(moduleEntity.getCourse().getModuleCode());
        String tempModCode = moduleEntity.getCourse().getModuleCode();
        String tempTakenYear = moduleEntity.getTakenYear();
        String tempTakenSem = moduleEntity.getTakenSem();
        if (cmbl.checkExistingModule(tempModCode, tempTakenYear, tempTakenSem) == true) {
            if (cmbl.linkLecturerToModule(tempModCode, tempTakenYear, tempTakenSem, lecturerUser) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lecturer " + lecturerUser + " assigned to module.", "");
                context.addMessage(null, fmsg);
                //context.getExternalContext().redirect("adminCourse.xhtml");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error.", "Failed to assign lecturer.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exists");
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

    public List<String> getDaylist() {
        return daylist;
    }

    public void setDaylist(List<String> daylist) {
        this.daylist = daylist;
    }

    public List<String> getTimelist() {
        return timelist;
    }

    public void setTimelist(List<String> timelist) {
        this.timelist = timelist;
    }

    public ArrayList<User> getLecturerlist() {
        return lecturerlist;
    }

    public void setLecturerlist(ArrayList<User> lecturerlist) {
        this.lecturerlist = lecturerlist;
    }

    public ArrayList<Lesson> getLessonlist() {
        return lessonlist;
    }

    public void setLessonlist(ArrayList<Lesson> lessonlist) {
        this.lessonlist = lessonlist;
    }

    public ArrayList<Module> getModulelist() {
        return modulelist;
    }

    public void setModulelist(ArrayList<Module> modulelist) {
        this.modulelist = modulelist;
    }

    public ArrayList<Course> getCourselist() {
        return courselist;
    }

    public void setCourselist(ArrayList<Course> courselist) {
        this.courselist = courselist;
    }

    public List<String> getModularCreditList() {
        return modularCreditList;
    }

    public void setModularCreditList(List<String> modularCreditList) {
        this.modularCreditList = modularCreditList;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }

    public Lesson getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(Lesson selectedLesson) {
        this.selectedLesson = selectedLesson;
    }

    public List<String> getSchoollist() {
        return schoollist;
    }

    public void setSchoollist(List<String> schoollist) {
        this.schoollist = schoollist;
    }
}
