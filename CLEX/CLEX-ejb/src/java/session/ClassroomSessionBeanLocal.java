/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Answer;
import entity.Course;
import entity.Lecturer;
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
    public void endPoll(Poll poll);
    
    public Module findModule(String moduleCode, String takenYear, String takenSem);
    
    public Poll createPoll(String moduleCode, String takenYear, String takenSem, 
            String datetime, String topic, double correctRate, String type, String content);
    
    public Poll createUnfinishedPoll(String moduleCode, String takenYear, String takenSem, 
            String datetime, String topic, double correctRate, String type, String content, 
            ArrayList<Answer> ans, int correctAns);
    
    public void updatePoll(Module module, Long id, String datetime, String topic, 
            double correctRate, String type, String content);
    
    public Course findCourse(String moduleCode);
    
    public Poll findPoll(Long id);
    
    public void updatePoll(Poll p, int correct, int total);
    
    public boolean removePoll(String moduleCode, String takenYear, String takenSem, Long id);
    
    public ArrayList<Poll> testViewPolls();
    
    public ArrayList<Module> viewModules(Lecturer lecturer);
    
    public ArrayList<Poll> viewPolls(Module module);
    
    //public ArrayList<Poll> viewAllPolls(Lecturer lecturer);
    
    public ArrayList<Poll> viewPolls(Lecturer lecturer);
    
    public double getCorrectRateByTopic(ArrayList<Poll> polls, String topic);
    
    public double getCorrectRateByType(ArrayList<Poll> polls, String type);

    public Answer createAnswer(String answer);
    
}
