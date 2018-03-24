/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.GroupTimeslot;
import entity.ProjectGroup;
import entity.Student;
import entity.Timeslot;
import entity.User;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Local;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author lin
 */
@Local
public interface ScheduleSessionBeanLocal {

    public DefaultScheduleEvent loadIcsFile(String path); 
    
    void updateTimeslot(Long id, String title, String startDate, String endDate, String details, String venue);
    void updateGroupTimeslot(Long id, String title, String startDate, String endDate, String details, String venue);

    public Timeslot createTimeslot(String username, String title, String startDate, 
            String endDate, String details, String venue);
    public Timeslot findTimeslot(Long id);
    public void deleteTimeslot(Long id);
    public void deleteTimeslot(Long id, User user);
    public void deleteGroupTimeslot(Long id, Student student);
    
    public ArrayList<Timeslot> getAllTimeslots(User userentitity);
    
    public void createGroupTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup pojectGroup);
    void updateGroupTimeslot(Long id, String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup pojectGroup);
    GroupTimeslot findGroupTimeslot(Long id);
}
