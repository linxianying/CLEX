/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Module;
import java.util.Collection;
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
    public void createLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode, String takenYear, String takenSem);
    
    public List getAllCourses();
    public List getAllModules();
    public List getAllLessons();
    
    public void editCourse(String moduleCode, String moduleName, String moduleInfo , boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload);
    public void editModule(String takenYear, String takenSem, String prerequisite, String preclusions, String moduleCode);
    public void editLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode, String takenYear, String takenSem);
    
    public boolean deleteCourse(String moduleCode);
    public boolean deleteModule(String moduleCode, String takenYear, String takenSem);
    public boolean deleteLesson(String day, String timeFrom, String timeEnd, String moduleCode, String takenYear, String takenSem);
    
    public boolean checkNewCourse(String moduleCode);
    public boolean checkExistingCourse(String moduleCode);
    public boolean checkNewModule(String moduleCode, String takenYear, String takenSem);
    public boolean checkExistingModule(String moduleCode, String takenYear, String takenSem);
    public boolean checkNewLesson(String moduleCode, String takenYear, String takenSem, String day, String timeFrom, String timeEnd);
    public boolean checkExistingLesson(String moduleCode, String takenYear, String takenSem, String day, String timeFrom, String timeEnd);
    
    public boolean linkLecturerToModule(String moduleCode, String takenYear, String takenSem, String username);
    
    public Collection<Module> getModulesFromLecturer(String username);
}
