/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Lesson;
import entity.Module;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class AdminCourseListBean implements Serializable{

    @EJB
    CourseMgmtBeanLocal cmbl;
    
    private List<Course> courses;
    private List<Course> filteredCourses;
    
    private List<Module> modules;
    private List<Module> filteredModules;
    
    private List<Lesson> lessons;
    private List<Lesson> filteredLessons;
    
    public AdminCourseListBean() {
    }
    
    @PostConstruct
    public void init() {
        courses = cmbl.getAllCourses();    
        modules = cmbl.getAllModules(); //eeren: got some problem with the front-end listing out, not sure how to solve, pretty sure it is able to retrieve the modules
        lessons = cmbl.getAllLessons(); //same for modules
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
}
