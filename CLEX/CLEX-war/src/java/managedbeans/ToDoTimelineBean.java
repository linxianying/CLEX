/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.Student;
import entity.Task;
import entity.User;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import session.ClexSessionBeanLocal;
import session.ToDoListSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "toDoTimelineBean")
@Dependent
public class ToDoTimelineBean {

    /**
     * Creates a new instance of ToDoTimelineBean
     */
    public ToDoTimelineBean() {
    
    }
    private TimelineModel model;
 
    private boolean selectable = true;
    private boolean zoomable = true;
    private boolean moveable = true;
    private boolean stackEvents = true;
    private String eventStyle = "box";
    private boolean axisOnTop;
    private boolean showCurrentTime = true;
    private boolean showNavigation = false;
    private int task;
    
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ToDoListSessionBeanLocal tdsbl;
    
    
    private User studentEntity;
    private String username;
    private String userType;
    private Collection<Task> tasks;
    private List<Task> ta = new ArrayList<Task>();
    private List<Task> filteredTa = new ArrayList<Task>();
    
    FacesContext context;
    HttpSession session;
 
    @PostConstruct
    protected void initialize()  {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        studentEntity = (Student) session.getAttribute("user");
        username = studentEntity.getUsername();
        
        
        model = new TimelineModel();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 
        //example
        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.JUNE, 12, 0, 0, 0);
        model.add(new TimelineEvent("PrimeUI 1.1", cal.getTime()));

        if(studentEntity!=null)
            tasks = studentEntity.getTasks();
        if(tasks!=null){
            try {
                Iterator<Task> itr = tasks.iterator();
                while(itr.hasNext()){
                    Task t = itr.next();
                    if(t.getStatus().equals("unfinished"))
                        ta.add(t);
                    model.add(new TimelineEvent(t.getTitle(), df.parse(t.getDeadline())));
                    //System.out.println("Timeline: " + t.getTitle() + " " + t.getDeadline());
                }
            } catch (ParseException ex) {
                Logger.getLogger(ToDoTimelineBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    public void test(){
        //
    }
    
    public int showTaskOverview(){
        int index = tdsbl.calculateTaskOverview((Student) session.getAttribute("user"));
        return 100-index;
    }
    
    public int showDailyTaskOverview(){
        int index = tdsbl.calculateDailyTaskOverview((Student) session.getAttribute("user"));
        return 100-index;
    }
    
    public void onSelect(TimelineSelectEvent e) {
        TimelineEvent timelineEvent = e.getTimelineEvent();
 
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public TimelineModel getModel() {
        return model;
    }
 
    public boolean isSelectable() {
        return selectable;
    }
 
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
 
    public boolean isZoomable() {
        return zoomable;
    }
 
    public void setZoomable(boolean zoomable) {
        this.zoomable = zoomable;
    }
 
    public boolean isMoveable() {
        return moveable;
    }
 
    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }
 
    public boolean isStackEvents() {
        return stackEvents;
    }
 
    public void setStackEvents(boolean stackEvents) {
        this.stackEvents = stackEvents;
    }
 
    public String getEventStyle() {
        return eventStyle;
    }
 
    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }
 
    public boolean isAxisOnTop() {
        return axisOnTop;
    }
 
    public void setAxisOnTop(boolean axisOnTop) {
        this.axisOnTop = axisOnTop;
    }
 
    public boolean isShowCurrentTime() {
        return showCurrentTime;
    }
 
    public void setShowCurrentTime(boolean showCurrentTime) {
        this.showCurrentTime = showCurrentTime;
    }
 
    public boolean isShowNavigation() {
        return showNavigation;
    }
 
    public void setShowNavigation(boolean showNavigation) {
        this.showNavigation = showNavigation;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public User getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(User studentEntity) {
        this.studentEntity = studentEntity;
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

    public ToDoListSessionBeanLocal getTdsbl() {
        return tdsbl;
    }

    public void setTdsbl(ToDoListSessionBeanLocal tdsbl) {
        this.tdsbl = tdsbl;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public List<Task> getTa() {
        return ta;
    }

    public void setTa(List<Task> ta) {
        this.ta = ta;
    }

    public List<Task> getFilteredTa() {
        return filteredTa;
    }

    public void setFilteredTa(List<Task> filteredTa) {
        this.filteredTa = filteredTa;
    }
    
    
}
