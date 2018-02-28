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
import entity.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import session.ClassroomSessionBeanLocal;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@Named(value = "lecturerModuleListBean")
@ViewScoped
public class LecturerModuleListBean implements Serializable{

    @EJB
    CourseMgmtBeanLocal cmbl;
    @EJB
    private ClassroomSessionBeanLocal crsbl;
    
    private ArrayList<Module> modules;
    private List<Module> filteredModules;
    
    private List<Lesson> lessons;
    private List<Lesson> filteredLessons;
    
    private Lecturer lecturerEntity;
    private ArrayList<Student> students;
    
    private String username;
    
    FacesContext context;
    HttpSession session;

    public LecturerModuleListBean() {
    }
    
    @PostConstruct
    public void init() { 
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        lecturerEntity = (Lecturer) session.getAttribute("user");
        username = lecturerEntity.getUsername();
        modules = crsbl.viewModules(lecturerEntity);
        System.out.println(modules.get(0).getStudents().size());
        //modules = (List) cmbl.getModulesFromLecturer(username);
        //lessons = cmbl.getAllLessons();
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
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

    public Lecturer getLecturerEntity() {
        return lecturerEntity;
    }

    public void setLecturerEntity(Lecturer lecturerEntity) {
        this.lecturerEntity = lecturerEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }

    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
    }

    public ClassroomSessionBeanLocal getCrsbl() {
        return crsbl;
    }

    public void setCrsbl(ClassroomSessionBeanLocal crsbl) {
        this.crsbl = crsbl;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
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
    
}
