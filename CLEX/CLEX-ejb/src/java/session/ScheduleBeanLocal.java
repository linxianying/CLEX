/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Timeslot;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ScheduleBeanLocal {

    boolean loadIcsFile();
    void createTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue);
    void updateTimeslot(Long id, String date, String timeFrom, String timeEnd, 
                String title, String details, String venue);

    public Timeslot findTimeslot(Long id);
    public void deleteTimeslot(Long id);
}
