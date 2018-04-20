/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.GroupTask;
import entity.IndividualGroupTask;
import entity.ProjectGroup;
import entity.Student;
import entity.Task;
import entity.User;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import session.ClexSessionBeanLocal;
import session.GroupFormationSessionBeanLocal;
import session.ToDoListSessionBeanLocal;

/**
 *
 * @author lin
 */
@ManagedBean(name = "tasksBean")
@SessionScoped
public class TasksBean {

    @EJB
    private ToDoListSessionBeanLocal tsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private GroupFormationSessionBeanLocal gfsbl;
    /**
     *
     * Creates a new instance of TasksBean
     */
    private Date date = new Date();
    private Long id;
    private Date deadline;
    private String title = "";
    private String details = "";
    private String status = "";
    private String urgency = "";
    private String strDate = "";
    private Task task;
    private String ddl;
    private Integer urgencyInt;
    private int groupOrPersonal;

    private Student studentEntity;
    private String username;
    private String userType;
    private boolean value;
    private List<Task> unfinishedTasks = new ArrayList<Task>();
    private List<Task> allTasks = new ArrayList<Task>();
    private Collection<IndividualGroupTask> indGroupTasks = new ArrayList<IndividualGroupTask>();
    private Collection<IndividualGroupTask> unfinishedIndGroupTasks = new ArrayList<IndividualGroupTask>();
    private Collection<Task> tasks;
    private ProjectGroup group;
    private Collection<ProjectGroup> projectGroups;
    private String groupInfo;
    private GroupTask gt;

    FacesContext context;
    HttpSession session;
    FacesMessage fmsg;
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public TasksBean() {

    }

    @PostConstruct
    protected void initialize() {
        refresh();
    }

    public void refresh() {
        value = false;
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        studentEntity = csbl.findStudent(username);
        tasks = null;
        unfinishedTasks = new ArrayList<Task>();
        unfinishedIndGroupTasks = new ArrayList<IndividualGroupTask>();
        if (studentEntity != null) {
            tasks = studentEntity.getTasks();
            indGroupTasks = studentEntity.getIndividualGroupTasks();
            projectGroups = studentEntity.getProjectGroups();
        }
        if (tasks != null) {
            Iterator<Task> itr = tasks.iterator();
            while (itr.hasNext()) {
                Task t = (Task) itr.next();
                if (t.getStatus().equals("unfinished")) {
                    unfinishedTasks.add(t);
                }
                //allTasks.add(t);
            }

        }
        if (indGroupTasks != null) {
            Iterator<IndividualGroupTask> itr = indGroupTasks.iterator();
            while (itr.hasNext()) {
                IndividualGroupTask t = (IndividualGroupTask) itr.next();
                if (t.getStatus().equals("unfinished")) {
                    unfinishedIndGroupTasks.add(t);
                }
                //allTasks.add(t);
            }

        }

    }

    public void test() {
        tsbl.createGroupTask("2018-04-07", "2018-04-22 23:59", "Group Task Test11",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")),
                getProjectUserName(csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2"))));
    }

    public String[] getProjectUserName(ProjectGroup p) {
        if (p.getGroupMembers() == null) {
            System.out.println("This project group is empty");
            return null;
        }
        Iterator itr = p.getGroupMembers().iterator();
        String[] name = new String[p.getGroupMembers().size()];
        System.out.println("This project group size is " + p.getGroupMembers().size());
        int index = 0;
        Student s;
        while (itr.hasNext()) {
            s = (Student) itr.next();
            name[index] = s.getUsername();
            index++;
        }
        return name;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected",
                ft.format(event.getObject())));

        setDdl(ft.format(event.getObject()));
        System.out.println("onDateSelect: ddl is " + ddl);

    }

    public void checkTask(Task task) {
        String summary = value ? "Checked. This task is finished!" : "Unchecked. This task is unfinished.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));

        if (value == true) {
            //finished
            tsbl.finishTask(task.getId());
            System.out.println("Task " + task.getId() + "is finished. " + task.getStatus());
            value = false;
        } else {
            tsbl.unfinishTask(task.getId());
        }
        refresh();
    }

    public void checkGroupTask(IndividualGroupTask groupTask) {
        String summary = value ? "Checked. This group task is finished!" : "Unchecked. This group task is unfinished.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));

        if (value == true) {
            //finished
            tsbl.finishIndGroupTask(groupTask.getId());
            System.out.println("GroupTask " + groupTask.getId() + " is " + groupTask.getStatus());
            value = false;
        } else {
            tsbl.unfinishIndGroupTask(groupTask.getId());
        }
        refresh();
    }

