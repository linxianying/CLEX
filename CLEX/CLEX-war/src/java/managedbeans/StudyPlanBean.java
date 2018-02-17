/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Student;
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
    
    private String username;
    private String moduleCode;
    private String pickYear;
    private String pickSem;
    
    private Student student;
    private Course course;
    
    
    public StudyPlanBean() {
    }
    
    public void addStudyPlan() {
        cpsbl.addStudyPlan(username, moduleCode, pickYear, pickSem);
    }
    
}
