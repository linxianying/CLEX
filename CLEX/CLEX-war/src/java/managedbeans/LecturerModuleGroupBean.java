/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.GroupFormationSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "lecturerModuleGroupBean")
@SessionScoped
public class LecturerModuleGroupBean implements Serializable {

    public LecturerModuleGroupBean() {
    }
    @EJB
    private ClexSessionBeanLocal csbl;
    
    @EJB
    private GroupFormationSessionBeanLocal gfsbl;
    
    FacesContext context;
    HttpSession session;
    
    private Lecturer lecturer;
    private String username;
    private Module module;
    private SuperGroup superGroup;
    private Collection<ProjectGroup> groups;
    
    //for enable group formation process
    //whether is to form groups by stduents themselves or auto assign them
    private String formMethod;
    private int numOfGroups;
    private int avgStudentNum;
    private int minStudentNum;
    private int maxStudentNum;
    private Date deadline;
    
    //for change student group
    private Student student;
    private ProjectGroup fromGroup;
    private ProjectGroup toGroup;

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        lecturer = (Lecturer) session.getAttribute("user");
        username = lecturer.getUsername();
        module = (Module) session.getAttribute("managedModule");
        refresh();
    }
    
    public void refresh() {
        superGroup = module.getSuperGroup();
        if (superGroup != null)
            groups = superGroup.getProjectGroups();
        
        formMethod = null;
        numOfGroups = 0;
        avgStudentNum = 0;
        minStudentNum = 0;
        maxStudentNum = 0;
        deadline = null;
    }

    public void formGroup(){
        if (formMethod.equals("auto"))
            this.autoAssign();
        else if (formMethod.equals("student")) {
            if (minStudentNum != 0 && maxStudentNum != 0)
                csbl.createSuperGroup(numOfGroups, avgStudentNum, minStudentNum, maxStudentNum, module);
            else if (minStudentNum == 0 && maxStudentNum == 0)
                csbl.createSuperGroup(numOfGroups, avgStudentNum, module);
            else if (minStudentNum != 0 && maxStudentNum == 0)
                csbl.createSuperGroupWithMin(numOfGroups, avgStudentNum, minStudentNum, module);
            else if (minStudentNum == 0 && maxStudentNum != 0)
                csbl.createSuperGroupWithMax(numOfGroups, avgStudentNum, maxStudentNum, module);
            //create the project groups according 
            superGroup = module.getSuperGroup();
            for (int i=1; i<=numOfGroups; i++) {
                csbl.createProjectGroup(superGroup, ("N"+i), 0.0);
            }
        }
        
        this.refresh();
    }
    
    public void autoAssign() {
        //--------------------------------------------Havn't implement--------------------------------------------
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Auto assign error","Cannot assign the group as required");
        context.addMessage(null, fmsg);
    }
    
    public void changeStudentGroup() {
        //--------------------------------------------front end Havn't implement--------------------------------------------
        gfsbl.changeStudentGroup(student, toGroup, fromGroup);
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Change student's group",
                "Successfully change student " + student.getName() + " from group " + fromGroup.getName() +" to group " + toGroup.getName());
        context.addMessage(null, fmsg);
    }
    
    public void addProjectGroup() {
        //--------------------------------------------front end Havn't implement--------------------------------------------
        int number = 1;
        if (superGroup.getProjectGroups() != null)
            number = superGroup.getProjectGroups().size()+1;
        csbl.createProjectGroup(superGroup, "N"+number, 0.0);
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

    public int getNumOfGroups() {
        return numOfGroups;
    }

    public void setNumOfGroups(int numOfGroups) {
        this.numOfGroups = numOfGroups;
    }

    public int getAvgStudentNum() {
        return avgStudentNum;
    }

    public void setAvgStudentNum(int avgStudentNum) {
        this.avgStudentNum = avgStudentNum;
    }

    public int getMinStudentNum() {
        return minStudentNum;
    }

    public void setMinStudentNum(int minStudentNum) {
        this.minStudentNum = minStudentNum;
    }

    public int getMaxStudentNum() {
        return maxStudentNum;
    }

    public void setMaxStudentNum(int maxStudentNum) {
        this.maxStudentNum = maxStudentNum;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public GroupFormationSessionBeanLocal getGfsbl() {
        return gfsbl;
    }

    public void setGfsbl(GroupFormationSessionBeanLocal gfsbl) {
        this.gfsbl = gfsbl;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ProjectGroup getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(ProjectGroup fromGroup) {
        this.fromGroup = fromGroup;
    }

    public ProjectGroup getToGroup() {
        return toGroup;
    }

    public void setToGroup(ProjectGroup toGroup) {
        this.toGroup = toGroup;
    }
    
}
