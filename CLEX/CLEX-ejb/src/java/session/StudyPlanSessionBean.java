/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Grade;
import entity.Module;
import entity.Student;
import entity.StudyPlan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author caoyu
 */
@Stateless
public class StudyPlanSessionBean implements StudyPlanSessionBeanLocal {

    @PersistenceContext
    EntityManager em;

    private String pickYear;
    private String pickSem;
    private String username;
    private String moduleCode;
    private Module module;
    private StudyPlan studyPlan;
    private Student student;
    private Course course;

    private int currentYear;
    private int currentSem;
    //number of semesters the student has taken
    private int numOfSemTaken;

    private ArrayList<Course> takenCourses;
    private ArrayList<Module> takenModules;
    private ArrayList<Module> takingModules;
    private ArrayList<ArrayList<Course>> takenCoursesInOrder;
    private ArrayList<StudyPlan> studyPlans;
    private ArrayList<ArrayList<StudyPlan>> studyPlansInOrder;
    private ArrayList<Course> expectedCurrentCourses;
    private ArrayList<Course> expectedStudyPlanCourses;
    private ArrayList<Grade> grades;
    private ArrayList<ArrayList<Grade>> gradesInOrder;
    private double currentCap;
    private double expectedCap;
    private HashMap<String, String> expectedCourseGrade;

    @Override
    public Student findStudent(String username) {
        Student u = new Student();
        u = null;
        try {
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            u = (Student) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Student " + username + " does not exist.");
            u = null;
        }
        this.username = username;
        this.student = u;
        return student;
    }

    @Override
    public Course findCourse(String moduleCode) {
        course = new Course();
        try {
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            course = (Course) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Course " + moduleCode + " does not exist.");
            return null;
        }
        this.moduleCode = moduleCode;
        return course;
    }

    @Override
    public Module findModule(String takenYear, String takenSem, String moduleCode) {
        Module m = new Module();
        m = null;
        try {
            System.out.println("Try to find Module " + moduleCode + ", takenYear=" + takenYear
                    + ", takenSem=" + takenSem);
            Query q = em.createQuery("SELECT m FROM SchoolModule m WHERE "
                    + "m.takenYear=:takenYear AND m.takenSem=:takenSem AND "
                    + "m.course.moduleCode=:moduleCode");
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            q.setParameter("moduleCode", moduleCode);
            m = (Module) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("SP session bean: findModule:Module " + moduleCode + " does not exist.");
            m = null;
            return null;
        }
        return m;
    }

    // find all modules taken by the user
    @Override
    public ArrayList<Module> getTakenModules(String username) {
        Collection<Module> all = new ArrayList<Module>();
        takenModules = new ArrayList<Module>();
        findStudent(username);
        all = this.student.getModules();
        for (Module m : all) {
            takenModules.add(m);
        }
        System.out.println("StudyPlanSessionbean: getTakenModules: student:"
                + username + "'s takenModules:" + takenModules.size());
        return takenModules;
    }

    //set all course taken by the user in order of year and sem
    //Update:instead of all courses, leave out the courses taking this semester
    @Override
    public ArrayList<ArrayList<Course>> getTakenModulesInOrder(String username) {
        takenCoursesInOrder = new ArrayList<ArrayList<Course>>();
        takenModules = this.getTakenModules(username);
        //the iterator of for loop to change to next semster
        int year = Integer.parseInt(this.findStudent(username).getMatricYear());
        int sem = 1;
        int numOfSemTaken = checkNumOfSemTaken(username) - 1;
        //from matric year sem 1, check what are the courses taken for the sem
        //then increase the year/sem to next semster
        for (int i = 0; i < numOfSemTaken; i++) {
//            System.out.println("check for year " + year + ", sem " + sem);
            ArrayList<Course> currentCourses = new ArrayList<Course>();
            //go through all the modules the student takes
            for (int index = 0; index < takenModules.size(); index++) {
                //if the module is taken in "year" "sem", add it to the currentCourses 
                if (Integer.parseInt(takenModules.get(index).getTakenYear()) == year) {
                    if (Integer.parseInt(takenModules.get(index).getTakenSem()) == sem) {
                        currentCourses.add(takenModules.get(index).getCourse());
                    }
                }
            }
            takenCoursesInOrder.add(currentCourses);
            currentCourses = null;
            //increase sem year to next semester
            if (sem == 1) {
                sem = 2;
            } else {
                year++;
                sem = 1;
            }
        }
        return takenCoursesInOrder;
    }

