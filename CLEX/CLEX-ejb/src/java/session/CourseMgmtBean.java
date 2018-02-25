/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Lecturer;
import entity.Lesson;
import entity.Module;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eeren
 */
@Stateless
public class CourseMgmtBean implements CourseMgmtBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    Course courseEntity;
    Module moduleEntity;
    Lesson lessonEntity;
    Lecturer lecturerEntity;
    
    Collection<Module> modules;
    Collection<Lesson> lessons;
    
    @Override
    public void createCourse(String moduleCode, String moduleName, String moduleInfo , boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload) {
        Course course = new Course();
        course.createCourse(moduleCode, moduleName, moduleInfo, discontinuedBool, discountinuedYear, discountinuedSem, 
                offeredSem, school,moduleCredit, workload);
        em.persist(course);
        em.flush();
    }
    
    @Override
    public void createModule(String takenYear, String takenSem, String prerequisite, String preclusions, String moduleCode) {
        Module module = new Module();
        courseEntity = findCourse(moduleCode);
        module.createModule(takenYear, takenSem, prerequisite, preclusions, courseEntity);
        courseEntity.getModules().add(module);
        em.merge(courseEntity);
        em.persist(module);
        em.flush();
    }
    
    @Override
    public void createLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode, String takenYear, String takenSem) {
        Lesson lesson = new Lesson();
        courseEntity = findCourse(moduleCode);
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lesson.createLesson(day, timeFrom, timeEnd, type, venue, moduleEntity);
        moduleEntity.getLessons().add(lesson);
        em.merge(moduleEntity);
        em.persist(lesson);
        em.flush();
    }
    
    @Override
    public void editCourse(String moduleCode, String moduleName, String moduleInfo, boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload){
        courseEntity = findCourse(moduleCode);
        
        courseEntity.setModuleName(moduleName);
        courseEntity.setDiscontinuedBool(discontinuedBool);
        courseEntity.setDiscountinuedYear(discountinuedYear);
        courseEntity.setDiscountinuedSem(discountinuedSem);
        courseEntity.setOfferedSem(offeredSem);
        courseEntity.setSchool(school);
        courseEntity.setModularCredits(moduleCredit);
        courseEntity.setWorkload(workload);
        
        em.merge(courseEntity);
        em.flush();
    }
    
    @Override
    public void editModule(String takenYear, String takenSem, String prerequisite, String preclusions, String moduleCode){
        courseEntity = findCourse(moduleCode);
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        
        moduleEntity.setPrerequisite(prerequisite);
        moduleEntity.setPreclusions(preclusions);

        em.merge(moduleEntity);
        em.flush();
    }
    
    @Override
    public void editLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode, String takenYear, String takenSem){
        courseEntity = findCourse(moduleCode);
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lessonEntity = findLesson(moduleEntity, day, timeFrom, timeEnd);
        
        lessonEntity.setType(type);
        lessonEntity.setVenue(venue);
        
        em.merge(lessonEntity);
        em.flush();
    }
    
    @Override
    public boolean deleteCourse(String moduleCode){
        courseEntity = findCourse(moduleCode);
        if(!courseEntity.getModules().isEmpty()){
            return false;
        }
        em.remove(courseEntity);
        em.flush();
        return true;
    }
    
    /*public boolean deleteModule(){
    }*/
    
    @Override
    public boolean deleteLesson(String day, String timeFrom, String timeEnd, String moduleCode, String takenYear, String takenSem){
        courseEntity = findCourse(moduleCode);
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lessonEntity = findLesson(moduleEntity, day, timeFrom, timeEnd);
        lessons = moduleEntity.getLessons();
    
        if(lessons.remove(lessonEntity) == true){
            em.merge(moduleEntity);
            em.remove(lessonEntity);
            em.flush();
            return true;
        }

        return false;
    }
    
    @Override
    public List getAllCourses(){
        List<Course> courses = new ArrayList<Course>();
        Query q = em.createQuery("Select c FROM Course c");
        for(Object o: q.getResultList()){
            courseEntity = (Course) o;
            courses.add(courseEntity);
        }
        return courses;
    }
    
    @Override
    public List getAllModules(){
        List<Module> modules = new ArrayList<Module>();
        Query q = em.createQuery("Select m FROM SchoolModule m");
        for(Object o: q.getResultList()){
            moduleEntity = (Module) o;
            modules.add(moduleEntity);
        }
        return modules;
    }
     
    @Override
    public List getAllLessons(){
        List<Lesson> lessons = new ArrayList<Lesson>();
        Query q = em.createQuery("Select l FROM Lesson l");
        for(Object o: q.getResultList()){
            lessonEntity = (Lesson) o;
           lessons.add(lessonEntity);
        }
        return lessons;
    }
    
    @Override
    public boolean checkNewCourse(String moduleCode) {
        if(findCourse(moduleCode) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkExistingCourse(String moduleCode) {
        if(findCourse(moduleCode) != null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewModule(String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode);
        if(courseEntity != null){
            if(findModule(courseEntity, takenYear, takenSem) == null){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean checkExistingModule(String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode);
        if(courseEntity != null){
            if(findModule(courseEntity, takenYear, takenSem) != null){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean checkNewLesson(String moduleCode, String takenYear, String takenSem, String day, String timeFrom, String timeEnd){
        courseEntity = findCourse(moduleCode);
        if(courseEntity != null){
            moduleEntity = findModule(courseEntity, takenYear, takenSem);
            if(moduleEntity != null){
                if(findLesson(moduleEntity, day, timeFrom, timeEnd) == null){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean checkExistingLesson(String moduleCode, String takenYear, String takenSem, String day, String timeFrom, String timeEnd){
        courseEntity = findCourse(moduleCode);
        if(courseEntity != null){
            moduleEntity = findModule(courseEntity, takenYear, takenSem);
            if(moduleEntity != null){
                if(findLesson(moduleEntity, day, timeFrom, timeEnd) != null){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return false;
    }    
    
    @Override
    public boolean linkLecturerToModule(String moduleCode, String takenYear, String takenSem, String username){
        courseEntity = findCourse(moduleCode);
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lecturerEntity = findLecturer(username);
        
        if(lecturerEntity == null){
            return false;
        }
        
        moduleEntity.getLecturers().add(lecturerEntity);
        lecturerEntity.getModules().add(moduleEntity);
        em.merge(moduleEntity);
        em.merge(lecturerEntity);       
        em.flush();
        return true;
    }
    
    public Collection<Module> getModulesFromCourse(String moduleCode){
        courseEntity = findCourse(moduleCode);
        modules = courseEntity.getModules();
        return modules;
    }
    
    public Collection<Lesson> getLessonsFromModule(String moduleCode, String takenYear, String takenSem){
        courseEntity = findCourse(moduleCode);
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lessons = moduleEntity.getLessons();
        return lessons;
    }
   
    public Course findCourse(String moduleCode){
        courseEntity = null;
        try{
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            courseEntity = (Course) q.getSingleResult();
            System.out.println("Course " + moduleCode + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            courseEntity = null;
        }
        return courseEntity;
    }
    
    public Module findModule(Course course, String takenYear, String takenSem){
        moduleEntity = null;
        try{
            Query q = em.createQuery("SELECT m FROM SchoolModule m WHERE m.takenYear=:takenYear AND m.takenSem=:takenSem AND m.course.id=:courseid");
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            q.setParameter("courseid", course.getId());
            moduleEntity = (Module) q.getSingleResult();
            System.out.println("Module found.");
        }
        catch(NoResultException e){
            System.out.println("Module does not exist.");
            moduleEntity = null;
        }
        return moduleEntity;
    }
    
    public Lesson findLesson(Module module, String day, String timeFrom, String timeEnd){
        lessonEntity = null;
        try{
            Query q = em.createQuery("SELECT l FROM Lesson l WHERE l.day=:day AND l.timeFrom=:timeFrom AND l.timeEnd=:timeEnd AND l.module.id=:moduleid");
            q.setParameter("day", day);
            q.setParameter("timeFrom", timeFrom);
            q.setParameter("timeEnd", timeEnd);
            q.setParameter("moduleid", module.getId());
            lessonEntity = (Lesson) q.getSingleResult();
            System.out.println("Lesson found.");
        }
        catch(NoResultException e){
            System.out.println("Lesson does not exist.");
            lessonEntity = null;
        }
        return lessonEntity;
    }
    
    public Lecturer findLecturer(String username){
        Lecturer lecturerEntity = null;
        try{
            Query q = em.createQuery("SELECT l FROM Lecturer l WHERE l.username=:username");
            q.setParameter("username", username);
            lecturerEntity = (Lecturer) q.getSingleResult();
            System.out.println("Lecturer " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Lecturer " + username + " does not exist.");
            lecturerEntity = null;
        }
        return lecturerEntity;
    }
    
}
