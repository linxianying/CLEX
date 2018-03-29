/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.Student;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "pollingBean")
@SessionScoped
public class PollingBean implements Serializable {

    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectSessionBeanLocal psbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private Student student;
    private ArrayList<Module> takingModules;
    private String currentYear;
    private String currentSem;
    private Module module;
    /**
     * Creates a new instance of PollingBean
     */
    public PollingBean() {
    }
    
    @PostConstruct
    public void init() {
        System.out.println("PollingBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        student = (Student) session.getAttribute("user");
        username = student.getUsername();
        
        takingModules = psbl.getTakingModules(student);
        this.setCurrentYearSem();
        System.out.println("ProjectBean Finish initialization");
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

    public ArrayList<Module> getTakingModules() {
        return takingModules;
    }

    public void setTakingModules(ArrayList<Module> takingModules) {
        this.takingModules = takingModules;
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
    
    public void setCurrentYearSem() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = "2";
            year--;
        }
        else {
            currentSem = "1";
        }
        currentYear = Integer.toString(year);
        //System.out.println("projectBean: Current Year:" + currentYear + ", current sem:" + currentSem);
    }
    
    
}
