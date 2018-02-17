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

    void createStudyPlan();

    void findStudent(String username);

    void findCourse(String moduleCode);

    void addStudyPlan(String username, String moduleCode, String pickYear, String pickSem);
    
}
