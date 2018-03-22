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
import org.primefaces.context.RequestContext;
import session.ClexSessionBeanLocal;
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
    @EJB
    ClexSessionBeanLocal csbl;
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

    private String lecturerUser1;
    private ArrayList<Lecturer> lecturerlist1; //list of lecturer assigned

    //for assigning
    private ArrayList<Lecturer> lecturerlistbyschool1; //list of lecturer free
    private List<String> schoollist1;
    private ArrayList<Module> modulelistbyschool1;
    private String schoolname1;
    private Module moduleEntity1;
    private String takenYear1;
    private String takenSem1;
    private String bigString1; //for finding module
    private Lecturer lecturerEntity1;
    private String moduleCode1;
    private String moduleName1;

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
        schoollist1 = (List) cmbl.getAllSchools();
        schoolname1 = schoollist1.get(0);
        lecturerlistbyschool1 = (ArrayList<Lecturer>) cmbl.getLecturerFromSchool(schoolname1);
        modulelistbyschool1 = (ArrayList<Module>) cmbl.getModulesFromSchool(schoolname1);
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
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Module " + moduleCode.toUpperCase() + " has been created.");
            context.addMessage(null, fmsg);
            refresh();
            modulelistbyschool = (ArrayList<Module>) cmbl.getModulesFromSchool(schoolname);
            reqcontext.execute("PF('modulesTable').clearFilters()");
            reqcontext.update("panel1:moduletable, createTab:createModuleForm, createTab:createLessonForm: panelForm:tabView1:assignform:modulelistbyschoolmenu");
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
            schoolname = "";
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
                moduleName1 = "";
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

    public void onSchoolChange2() {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, schoolname1 + " selected!", "");
        context.addMessage(null, fmsg);
        lecturerlistbyschool1 = (ArrayList<Lecturer>) cmbl.getLecturerFromSchool(schoolname1);
        modulelistbyschool1 = (ArrayList<Module>) cmbl.getModulesFromSchool(schoolname1);
    }

    public void onLecturerChange() {
        System.out.println(lecturerUser1);
        lecturerEntity1 = csbl.findLecturer(lecturerUser1);
        System.out.println("on lect change " + lecturerEntity1.getName());
    }

    public void onModuleSelect() {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        moduleName1 = "";
        moduleCode1 = bigString1.substring(7);
        String bigYear2 = bigString1.substring(0, 4);
        String bigSem2 = bigString1.substring(5, 6);
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, moduleCode1 + " selected!", "");
        context.addMessage(null, fmsg);
        lecturerlist1 = (ArrayList<Lecturer>) getAssignedLecturer();
        moduleName1 = cmbl.findCourse(moduleCode1).getModuleName();
        Course courseEntity2 = cmbl.findCourse(moduleCode1);
        moduleEntity1 = cmbl.findModule(courseEntity2, bigYear2, bigSem2);
    }

    public void assignLecturer() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(bigString1.substring(0, 4)); //big year
        System.out.println(bigString1.substring(5, 6)); //big sem
        System.out.println(bigString1.substring(7)); //big code
        String bigYear2 = bigString1.substring(0, 4);
        String bigSem2 = bigString1.substring(5, 6);
        String bigCode2 = bigString1.substring(7);
        lecturerEntity1 = csbl.findLecturer(lecturerUser1);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkExistingModule(bigCode2, bigYear2, bigSem2) == true) {
            if (cmbl.linkLecturerToModule(bigCode2, bigYear2, bigSem2, lecturerUser1) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lecturer " + lecturerEntity1.getName() + " assigned to " + bigCode2 + ".");
                context.addMessage(null, fmsg);
                refresh();
                lecturerlist1 = (ArrayList<Lecturer>) getAssignedLecturer();
                reqcontext.update("assignform:lecassignlist");

            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error.", "Failed to assign lecturer.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exists");
            context.addMessage(null, fmsg);
        }
    }

    public void unassignLecturer(String lecturerUser) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempModuleCode = moduleEntity1.getCourse().getModuleCode();
        String temptakenYear = moduleEntity1.getTakenYear();
        String temptakenSem = moduleEntity1.getTakenSem();
        System.out.println("Unassigning " + lecturerUser + " from " + tempModuleCode);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkExistingModule(tempModuleCode, temptakenYear, temptakenSem) == true) {
            if (cmbl.removeLecturerFromModule(tempModuleCode, temptakenYear, temptakenSem, lecturerUser) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lecturer " + lecturerUser + " unassigned fom " + tempModuleCode + ".");
                context.addMessage(null, fmsg);
                refresh();
                lecturerlist1 = (ArrayList<Lecturer>) getAssignedLecturer();
                reqcontext.update("assignform:lecassignlist");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error.", "Failed to unassign lecturer.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exists");
            context.addMessage(null, fmsg);
        }
    }

    public Collection<Lecturer> getAssignedLecturer() {
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        String bigYear = bigString1.substring(0, 4);
        String bigSem = bigString1.substring(5, 6);
        Course courseEntity = cmbl.findCourse(moduleCode1);
        moduleEntity1 = cmbl.findModule(courseEntity, bigYear, bigSem);
        List<Lecturer> users = new ArrayList<Lecturer>();
        List<Lecturer> lecturers = (List) moduleEntity1.getLecturers();
        for (int i = 0; i < lecturers.size(); i++) {
            users.add(lecturers.get(i));
            System.out.print("assigned: " + users.get(i).getName());
        }
        reqcontext.update("assignform:lecassignlist");

        return users;
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

    public String getLecturerUser1() {
        return lecturerUser1;
    }

    public void setLecturerUser1(String lecturerUser1) {
        this.lecturerUser1 = lecturerUser1;
    }

    public ArrayList<Lecturer> getLecturerlist1() {
        return lecturerlist1;
    }

    public void setLecturerlist1(ArrayList<Lecturer> lecturerlist1) {
        this.lecturerlist1 = lecturerlist1;
    }

    public ArrayList<Lecturer> getLecturerlistbyschool1() {
        return lecturerlistbyschool1;
    }

    public void setLecturerlistbyschool1(ArrayList<Lecturer> lecturerlistbyschool1) {
        this.lecturerlistbyschool1 = lecturerlistbyschool1;
    }

    public List<String> getSchoollist1() {
        return schoollist1;
    }

    public void setSchoollist1(List<String> schoollist1) {
        this.schoollist1 = schoollist1;
    }

    public ArrayList<Module> getModulelistbyschool1() {
        return modulelistbyschool1;
    }

    public void setModulelistbyschool1(ArrayList<Module> modulelistbyschool1) {
        this.modulelistbyschool1 = modulelistbyschool1;
    }

    public String getSchoolname1() {
        return schoolname1;
    }

    public void setSchoolname1(String schoolname1) {
        this.schoolname1 = schoolname1;
    }

    public Module getModuleEntity1() {
        return moduleEntity1;
    }

    public void setModuleEntity1(Module moduleEntity1) {
        this.moduleEntity1 = moduleEntity1;
    }

    public String getTakenYear1() {
        return takenYear1;
    }

    public void setTakenYear1(String takenYear1) {
        this.takenYear1 = takenYear1;
    }

    public String getTakenSem1() {
        return takenSem1;
    }

    public void setTakenSem1(String takenSem1) {
        this.takenSem1 = takenSem1;
    }

    public String getBigString1() {
        return bigString1;
    }

    public void setBigString1(String bigString1) {
        this.bigString1 = bigString1;
    }

    public Lecturer getLecturerEntity1() {
        return lecturerEntity1;
    }

    public void setLecturerEntity1(Lecturer lecturerEntity1) {
        this.lecturerEntity1 = lecturerEntity1;
    }

    public String getModuleCode1() {
        return moduleCode1;
    }

    public void setModuleCode1(String moduleCode1) {
        this.moduleCode1 = moduleCode1;
    }

    public String getModuleName1() {
        return moduleName1;
    }

    //Others
    public void setModuleName1(String moduleName1) {
        this.moduleName1 = moduleName1;
    }
}
