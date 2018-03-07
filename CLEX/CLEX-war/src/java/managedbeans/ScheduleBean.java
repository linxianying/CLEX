/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Student;
import entity.Timeslot;
import entity.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.EJB;
import session.ClexSessionBeanLocal;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.UploadedFile;
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

    private User userEntity;
    private String username;
    private String userType;
    private String details;
    private String venue;
    private ArrayList<Timeslot> timeslots;
    private ScheduleModel eventModel = new DefaultScheduleModel();
    private ScheduleEvent event = new DefaultScheduleEvent();

    FacesContext context;
    HttpSession session;

    public ScheduleBean() {
    }

    @PostConstruct
    public void init() {
        userEntity = null;
        eventModel = new DefaultScheduleModel();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        username = userEntity.getUsername();
        userEntity = csbl.findUser(username);
        timeslots = sbl.getAllTimeslots(userEntity);
        Timeslot t;
        for (Timeslot timeslot : timeslots) {
            t = timeslot;
            eventModel.addEvent(new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getStartDate()), toCalendar(t.getEndDate()), t));
        }
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
        if (event.getId() == null) {
            Timeslot timeslot = sbl.createTimeslot(username, event.getTitle(), df.format(event.getStartDate()), df.format(event.getEndDate()), details, venue);
            eventModel.addEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
        } else {
            Timeslot timeslot = (Timeslot) event.getData();
            sbl.updateTimeslot(timeslot.getId(), event.getTitle(), df.format(event.getStartDate()), df.format(event.getEndDate()), details, venue);
            eventModel.updateEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
        }
        event = new DefaultScheduleEvent();
    }

    public void deleteEvent(ActionEvent actionEvent) {
        Timeslot timeslot = (Timeslot) event.getData();
        sbl.deleteTimeslot(timeslot.getId(), userEntity);
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
        Timeslot timeslot = (Timeslot) tempEvent.getData();
        sbl.updateTimeslot(timeslot.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
        eventModel.updateEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated", "Change in Days:" + event.getDayDelta() + ", Change in Minutes:" + event.getMinuteDelta());
        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ScheduleEvent tempEvent = event.getScheduleEvent();
        Timeslot timeslot = (Timeslot) tempEvent.getData();
        sbl.updateTimeslot(timeslot.getId(), tempEvent.getTitle(), df.format(tempEvent.getStartDate()), df.format(tempEvent.getEndDate()), details, venue);
        eventModel.updateEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(timeslot.getStartDate()), toCalendar(timeslot.getEndDate()), timeslot));
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
            String newFilePath = "\\Users\\lin\\Downloads";
            System.err.println("Schedules.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("ScheduleshandleFileUpload(): newFilePath: " + newFilePath);

            UploadedFile uploadedFile = event.getFile();
            String fileName = uploadedFile.getFileName();
            String contentType = uploadedFile.getContentType();
            byte[] contents = uploadedFile.getContents();

            File file = new File(newFilePath);
                    
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }
                
                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void addIcsFile(){
        DefaultScheduleEvent event = sbl.loadIcsFile("");
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
            if(username!=null&title!=null&&startDate!=null&&endDate!=null&&description!=null)
                sbl.createTimeslot(username, title, startDate, endDate, description, "");
            if(username!=null&title!=null&&startDate!=null&&endDate!=null)
                sbl.createTimeslot(username, title, startDate, endDate, "", "");
        }else{
            System.out.println("event is null");
        }
    }

}
