/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Module;
import entity.Student;
import entity.StudyPlan;
import java.util.ArrayList;
import java.util.Collection;
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
    private Collection<StudyPlan> studyPlans;
    private double calculatedCap; 
    private Student student;
    //private Course course;
    private int currentYear;
    private int currentMonth;
    private DashboardModel model;
    
    public StudyPlanBean() {
        //for test purpose only
        this.username="namename";
        
    }
     
    @PostConstruct
    public void init() {
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
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     
    public DashboardModel getModel() {
        return model;
    }
    

    public void addStudyPlan() {
        cpsbl.addStudyPlan(username, moduleCode, pickYear, pickSem);
        
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
    
    public void takenCourse(){
    
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


    public double getCalculatedCap() {
        return calculatedCap;
    }

    public void setCalculatedCap(double calculatedCap) {
        this.calculatedCap = calculatedCap;
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
    
    //-------------------------------------------------------------------------
    //for test addStudyPlan, dont forget to create student and module before test
   
    public void testAddStudyPlan(){
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2017", 0.0);
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
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2017", 0.0);
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
        
        this.takenCourses = cpsbl.getTakenModules("namename");
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
    
    
}
