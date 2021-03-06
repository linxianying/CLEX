/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Course;
import entity.Grade;
import entity.Module;
import entity.Student;
import entity.StudyPlan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import entity.Thread;
import entity.User;
import javax.ejb.EJB;
import session.ClexSessionBeanLocal;
import session.StudyPlanSessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import session.CommunitySessionBeanLocal;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "studyPlanBean")
@SessionScoped
public class StudyPlanBean {
    
    @EJB
    private StudyPlanSessionBeanLocal cpsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private CourseMgmtBeanLocal cmbl;
    @EJB
    private CommunitySessionBeanLocal cmsbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private String moduleCode;
    //private String matricYear;
    private String pickYear;
    private String pickSem;
    private ArrayList<Course> takenCourses;
    private ArrayList<ArrayList<Course>> takenCoursesInOrder;
    private ArrayList<Grade> grades;
    private ArrayList<ArrayList<Grade>> gradesInOrder;
    private ArrayList<Module> takingModules;
    private ArrayList<StudyPlan> studyPlans;
    private ArrayList<ArrayList<StudyPlan>> studyPlansInOrer;
    private HashMap<String, String> expectedCourseGrade;
    private double cap;
    private double expectedCap;
    private String newModuleGrade;
    private String newCurrentModuleGrade;
    private int allCredits;
    private Student student;
    //for add study plan
    private String addModuleCode;
    private int addPickYear;
    private String addPickSem;
    private String addErrorMsg;
    private boolean addButton;
    
    private String updateModuleCode;
    private String updatePickYear;
    private String updatePickSem;
    
    private List<Course> courses;
    
    Course courseFront; //for rendering the info after the student select the module
    
    //if the school is of 5 based grading system, =5, otherwise =4;
    private int grading;
    
    private int currentYear;
    private int currentSem;
    private int matricYear;
    
    private String addGradeModuleCode;
    private int addGradePickYear;
    private int addGradePickSem;
    private String addGradeModuleGrade;
    private List<Thread> moduleReviews;
    
    private String addCurrentModuleCode;
    private String showModuleInfo;
    private String showWorkload;
    private String showPrerequisite;
    private String showPreclusions;
    private Module showModule;
    
    //for reviews:
    private Thread selectedReview;
    private boolean createThreadCheck;
    private User userEntity;
    private String content;
    private Thread threadEntity;
    private String threadTitle;
    private String moduleYear;
    private String moduleSem;
    private Course courseEntity;
    
    public StudyPlanBean() {
    }
    
