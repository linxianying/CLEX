/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Student;
import entity.Task;
import entity.User;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import session.ToDoListSessionBeanLocal;

/**
 *
 * @author lin
 */
@ManagedBean(name = "tasksBean")
@SessionScoped
public class TasksBean {

    @EJB
    ToDoListSessionBeanLocal tsbl;
    /**
     * Creates a new instance of TasksBean
     */
    private Date date = new Date();
    private Long id;
    private Date deadline;
    private String title="";
    private String details="";
    private String status="";
    private String urgency="";
    private String strDate="";
    private Task task;
    private String ddl;
    private Integer urgencyInt;
    
    
    private User studentEntity;
    private String username;
    private String userType;
    private Collection<Task> tasks;
    
    
    FacesContext context;
    HttpSession session;
    FacesMessage fmsg;
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    public TasksBean() {
        
    }
    
    @PostConstruct
    protected void initialize()  {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        studentEntity = (Student) session.getAttribute("user");
        username = studentEntity.getUsername();
        
        System.out.println("finish intialization");
    }
    
    
    
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", 
                ft.format(event.getObject())));
        
        setDdl(ft.format(event.getObject()));
        System.out.println("onDateSelect: ddl is " + ddl);
        
    }
    
    public void addTask(){
        fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        
        System.out.println("Add Task: ddl is " + ddl);
        //if(deadline!=null)
        //    System.out.println("addTask: deadline is "+deadline);
        //    ddl = ft.format(deadline);
        task = tsbl.createTask(username, ft.format(Calendar.getInstance().getTime()), ddl, title, details, "unfinished");
        if(urgencyInt!=null){
            urgency = urgencyInt+"";
            task.setUrgency(urgency);
            
        }
        else{
            System.out.println("task with id " + task.getId() + " does not set urgency");
        }
        if(task!=null&&task.getUrgency()!=null){
            System.out.println("New task created in studentMain: with id " +  task.getId());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Task '" + task.getTitle() + "' with urgency " +
                    task.getUrgency() +  " is created.", "Successfuly");
            context.addMessage(null, fmsg);
                
        }
        else if(task!=null){
            System.out.println("New task created in studentMain: with id " +  task.getId());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Task '" + task.getTitle() + " is created.", "Successfuly");
            context.addMessage(null, fmsg);
        }
        else{
            System.out.println("New task creation failed");
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Task creation failed.", "Unsuccessfuly");
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

    public User getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(User studentEntity) {
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

    
    
}