    //check whether all studyPlans have been 
    public boolean checkAllStudyPlansAdded(ArrayList<ArrayList<StudyPlan>> studyPlansInOrder, ArrayList<StudyPlan> all) {
        boolean check = false;
        int size = 0;
        for (ArrayList<StudyPlan> s : studyPlansInOrder) {
            size += s.size();
        }
        if (size == all.size()) {
            check = true;
        }
        return check;
    }

    //find whether this studyplan exits, if not, set this.studyPlan to it
    @Override
    public boolean checkStudyPlan(String username, String moduleCode) {
        this.findStudent(username);
        this.findCourse(moduleCode);
        Long studentId = this.findStudent(username).getId();
        Long courseId = this.findCourse(moduleCode).getId();

        StudyPlan s = new StudyPlan();
        s = null;
        try {
            Query q = em.createQuery("SELECT s FROM StudyPlan s WHERE s.student.id =:studentId AND s.course.id =:courseId");
            q.setParameter("studentId", studentId);
            q.setParameter("courseId", courseId);
            s = (StudyPlan) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("StudyPlanSessionBean: findStudyPlan method: No result");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.studyPlan = s;
        return true;
    }

    @Override
    public StudyPlan findStudyPlan(String username, String moduleCode) {
        this.findStudent(username);
        this.findCourse(moduleCode);
        Long studentId = this.findStudent(username).getId();
        Long courseId = this.findCourse(moduleCode).getId();

        StudyPlan s = new StudyPlan();
        s = null;
        try {
            Query q = em.createQuery("SELECT s FROM StudyPlan s WHERE s.student.id =:studentId AND s.course.id =:courseId");
            q.setParameter("studentId", studentId);
            q.setParameter("courseId", courseId);
            s = (StudyPlan) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("StudyPlanSessionBean: findStudyPlan method: No result");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.studyPlan = s;
        return s;
    }

    @Override
    public void changeStudyPlan() {
        this.studyPlan.setPickSem(this.pickSem);
        this.studyPlan.setPickYear(this.pickYear);
        em.persist(this.studyPlan);
        em.flush();
    }

    @Override
    public void updateStudyPlan(String pickYear, String pickSem, String moduleCode, String username) {
        //if the studyplan is found
        if (checkStudyPlan(username, moduleCode)) {
            this.pickSem = pickSem;
            this.pickYear = pickYear;
            this.changeStudyPlan();
        } //if the studyPlan is not in DB, create one
        else {
            createStudyPlan(pickYear, pickSem, moduleCode, this.findStudent(username));
        }
    }


    private double capCulculator(Student student) {
        double cap = 0.0;
        int num = 0;
        Collection<Grade> grades = student.getGrades();
        for (Grade grade : grades) {
            String tempGrade = grade.getModuleGrade();
            if (tempGrade.equals("A") || tempGrade.equals("A+")) {
                cap = cap + 5;
                num++;
            } else if (tempGrade.equals("A-")) {
                cap = cap + 4.5;
                num++;
            } else if (tempGrade.equals("B+")) {
                cap = cap + 4;
                num++;
            } else if (tempGrade.equals("B")) {
                cap = cap + 3.5;
                num++;
            } else if (tempGrade.equals("B-")) {
                cap = cap + 3;
                num++;
            } else if (tempGrade.equals("C+")) {
                cap = cap + 2.5;
                num++;
            } else if (tempGrade.equals("C")) {
                cap = cap + 2;
                num++;
            } else if (tempGrade.equals("C-")) {
                cap = cap + 1.5;
                num++;
            } else if (tempGrade.equals("D+")) {
                cap = cap + 1.0;
                num++;
            } else if (tempGrade.equals("D")) {
                cap = cap + 0.5;
                num++;
            } else {
                num++;
            }
        }
        student.setCap((cap / num));
        return (cap / num);
    }

    @Override
    public void capCalculator(String username) {
        findStudent(username);
        double cap = capCulculator(this.student);
        em.persist(this.student);
        em.flush();
    }

    //convert grade (A+ to F) to point
    public double convertGradeToPoint(String moduleGrade) {
        double point = 0.0;
        if (moduleGrade.equals("A") || moduleGrade.equals("A+")) {
            point = 5.0;
        } else if (moduleGrade.equals("A-")) {
            point = 4.5;
        } else if (moduleGrade.equals("B+")) {
            point = 4.0;
        } else if (moduleGrade.equals("B")) {
            point = 3.5;
        } else if (moduleGrade.equals("B-")) {
            point = 3.0;
        } else if (moduleGrade.equals("C+")) {
            point = 2.5;
        } else if (moduleGrade.equals("C")) {
            point = 2.0;
        } else if (moduleGrade.equals("D+")) {
            point = 1.5;
        } else if (moduleGrade.equals("D")) {
            point = 1.0;
        } else if (moduleGrade.equals("F")) {
            point = 0.0;
        }
        return point;
    }

    //update the student's expected cap based on module's credit and garde,
    //type 1: change grade from ABCDF to SU
    @Override
    public double updateExpectedCapOne(int allCredits, double cap, int newModuleCredit, String oldGrade) {
        expectedCap = cap * allCredits - this.convertGradeToPoint(oldGrade) * newModuleCredit;
        expectedCap /= (allCredits - newModuleCredit);
        return expectedCap;
    }

    //calculate the student's expected cap based on new module's credit and garde for first time
    //or type 2: change grade from SU to ABCDF
    @Override
    public double updateExpectedCapTwo(int allCredits, double cap, int newModuleCredit, String newModuleGrade) {
        expectedCap = cap * allCredits + this.convertGradeToPoint(newModuleGrade) * newModuleCredit;
        expectedCap /= (allCredits + newModuleCredit);
        return expectedCap;
    }

    //update the student's expected cap based on module's credit and garde,
    //type 4: change grade from ABCDF to ABCDF
    @Override
    public double updateExpectedCapFour(int allCredits, double cap, int newModuleCredit, String newModuleGrade, String oldGrade) {
        expectedCap = cap * allCredits + (this.convertGradeToPoint(newModuleGrade) - this.convertGradeToPoint(oldGrade)) * newModuleCredit;
        expectedCap /= allCredits;
        return expectedCap;
    }

    @Override
    public void setStudentTakenModules(String username, String moduleCode, String takenYear, String takenSem) {
        student = this.findStudent(username);
        module = this.findModule(takenYear, takenSem, moduleCode);
        student.getModules().add(module);
        module.getStudents().add(student);
        em.persist(module);
        em.persist(student);

        em.flush();
    }

    //check current year nad sem to decide how many semesters the student has taken
    //!!Assume all Students start at sem1
    //eg. For AY2017-2018 sem 2, the current year would be 2018, 
    //but it will be remembered as 2017 sem 2


    @Override
    public HashMap<String, String> getExpectedCourseGrade(String username) {
        expectedCourseGrade = new HashMap<String, String>();
        takingModules = new ArrayList<Module>();
        takingModules = this.getCurrentModules(this.findStudent(username));
        for (Module m : takingModules) {
            expectedCourseGrade.put(m.getCourse().getModuleCode(), "none");
        }
        studyPlans = new ArrayList<StudyPlan>();
        studyPlans = this.getAllStudyPlans(this.findStudent(username));
        for (StudyPlan s : studyPlans) {
            expectedCourseGrade.put(s.getCourse().getModuleCode(), "none");
        }
        return expectedCourseGrade;
    }

    @Override
    public List<Course> getAllCourses() {
        Query q = em.createQuery("SELECT c FROM Course c");
        List<Course> all = q.getResultList();
        all = sortCourseByModuleCode(all);
        return all;
    }

    public List<Course> sortCourseByModuleCode(List<Course> courseList) {
        Collections.sort(courseList, new Comparator<Course>() {
            public int compare(Course c1, Course c2) {
                return c1.getModuleCode().compareTo(c2.getModuleCode());
            }
        });
        return courseList;
    }
    
    @Override
    public Grade findGrade(Long id) {
        Grade g = new Grade();
        g = null;
        try {
            Query q = em.createQuery("SELECT g FROM Grade g WHERE g.id=:id");
            q.setParameter("id", id);
            g = (Grade) q.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return g;
    }
    
    @Override
    public Module findModuleById(Long id) {
        Module m = new Module();
        m = null;
        try {
            Query q = em.createQuery("SELECT m FROM Module m WHERE m.id=:id");
            q.setParameter("id", id);
            m = (Module) q.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return m;
    }
    
    @Override
    public StudyPlan findStudyPlanById(Long id) {
        StudyPlan sp = new StudyPlan();
        sp = null;
        try {
            Query q = em.createQuery("SELECT sp FROM StudyPlan sp WHERE sp.id=:id");
            q.setParameter("id", id);
            sp = (StudyPlan) q.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return sp;
    }
    //--------------------------------------------------For new study plan page---------------------------------------------------------------------------

    @Override
    public ArrayList<Grade> getAllGrades(Student student) {
        Collection<Grade> all = new ArrayList<Grade>();
        grades = new ArrayList<Grade>();
        
        try{
            Query q = em.createQuery("SELECT g FROM Grade g WHERE g.student.id = :id");
            q.setParameter("id", student.getId());
            all = (List<Grade>) q.getResultList();
            for (Grade g : all) {
                grades.add(g);
            }
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not have any taken modules.");
            grades = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return grades;
    }
    
    @Override
    public ArrayList<ArrayList<Grade>> getAllGradesInOrder(Student student) {
        gradesInOrder = new ArrayList<ArrayList<Grade>>();
        grades = this.getAllGrades(student);
        //the iterator of for loop to change to next semster
        int year = 2018;
        if (student.getMatricYear() != null) {
            year = Integer.parseInt(student.getMatricYear());
        }
        int sem = 1;
        int numOfSemTaken = checkNumOfSemTaken(username) - 1;
        //from matric year sem 1, check what are the courses taken for the sem
        //then increase the year/sem to next semster
        for (int i = 0; i < numOfSemTaken; i++) {
            ArrayList<Grade> current = new ArrayList<Grade>();
            //go through all the modules the student takes
            for (int index = 0; index < grades.size(); index++) {
                //if the module is taken in "year" "sem", add it to the current 
                if (Integer.parseInt(grades.get(index).getModule().getTakenYear()) == year) {
                    if (Integer.parseInt(grades.get(index).getModule().getTakenSem()) == sem) {
                        current.add(grades.get(index));
                    }
                }
            }
            //if there is no Grade added for this semester, jump to next semester
            if (!current.isEmpty()) {
                gradesInOrder.add(current);
                current = null;
            }
            //increase sem year to next semester
            if (sem == 1) {
                sem = 2;
            } else {
                year++;
                sem = 1;
            }
        }
        return gradesInOrder;
    }
    
    @Override
    public ArrayList<Module> getCurrentModules(Student student) {
        List<Module> all = new ArrayList<Module>();
        takingModules = new ArrayList<Module>();
        
        //get current year and sem
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
        } else {
            currentSem = 1;
        }
        
        try{
            Query q = em.createQuery("SELECT m FROM Student s LEFT JOIN s.modules m Where s.id = :id AND m.takenYear = :currentYear AND m.takenSem = :currentSem");
            q.setParameter("id", student.getId());
            q.setParameter("currentYear", Integer.toString(currentYear));
            q.setParameter("currentSem", Integer.toString(currentSem));
            all = (List<Module>) q.getResultList();
            for (Module m : all) {
                takingModules.add(m);
            }
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not have any current modules.");
            takingModules = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return takingModules;
    }
    
    // find all studyPlan the user has
    @Override
    public ArrayList<StudyPlan> getAllStudyPlans(Student student) {
        List<StudyPlan> all = new ArrayList<StudyPlan>();
        studyPlans = new ArrayList<StudyPlan>();
        try{
            Query q = em.createQuery("SELECT sp FROM StudyPlan sp WHERE sp.student.id = :userId");
            q.setParameter("userId", student.getId());
            all = (List<StudyPlan>) q.getResultList();
            for (StudyPlan s : all) {
                studyPlans.add(s);
            }
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not have any study plan.");
            studyPlans = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return studyPlans;
    }
    
    //set all course taken by the user in order of year and sem
    //update all study plans first (remove study plans for previous semesters)
    @Override
    public ArrayList<ArrayList<StudyPlan>> getStudyPlanInOrder(Student student) {
        this.updateAllStudyPlans(username);
        studyPlansInOrder = new ArrayList<ArrayList<StudyPlan>>();
        studyPlans = this.getAllStudyPlans(student);
        
        Calendar now = Calendar.getInstance();
        int startYear = now.get(Calendar.YEAR);
        int startSem = 1;
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            startSem = 1;
        } else {
            startSem = 2;
        }
        do {
            ArrayList<StudyPlan> current = new ArrayList<StudyPlan>();
            for (StudyPlan s : studyPlans) {
                if (Integer.parseInt(s.getPickYear()) == startYear) {
                    if (Integer.parseInt(s.getPickSem()) == startSem) {
                        current.add(s);
                    }
                }
            }
            System.out.println("SPsb: getStudyPlanInOrder: current: " + current.size());
            //if for this semester, no modules are added to studyPlan, jump to next semester
            if (!current.isEmpty()) {
                studyPlansInOrder.add(current);
                current = null;
            }
            //increase sem year to next semester
            if (startSem == 1) {
                startSem = 2;
            } else {
                startYear++;
                startSem = 1;
            }
        } while (!checkAllStudyPlansAdded(studyPlansInOrder, studyPlans));
        return studyPlansInOrder;
    }
    
    @Override
    public void updateGradeYearSem(Long id, String moduleCode, int newYear, int newSem) {
        Grade g = this.findGrade(id);
        g.setModule(this.findModule(Integer.toString(newYear), Integer.toString(newSem), moduleCode));
        em.merge(g);
        em.flush();
    }
    
    @Override
    public void updateStudyPlanYearSem(Long id, int newYear, int newSem) {
        StudyPlan sp = this.findStudyPlanById(id);
        sp.setPickYear(Integer.toString(newYear));
        sp.setPickSem(Integer.toString(newSem));
        em.merge(sp);
        em.flush();
    }
    
     //the actual step that creates studyPlan enity and sets relationship
    @Override
    public void createStudyPlan(String pickYear, String pickSem, String moduleCode, Student student) {
        try {
            Course course = this.findCourse(moduleCode);
            // new studyPlan entity
            studyPlan = new StudyPlan();
            studyPlan.createStudyPlan(pickYear, pickSem, course, student);
            //set relationship between StudyPlan and Student
            this.student.getStudyPlan().add(studyPlan);
            //set relationship between StudyPlan and course
            studyPlan.setCourse(course);
            em.persist(studyPlan);
            em.merge(student);
            em.flush();
            System.out.println("StudyPlanSessionBean: createStudyPlan:");
            System.out.println("studyPlan " + course.getModuleCode() + " at year "
                    + pickYear + ", at sem " + pickSem + " added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void addGrade(String pickYear, String pickSem, String moduleCode, Student student, String moduleGrade) {
        module = this.findModule(pickYear, pickSem, moduleCode);
        Grade g = new Grade();
        g.createGrade(moduleGrade, module, student);
        student.getGrades().add(g);
        //update cap if not SUed
        if (!moduleGrade.equals("S") && !moduleGrade.equals("U")) {
            student.setCap(this.updateCurrentCapOnAdd(student.getCap(), this.getNumOfCredits(this.getAllGrades(student)), 
                    Integer.parseInt(module.getCourse().getModularCredits()), moduleGrade));
        }
        em.persist(g);
        module.getStudents().add(student);
        em.merge(module);
        em.merge(student);
        em.flush();
    }
    
    @Override
    public void addTakingModule(String pickYear, String pickSem, String moduleCode, Student student) {
        module = this.findModule(pickYear, pickSem, moduleCode);
        student.getModules().add(module);
        em.merge(student);
        
        module.getStudents().add(student);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void removeStudyPlan(String username, String moduleCode) {
        studyPlan = this.findStudyPlan(username, moduleCode);
        student = this.findStudent(username);
        student.getStudyPlan().remove(studyPlan);
        em.merge(student);
        em.remove(studyPlan);
        em.flush();
    }
    
    @Override
    public void removeModule(Student student, Module module) {
        student.getModules().remove(module);
        module.getStudents().remove(student);
        em.merge(student);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void removeGrade(Student student, Grade grade) {
        grade = this.findGrade(grade.getId());
        module = grade.getModule();
        student.getGrades().remove(grade);
        module.getStudents().remove(student);
        //update cap if not SUed
        if (!grade.getModuleGrade().equals("S") && !grade.getModuleGrade().equals("U")) {
        student.setCap(this.updateCurrentCapOnDelete(student.getCap(), this.getNumOfCredits(this.getAllGrades(student)), 
                Integer.parseInt(module.getCourse().getModularCredits()), grade.getModuleGrade()));
        }
        em.merge(student);
        em.merge(module);
        em.remove(grade);
        em.flush();
    }
    
    //check whether a module is inside a student's study plan or not
    @Override
    public boolean checkInSP(ArrayList<StudyPlan> all, String moduleCode) {
        boolean contains = false;
        if (all != null && !all.isEmpty()) {
             for (StudyPlan sp: all)
                if (sp.getCourse().getModuleCode().equals(moduleCode))
                    contains = true;
        }
        return contains;
    }
    
    //check whether a module is inside a student's current taking module or not
    @Override
    public boolean checkInCM(ArrayList<Module> all, String moduleCode) {
        boolean contains = false;
        if (all != null && !all.isEmpty()) {
            for (Module m: all)
               if (m.getCourse().getModuleCode().equals(moduleCode))
                   contains = true;
        }
        return contains;
    }
    
    //check whether a module is inside a student's taken module or not
    @Override
    public boolean checkInGrade(ArrayList<Grade> all, String moduleCode) {
        boolean contains = false;
        if (all != null && !all.isEmpty()) {
            for (Grade g: all)
               if (g.getModule().getCourse().getModuleCode().equals(moduleCode))
                   contains = true;
        }
        return contains;
    }
    
    @Override
    public int checkNumOfSemTaken(String username) {
        numOfSemTaken = 1;
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
            numOfSemTaken++;
        } else {
            currentSem = 1;
        }

        int matricYear = Integer.parseInt(this.findStudent(username).getMatricYear());
        numOfSemTaken += 2 * (currentYear - matricYear);
        return numOfSemTaken;
    }
    
    //check whether there is any syudyPlan planned for semesters passed already
    //if yes, delete these studyPlan
    @Override
    public void updateAllStudyPlans(String username) {
        Collection<StudyPlan> all = this.getAllStudyPlans(this.findStudent(username));
        Calendar now = Calendar.getInstance();
        int currentSem = 1;
        int currentYear = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
        }
        student = this.findStudent(username);
        //skip if the student doesn't have any studyPlan
        if (all != null) {
            for (StudyPlan studyPlan : all) {
                if (Integer.parseInt(studyPlan.getPickYear()) < currentYear) {
                    student.getStudyPlan().remove(studyPlan);
                    em.remove(studyPlan);
                    em.persist(student);
                } else if ((Integer.parseInt(studyPlan.getPickYear()) == currentYear)
                        && (Integer.parseInt(studyPlan.getPickSem()) <= currentSem)) {
                    student.getStudyPlan().remove(studyPlan);
                    em.remove(studyPlan);
                    em.persist(student);
                }
            }
        }
    }

    //get all the credits the student have taken
    //modules getting SU is not counted
    @Override
    public int getNumOfCredits(ArrayList<Grade> grades) {
        int credits = 0;
        for (Grade grade : grades) {
            if ((!grade.getModuleGrade().equals("S")) && (!grade.getModuleGrade().equals("U"))) {
                credits += Integer.parseInt(grade.getModule().getCourse().getModularCredits());
            }
        }
        System.out.println("student " + username + "'s total credits taken is " + credits);
        return credits;
    }
    
    //called by addGrade
    //calculate new cap when new grade added
    private double updateCurrentCapOnAdd(double currentCap, int allCredits, int newModuleCredit, String grade) {
        double newCap = currentCap * allCredits + this.convertGradeToPoint(grade) * newModuleCredit;
        newCap /= (allCredits + newModuleCredit);
        return newCap;
    }
    
    //called by addGrade
    //calculate new cap when a grade is deleted
    private double updateCurrentCapOnDelete(double currentCap, int allCredits, int deleteModuleCredit, String grade) {
        double newCap = currentCap * allCredits - this.convertGradeToPoint(grade) * deleteModuleCredit;
        //if this is the only one Grade recorded, after delete reset cap to 5.0
        if ((allCredits - deleteModuleCredit) == 0)
            return 5.0;
        else 
            newCap /= (allCredits - deleteModuleCredit);
        return newCap;
    }

}
