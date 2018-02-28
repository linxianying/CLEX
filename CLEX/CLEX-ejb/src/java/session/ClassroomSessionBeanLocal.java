/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Module;
import entity.Poll;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ClassroomSessionBeanLocal {
    public Module findModule(String moduleCode, String takenYear, String takenSem);
    public Poll createPoll(String moduleCode, String takenYear, String takenSem, 
            String datetime, String topic, double correctRate, String type, String content);
    public void updatePoll(Module module, Long id, String datetime, String topic, 
            double correctRate, String type, String content);
    public Course findCourse(String moduleCode);
    public Poll findPoll(Long id);
    public boolean removePoll(String moduleCode, String takenYear, String takenSem, Long id);
    public ArrayList<Poll> testViewPolls();
}
