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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClassroomSessionBeanLocal;
import session.ClexSessionBeanLocal;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "moduleInfoBean")
@Dependent
public class ModuleInfoBean {

    /**
     * Creates a new instance of ModuleInfoBean
     */
    @EJB
    CourseMgmtBeanLocal cmbl;
    
    @EJB
    ClexSessionBeanLocal csbl;
    
    @EJB
    ClassroomSessionBeanLocal crsbl;
    
    FacesContext context;
    HttpSession session;
    private Lecturer lecturerEntity;
    private String username;
    private String moduleCode;
    private String moduleInfo;
    private String moduleTitle;
    private String workload;
    private String moduleCredit;
    private String pickYear;
    private String pickSem;
    private String prerequisite;
    private String preclusions;
    private Module module;
    private SuperGroup superGroup;
    private Collection<ProjectGroup> projectGroups;
    private Collection<Student> students;
    
    public ModuleInfoBean() {
    }
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        lecturerEntity = (Lecturer) session.getAttribute("user");
        username = lecturerEntity.getUsername();
        //lessons = cmbl.getAllLessons();
        moduleCode = (String) session.getAttribute("moduleCode");
        pickSem = (String) session.getAttribute("pickSem");
        pickYear = (String) session.getAttribute("pickYear");
        module = csbl.findModule(moduleCode, pickYear, pickSem);
        moduleInfo = module.getCourse().getModuleInfo();
        moduleTitle = module.getCourse().getModuleName();
        moduleCredit = module.getCourse().getModularCredits();
        workload = module.getCourse().getWorkload();
        prerequisite = module.getPrerequisite();
        preclusions = module.getPreclusions();
        superGroup = module.getSuperGroup();
        if(superGroup!=null)
            projectGroups = (ArrayList) superGroup.getProjectGroups();
        students = module.getStudents();
    }

    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }

    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ClassroomSessionBeanLocal getCrsbl() {
        return crsbl;
    }

    public void setCrsbl(ClassroomSessionBeanLocal crsbl) {
        this.crsbl = crsbl;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getModuleCredit() {
        return moduleCredit;
    }

    public void setModuleCredit(String moduleCredit) {
        this.moduleCredit = moduleCredit;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
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

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getPreclusions() {
        return preclusions;
    }

    public void setPreclusions(String preclusions) {
        this.preclusions = preclusions;
    }

    public SuperGroup getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(SuperGroup superGroup) {
        this.superGroup = superGroup;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public Collection<ProjectGroup> getProjectGroups() {
        return projectGroups;
    }

    public void setProjectGroups(Collection<ProjectGroup> projectGroups) {
        this.projectGroups = projectGroups;
    }
    
    
}
