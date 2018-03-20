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
import entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    User userEntity;

    Collection<Module> modules;
    Collection<Lesson> lessons;

    @Override
    public List getAllTimings() {
        List<String> timelist = new ArrayList<String>();
        timelist.add(0, "00:00");
        timelist.add(1, "01:00");
        timelist.add(2, "02:00");
        timelist.add(3, "03:00");
        timelist.add(4, "04:00");
        timelist.add(5, "05:00");
        timelist.add(6, "06:00");
        timelist.add(7, "07:00");
        timelist.add(8, "08:00");
        timelist.add(9, "09:00");
        timelist.add(10, "10:00");
        timelist.add(11, "11:00");
        timelist.add(12, "12:00");
        timelist.add(13, "13:00");
        timelist.add(14, "14:00");
        timelist.add(15, "15:00");
        timelist.add(16, "16:00");
        timelist.add(17, "17:00");
        timelist.add(18, "18:00");
        timelist.add(19, "19:00");
        timelist.add(20, "20:00");
        timelist.add(21, "21:00");
        timelist.add(22, "22:00");
        timelist.add(23, "23:00");
        return timelist;
    }

    @Override
    public List getYearList() {
        List<String> yearlist = new ArrayList<String>();
        int x = 2016;
        for (int i = 0; i < 84; i++) {
            x++;
            String year = Integer.toString(x);
            yearlist.add(i, year);
        }

        return yearlist;
    }

    @Override
    public List getAllSchools() {
        List<String> schoollist = new ArrayList<String>();
        schoollist.add(0, "NUS");
        schoollist.add(1, "NTU");
        schoollist.add(2, "SMU");
        schoollist.add(3, "SUTD");
        schoollist.add(4, "SUSS");
        schoollist.add(5, "SIT");
        schoollist.add(6, "NP");
        schoollist.add(7, "NYP");
        schoollist.add(8, "SP");
        schoollist.add(9, "RP");
        schoollist.add(10, "TP");
        return schoollist;
    }

    @Override
    public List getAllModularCredits() {
        List<String> creditList = new ArrayList<String>();
        creditList.add(0, "0");
        creditList.add(1, "1");
        creditList.add(2, "2");
        creditList.add(3, "3");
        creditList.add(4, "4");
        creditList.add(5, "5");
        creditList.add(6, "6");
        creditList.add(7, "7");
        creditList.add(8, "8");
        creditList.add(9, "9");
        creditList.add(10, "10");
        creditList.add(11, "11");
        creditList.add(12, "12");
        creditList.add(13, "13");
        creditList.add(14, "14");
        creditList.add(15, "15");
        creditList.add(16, "16");
        creditList.add(17, "17");
        creditList.add(18, "18");
        creditList.add(19, "19");
        creditList.add(20, "20");
        return creditList;
    }

    @Override
    public List getAllDays() {
        List<String> daylist = new ArrayList<String>();
        daylist.add(0, "Monday");
        daylist.add(1, "Tuesday");
        daylist.add(2, "Wednesday");
        daylist.add(3, "Thursday");
        daylist.add(4, "Friday");
        daylist.add(5, "Saturday");
        daylist.add(6, "Sunday");
        return daylist;
    }

    @Override
    public Collection<User> getLecturerName() {
        List<User> users = new ArrayList<User>();
        Query q = em.createQuery("Select u FROM BasicUser u");
        for (Object o : q.getResultList()) {
            userEntity = (User) o;
            if (userEntity.getUserType().equals("Lecturer")) {
                users.add(userEntity);
            }
        }
        return users;
    }

    @Override
    public void createCourse(String moduleCode, String moduleName, String moduleInfo, boolean discontinuedBool,
            String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload) {
        Course course = new Course();
        course.createCourse(moduleCode.toUpperCase(), moduleName, moduleInfo, discontinuedBool, discountinuedYear, discountinuedSem,
                offeredSem, school.toUpperCase(), moduleCredit, workload);
        em.persist(course);
        em.flush();
    }

    @Override
    public void createModule(String takenYear, String takenSem, String prerequisite, String preclusions, String moduleCode) {
        Module module = new Module();
        courseEntity = findCourse(moduleCode.toUpperCase());
        module.createModule(takenYear, takenSem, prerequisite, preclusions, courseEntity);
        courseEntity.getModules().add(module);
        em.merge(courseEntity);
        em.persist(module);
        em.flush();
    }

    @Override
    public void createLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode, String takenYear, String takenSem) {
        Lesson lesson = new Lesson();
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lesson.createLesson(day, timeFrom, timeEnd, type, venue, moduleEntity);
        moduleEntity.getLessons().add(lesson);
        em.merge(moduleEntity);
        em.persist(lesson);
        em.flush();
    }

    @Override
    public void editCourse(String moduleCode, String moduleName, String moduleInfo, boolean discontinuedBool,
            String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload) {
        courseEntity = findCourse(moduleCode.toUpperCase());

        courseEntity.setModuleName(moduleName);
        courseEntity.setModuleInfo(moduleInfo);
        courseEntity.setDiscontinuedBool(discontinuedBool);
        courseEntity.setDiscountinuedYear(discountinuedYear);
        courseEntity.setDiscountinuedSem(discountinuedSem);
        courseEntity.setOfferedSem(offeredSem);
        courseEntity.setSchool(school.toUpperCase());
        courseEntity.setModularCredits(moduleCredit);
        courseEntity.setWorkload(workload);

        em.merge(courseEntity);
        em.flush();
    }

    @Override
    public void editModule(String takenYear, String takenSem, String prerequisite, String preclusions, String moduleCode) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);

        moduleEntity.setPrerequisite(prerequisite);
        moduleEntity.setPreclusions(preclusions);

        em.merge(moduleEntity);
        em.flush();
    }

    @Override
    public void editLesson(String day, String timeFrom, String timeEnd, String type, String venue, String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lessonEntity = findLesson(moduleEntity, day, timeFrom, timeEnd);

        lessonEntity.setType(type);
        lessonEntity.setVenue(venue);

        em.merge(lessonEntity);
        em.flush();
    }

    @Override
    public boolean deleteCourse(String moduleCode) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        if (!courseEntity.getModules().isEmpty()) {
            return false;
        }
        em.remove(courseEntity);
        em.flush();
        return true;
    }

    @Override
    public boolean deleteModule(String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        modules = courseEntity.getModules();

        if (!moduleEntity.getLessons().isEmpty() || !moduleEntity.getLecturers().isEmpty()) {
            return false;
        }

        if (modules.remove(moduleEntity) == true) {
            em.merge(courseEntity);
            em.remove(moduleEntity);
            em.flush();
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteLesson(String day, String timeFrom, String timeEnd, String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lessonEntity = findLesson(moduleEntity, day, timeFrom, timeEnd);
        lessons = moduleEntity.getLessons();

        if (lessons.remove(lessonEntity) == true) {
            em.merge(moduleEntity);
            em.remove(lessonEntity);
            em.flush();
            return true;
        }

        return false;
    }

    @Override
    public List getAllCourses() {
        List<Course> courses = new ArrayList<Course>();
        Query q = em.createQuery("Select c FROM Course c");
        for (Object o : q.getResultList()) {
            courseEntity = (Course) o;
            courses.add(courseEntity);
        }
        courses = sortCourseByModuleCode(courses);
        return courses;
    }

    @Override
    public List getAllModules() {
        List<Module> modules = new ArrayList<Module>();
        Query q = em.createQuery("Select m FROM SchoolModule m");
        for (Object o : q.getResultList()) {
            moduleEntity = (Module) o;
            modules.add(moduleEntity);
        }
        modules = sortModuleByModuleCode(modules);
        return modules;
    }

    @Override
    public List getAllLessons() {
        List<Lesson> lessons = new ArrayList<Lesson>();
        Query q = em.createQuery("Select l FROM Lesson l");
        for (Object o : q.getResultList()) {
            lessonEntity = (Lesson) o;
            lessons.add(lessonEntity);
        }
        return lessons;
    }

    @Override
    public boolean checkNewCourse(String moduleCode) {
        if (findCourse(moduleCode.toUpperCase()) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkExistingCourse(String moduleCode) {
        if (findCourse(moduleCode.toUpperCase()) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkNewModule(String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        if (courseEntity != null) {
            if (findModule(courseEntity, takenYear, takenSem) == null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean checkExistingModule(String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        if (courseEntity != null) {
            if (findModule(courseEntity, takenYear, takenSem) != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean checkNewLesson(String moduleCode, String takenYear, String takenSem, String day, String timeFrom, String timeEnd) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        if (courseEntity != null) {
            moduleEntity = findModule(courseEntity, takenYear, takenSem);
            if (moduleEntity != null) {
                if (findLesson(moduleEntity, day, timeFrom, timeEnd) == null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean checkExistingLesson(String moduleCode, String takenYear, String takenSem, String day, String timeFrom, String timeEnd) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        if (courseEntity != null) {
            moduleEntity = findModule(courseEntity, takenYear, takenSem);
            if (moduleEntity != null) {
                if (findLesson(moduleEntity, day, timeFrom, timeEnd) != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean linkLecturerToModule(String moduleCode, String takenYear, String takenSem, String username) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lecturerEntity = findLecturer(username);

        if (lecturerEntity == null) {
            return false;
        }

        if (lecturerEntity.getModules().contains(moduleEntity)) {
            return false;
        }

        moduleEntity.getLecturers().add(lecturerEntity);
        lecturerEntity.getModules().add(moduleEntity);
        em.merge(moduleEntity);
        em.merge(lecturerEntity);
        em.flush();
        return true;
    }

    @Override
    public boolean removeLecturerFromModule(String moduleCode, String takenYear, String takenSem, String username) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lecturerEntity = findLecturer(username);

        if (lecturerEntity == null) {
            return false;
        }

        if (!lecturerEntity.getModules().contains(moduleEntity)) {
            return false;
        }

        moduleEntity.getLecturers().remove(lecturerEntity);
        lecturerEntity.getModules().remove(moduleEntity);
        em.merge(moduleEntity);
        em.merge(lecturerEntity);
        em.flush();
        return true;
    }

    @Override
    public boolean checkLectTeachModule(String username, String moduleCode, String takenYear, String takenSem) {
        lecturerEntity = findLecturer(username);
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);

        if (lecturerEntity == null) {
            return false;
        }

        if (lecturerEntity.getModules().contains(moduleEntity)) {
            return true;
        }

        return false;
    }

    public Collection<Module> getModulesFromCourse(String moduleCode) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        modules = courseEntity.getModules();
        return modules;
    }

    @Override
    public Collection<Module> getModulesFromLecturer(String username) {
        lecturerEntity = findLecturer(username);
        modules = lecturerEntity.getModules();
        return modules;
    }

    @Override
    public Collection<Lesson> getLessonsFromLecturer(String username) {
        lecturerEntity = findLecturer(username);
        List<Module> moduleList = (List) lecturerEntity.getModules();
        List<Lesson> lessonList = new ArrayList<Lesson>();
        Collection<Lesson> lectLessons = new ArrayList<Lesson>();

        for (int i = 0; i < modules.size(); i++) {
            lessonList = (List) moduleList.get(i).getLessons();
            for (int j = 0; j < lessonList.size(); j++) {
                lessonEntity = lessonList.get(j);
                lectLessons.add(lessonEntity);
            }
        }
        return lectLessons;
    }

    public Collection<Lesson> getLessonsFromModule(String moduleCode, String takenYear, String takenSem) {
        courseEntity = findCourse(moduleCode.toUpperCase());
        moduleEntity = findModule(courseEntity, takenYear, takenSem);
        lessons = moduleEntity.getLessons();
        return lessons;
    }

    @Override
    public Course findCourse(String moduleCode) {
        courseEntity = null;
        try {
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode.toUpperCase());
            courseEntity = (Course) q.getSingleResult();
            System.out.println("Course " + moduleCode.toUpperCase() + " found.");
        } catch (NoResultException e) {
            System.out.println("Course " + moduleCode.toUpperCase() + " does not exist.");
            courseEntity = null;
        }
        return courseEntity;
    }

    @Override
    public Module findModule(Course course, String takenYear, String takenSem) {
        moduleEntity = null;
        try {
            Query q = em.createQuery("SELECT m FROM SchoolModule m WHERE m.takenYear=:takenYear AND m.takenSem=:takenSem AND m.course.id=:courseid");
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            q.setParameter("courseid", course.getId());
            moduleEntity = (Module) q.getSingleResult();
            System.out.println("Module found.");
        } catch (NoResultException e) {
            System.out.println("Module does not exist.");
            moduleEntity = null;
        }
        return moduleEntity;
    }

    @Override
    public Lesson findLesson(Module module, String day, String timeFrom, String timeEnd) {
        lessonEntity = null;
        try {
            Query q = em.createQuery("SELECT l FROM Lesson l WHERE l.day=:day AND l.timeFrom=:timeFrom AND l.timeEnd=:timeEnd AND l.module.id=:moduleid");
            q.setParameter("day", day);
            q.setParameter("timeFrom", timeFrom);
            q.setParameter("timeEnd", timeEnd);
            q.setParameter("moduleid", module.getId());
            lessonEntity = (Lesson) q.getSingleResult();
            System.out.println("Lesson found.");
        } catch (NoResultException e) {
            System.out.println("Lesson does not exist.");
            lessonEntity = null;
        }
        return lessonEntity;
    }

    public Lecturer findLecturer(String username) {
        Lecturer lecturerEntity = null;
        try {
            Query q = em.createQuery("SELECT l FROM Lecturer l WHERE l.username=:username");
            q.setParameter("username", username);
            lecturerEntity = (Lecturer) q.getSingleResult();
            System.out.println("Lecturer " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("Lecturer " + username + " does not exist.");
            lecturerEntity = null;
        }
        return lecturerEntity;
    }

    public List<Course> sortCourseByModuleCode(List<Course> courseList) {
        Collections.sort(courseList, new Comparator<Course>() {
            public int compare(Course c1, Course c2) {
                return c1.getModuleCode().compareTo(c2.getModuleCode());
            }
        });
        return courseList;
    }

    public List<Module> sortModuleByModuleCode(List<Module> moduleList) {
        Collections.sort(moduleList, new Comparator<Module>() {
            public int compare(Module m1, Module m2) {
                return m1.getCourse().getModuleCode().compareTo(m2.getCourse().getModuleCode());
            }
        });
        return moduleList;
    }

    @Override
    public List getCoursesFromSchool(String school) {
        System.out.println("getCourses retrieving from " + school);
        List<Course> courseList = new ArrayList<Course>();
        List<Course> courses = getAllCourses();
        for (int i = 0; i < courses.size(); i++) {
            courseEntity = courses.get(i);
            if (courseEntity.getSchool().equals(school)) {
                courseList.add(courseEntity);
            }
        }
        courseList = sortCourseByModuleCode(courseList);
        return courseList;
    }

    @Override
    public List getModulesFromSchool(String school) {
        System.out.println("getModules retrieving from " + school);
        List<Module> moduleList = new ArrayList<Module>();
        List<Module> modules = getAllModules();
        for (int i = 0; i < modules.size(); i++) {
            moduleEntity = modules.get(i);
            if (moduleEntity.getCourse().getSchool().equals(school)) {
                moduleList.add(moduleEntity);
            }
        }
        moduleList = sortModuleByModuleCode(moduleList);
        return moduleList;
    }
}
