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
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import session.ClexSessionBeanLocal;
import session.StudyPlanSessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author caoyu
 */
@ManagedBean(name="studyPlanBean")
@SessionScoped
public class StudyPlanBean {
    
    @EJB
    private StudyPlanSessionBeanLocal cpsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private String username;
    private String moduleCode;
    private String pickYear;
    private String pickSem;
    private ArrayList<Course> takenCourses;
    private ArrayList<ArrayList<Course>> takenCoursesInOrder;
    private ArrayList<Grade> grades;
    private ArrayList<ArrayList<Grade>> gradesInOrder;
    private ArrayList<Module> takingModules;
    private Collection<StudyPlan> studyPlans;
    private ArrayList<ArrayList<StudyPlan>> studyPlansInOrer;
    private HashMap<String,String> expectedCourseGrade;
    private double cap;
    private double expectedCap; 
    private String newModuleGrade;
    private String newCurrentModuleGrade;
    private int allCredits;
    private Student student;
    
    //private int year;
    private DashboardModel model;
    //for add study plan
    private String addModuleCode;
    private String addPickYear;
    private String addPickSem;
    private String addErrorMsg;
    private boolean addButton;
    
    public StudyPlanBean() {
    }
     
    @PostConstruct
    public void init() {
        //for test purpose only
        this.username="namename";
        cap = cpsbl.findStudent(username).getCap();
        gradesInOrder = cpsbl.getAllGradesInOrder(username);
        takingModules = cpsbl.getCurrentModules(username);
        studyPlansInOrer = cpsbl.getStudyPlanInOrder(username);
        expectedCourseGrade = cpsbl.getExpectedCourseGrade(username);
        expectedCap = cap;
        System.out.println("Expected Cap reset to " + expectedCap);
        this.setNewModuleGrade("A+");
        //newModuleGrade = "A+";
        this.setNewCurrentModuleGrade("A+");
        //newCurrentModuleGrade = "A+";
        allCredits = cpsbl.getNumOfCredits(username);
        addModuleCode = null;
        addPickYear = null;
        addPickSem = null;
        addErrorMsg = null;
        addButton = true;
        System.out.println("addButton:" + addButton);
        System.out.println("finish to render StudyPlanBean");
        
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
        DashboardColumn column4 = new DefaultDashboardColumn();
        DashboardColumn column5 = new DefaultDashboardColumn();
        DashboardColumn column6 = new DefaultDashboardColumn();
         
        column1.addWidget("testMod1");
        column1.addWidget("testMod2");  
        column2.addWidget("testMod3");
        column2.addWidget("testMod4");
        column3.addWidget("testMod5");
 
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
        model.addColumn(column4);
        model.addColumn(column5);
        model.addColumn(column6);
    }
     
    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());
         
        addMessage(message);
    }
    
    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");
         
        addMessage(message);
    }
     
    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());
         
        addMessage(message);
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
        this.addModuleCode = addModuleCode;
    }

    public String getAddPickYear() {
        return addPickYear;
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

    public void setAddPickYear(String addPickYear) {
        this.addPickYear = addPickYear;
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
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     
    public DashboardModel getModel() {
        return model;
    }

    private String genSalt(){
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

    public Collection<StudyPlan> getStudyPlans() {
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

    public void checkStudyPlan() {
        //this course does not exist
        if (cpsbl.findCourse(addModuleCode) == null) {
            addErrorMsg = "Course " + addModuleCode + " does not exist";
            this.addButton = false;
        }
        //this course already in studyPlan
        else if (cpsbl.checkStudyPlan(username, addModuleCode)) {
            addErrorMsg = "Course " + addModuleCode + " already exists in your study plan";
            this.addButton = false;
        }
        //this course already in takenCourses list
        else if (cpsbl.checkStudentModule(username, addModuleCode)) {
            addErrorMsg = "You have already taken course " + addModuleCode;
            this.addButton = false;
        }
        else {
            addErrorMsg = null;
            this.addButton = true;
        }
    }
    
    public void addStudyPlan() {
        cpsbl.addStudyPlan(addPickYear, addPickSem, addModuleCode, username);
        studyPlansInOrer = cpsbl.getStudyPlanInOrder(username);
    }
    
    public void deleteStudyPlan(String moduleCode) {
        cpsbl.removeStudyPlan(username, moduleCode);
        studyPlansInOrer = cpsbl.getStudyPlanInOrder(username);
    }
    
    public void updateExpectedCap(int newModuleCredit, String moduleCode) {
        String oldGrade = checkExpectedCourseGrade(moduleCode);
        //System.out.println("newModuleGrade" + newModuleGrade);
        //the first time set the expected grade 
        if (oldGrade.equals("none")) {
            System.out.println(moduleCode+": first time;");
            //it is not S or U, since setting as S or U for first time will be treated as the module has not been setted an expected grade before
            if ((!this.newModuleGrade.equals("S")) && (!this.newModuleGrade.equals("U"))) {
                //update the expectedCap for first time for this module
                this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newModuleGrade);
                //update all the credits taken
                this.allCredits += newModuleCredit;
                //set the course's expected grade in hashmap
                expectedCourseGrade.put(moduleCode, newModuleGrade);
            }
        }
        //not the first time set the expected grade
        else {
            //if the last setted garde is S/U
            if (oldGrade.equals("S") || oldGrade.equals("U")) {
                // if this time it is S/U again, do nothing
                // if not, type 2 
                if ((!this.newModuleGrade.equals("S"))&&(!this.newModuleGrade.equals("U"))) {
                    System.out.println(moduleCode+": type 2;");
                    this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newModuleGrade);
                    //update all the credits taken
                    this.allCredits += newModuleCredit;
                    //set the course's expected grade in hashmap
                    expectedCourseGrade.put(moduleCode, newModuleGrade);
                }
                else {
                    System.out.println(moduleCode+": type 3;");
                }
            }
            //if the last setted garde is not S/U
            else {
                //if it is changed to S/U, type 1
                if ((this.newModuleGrade.equals("S")) || (this.newModuleGrade.equals("U"))) {
                    System.out.println(moduleCode+": type 1;");
                    this.expectedCap = cpsbl.updateExpectedCapOne(this.allCredits, this.expectedCap, newModuleCredit, oldGrade);
                    this.allCredits -= newModuleCredit;
                    expectedCourseGrade.put(moduleCode, newModuleGrade);
                }
                // if it is not changed to S/U, type 4
                else {
                    System.out.println(moduleCode+": type 4;");
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
            System.out.println(moduleCode+": first time;");
            //it is not S or U, since setting as S or U for first time will be treated as the module has not been setted an expected grade before
            if ((!this.newCurrentModuleGrade.equals("S")) && (!this.newCurrentModuleGrade.equals("U"))) {
                //update the expectedCap for first time for this module
                this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newCurrentModuleGrade);
                //update all the credits taken
                this.allCredits += newModuleCredit;
                //set the course's expected grade in hashmap
                expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
            }
        }
        //not the first time set the expected grade
        else {
            //if the last setted garde is S/U
            if (oldGrade.equals("S") || oldGrade.equals("U")) {
                // if this time it is S/U again, do nothing
                // if not, type 2 
                if ((!this.newCurrentModuleGrade.equals("S"))&&(!this.newCurrentModuleGrade.equals("U"))) {
                    System.out.println(moduleCode+": type 2;");
                    this.expectedCap = cpsbl.updateExpectedCapTwo(this.allCredits, this.expectedCap, newModuleCredit, newCurrentModuleGrade);
                    //update all the credits taken
                    this.allCredits += newModuleCredit;
                    //set the course's expected grade in hashmap
                    expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
                }
                else {
                    System.out.println(moduleCode+": type 3;");
                }
            }
            //if the last setted garde is not S/U
            else {
                //if it is changed to S/U, type 1
                if ((this.newCurrentModuleGrade.equals("S")) || (this.newCurrentModuleGrade.equals("U"))) {
                    System.out.println(moduleCode+": type 1;");
                    this.expectedCap = cpsbl.updateExpectedCapOne(this.allCredits, this.expectedCap, newModuleCredit, oldGrade);
                    this.allCredits -= newModuleCredit;
                    expectedCourseGrade.put(moduleCode, newCurrentModuleGrade);
                }
                // if it is not changed to S/U, type 4
                else {
                    System.out.println(moduleCode+": type 4;");
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
    public String checkExpectedCourseGrade(String moduleCode){
        if (expectedCourseGrade.containsKey(moduleCode)) {
            return expectedCourseGrade.get(moduleCode);
        }
        else {
            System.out.println("sp bean: checkExpectedCourseGrade: Error，" 
                    + moduleCode + " is not found in expectedCourseGrade");
        }
        return "none";
    }
    
    //-------------------------------------------------------------------------
    //for test addStudyPlan, dont forget to create student and module before test
   
    public void testAddStudyPlan(){
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        }
        cpsbl.addStudyPlan("namename", "IS4103", "2018", "2");
    }
    
    public void testAddModuleFromNUSMods(){
        csbl.dragAllNusMods(username);
    }
    
    public void getTimetable(){
        csbl.getTimetable("IS4103");
    }
    
    public void testUpdateStudyPlan() {
        cpsbl.updateStudyPlan("namename", "IS4103", "2020", "1");
    }
    
    public void testRemoveStudyPlan(){
        cpsbl.removeStudyPlan("namename", "IS4103");
    }
    
    public void testViewTakenCourses() {
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        }
        this.student = cpsbl.findStudent("namename");
        //to add some taken modules for student
        //first,create some modules
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("IS3106"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("CS1020"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("CS2100"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("GER1000"));
        //add these modules to the student's Module list
        cpsbl.setStudentTakenModules("namename", "CP3109", "2018", "1");
        cpsbl.setStudentTakenModules("namename", "IS3106", "2018", "1");
        cpsbl.setStudentTakenModules("namename", "CS1020", "2018", "1");
        cpsbl.setStudentTakenModules("namename", "CS2100", "2018", "1");
        cpsbl.setStudentTakenModules("namename", "GER1000", "2018", "1");
        
        this.takenCourses = cpsbl.getTakenCourses("namename");
        System.out.println(takenCourses);
        System.out.println("sp bean: testViewTakenCourses finish ");
    }
    
    public void testViewStudyPlan() {
        cpsbl.addStudyPlan("2018", "1", "ACC1002X", "namename");
        cpsbl.addStudyPlan("2018", "1", "MA1101R", "namename");
        cpsbl.addStudyPlan("2018", "1", "IS1103", "namename");
        cpsbl.addStudyPlan("2018", "1", "IS1105", "namename");
        cpsbl.addStudyPlan("2018", "1", "CS2102", "namename");
        this.studyPlans = cpsbl.getAllStudyPlans("namename");
    }
    
    public void testViewTakenCoursesInOrder(){
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        }
        this.student = cpsbl.findStudent("namename");
        //to add some taken modules for student
        //first,create some modules
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("IS3106"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CS1020"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CS2100"));
        csbl.createModule("2017", "1", "none", "none", csbl.findCourse("GER1000"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PS2240"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("ST2334"));
        //add these modules to the student's Module list
        cpsbl.setStudentTakenModules("namename", "CP3109", "2015", "1");
        cpsbl.setStudentTakenModules("namename", "IS3106", "2015", "2");
        cpsbl.setStudentTakenModules("namename", "CS1020", "2016", "1");
        cpsbl.setStudentTakenModules("namename", "CS2100", "2016", "2");
        cpsbl.setStudentTakenModules("namename", "GER1000", "2017", "1");
        cpsbl.setStudentTakenModules("namename", "PS2240", "2017", "2");
        //cpsbl.setStudentTakenModules("namename", "ST2334", "2018", "1");
        
        takenCoursesInOrder = cpsbl.getTakenModulesInOrder("namename");
        System.out.print("sp bean: takenCoursesInOrder:");
        System.out.println(takenCoursesInOrder.size());
        System.out.println(takenCoursesInOrder);
        System.out.println("sp bean: testViewTakenCoursesInOrder finish ");
        
        //year = Integer.parseInt(student.getMatricYear());
    }
    
    
}
