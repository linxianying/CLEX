/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Student;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface StudyPlanSessionBeanLocal {

    public void createStudyPlan();

    public void findStudent(String username);

    public void findCourse(String moduleCode);

    public void addStudyPlan(String username, String moduleCode, String pickYear, String pickSem);

    public boolean findStudyPlan(String username, String moduleCode);

    public void updateStudyPlan(String username, String moduleCode, String pickYear, String pickSem);

    public void changeStudyPlan();

    public void removeStudyPlan(String username, String moduleCode);
    
    public void capCalculator(String username);

    
}
