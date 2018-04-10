/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;
import entity.GroupTask;
import entity.GroupTimeslot;
import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import javax.ejb.EJB;
import session.ClexSessionBeanLocal;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.UploadedFile;
import session.GroupFormationSessionBeanLocal;
import session.ScheduleSessionBeanLocal;
import session.ToDoListSessionBeanLocal;


/**
 *
 * @author lin
 */
@ManagedBean(name = "projectScheduleBean")
@SessionScoped
public class ProjectScheduleBean implements Serializable{

    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ToDoListSessionBeanLocal tdsbl;
    @EJB
    private ScheduleSessionBeanLocal sbl;
    @EJB
    private GroupFormationSessionBeanLocal gfsbl;

    private String details="";
    private String venue="";
    private Student student;
    private Module module;
    private Collection<GroupTimeslot> groupTimeslots;
    private ScheduleModel eventModel = new DefaultScheduleModel();
    private ScheduleEvent event = new DefaultScheduleEvent();
    private ProjectGroup group;
    private Collection<GroupTask> groupTasks;
    private Collection<GroupTask> unfinishedGroupTasks;
    private boolean value;

    FacesContext context;
    HttpSession session;
    private UploadedFile uploadedFile;

    public ProjectScheduleBean() {
    }

    @PostConstruct
    public void init() {
        refresh();
        
    }
    
    public void refresh(){
        eventModel = new DefaultScheduleModel();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        group = (ProjectGroup) session.getAttribute("projectGroup");
        module = (Module) session.getAttribute("module");
        unfinishedGroupTasks = new ArrayList<GroupTask>();
        groupTimeslots = group.getGroupTimeslots();
        groupTasks = group.getGroupTasks();
        
        System.out.println("GroupTask: " + groupTasks.size());
        GroupTimeslot g;
        for(GroupTimeslot group : groupTimeslots){
            g = group;
            DefaultScheduleEvent dse = new DefaultScheduleEvent(g.getTitle(), toCalendar(g.getTimeFrom()), toCalendar(g.getTimeEnd()), g);
            dse.setDescription(g.getDetails());
            System.out.println(g.getDetails());
            eventModel.addEvent(dse);
        }
        GroupTask groupTask;
        if(groupTasks!=null){
            for(GroupTask gt : groupTasks){
                groupTask = gt;
                if(gt.getStatus().equals("unfinished"))
                    unfinishedGroupTasks.add(gt);
            }
        }
    }
    
    public void checkGroupTask(GroupTask groupTask){
        String summary = value ? "Checked. This group task is finished!" : "Unchecked. This group task is unfinished.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        
        if(value==true){
            //finished
            tdsbl.finishGroupTask(groupTask.getId());
            System.out.println("GroupTask " + groupTask.getId() + " is " + groupTask.getStatus());
            value = false;
        }else{
            tdsbl.unfinishGroupTask(groupTask.getId());
        }
        refresh();
    }
    

    //Convert String to Date format
    public static Date toCalendar(String date) {
        Calendar t = Calendar.getInstance();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d = df.parse(date);
            t.setTime(d);
        } catch (Exception e) {
            System.err.println(e);
        }
        return t.getTime();
    }
    
     public void reset(){
        
        if(!groupTimeslots.isEmpty()){
            System.out.println("begin_________________________");
            for (GroupTimeslot t : groupTimeslots) {
                
            }
        }
        eventModel.clear();
        //List<ScheduleEvent> events = eventModel.getEvents();
        //if(!events.isEmpty()){
        //    for (ScheduleEvent s: events){
        //        eventModel.deleteEvent(s);
        //   }
        //}
    }

    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        return calendar.getTime();
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ToDoListSessionBeanLocal getTdsbl() {
        return tdsbl;
    }

    public void setTdsbl(ToDoListSessionBeanLocal tdsbl) {
        this.tdsbl = tdsbl;
    }

    public ScheduleSessionBeanLocal getSbl() {
        return sbl;
    }

    public void setSbl(ScheduleSessionBeanLocal sbl) {
        this.sbl = sbl;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void addEvent(ActionEvent actionEvent) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (event.getId() == null){
            GroupTimeslot gts = csbl.createProjectGroupTimeslot("", df.format(event.getStartDate()), df.format(event.getEndDate()), 
                event.getTitle(), details, venue,  group);
            eventModel.addEvent(new DefaultScheduleEvent(gts.getTitle(), toCalendar(gts.getTimeFrom()), toCalendar(gts.getTimeEnd()), gts));
        }
        else{
            GroupTimeslot t = (GroupTimeslot) event.getData();
            sbl.updateTimeslot(t.getId(), event.getTitle(), 
                    df.format(event.getStartDate()), df.format(event.getEndDate()), details, venue);
            eventModel.updateEvent(new DefaultScheduleEvent(t.getTitle(), 
                    toCalendar(t.getTimeFrom()), toCalendar(t.getTimeEnd()), t));          
        } 
        event = new DefaultScheduleEvent();
        
        refresh();
    }

    public void deleteEvent(ActionEvent actionEvent) {
        
        GroupTimeslot t = (GroupTimeslot) event.getData();
        //sbl.deleteGroupTimeslot(t.getId(), student);
        eventModel.deleteEvent(event);
        
        
    }


    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ScheduleEvent tempEvent = event.getScheduleEvent();
           
        GroupTimeslot t = (GroupTimeslot) tempEvent.getData();
        sbl.updateGroupTimeslot(t.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
        eventModel.updateEvent(new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getTimeFrom()), toCalendar(t.getTimeEnd()), t));

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated", "Change in Days:" + event.getDayDelta() + ", Change in Minutes:" + event.getMinuteDelta());
        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ScheduleEvent tempEvent = event.getScheduleEvent();
        
        GroupTimeslot t = (GroupTimeslot) tempEvent.getData();

        sbl.updateGroupTimeslot(t.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
        eventModel.updateEvent(new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getTimeFrom()), toCalendar(t.getTimeEnd()), t));
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }


    public ProjectGroup getGroup() {
        return group;
    }

    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

    public GroupFormationSessionBeanLocal getGfsbl() {
        return gfsbl;
    }

    public void setGfsbl(GroupFormationSessionBeanLocal gfsbl) {
        this.gfsbl = gfsbl;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Collection<GroupTimeslot> getGroupTimeslots() {
        return groupTimeslots;
    }

    public void setGroupTimeslots(Collection<GroupTimeslot> groupTimeslots) {
        this.groupTimeslots = groupTimeslots;
    }

    public Collection<GroupTask> getGroupTasks() {
        return groupTasks;
    }

    public void setGroupTasks(Collection<GroupTask> groupTasks) {
        this.groupTasks = groupTasks;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Collection<GroupTask> getUnfinishedGroupTasks() {
        return unfinishedGroupTasks;
    }

    public void setUnfinishedGroupTasks(Collection<GroupTask> unfinishedGroupTasks) {
        this.unfinishedGroupTasks = unfinishedGroupTasks;
    }

    
}
