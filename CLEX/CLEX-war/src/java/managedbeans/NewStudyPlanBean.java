/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Grade;
import entity.Module;
import entity.Student;
import entity.StudyPlan;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import session.ClexSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.StudyPlanSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "newStudyPlanBean")
@SessionScoped
public class NewStudyPlanBean implements Serializable {

    public NewStudyPlanBean() {
    }
    
    private DashboardModel model;
    @EJB
    private StudyPlanSessionBeanLocal spsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    CourseMgmtBeanLocal cmbl;

    FacesContext context;
    HttpSession session;

    private Student student;
    private double cap;
    private double expectedCap;
    private ArrayList<Grade> grades;
    private ArrayList<Module> takingModules;
    private ArrayList<StudyPlan> studyPlans;
    private int currentYear;
    private int currentSem;
    private int matricYear;
    private int matricSem;
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        student = (Student) session.getAttribute("user");
        cap = student.getCap();
        
        if (student.getGrades().size() > 0) {
            grades = spsbl.getAllGrades(student);
        }

        takingModules = spsbl.getCurrentModules(student);
        
        if (student.getStudyPlan() != null) {
            studyPlans = spsbl.getAllStudyPlans(student);
        }
        
        this.setYearSem();
        
        
        
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
        DashboardColumn column4 = new DefaultDashboardColumn();
        DashboardColumn column5 = new DefaultDashboardColumn();
        DashboardColumn column6 = new DefaultDashboardColumn();
        DashboardColumn column7 = new DefaultDashboardColumn();
        DashboardColumn column8 = new DefaultDashboardColumn();

        column1.addWidget("y11");
        column2.addWidget("y12");
        column3.addWidget("y21");
        column4.addWidget("y22");
        column5.addWidget("y31");
        column6.addWidget("y32");
        column7.addWidget("y41");
        column8.addWidget("y42");
        
        //if yearSem=1 it's year1sem1; if=2, year1sem2 etc
        int yearSem = 0;
        for (Module m: takingModules) {
            yearSem = (currentYear-matricYear)*2+currentSem;
            switch(yearSem){
                case 1: 
                    column1.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 2: 
                    column2.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 3: 
                    column3.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 4: 
                    column4.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 5: 
                    column5.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 6: 
                    column6.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 7: 
                    column7.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                case 8: 
                    column8.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
                default: 
                    column8.addWidget("TM: "+m.getCourse().getModuleCode());
                    break;
            }
        }
        
        yearSem = 0;
        for (Grade g: grades) {
            yearSem = (Integer.parseInt(g.getModule().getTakenYear())-matricYear)*2+Integer.parseInt(g.getModule().getTakenSem());
            switch(yearSem){
                case 1: 
                    column1.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 2: 
                    column2.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 3: 
                    column3.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 4: 
                    column4.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 5: 
                    column5.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 6: 
                    column6.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 7: 
                    column7.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                case 8: 
                    column8.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
                default: 
                    column8.addWidget("G"+g.getModule().getCourse().getModuleCode());
                    break;
            }
        }
        
        yearSem = 0;
        for (StudyPlan m: studyPlans) {
            yearSem = (Integer.parseInt(m.getPickYear())-matricYear)*2+Integer.parseInt(m.getPickSem());
            switch(yearSem){
                case 1: 
                    column1.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 2: 
                    column2.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 3: 
                    column3.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 4: 
                    column4.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 5: 
                    column5.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 6: 
                    column6.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 7: 
                    column7.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                case 8: 
                    column8.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
                default: 
                    column8.addWidget("SP"+m.getCourse().getModuleCode());
                    break;
            }
        }
        
        column1.addWidget("c1");
        column2.addWidget("c2");
        column3.addWidget("c3");
        column4.addWidget("c4");
        column5.addWidget("c5");
        column6.addWidget("c6");
        column7.addWidget("c7");
        column8.addWidget("c8");
        
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
        model.addColumn(column4);
        model.addColumn(column5);
        model.addColumn(column6);
        model.addColumn(column7);
        model.addColumn(column8);
        
        System.out.println("newStudyPlanBean finish initialization");
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public StudyPlanSessionBeanLocal getSpsbl() {
        return spsbl;
    }

    public void setSpsbl(StudyPlanSessionBeanLocal spsbl) {
        this.spsbl = spsbl;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }

    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }

    public double getExpectedCap() {
        return expectedCap;
    }

    public void setExpectedCap(double expectedCap) {
        this.expectedCap = expectedCap;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public ArrayList<Module> getTakingModules() {
        return takingModules;
    }

    public void setTakingModules(ArrayList<Module> takingModules) {
        this.takingModules = takingModules;
    }

    public ArrayList<StudyPlan> getStudyPlans() {
        return studyPlans;
    }

    public void setStudyPlans(ArrayList<StudyPlan> studyPlans) {
        this.studyPlans = studyPlans;
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

    
    
  
    
    public void setYearSem(){
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
        //for test purpose
        matricYear = 2015;
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

    
    
    
    
    
    
}

     
        