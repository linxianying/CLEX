/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.GroupTimeslot;
import entity.ProjectGroup;
import entity.Timeslot;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ScheduleSessionBeanLocal {

    boolean loadIcsFile();
    Timeslot createTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue);
    void updateTimeslot(Long id, String date, String timeFrom, String timeEnd, 
                String title, String details, String venue);

    public Timeslot findTimeslot(Long id);
    public void deleteTimeslot(Long id);
    
    public void createGroupTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup pojectGroup);
    void updateGroupTimeslot(Long id, String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup pojectGroup);
    GroupTimeslot findGroupTimeslot(Long id);
}
