/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.PeerReviewAnswer;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.GroupFormationSessionBeanLocal;
import session.PRAnswerSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "lecturerPRBean")
@SessionScoped
public class LecturerPRBean implements Serializable {
    @EJB
    private GroupFormationSessionBeanLocal gfsbl;
    @EJB
    private PRAnswerSessionBeanLocal prasbl;
    @EJB
    private PRQuestionSessionBeanLocal prqsbl;
    public LecturerPRBean() {
    }
    
    FacesContext context;
    HttpSession session;
    
    private Lecturer lecturer;
    private String username;
    private Module module;
    private SuperGroup superGroup;
    private Collection<ProjectGroup> groups;
    private Collection<Student> students;
    private Collection<Student> filteredstudents;
    private PeerReviewAnswer answers;
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        lecturer = (Lecturer) session.getAttribute("user");
        username = lecturer.getUsername();
        module = (Module) session.getAttribute("managedModule");
        superGroup = module.getSuperGroup();
        if (superGroup != null)
            groups = gfsbl.getAllProjectGroups(superGroup.getId());
        students = module.getStudents();
    }

    
    
    public String checkStudentGroup(Student student) {
        return gfsbl.checkStudentGroup(student.getId(), superGroup.getId());
    }
    
    public boolean checkStudentPRFormSubmit(Student student) {
        return prasbl.checkPRFormSubmit(student, module);
    }
    
    public void goViewPR(Student student) {
        answers = prasbl.getPRAnswer(module, student);
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("managedStudent", student);
        session.setAttribute("managedPRAnswer", answers);
    }
    
    
    public GroupFormationSessionBeanLocal getGfsbl() {
        return gfsbl;
    }

    public void setGfsbl(GroupFormationSessionBeanLocal gfsbl) {
        this.gfsbl = gfsbl;
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

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public SuperGroup getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(SuperGroup superGroup) {
        this.superGroup = superGroup;
    }

    public Collection<ProjectGroup> getGroups() {
        return groups;
    }

    public void setGroups(Collection<ProjectGroup> groups) {
        this.groups = groups;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public Collection<Student> getFilteredstudents() {
        return filteredstudents;
    }

    public void setFilteredstudents(Collection<Student> filteredstudents) {
        this.filteredstudents = filteredstudents;
    }
    
    
    
    
}