    public void addTask() {
        fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        studentEntity = (Student) session.getAttribute("user");
        username = studentEntity.getUsername();
        System.out.println("add task username: " + username);
        //if(deadline!=null)
        //    System.out.println("addTask: deadline is "+deadline);
        //    ddl = ft.format(deadline);
        if (groupOrPersonal == 1) {
            System.out.println("personal task creation");
            task = tsbl.createTask(username, ft.format(Calendar.getInstance().getTime()), ddl, title, details, "unfinished");

            if (urgencyInt != null) {
                urgency = urgencyInt + "";
                task.setUrgency(urgency);
                System.out.println("Add Task: urgency is " + urgency);
            } else {
                System.out.println("task with id " + task.getId() + " does not set urgency");
            }
            if (task != null && task.getUrgency() != null) {
                tsbl.linkTaskStudent(task.getId(), username);
                System.out.println("New task created in studentMain: with id " + task.getId());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Task '" + task.getTitle() + "' with urgency "
                        + task.getUrgency() + " is created.", "Successfuly");
                context.addMessage(null, fmsg);

            } else if (task != null) {
                tsbl.linkTaskStudent(task.getId(), username);
                System.out.println("New task created in studentMain: with id " + task.getId());
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Task '" + task.getTitle() + " is created.", "Successfuly");
                context.addMessage(null, fmsg);
            } else {
                System.out.println("New task creation failed");
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Task creation failed.", "Unsuccessfuly");
                context.addMessage(null, fmsg);
            }

        }
        if (groupOrPersonal == 2) {
            System.out.println("group task creation");
            if (groupInfo != null) {
                group = gfsbl.findProjectGroup(Long.parseLong(groupInfo));
            }
            System.out.println("group: " + group.getId());
            gt = tsbl.createGroupTask(ft.format(Calendar.getInstance().getTime()), ddl,
                    group.getSuperGroup().getModule().getCourse().getModuleCode() + " - " + title,
                    details, "unfinished", group, getProjectUserName(group));
            if (urgencyInt != null) {
                urgency = urgencyInt + "";
                gt.setUrgency(urgency);
                //System.out.println("Add GroupTask: urgency is " + urgency);
            }
            if (gt != null && gt.getUrgency() != null) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "GroupTask '" + gt.getTitle() + "' with urgency "
                        + gt.getUrgency() + " is created.", "Successfuly");

                context.addMessage(null, fmsg);

            } else if (gt != null) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "GroupTask '" + gt.getTitle() + " is created.", "Successfuly");
                context.addMessage(null, fmsg);
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "GroupTask creation failed.", "Unsuccessfuly");
                context.addMessage(null, fmsg);
            }
        }
        ddl = "";
        title = "";
        details = "";
        refresh();
    }

    public ProjectGroup findGroupViaGroupInfo(Student student, String groupInfo) {
        String[] arr = groupInfo.split("-");
        if (arr.length < 2) {
            return null;
        }
        String moduleCode = arr[0];
        //String groupName = 
        return null;
    }

    public void deleteTask(Task task) {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (tsbl.removeTask(task.getId(), studentEntity) == true) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Task with id " + task.getId() + " has been deleted.", "");
            context.addMessage(null, fmsg);
            reqcontext.execute("PF('usersTable').clearFilters()");
            reqcontext.update("userForm:userlist");
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete task", "");
            context.addMessage(null, fmsg);
        }

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public List<Task> getUnfinishedTasks() {
        return unfinishedTasks;
    }

    public void setUnfinishedTasks(List<Task> unfinishedTasks) {
        this.unfinishedTasks = unfinishedTasks;
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(List<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Student getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(Student studentEntity) {
        this.studentEntity = studentEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
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

    public ToDoListSessionBeanLocal getTsbl() {
        return tsbl;
    }

    public void setTsbl(ToDoListSessionBeanLocal tsbl) {
        this.tsbl = tsbl;
    }

    public SimpleDateFormat getFt() {
        return ft;
    }

    public void setFt(SimpleDateFormat ft) {
        this.ft = ft;
    }

    public String getDdl() {
        return ddl;
    }

    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

    public Integer getUrgencyInt() {
        return urgencyInt;
    }

    public void setUrgencyInt(Integer urgencyInt) {
        this.urgencyInt = urgencyInt;
    }

    public FacesMessage getFmsg() {
        return fmsg;
    }

    public void setFmsg(FacesMessage fmsg) {
        this.fmsg = fmsg;
    }

    public int getGroupOrPersonal() {
        return groupOrPersonal;
    }

    public void setGroupOrPersonal(int groupOrPersonal) {
        this.groupOrPersonal = groupOrPersonal;
    }

    public Collection<IndividualGroupTask> getIndGroupTasks() {
        return indGroupTasks;
    }

    public void setIndGroupTasks(Collection<IndividualGroupTask> indGroupTasks) {
        this.indGroupTasks = indGroupTasks;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public Collection<IndividualGroupTask> getUnfinishedIndGroupTasks() {
        return unfinishedIndGroupTasks;
    }

    public void setUnfinishedIndGroupTasks(Collection<IndividualGroupTask> unfinishedIndGroupTasks) {
        this.unfinishedIndGroupTasks = unfinishedIndGroupTasks;
    }

    public ProjectGroup getGroup() {
        return group;
    }

    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

    public Collection<ProjectGroup> getProjectGroups() {
        return projectGroups;
    }

    public void setProjectGroups(Collection<ProjectGroup> projectGroups) {
        this.projectGroups = projectGroups;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }

}
