/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Module;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author eeren
 */
@Local
public interface CourseMgmtBeanLocal {
    public void createCourse(String moduleCode, String moduleName, String moduleInfo , boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload);
    public void createModule(String takenYear, String takenSem, String prerequisite, String preclusions, String moduleCode);
   // public void createLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode);
    
    public List getAllCourses();
    public List getAllModules();
   // public List getAllLessons();
    /*
    public boolean editCourse();
    public boolean editModule();
    public boolean editLesson();
    */
    
    public boolean checkNewCourse(String moduleCode);
    public boolean checkNewModule(String moduleCode, String takenYear, String takenSem);
    public boolean checkExistingModule(String moduleCode, String takenYear, String takenSem);
    
    public boolean linkLecturerToModule(String moduleCode, String takenYear, String takenSem, String username);
}