    @PostConstruct
    public void init() {
        createThreadCheck = false;
        refresh();
        setYearSem();
        courses = cpsbl.getAllCourses();
        grading = this.checkGrading();
        this.refresh();
    }
    
    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        username = (String) session.getAttribute("username");
        student = csbl.findStudent(username);
        cap = cpsbl.findStudent(username).getCap();
        expectedCap = cap;
        gradesInOrder = cpsbl.getAllGradesInOrder(student);
        takingModules = cpsbl.getCurrentModules(student);
        if (student.getStudyPlan() != null) {
            studyPlansInOrer = cpsbl.getStudyPlanInOrder(student);
            expectedCourseGrade = cpsbl.getExpectedCourseGrade(username);
            System.out.println("StudyPlanBean: Expected Cap reset to " + expectedCap);
        }
        this.setNewModuleGrade("select");
        this.setNewCurrentModuleGrade("select");
        allCredits = cpsbl.getNumOfCredits(cpsbl.getAllGrades(student));
        courseFront = null;
        addModuleCode = null;
        addPickSem = null;
        addErrorMsg = null;
        addButton = false;
        updatePickYear = null;
        updatePickSem = null;
        addGradeModuleCode = null;
        addGradeModuleGrade = null;
        System.out.println("finish to render StudyPlanBean");
    }
    
    public void createReview(Module moduleEntity) {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        if (userEntity != null) {
            if (content.equals("")) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contents needed.", "Please fill up the content field.");
            } else {
                threadTitle = createReviewTitle(moduleEntity);
                threadEntity = cmsbl.getExistingReview(threadTitle, userEntity.getSchool());
                if (threadEntity != null) {
                    if (cmsbl.createReply(threadEntity.getId(), username, content)) {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Review created.");
                    } else {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create review.", "Please ensure you are logged in.");
                    }
                } else {
                    createNewReview(moduleEntity, threadTitle, content, fmsg);
                }
            }
            createThreadCheck = false;
            content = "";
            threadTitle = "";
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to review module.", "Please ensure you are logged in.");
        }
    }
    
    public void createNewReview(Module moduleEntity, String threadTitle, String content, FacesMessage fmsg) {
        if (cmsbl.createThread(username, content, threadTitle, "Course Review", moduleEntity.getCourse().getSchool())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Review created.");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create review.", "Please ensure you are logged in.");
        }
    }
    
    public String createReviewTitle(Module moduleEntity) {
        Course tempCourse = moduleEntity.getCourse();
        return tempCourse.getModuleCode() + " " + tempCourse.getModuleName() + " - Year " + moduleEntity.getTakenYear() + " Sem " + moduleEntity.getTakenSem();
    }
    
    public void onReviewSelect(SelectEvent event) {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        System.out.println("Selected Review ID: " + selectedReview.getId());
        try {
            session.setAttribute("id", selectedReview.getId());
            context.getExternalContext().redirect("viewThread.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getModuleInfo(Module moduleEntity) {
        showModuleInfo = "";
        showWorkload = "";
        showPrerequisite = "";
        showPreclusions = "";
        showModuleInfo = moduleEntity.getCourse().getModuleInfo();
        showWorkload = moduleEntity.getCourse().getWorkload();
        showPrerequisite = moduleEntity.getPrerequisite();
        showPreclusions = moduleEntity.getPreclusions();
        showModule = moduleEntity;
        System.out.println(showWorkload);
        System.out.println(showPrerequisite);
        
    }
    
    public void getModuleReviewsList(String moduleCode) {
        courseEntity = cpsbl.findCourse(moduleCode);
        System.out.println("!!!!!!!!!!! " + moduleCode + ", is " + courseEntity);
        if(moduleReviews != null) {
            moduleReviews.clear();
        }
        showModuleInfo = "";
        showWorkload = "";
        showModuleInfo = courseEntity.getModuleInfo();
        showWorkload = courseEntity.getWorkload();
        String moduleCode1 = courseEntity.getModuleCode();
        System.out.println("Searching reviews for " + moduleCode1 + "...");
        moduleReviews = cmsbl.searchThreadByTitle(moduleCode1, courseEntity.getSchool());
        moduleReviews = cmsbl.filterNonTagCourseReview(moduleReviews);
    }
    
    public int checkGrading() {
        if (student.getSchool().equals("SMU") || student.getSchool().equals("SP") || student.getSchool().equals("RP") || student.getSchool().equals("TP") || student.getSchool().equals("NP") || student.getSchool().equals("NYP")) {
            return 4;
        } else {
            return 5;
        }
    }
    
    public void setYearSem() {
        Calendar now = Calendar.getInstance();
        currentYear = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
        } else {
            currentSem = 1;
        }
        matricYear = Integer.parseInt(student.getMatricYear());
    }
    
    public void checkStudyPlan() {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        //System.out.println(addPickYear + " " + addPickSem);
        if (addModuleCode.endsWith("select")) {
            this.addButton = false;
        } //this course already in studyPlan
        else if (cpsbl.checkInSP(cpsbl.getAllStudyPlans(student), addModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The module " + addModuleCode + " is already in your study plan!", "Please change to another module.");
            context.addMessage(null, fmsg);
            courseFront = null;
            this.addButton = false;
        } //this course is currently taken
        else if (cpsbl.checkInCM(this.takingModules, addModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are currently taking " + addModuleCode.toUpperCase() + "!", "Please change to another module.");
            context.addMessage(null, fmsg);
            courseFront = null;
            this.addButton = false;
        } //this course already in takenCourses list
        else if (cpsbl.checkInGrade(this.grades, addModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You have already taken " + addModuleCode.toUpperCase() + "!", "Please change to another module.");
            context.addMessage(null, fmsg);
            courseFront = null;
            this.addButton = false;
        } else {
            fmsg = null;
            courseFront = cmbl.findCourse(addModuleCode);
            this.addButton = true;
            addPickSem = courseFront.getOfferedSem();
        }
    }
    
    //to check whether the SP is added to previous sem, if so, error msg
    //the min value of addPickYear is current year, so impossible to be less then current, so no need to check
    public boolean checkAddSPYearSem() {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        if (addPickYear == (currentYear - matricYear + 1) && Integer.parseInt(addPickSem) <= currentSem) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You've selected a previous semester!", "Please change to a semester before current semester.");
            context.addMessage(null, fmsg);
            return false;
        } else {
            return true;
        }
    }
    
    public void addStudyPlan() {
        if (this.checkAddSPYearSem()) {
            System.out.println("studyPlanBean: addStudyPlan: addModuleCode" + addModuleCode);
            System.out.println("addpickyear" + addPickYear);
            System.out.println("addpickSem" + addPickSem);
            addPickYear += this.matricYear - 1;
            context = FacesContext.getCurrentInstance();
            System.out.println("username" + username);
            cpsbl.createStudyPlan(Integer.toString(addPickYear), addPickSem, addModuleCode.toUpperCase(), csbl.findStudent(username));
            addModuleCode = null;
            //        addPickYear = null;
            addPickSem = null;
            this.addButton = false;
            refresh();
        }
    }
    
    public void checkAddModule() {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        if (addCurrentModuleCode.endsWith("select")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No module selected", "Please choose a module.");
            context.addMessage(null, fmsg);
        } //this course already in studyPlan
        else if (cpsbl.checkInSP(cpsbl.getAllStudyPlans(student), addCurrentModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The module " + addCurrentModuleCode + " is already in your study plan!", "Please change to another module.");
            context.addMessage(null, fmsg);
        } //this course is currently taken
        else if (cpsbl.checkInCM(this.takingModules, addCurrentModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are currently taking" + addCurrentModuleCode.toUpperCase() + "!", "Please change to another module.");
            context.addMessage(null, fmsg);
        } //this course already in takenCourses list
        else if (cpsbl.checkInGrade(cpsbl.getAllGrades(student), addCurrentModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You have already taken " + addCurrentModuleCode.toUpperCase() + "!", "Please change to another module.");
            context.addMessage(null, fmsg);
        } else {
            fmsg = null;
        }
    }
    
    public void addCurrentModule() {
        cpsbl.addTakingModule(Integer.toString(currentYear), Integer.toString(currentSem), addCurrentModuleCode, student);
        refresh();
    }
    
    public void checkAddGrade() {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        if (addGradeModuleCode.endsWith("select")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No module selected", "Please choose a module.");
            context.addMessage(null, fmsg);
        } //this course already in studyPlan
        else if (cpsbl.checkInSP(cpsbl.getAllStudyPlans(student), addGradeModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The module " + addGradeModuleCode + " is already in your study plan!", "Please change to another module.");
            context.addMessage(null, fmsg);
        } //this course is currently taken
        else if (cpsbl.checkInCM(this.takingModules, addGradeModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are currently taking " + addGradeModuleCode.toUpperCase() + "!", "Please change to another module.");
            context.addMessage(null, fmsg);
        } //this course already in takenCourses list
        else if (cpsbl.checkInGrade(cpsbl.getAllGrades(student), addGradeModuleCode.toUpperCase())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You have already taken " + addGradeModuleCode.toUpperCase() + "!", "Please change to another module.");
            context.addMessage(null, fmsg);
        } else {
            fmsg = null;
        }
    }
    
    //to check whether the Grade is added to current sem, if so, error msg
    public boolean checkAddGradeYearSem() {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        if (addGradePickYear == (currentYear - matricYear + 1) && addGradePickSem == currentSem) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You've selected the current semester", "Please change to a semester before the current semester.");
            context.addMessage(null, fmsg);
            return false;
        } else {
            return true;
        }
    }
    
    public void addGrade() {
        if (checkAddGradeYearSem()) {
            cpsbl.addGrade(Integer.toString(addGradePickYear + matricYear - 1), Integer.toString(addGradePickSem), addGradeModuleCode, student, addGradeModuleGrade);
        }
        refresh();
    }
    
    public void deleteStudyPlan(String moduleCode) {
        cpsbl.removeStudyPlan(username, moduleCode);
        refresh();
//        studyPlansInOrer = cpsbl.getStudyPlanInOrder(student);
    }
    
    public void deleteModule(Module module) {
        cpsbl.removeModule(student, module);
        refresh();
    }
    
    public void deleteGrade(Grade grade) {
        cpsbl.removeGrade(student, grade);
        refresh();
    }
    
    public void updateExpectedCap(int newModuleCredit, String moduleCode) {
        String oldGrade = checkExpectedCourseGrade(moduleCode);
        //the first time set the expected grade
        if (oldGrade.equals("none")) {
            System.out.println(moduleCode + ": first time;");
            //it is not S or U, since setting as S or U for first time will be treated as the module has not been setted an expected grade before
            if ((!this.newModuleGrade.equals("S")) && (!this.newModuleGrade.equals("U")) && (!this.newModuleGrade.equals("select"))) {
                //update the expectedCap for first time for this module
                this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newModuleGrade);
                //update all the credits taken
                this.allCredits += newModuleCredit;
                //set the course's expected grade in hashmap
                expectedCourseGrade.put(moduleCode, newModuleGrade);
            }
        } //not the first time set the expected grade
        else {
            //if the last setted garde is S/U or select
            if (oldGrade.equals("S") || oldGrade.equals("U") || oldGrade.equals("select")) {
                // if this time it is S/U or select again, do nothing
                // if not, type 2
                if ((!this.newModuleGrade.equals("S")) && (!this.newModuleGrade.equals("U")) && (!this.newModuleGrade.equals("select"))) {
                    System.out.println(moduleCode + ": type 2;");
                    this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newModuleGrade);
                    //update all the credits taken
                    this.allCredits += newModuleCredit;
                    //set the course's expected grade in hashmap
                    expectedCourseGrade.put(moduleCode, newModuleGrade);
                } else {
                    System.out.println(moduleCode + ": type 3;");
                }
            } //if the last setted garde is not S/U or select
            else {
                //if it is changed to S/U or select, type 1
                if ((this.newModuleGrade.equals("S")) || (this.newModuleGrade.equals("U")) || (this.newModuleGrade.equals("select"))) {
                    this.expectedCap = cpsbl.updateExpectedCapOne(this.allCredits, this.expectedCap, newModuleCredit, oldGrade);
                    this.allCredits -= newModuleCredit;
                    expectedCourseGrade.put(moduleCode, newModuleGrade);
                } // if it is not changed to S/U or select, type 4
                else {
                    System.out.println(moduleCode + ": type 4;");
                    this.expectedCap = cpsbl.updateExpectedCapFour(this.allCredits, this.expectedCap, newModuleCredit, newModuleGrade, oldGrade);
                    expectedCourseGrade.put(moduleCode, newModuleGrade);
                }
            }
        }
        System.out.println("Expected cap change to " + expectedCap);
    }
    
    public void updateCurrentExpectedCap(int newModuleCredit, String moduleCode) {
        String oldGrade = checkExpectedCourseGrade(moduleCode);
        //System.out.println("newCurrentModuleGrade" + newCurrentModuleGrade);
        //the first time set the expected grade
        if (oldGrade.equals("none")) {
            //it is not S/U, since setting as S or U for first time will be treated as the module has not been setted an expected grade before
            if ((!this.newCurrentModuleGrade.equals("S")) && (!this.newCurrentModuleGrade.equals("U")) && (!this.newCurrentModuleGrade.equals("select"))) {
                //update the expectedCap for first time for this module
                this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newCurrentModuleGrade);
                //update all the credits taken
                this.allCredits += newModuleCredit;
                //set the course's expected grade in hashmap
                expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
            }
        } //not the first time set the expected grade
        else {
            //if the last setted garde is S/U or select
            if (oldGrade.equals("S") || oldGrade.equals("U") || oldGrade.equals("select")) {
                // if this time it is S/U  or select again, do nothing
                // if not, type 2
                if ((!this.newCurrentModuleGrade.equals("S")) && (!this.newCurrentModuleGrade.equals("U")) && (!this.newCurrentModuleGrade.equals("select"))) {
                    System.out.println(moduleCode + ": type 2;");
                    this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newCurrentModuleGrade);
                    //update all the credits taken
                    this.allCredits += newModuleCredit;
                    //set the course's expected grade in hashmap
                    expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
                } else {
                    System.out.println(moduleCode + ": type 3;");
                }
            } //if the last setted garde is not S/U
            else {
                //if it is changed to S/U or select, type 1
                if ((this.newCurrentModuleGrade.equals("S")) || (this.newCurrentModuleGrade.equals("U")) || (this.newCurrentModuleGrade.equals("select"))) {
                    System.out.println(moduleCode + ": type 1;");
                    this.expectedCap = cpsbl.updateExpectedCapOne(this.allCredits, this.expectedCap, newModuleCredit, oldGrade);
                    this.allCredits -= newModuleCredit;
                    expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
                } // if it is not changed to S/U or select, type 4
                else {
                    System.out.println(moduleCode + ": type 4;");
                    this.expectedCap = cpsbl.updateExpectedCapFour(this.allCredits, this.expectedCap, newModuleCredit, newCurrentModuleGrade, oldGrade);
                    expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
                }
            }
        }
        System.out.println("Expected cap change to " + expectedCap);
    }
    
    //check this expected garde is updated for the first time (an expected garde has been set before)or not
    //if this is the first time, return none
    // if not the first time, return the expected grade set for this course last time
    public String checkExpectedCourseGrade(String moduleCode) {
        if (expectedCourseGrade.containsKey(moduleCode)) {
            return expectedCourseGrade.get(moduleCode);
        } else {
            System.out.println("sp bean: checkExpectedCourseGrade: Error，"
                    + moduleCode + " is not found in expectedCourseGrade");
        }
        return "none";
    }
    
    public void updateStudyPlan(String updateModuleCode) {
        try {
            if (checkUpdateStudyPlan(updateModuleCode)) {
                cpsbl.updateStudyPlan(username, updateModuleCode, updatePickYear, updatePickSem);
                studyPlansInOrer = cpsbl.getStudyPlanInOrder(student);
                context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("studyPlan.xhtml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean checkUpdateStudyPlan(String updateModuleCode) {
        System.out.println("find " + username + "'s study plan for " + updateModuleCode);
        StudyPlan updatedStudyPlan = cpsbl.findStudyPlan(username, updateModuleCode);
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        if (updatePickYear.equals(updatedStudyPlan.getPickYear()) && updatePickSem.equals(updatedStudyPlan.getPickSem())) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The study plan for " + updateModuleCode + " is already in Year " + updatePickYear + " Sem " + updatePickSem, "Please change a semester");
            context.addMessage(null, fmsg);
            return false;
        } else {
            fmsg = null;
            return true;
        }
    }
    
    //convert pickyear (eg. 2018) to Year 4
    public String convertPickYear(String pickYear) {
        int year = Integer.parseInt(pickYear) + 1 - this.matricYear;
        return Integer.toString(year);
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
    
    public HashMap<String, String> getExpectedCourseGrade() {
        return expectedCourseGrade;
    }
    
    public void setExpectedCourseGrade(HashMap<String, String> expectedCourseGrade) {
        this.expectedCourseGrade = expectedCourseGrade;
    }
    
    public String getAddModuleCode() {
        return addModuleCode;
    }
    
    public void setAddModuleCode(String addModuleCode) {
        this.addModuleCode = addModuleCode.toUpperCase();
    }
    
    public String getShowModuleInfo() {
        return showModuleInfo;
    }
    
    public void setShowModuleInfo(String showModuleInfo) {
        this.showModuleInfo = showModuleInfo;
    }
    
    public String getShowWorkload() {
        return showWorkload;
    }
    
    public void setShowWorkload(String showWorkload) {
        this.showWorkload = showWorkload;
    }
    
    public ArrayList<Grade> getGrades() {
        return grades;
    }
    
    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }
    
    public double getCap() {
        return cap;
    }
    
    public void setCap(double cap) {
        this.cap = cap;
    }
    
    public String getNewCurrentModuleGrade() {
        return newCurrentModuleGrade;
    }
    
    public void setNewCurrentModuleGrade(String newCurrentModuleGrade) {
        this.newCurrentModuleGrade = newCurrentModuleGrade;
    }
    
    public ArrayList<ArrayList<Grade>> getGradesInOrder() {
        return gradesInOrder;
    }
    
    public void setGradesInOrder(ArrayList<ArrayList<Grade>> gradesInOrder) {
        this.gradesInOrder = gradesInOrder;
    }
    
    public String getNewModuleGrade() {
        return newModuleGrade;
    }
    
    public void setNewModuleGrade(String newModuleGrade) {
        this.newModuleGrade = newModuleGrade;
    }
    
    public String getAddPickSem() {
        return addPickSem;
    }
    
    public void setAddPickSem(String addPickSem) {
        this.addPickSem = addPickSem;
    }
    
    public int getAllCredits() {
        return allCredits;
    }
    
    public void setAllCredits(int allCredits) {
        this.allCredits = allCredits;
    }
    
    private String genSalt() {
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();
        return salt;
    }
    
    public void save() {
        addMessage("Success", "Data saved");
    }
    
    public void update() {
        addMessage("Success", "Data updated");
    }
    
    public void delete() {
        addMessage("Success", "Data deleted");
    }
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public StudyPlanSessionBeanLocal getCpsbl() {
        return cpsbl;
    }
    
    public void setCpsbl(StudyPlanSessionBeanLocal cpsbl) {
        this.cpsbl = cpsbl;
    }
    
    public ArrayList<Course> getTakenCourses() {
        return takenCourses;
    }
    
    public void setTakenCourses(ArrayList<Course> takenCourses) {
        this.takenCourses = takenCourses;
    }
    
    public ArrayList<StudyPlan> getStudyPlans() {
        return studyPlans;
    }
    
    public void setStudyPlans(ArrayList<StudyPlan> studyPlans) {
        this.studyPlans = studyPlans;
    }
    
    public ArrayList<ArrayList<StudyPlan>> getStudyPlansInOrer() {
        return studyPlansInOrer;
    }
    
    public void setStudyPlansInOrer(ArrayList<ArrayList<StudyPlan>> studyPlansInOrer) {
        this.studyPlansInOrer = studyPlansInOrer;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }
    
    public ArrayList<Module> getTakingModules() {
        return takingModules;
    }
    
    public void setTakingModules(ArrayList<Module> takingModules) {
        this.takingModules = takingModules;
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
    
    public String getModuleCode() {
        return moduleCode;
    }
    
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    public String getPickYear() {
        return pickYear;
    }
    
    public void setPickYear(String pickYear) {
        this.pickYear = pickYear;
    }
    
    public String getPickSem() {
        return pickSem;
    }
    
    public void setPickSem(String pickSem) {
        this.pickSem = pickSem;
    }
    
    public double getExpectedCap() {
        return expectedCap;
    }
    
    public void setExpectedCap(double expectedCap) {
        this.expectedCap = expectedCap;
    }
    
    public ArrayList<ArrayList<Course>> getTakenCoursesInOrder() {
        return takenCoursesInOrder;
    }
    
    public void setTakenCoursesInOrder(ArrayList<ArrayList<Course>> takenCoursesInOrder) {
        this.takenCoursesInOrder = takenCoursesInOrder;
    }
    
    public String getAddErrorMsg() {
        return addErrorMsg;
    }
    
    public void setAddErrorMsg(String addErrorMsg) {
        this.addErrorMsg = addErrorMsg;
    }
    
    public boolean isAddButton() {
        return addButton;
    }
    
    public void setAddButton(boolean addButton) {
        this.addButton = addButton;
    }
    
    public List<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    
    public String getUpdateModuleCode() {
        return updateModuleCode;
    }
    
    public void setUpdateModuleCode(String updateModuleCode) {
        this.updateModuleCode = updateModuleCode;
    }
    
    public String getUpdatePickYear() {
        return updatePickYear;
    }
    
    public void setUpdatePickYear(String updatePickYear) {
        this.updatePickYear = updatePickYear;
    }
    
    public String getUpdatePickSem() {
        return updatePickSem;
    }
    
    public void setUpdatePickSem(String updatePickSem) {
        this.updatePickSem = updatePickSem;
    }
    
    public Course getCourseFront() {
        return courseFront;
    }
    
    public void setCourseFront(Course courseFront) {
        this.courseFront = courseFront;
    }
    
    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }
    
    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
    }
    
    public int getCurrentYear() {
        return currentYear;
    }
    
    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }
    
    public int getCurrentSem() {
        return currentSem;
    }
    
    public void setCurrentSem(int currentSem) {
        this.currentSem = currentSem;
    }
    
    public int getMatricYear() {
        return matricYear;
    }
    
    public void setMatricYear(int matricYear) {
        this.matricYear = matricYear;
    }
    
    public int getAddPickYear() {
        return addPickYear;
    }
    
    public void setAddPickYear(int addPickYear) {
        this.addPickYear = addPickYear;
    }
    
    public String getAddGradeModuleCode() {
        return addGradeModuleCode;
    }
    
    public void setAddGradeModuleCode(String addGradeModuleCode) {
        this.addGradeModuleCode = addGradeModuleCode;
    }
    
    public int getAddGradePickYear() {
        return addGradePickYear;
    }
    
    public void setAddGradePickYear(int addGradePickYear) {
        this.addGradePickYear = addGradePickYear;
    }
    
    public int getAddGradePickSem() {
        return addGradePickSem;
    }
    
    public void setAddGradePickSem(int addGradePickSem) {
        this.addGradePickSem = addGradePickSem;
    }
    
    public String getAddGradeModuleGrade() {
        return addGradeModuleGrade;
    }
    
    public void setAddGradeModuleGrade(String addGradeModuleGrade) {
        this.addGradeModuleGrade = addGradeModuleGrade;
    }
    
    public String getAddCurrentModuleCode() {
        return addCurrentModuleCode;
    }
    
    public void setAddCurrentModuleCode(String addCurrentModuleCode) {
        this.addCurrentModuleCode = addCurrentModuleCode;
    }
    
    public int getGrading() {
        return grading;
    }
    
    public void setGrading(int grading) {
        this.grading = grading;
    }
    
    public List<Thread> getModuleReviews() {
        return moduleReviews;
    }
    
    public void setModuleReviews(List<Thread> moduleReviews) {
        this.moduleReviews = moduleReviews;
    }
    
    public Thread getSelectedReview() {
        return selectedReview;
    }
    
    public void setSelectedReview(Thread selectedReview) {
        this.selectedReview = selectedReview;
    }
    
    public boolean isCreateThreadCheck() {
        return createThreadCheck;
    }
    
    public void setCreateThreadCheck(boolean createThreadCheck) {
        this.createThreadCheck = createThreadCheck;
    }
    
    public User getUserEntity() {
        return userEntity;
    }
    
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Thread getThreadEntity() {
        return threadEntity;
    }
    
    public void setThreadEntity(Thread threadEntity) {
        this.threadEntity = threadEntity;
    }
    
    public String getThreadTitle() {
        return threadTitle;
    }
    
    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }
    
    public String getModuleYear() {
        return moduleYear;
    }
    
    public void setModuleYear(String moduleYear) {
        this.moduleYear = moduleYear;
    }
    
    public String getModuleSem() {
        return moduleSem;
    }
    
    public void setModuleSem(String moduleSem) {
        this.moduleSem = moduleSem;
    }
    
    public String getShowPrerequisite() {
        return showPrerequisite;
    }
    
    public void setShowPrerequisite(String showPrerequisite) {
        this.showPrerequisite = showPrerequisite;
    }
    
    public String getShowPreclusions() {
        return showPreclusions;
    }
    
    public void setShowPreclusions(String showPreclusions) {
        this.showPreclusions = showPreclusions;
    }
    
    public Module getShowModule() {
        return showModule;
    }
    
    public void setShowModule(Module showModule) {
        this.showModule = showModule;
    }
    
    public Course getCourseEntity() {
        return courseEntity;
    }
    
    public void setCourseEntity(Course courseEntity) {
        this.courseEntity = courseEntity;
    }
    
}
