/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

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
import java.sql.Timestamp;
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
import org.primefaces.event.FileUploadEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
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

    public ScheduleBean() {
    }

    public void testAddEvents() {
        System.out.println("ScheduleBean: testAddEvents");
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d1 = df.parse("2018-02-27 01:10");
            Date d2 = df.parse("2018-02-27 12:00");
            Timestamp d3 = new Timestamp(d1.getTime());
            Timestamp d4 = new Timestamp(d1.getTime());
            Timeslot timeslot = sbl.createTimeslot("Event test", d3, d4, "some minor detail", "place");
            System.out.print("timeslot create: " + timeslot.getId() + " " + d1 + "///" + d2);

            eventModel.addEvent(new DefaultScheduleEvent(timeslot.getTitle(), toCalendar(d1), toCalendar(d2)));

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        /*this.username = "namename";
        userEntity = csbl.findUser(username);
        timeslots = sbl.getAllTimeslots(userEntity);
        Timeslot t;
        System.out.println("---------------------------------------------managed bean test" + userEntity.getUsername());
        System.out.println("---------------------------------------------managed bean test" + timeslots.get(0).getTitle());
        for (Timeslot timeslot : timeslots) {
            t = timeslot;
            System.out.println("-----------------------------------------managed bean test" + t.getStartDate());
            System.out.println("-----------------------------------------managed bean test" + t.getEndDate());
            eventModel.addEvent(new DefaultScheduleEvent(t.getTitle(), toCalendar(t.getStartDate()), toCalendar(t.getEndDate())));
        }*/
    }

    public static Date toCalendar(Date date) {
        Calendar t = Calendar.getInstance();
        t.set(Calendar.DATE, t.get(Calendar.DATE));
        t.set(Calendar.HOUR_OF_DAY, 8);
        t.set(Calendar.MINUTE, 1);
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
        if (event.getId() == null) {
            eventModel.addEvent(event);
            sbl.createTimeslot(event.getTitle(), event.getStartDate(), event.getEndDate(), details, venue);
        } else {
            eventModel.updateEvent(event);
            sbl.updateTimeslot(Long.parseLong(event.getId()), event.getTitle(), event.getStartDate(), event.getEndDate(), details, venue);
        }

        event = new DefaultScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated", "Change in Days:" + event.getDayDelta() + ", Change in Minutes:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void handleFileUpload(FileUploadEvent event) {

        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("Schedules.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("ScheduleshandleFileUpload(): newFilePath: " + newFilePath);

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

}
