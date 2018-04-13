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
    private Collection<Student> students;
    private Date currentDate;
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
    private Long toGroupId;
    private Long fromGroupId;
    
    private ProjectGroup deleteGroup;

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        lecturer = (Lecturer) session.getAttribute("user");
        username = lecturer.getUsername();
        module = (Module) session.getAttribute("managedModule");
        students = module.getStudents();
        superGroup = module.getSuperGroup();
        currentDate = new Date();
        refresh();
    }
    
    public void refresh() {
        if (superGroup != null)
            groups = gfsbl.getAllProjectGroups(superGroup.getId());
        formMethod = "student";
        numOfGroups = 1;
        avgStudentNum = 1;
        minStudentNum = 0;
        maxStudentNum = 0;
        deadline = null;
        toGroupId = null;
        fromGroupId = null;
    }

    public void formGroup(){
        //checking input
        if (minStudentNum != 0 && minStudentNum>avgStudentNum) {
            context = FacesContext.getCurrentInstance();
            FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, numOfGroups + "minimum number > average","Simply set minimum to 0 if no need");
            context.addMessage(null, fmsg);
        }
        if (maxStudentNum != 0 && maxStudentNum<avgStudentNum) {
            context = FacesContext.getCurrentInstance();
            FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, numOfGroups + "maximum number < average","Simply set maximum to 0 if no need");
            context.addMessage(null, fmsg);
        }
        
        if (formMethod.equals("auto")) {
            this.autoAssignAll();
            this.closeGroupFormation();
        }
        else if (formMethod.equals("student")) {
            if (maxStudentNum != 0 && maxStudentNum*numOfGroups<module.getStudents().size()) {
                int num = module.getStudents().size()/maxStudentNum;
                if (module.getStudents().size()%maxStudentNum != 0)
                    num++;
                context = FacesContext.getCurrentInstance();
                FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, numOfGroups + " groups not enough","At least " + num + " groups needed");
                context.addMessage(null, fmsg);
                return;
            }
            
            if (maxStudentNum == 0 && avgStudentNum*numOfGroups<module.getStudents().size()) {
                int num = module.getStudents().size()/avgStudentNum;
                if (module.getStudents().size()%avgStudentNum != 0)
                    num++;
                context = FacesContext.getCurrentInstance();
                FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, numOfGroups + " groups not enough","At least " + num + " groups needed");
                context.addMessage(null, fmsg);
                return;
            }
            
            if (minStudentNum != 0 && maxStudentNum != 0)
                superGroup = gfsbl.createSuperGroup(numOfGroups, avgStudentNum, minStudentNum, maxStudentNum, module);
            else if (minStudentNum == 0 && maxStudentNum == 0)
                superGroup = gfsbl.createSuperGroup(numOfGroups, avgStudentNum, module);
            else if (minStudentNum != 0 && maxStudentNum == 0)
                superGroup = gfsbl.createSuperGroupWithMin(numOfGroups, avgStudentNum, minStudentNum, module);
            else if (minStudentNum == 0 && maxStudentNum != 0)
                superGroup = gfsbl.createSuperGroupWithMax(numOfGroups, avgStudentNum, maxStudentNum, module);
            //create the project groups accordingly
            for (int i=1; i<=numOfGroups; i++) {
                csbl.createProjectGroup(superGroup, ("N"+i), 0.0);
            }
        }
        
        this.refresh();
    }
    
    public void autoAssignAll() {
        superGroup = gfsbl.createSuperGroup(0, avgStudentNum, module);
        gfsbl.autoAssignAll(module.getId(), superGroup.getId(), avgStudentNum);
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Success","Random assign all the students");
        context.addMessage(null, fmsg);
        this.refresh();
    }
    
    //assign students who havnt joined any group to a random group
    public void autoAssign() {
        int numberOfStudent = gfsbl.autoAssign(module.getId(), superGroup.getId());
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Success","Random assign the " + numberOfStudent + " students");
        context.addMessage(null, fmsg);
//        this.refresh();
    }
    
    public void closeGroupFormation() {
        gfsbl.closeGroupFormation(superGroup.getId());
        superGroup = gfsbl.findSuperGroup(superGroup.getId());
        this.refresh();
    }
    
    public void changeStudentGroup(Student student) {
        this.fromGroupId = gfsbl.checkStudentGroupId(student.getId(), superGroup.getId());
        //the student does not have a group
        if (fromGroupId == null) {
            gfsbl.setStudentGroup(student.getId(), toGroupId);
            context = FacesContext.getCurrentInstance();
            FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                    "You have set student " + student.getName() + " to group " +  gfsbl.findProjectGroup(toGroupId).getName());
            context.addMessage(null, fmsg);
            this.refresh();
        }
        else if (fromGroupId == toGroupId) {
            context = FacesContext.getCurrentInstance();
            FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Attention",
                    "Studen t" + student.getName() + " is already in group " +  gfsbl.findProjectGroup(toGroupId).getName());
            context.addMessage(null, fmsg);
            toGroupId = null;
            fromGroupId = null;
        }
        //change the stdudent to other group
        else {
            gfsbl.changeStudentGroup(student.getId(), toGroupId, fromGroupId);
            context = FacesContext.getCurrentInstance();
            FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                    "You have changed student " + student.getName() + " from group " + gfsbl.findProjectGroup(fromGroupId).getName() +" to group " +  gfsbl.findProjectGroup(toGroupId).getName());
            context.addMessage(null, fmsg);
            this.refresh();
        }
    }
    
    public void addProjectGroup() {
        int number = 1;
        if (superGroup.getProjectGroups() != null)
            number = groups.size()+1;
        gfsbl.addProjectGroup(superGroup.getId(), "N"+number);
        this.refresh();
    }
    
    
    public void deleteProjectGroup(ProjectGroup deleteGroup) {
        // if there are students in this group
        if (deleteGroup.getGroupMembers() != null && !deleteGroup.getGroupMembers().isEmpty()) {
            context = FacesContext.getCurrentInstance();
            FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail to delete group " + deleteGroup.getName(),
                "There are students in this group, please assign them to other groups first");
            context.addMessage(null, fmsg);
        }
        else {
            gfsbl.deleteProjectGroup(deleteGroup.getId());
            this.refresh();
        }
    }
    
    
    
    //check whether a student has a group
    public String checkStudentGroup(Student student) {
        return gfsbl.checkStudentGroup(student.getId(), superGroup.getId());
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

    public ProjectGroup getDeleteGroup() {
        return deleteGroup;
    }

    public void setDeleteGroup(ProjectGroup deleteGroup) {
        this.deleteGroup = deleteGroup;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public Long getFromGroupId() {
        return fromGroupId;
    }

    public void setFromGroupId(Long fromGroupId) {
        this.fromGroupId = fromGroupId;
    }

    public Long getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(Long toGroupId) {
        this.toGroupId = toGroupId;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

}
