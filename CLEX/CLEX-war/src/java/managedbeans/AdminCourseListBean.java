/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Lesson;
import entity.Module;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class AdminCourseListBean implements Serializable {

    @EJB
    CourseMgmtBeanLocal cmbl;

    private List<Course> courses;
    private List<Course> filteredCourses;

    private List<Module> modules;
    private List<Module> filteredModules;

    private List<Lesson> lessons;
    private List<Lesson> filteredLessons;

    private Course courseEntity;

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
    private Module moduleEntity;
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
    private List<String> yearlist;

    private ArrayList<User> lecturerlist;
    private ArrayList<Module> modulelist;
    private ArrayList<Course> courselist;

    private ArrayList<Lesson> lessonlist; //list of all lesson entity
    private List<String> daylist;

    //for prepopulating
    private String schoolname;
    private String semesterText;
    private String moduleNameText;
    //For Module prepopulating
    private ArrayList<Course> courselistbyschool;

    private String moduleInfoText;
    private String discontYearText;
    private String discontSemText;
    private String workloadText;
    private String mcText;
    private String discontText;

    //For Lesson prepopulating
    private ArrayList<Module> modulelistbyschool;
    private String bigString;

    private String moduleofferedyearforlesson;
    private String moduleofferedsemforlesson;

    public AdminCourseListBean() {
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        courses = cmbl.getAllCourses();
        modules = cmbl.getAllModules();
        lessons = cmbl.getAllLessons();
        schoollist = (List) cmbl.getAllSchools();
        courselist = (ArrayList<Course>) cmbl.getAllCourses();
        modulelist = (ArrayList<Module>) cmbl.getAllModules();
        timelist = (List) cmbl.getAllTimings();
        daylist = (List) cmbl.getAllDays();
        lecturerlist = (ArrayList<User>) cmbl.getLecturerName();
        lessonlist = (ArrayList<Lesson>) cmbl.getAllLessons();
        modularCreditList = (List) cmbl.getAllModularCredits();
        yearlist = cmbl.getYearList();
        System.out.println("refresh() end");
    }

    public void onSchoolChange() {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", schoolname + " selected.");
        context.addMessage(null, fmsg);
        System.out.println("onSchool retrieving from " + schoolname);
        takenSem = "";
        moduleInfoText = "";
        moduleNameText = "";
        discontYearText = "";
        discontSemText = "";
        workloadText = "";
        mcText = "";
        moduleofferedyearforlesson = "";
        moduleofferedsemforlesson = "";
        discontText = "";
        semesterText = "";
        courselistbyschool = null;
        courselistbyschool = (ArrayList<Course>) cmbl.getCoursesFromSchool(schoolname);
        modulelistbyschool = null;
        modulelistbyschool = (ArrayList<Module>) cmbl.getModulesFromSchool(schoolname);
    }

    public void onCourseChange() { //for creating module
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        takenSem = cmbl.findCourse(moduleCode).getOfferedSem();
        moduleInfoText = cmbl.findCourse(moduleCode).getModuleInfo();
        moduleNameText = cmbl.findCourse(moduleCode).getModuleName();
        discontYearText = cmbl.findCourse(moduleCode).getDiscountinuedYear();
        discontSemText = cmbl.findCourse(moduleCode).getDiscountinuedSem();
        workloadText = cmbl.findCourse(moduleCode).getWorkload();
        mcText = cmbl.findCourse(moduleCode).getModularCredits();
        String schoolname1 = cmbl.findCourse(moduleCode).getSchool();
        boolean checkDiscontinued = cmbl.findCourse(moduleCode).isDiscontinuedBool();
        if (checkDiscontinued == false) {
            discontText = moduleCode + " is still offered.";
        } else if (checkDiscontinued == true) {
            discontText = moduleCode + " has been discontinued.";
        } else {
            discontText = schoolname1 + " has yet to decide if " + moduleCode + " would be discontinued.";
        }

        if (takenSem.equals("ALL")) {
            takenSem = "";
            semesterText = moduleCode + " is offered in both semesters.";
        } else if (takenSem.equals("1")) {
            takenSem = "1";
            semesterText = moduleCode + " is only offered in semester 1.";
        } else {
            takenSem = "2";
            semesterText = moduleCode + " is only offered in semester 2.";
        }

        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", moduleCode + " selected.");
        context.addMessage(null, fmsg);
    }

    public void onModuleChange() { //for creating lesson
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(bigString.substring(0, 4)); //big year
        System.out.println(bigString.substring(5, 6)); //big sem
        System.out.println(bigString.substring(7)); //big code
        String bigYear = bigString.substring(0, 4);
        String bigSem = bigString.substring(5, 6);
        String bigCode = bigString.substring(7);
        courseEntity = cmbl.findCourse(bigCode);
        moduleEntity = cmbl.findModule(courseEntity, bigYear, bigSem);
        System.out.println("For finding - Year: " + bigYear + " Semester: " + bigSem);
        moduleofferedyearforlesson = moduleEntity.getTakenYear();
        moduleofferedsemforlesson = moduleEntity.getTakenSem();
        System.out.println("For Rendering - Year: " + moduleofferedyearforlesson + " Semester: " + moduleofferedsemforlesson + " " + moduleEntity.getCourse().getModuleCode() + " selected");
        moduleNameText = courseEntity.getModuleName();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", bigCode + " selected.");
        context.addMessage(null, fmsg);
    }

    public void enterCourse() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("enterCourse MC: " + moduleCode);
        System.out.println("enterCourse MN: " + moduleName);
        System.out.println("enterCourse MI: " + moduleInfo);
        System.out.println("enterCourse OS: " + offeredSem);
        System.out.println("enterCourse S: " + school);
        System.out.println("enterCourse MCs: " + modularCredits);
        System.out.println("enterCourse WL: " + workload);

        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkNewCourse(moduleCode) == true) {
            cmbl.createCourse(moduleCode, moduleName, moduleInfo, false, "", "", offeredSem, school, modularCredits, workload);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Course " + moduleCode.toUpperCase() + " has been created.");
            context.addMessage(null, fmsg);
            refresh();
            reqcontext.execute("PF('coursesTable').clearFilters()");
            reqcontext.update("panel:coursetable, createTab:createModuleForm");
            schoolname = "";
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Course '" + moduleCode + "' already exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void enterModule() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();

        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkNewModule(moduleCode, takenYear, takenSem) == true) {
            cmbl.createModule(takenYear, takenSem, prerequisite, preclusions, moduleCode);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Module" + moduleCode.toUpperCase() + " has been created.");
            context.addMessage(null, fmsg);
            refresh();
            reqcontext.execute("PF('modulesTable').clearFilters()");
            reqcontext.update("panel1:moduletable, createTab:createModuleForm, createTab:createLessonForm");
            schoolname = "";
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module already exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void enterLesson() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkNewLesson(moduleCode, takenYear, takenSem, day, timeFrom, timeEnd) == true) {
            cmbl.createLesson(day, timeFrom, timeEnd, type, venue, moduleCode, takenYear, takenSem);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lesson " + moduleCode.toUpperCase() + " has been created.");
            context.addMessage(null, fmsg);
            refresh();
            reqcontext.execute("PF('lessonsTable').clearFilters()");
            reqcontext.update("panel2:lessontable, createTab:createLessonForm");
            schoolname ="";
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
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", tempModCode.toUpperCase() + " has been updated.");
            refresh();
            reqcontext.update("panel:coursetable");
            context.addMessage(null, fmsg);
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Course " + tempModCode + " does not exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void editModule(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String tempModCode = moduleEntity.getCourse().getModuleCode();
        String tempTakenYear = moduleEntity.getTakenYear();
        String tempTakenSem = moduleEntity.getTakenSem();
        String tempPrerequisite = moduleEntity.getPrerequisite();
        String tempPreclusions = moduleEntity.getPreclusions();
        if (cmbl.checkExistingModule(tempModCode, tempTakenYear, tempTakenSem) == true) {
            cmbl.editModule(tempTakenYear, tempTakenSem, tempPrerequisite, tempPreclusions, tempModCode);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Module " + tempModCode.toUpperCase() + " has been edited.");
            context.addMessage(null, fmsg);
            refresh();
            reqcontext.update("panel1:moduletable");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module " + tempModCode + " does not exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void editLesson(Lesson lessonEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
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
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lesson " + tempModCode.toUpperCase() + " has been edited.");
            context.addMessage(null, fmsg);
            refresh();
            reqcontext.update("panel2:lessontable");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson " + tempModCode + " does not exists.");
            context.addMessage(null, fmsg);
        }
    }

    public void removeCourse(Course courseEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String modCode = courseEntity.getModuleCode();
        if (cmbl.checkExistingCourse(modCode) == true) {
            if (cmbl.deleteCourse(modCode) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", modCode.toUpperCase() + " has been deleted.");
                System.out.println("yes");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.execute("PF('coursesTable').clearFilters()");
                reqcontext.update("panel:coursetable");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete course.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Course " + modCode + " not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void removeModule(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String modCode = moduleEntity.getCourse().getModuleCode();
        String tempTakenYear = moduleEntity.getTakenYear();
        String tempTakenSem = moduleEntity.getTakenSem();
        System.out.println(modCode);
        System.out.println(tempTakenYear);
        System.out.println(tempTakenSem);
        if (cmbl.checkExistingModule(modCode, tempTakenYear, tempTakenSem) == true) {
            if (cmbl.deleteModule(modCode, tempTakenYear, tempTakenSem) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", modCode.toUpperCase() + " has been deleted.");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.execute("PF('modulesTable').clearFilters()");
                reqcontext.update("panel1:moduletable");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete module.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module not found.");
            context.addMessage(null, fmsg);
        }
    }

    public void removeLesson(Lesson lessonEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String tempDay = lessonEntity.getDay();
        String tempTimeStart = lessonEntity.getTimeFrom();
        String tempTimeEnd = lessonEntity.getTimeEnd();
        String tempModCode = lessonEntity.getModule().getCourse().getModuleCode();
        String tempTakenYear = lessonEntity.getModule().getTakenYear();
        String tempTakenSem = lessonEntity.getModule().getTakenSem();

        if (cmbl.checkExistingLesson(tempModCode, tempTakenYear, tempTakenSem, tempDay, tempTimeStart, tempTimeEnd) == true) {
            if (cmbl.deleteLesson(tempDay, tempTimeStart, tempTimeEnd, tempModCode, tempTakenYear, tempTakenSem) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lesson has been deleted.");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.execute("PF('lessonsTable').clearFilters()");
                reqcontext.update("panel2:lessontable");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete lesson.", "");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson not found.");
            context.addMessage(null, fmsg);
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getFilteredCourses() {
        return filteredCourses;
    }

    public void setFilteredCourses(List<Course> filteredCourses) {
        this.filteredCourses = filteredCourses;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Module> getFilteredModules() {
        return filteredModules;
    }

    public void setFilteredModules(List<Module> filteredModules) {
        this.filteredModules = filteredModules;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Lesson> getFilteredLessons() {
        return filteredLessons;
    }

    public void setFilteredLessons(List<Lesson> filteredLessons) {
        this.filteredLessons = filteredLessons;
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

    public String getModularCredits() {
        return modularCredits;
    }

    public void setModularCredits(String modularCredits) {
        this.modularCredits = modularCredits;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
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

    public List<String> getModularCreditList() {
        return modularCreditList;
    }

    public void setModularCreditList(List<String> modularCreditList) {
        this.modularCreditList = modularCreditList;
    }

    public List<String> getTimelist() {
        return timelist;
    }

    public void setTimelist(List<String> timelist) {
        this.timelist = timelist;
    }

    public List<String> getSchoollist() {
        return schoollist;
    }

    public void setSchoollist(List<String> schoollist) {
        this.schoollist = schoollist;
    }

    public ArrayList<User> getLecturerlist() {
        return lecturerlist;
    }

    public void setLecturerlist(ArrayList<User> lecturerlist) {
        this.lecturerlist = lecturerlist;
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

    public ArrayList<Lesson> getLessonlist() {
        return lessonlist;
    }

    public void setLessonlist(ArrayList<Lesson> lessonlist) {
        this.lessonlist = lessonlist;
    }

    public List<String> getDaylist() {
        return daylist;
    }

    public void setDaylist(List<String> daylist) {
        this.daylist = daylist;
    }

    public String getSemesterText() {
        return semesterText;
    }

    public void setSemesterText(String semesterText) {
        this.semesterText = semesterText;
    }

    public String getModuleInfoText() {
        return moduleInfoText;
    }

    public void setModuleInfoText(String moduleInfoText) {
        this.moduleInfoText = moduleInfoText;
    }

    public List<String> getYearlist() {
        return yearlist;
    }

    public void setYearlist(List<String> yearlist) {
        this.yearlist = yearlist;
    }

    public String getModuleNameText() {
        return moduleNameText;
    }

    public void setModuleNameText(String moduleNameText) {
        this.moduleNameText = moduleNameText;
    }

    public String getDiscontYearText() {
        return discontYearText;
    }

    public void setDiscontYearText(String discontYearText) {
        this.discontYearText = discontYearText;
    }

    public String getDiscontSemText() {
        return discontSemText;
    }

    public void setDiscontSemText(String discontSemText) {
        this.discontSemText = discontSemText;
    }

    public String getWorkloadText() {
        return workloadText;
    }

    public void setWorkloadText(String workloadText) {
        this.workloadText = workloadText;
    }

    public String getMcText() {
        return mcText;
    }

    public void setMcText(String mcText) {
        this.mcText = mcText;
    }

    public String getDiscontText() {
        return discontText;
    }

    public void setDiscontText(String discontText) {
        this.discontText = discontText;
    }

    public ArrayList<Course> getCourselistbyschool() {
        return courselistbyschool;
    }

    public void setCourselistbyschool(ArrayList<Course> courselistbyschool) {
        this.courselistbyschool = courselistbyschool;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public ArrayList<Module> getModulelistbyschool() {
        return modulelistbyschool;
    }

    public void setModulelistbyschool(ArrayList<Module> modulelistbyschool) {
        this.modulelistbyschool = modulelistbyschool;
    }

    public String getModuleofferedyearforlesson() {
        return moduleofferedyearforlesson;
    }

    public void setModuleofferedyearforlesson(String moduleofferedyearforlesson) {
        this.moduleofferedyearforlesson = moduleofferedyearforlesson;
    }

    public String getModuleofferedsemforlesson() {
        return moduleofferedsemforlesson;
    }

    public void setModuleofferedsemforlesson(String moduleofferedsemforlesson) {
        this.moduleofferedsemforlesson = moduleofferedsemforlesson;
    }

    public Course getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(Course courseEntity) {
        this.courseEntity = courseEntity;
    }

    public Module getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(Module moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public String getBigString() {
        return bigString;
    }

    public void setBigString(String bigString) {
        this.bigString = bigString;
    }
}
