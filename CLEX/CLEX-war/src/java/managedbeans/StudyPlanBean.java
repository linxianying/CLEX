/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Student;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import session.ClexSessionBeanLocal;
import session.StudyPlanSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean
@RequestScoped
public class StudyPlanBean {
    
    @EJB
    private StudyPlanSessionBeanLocal cpsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private String username;
    private String moduleCode;
    private String pickYear;
    private String pickSem;
    
    //private Student student;
    //private Course course;
    
    
    public StudyPlanBean() {
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
    
    //-------------------------------------------------------------------------
    //for test addStudyPlan, dont forget to create student and module before test
    public void testAddStudyPlan(){
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2017", 0.0);
        }
        csbl.createCourse("IS4103", "capstone", "...", false, "2020", "2", "NUS");
        cpsbl.addStudyPlan("namename", "IS4103", "2018", "2");
    }

    public StudyPlanSessionBeanLocal getCpsbl() {
        return cpsbl;
    }

    public void setCpsbl(StudyPlanSessionBeanLocal cpsbl) {
        this.cpsbl = cpsbl;
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
    
}
