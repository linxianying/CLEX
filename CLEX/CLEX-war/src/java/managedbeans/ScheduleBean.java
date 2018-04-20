/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.GroupTimeslot;
import entity.ProjectGroup;
import entity.Student;
import entity.Timeslot;
import entity.User;
import java.io.File;
import java.io.InputStream;
import javax.ejb.EJB;
import session.ClexSessionBeanLocal;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Component;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;

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
@ManagedBean(name = "scheduleBean")
@SessionScoped
public class ScheduleBean implements Serializable {

    /**
     * Creates a new instance of ScheduleBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ToDoListSessionBeanLocal tdsbl;
    @EJB
    private ScheduleSessionBeanLocal sbl;
    @EJB
    private GroupFormationSessionBeanLocal gfsbl;

    private User userEntity;
    private String username;
    private String userType;
    private String details="";
    private String venue="";
    private Student student;
    private ArrayList<Timeslot> timeslots;
    private List<GroupTimeslot> groupTimeslots;
    private ScheduleModel eventModel = new DefaultScheduleModel();
    private ScheduleEvent event = new DefaultScheduleEvent();
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    private ProjectGroup group;
    private Collection<ProjectGroup> projectGroups;
    private String groupInfo;
    private int groupOrPersonal;

    FacesContext context;
    HttpSession session;
    private UploadedFile uploadedFile;

    public ScheduleBean() {
    }

    @PostConstruct
    public void init() {
        refresh();
        
    }
    
    public void refresh(){
        userEntity = null;
        eventModel = new DefaultScheduleModel();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        userType = session.getAttribute("userType")+"";
        userEntity = csbl.findUser(username);
        timeslots = sbl.getAllTimeslots(userEntity);
        Timeslot t;
        for (Timeslot timeslot : timeslots) {
            t = timeslot;
            DefaultScheduleEvent dse = new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getStartDate()), toCalendar(t.getEndDate()), t);
            dse.setDescription(t.getDetails());
            //System.out.println(t.getDetails());
            eventModel.addEvent(dse);
        }
        if(userType.equals("1")){ //student
            System.out.println("this is a student");
            student = (Student) userEntity;
            groupTimeslots = (List<GroupTimeslot>) student.getGroupTimeslots();
            GroupTimeslot g;
            for(GroupTimeslot group : groupTimeslots){
                g = group;
                DefaultScheduleEvent dse = new DefaultScheduleEvent(g.getTitle(), toCalendar(g.getTimeFrom()), toCalendar(g.getTimeEnd()), g);
                dse.setDescription(g.getDetails());
                System.out.println(g.getDetails());
                eventModel.addEvent(dse);
           }
            projectGroups = student.getProjectGroups();
        }
        //csbl.createProjectGroupTimeslot("", "2018-02-28 06:00", "2018-02-28 07:00", "Group Meeting", 
        //        "First System Release", "Biz Lib", csbl.findProjectgroup("N1", csbl.findModule("CS2100", "2016", "2")));
        
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
        System.out.println("Reset function begin" + timeslots.size());
        //timeslots = sbl.getAllTimeslots(userEntity);
        if(!timeslots.isEmpty()){
            for (Timeslot timeslot : timeslots) {
                System.out.println(timeslot.getTitle());
                sbl.deleteTimeslot(timeslot.getId(), userEntity);

            }
        }
        if(userType.equals("1")){
            
            if(!groupTimeslots.isEmpty())
            for (GroupTimeslot t : groupTimeslots) {
                sbl.deleteGroupTimeslot(t.getId(), (Student) userEntity);
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

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
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
        System.out.println(event.getId() + "/" +groupOrPersonal+"/"+groupInfo);
        if (event.getId() == null&&(groupOrPersonal==1||userType.equals("2"))) {
            Timeslot timeslot = sbl.createTimeslot(username, event.getTitle(), df.format(event.getStartDate()), df.format(event.getEndDate()), details, venue);
            eventModel.addEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
        } 
        else if(event.getId() == null && groupOrPersonal == 2 && groupInfo != null){
            System.out.println("Add GroupTimeslot: " + groupInfo);
            group = gfsbl.findProjectGroup(Long.parseLong(groupInfo));
            GroupTimeslot gts = csbl.createProjectGroupTimeslot("", df.format(event.getStartDate()), df.format(event.getEndDate()), 
                event.getTitle(), details, venue,  group);
            eventModel.addEvent(new DefaultScheduleEvent(gts.getTitle(), toCalendar(gts.getTimeFrom()), toCalendar(gts.getTimeEnd()), gts));
        }
        else{
            
            try{
                Timeslot timeslot = (Timeslot) event.getData();
                sbl.updateTimeslot(timeslot.getId(), event.getTitle(), 
                        df.format(event.getStartDate()), df.format(event.getEndDate()), details, venue);
                eventModel.updateEvent(new DefaultScheduleEvent(timeslot.getTitle(), 
                        toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
            }catch(ClassCastException e){
                
                GroupTimeslot t = (GroupTimeslot) event.getData();
                sbl.updateTimeslot(t.getId(), event.getTitle(), 
                        df.format(event.getStartDate()), df.format(event.getEndDate()), details, venue);
                eventModel.updateEvent(new DefaultScheduleEvent(t.getTitle(), 
                        toCalendar(t.getTimeFrom()), toCalendar(t.getTimeEnd()), t));
            }
        } 
        event = new DefaultScheduleEvent();
        
        refresh();
    }

    public void deleteEvent(ActionEvent actionEvent) {
        try{
            Timeslot timeslot = (Timeslot) event.getData();
            sbl.deleteTimeslot(timeslot.getId(), userEntity);
            eventModel.deleteEvent(event);
        }catch(ClassCastException e){
            GroupTimeslot t = (GroupTimeslot) event.getData();
            sbl.deleteGroupTimeslot(t.getId(), student);
            eventModel.deleteEvent(event);
        }
        
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
        try{
            
            Timeslot timeslot = (Timeslot) tempEvent.getData();
            sbl.updateTimeslot(timeslot.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
            eventModel.updateEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
        } catch(ClassCastException e){
           
            GroupTimeslot t = (GroupTimeslot) tempEvent.getData();
            sbl.updateGroupTimeslot(t.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
            eventModel.updateEvent(new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getTimeFrom()), toCalendar(t.getTimeEnd()), t));
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated", "Change in Days:" + event.getDayDelta() + ", Change in Minutes:" + event.getMinuteDelta());
        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ScheduleEvent tempEvent = event.getScheduleEvent();
        try{
            Timeslot timeslot = (Timeslot) tempEvent.getData();
            
            sbl.updateTimeslot(timeslot.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
            eventModel.updateEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
        } catch(ClassCastException e){
            GroupTimeslot t = (GroupTimeslot) tempEvent.getData();
            
            sbl.updateGroupTimeslot(t.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
            eventModel.updateEvent(new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getTimeFrom()), toCalendar(t.getTimeEnd()), t));
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public ArrayList<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(ArrayList<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public void handleFileUpload(FileUploadEvent event) {
        
        try {
            //String newFilePath = "\""+"Users"+"\""+ "lin"+"\""+"Downloads"; 
            System.err.println("Schedules.handleFileUpload(): File name: " + event.getFile().getFileName());
            File f = new File("");
            //System.out.println(f.getAbsolutePath());
            uploadedFile = event.getFile();
            
            InputStream in = uploadedFile.getInputstream();
            Path folder = Paths.get(f.getAbsolutePath());
            String filename = FilenameUtils.getBaseName(uploadedFile.getFileName()); 
            String extension = FilenameUtils.getExtension(uploadedFile.getFileName());
            Path file = Files.createTempFile(folder, filename + "-", "." + extension);
            try (InputStream input = uploadedFile.getInputstream()) {
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Uploaded file successfully saved in " + file);
            CalendarBuilder builder = new CalendarBuilder();
            net.fortuna.ical4j.model.Calendar calendar = builder.build(in);
            for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
                Component component = (Component) i.next();
                //new event
                if(component.getProperty("DTSTART")!=null&&component.getProperty("DTEND")!=null&&component.getProperty("SUMMARY")!=null){
                    String start = component.getProperty("DTSTART").getValue();
                    String end = component.getProperty("DTEND").getValue();
                    String summary = component.getProperty("SUMMARY").getValue();
                    String description = component.getProperty("DESCRIPTION").getValue();

                    System.out.println("Information gathered( start: "+start+" end: "+ end + " summary:" + summary+")");

                    DefaultScheduleEvent dsf =  new DefaultScheduleEvent(summary, SDF.parse(start), SDF.parse(end));
                    dsf.setDescription(description);

                    addIcsFile(dsf);
                    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("File Uploaded Successfully"));
                }
                in.close();
            }
            } catch (Exception ex) {
            System.err.println(ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
        
        
    }
    
    public void addIcsFile(DefaultScheduleEvent event){
        
        if(event!=null){
            eventModel.addEvent(event);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String startDate = df.format(event.getStartDate());
            String endDate = df.format(event.getEndDate());
            String title = event.getTitle();
            String description = event.getDescription();
            System.out.println(startDate);
            System.out.println(endDate);
            System.out.println(title);
            System.out.println(description);
            if(description!=null && description.length()>1028)
                description = description.substring(0, 1000);
            if(username!=null&title!=null&&startDate!=null&&endDate!=null&&description!=null)
                sbl.createTimeslot(username, title, startDate, endDate, description, "");
            if(username!=null&title!=null&&startDate!=null&&endDate!=null)
                sbl.createTimeslot(username, title, startDate, endDate, "", "");
        }else{
            System.out.println("event is null");
        }
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

    public List<GroupTimeslot> getGroupTimeslots() {
        return groupTimeslots;
    }

    public void setGroupTimeslots(List<GroupTimeslot> groupTimeslots) {
        this.groupTimeslots = groupTimeslots;
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

    public int getGroupOrPersonal() {
        return groupOrPersonal;
    }

    public void setGroupOrPersonal(int groupOrPersonal) {
        this.groupOrPersonal = groupOrPersonal;
    }
    

}
