/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Student;
import entity.Module;
import entity.Course;
import entity.Grade;
import entity.GroupTask;
import entity.GroupTimeslot;
import entity.Guest;
import entity.Lecturer;
import entity.Ledger;
import entity.Lesson;
import entity.ProjectGroup;
import entity.StudyPlan;
import entity.SuperGroup;
import entity.Task;
import entity.Timeslot;
import entity.Transaction;
import entity.User;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javaClass.JsonReader;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author lin
 */
@Stateless
public class ClexSessionBean implements ClexSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private User userEntity;
    private Admin adminEntity;
    private Student studentEntity;
    private Module moduleEntity;
    private Lecturer lecturerEntity;
    private Guest guestEntity;
    private Course courseEntity;
    private Timeslot timeslotEntity;
    private Task taskEntity;
    private GroupTask groupTaskEntity;
    private ProjectGroup projectGroupEntity;
    private SuperGroup superGroupEntity;
    private Lesson lessonEntity;
    private Transaction transactionEntity;
    private Ledger ledgerEntity;
    private GroupTimeslot groupTimeslot;
    
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void createStudent(String username, String password, String name, String studentId, String email, String school, Long contactNum, String salt, 
                String faculty, String major, String matricYear, String matricSem, double cap){
        studentEntity = new Student();
        studentEntity.createStudent(username, hashPassword(password, salt), name, studentId, email, school, contactNum, salt,
                 faculty, major, matricYear, matricSem, cap);
        em.persist(studentEntity);
        em.flush();
    }
    
    @Override
    public Timeslot createTimeslot(String title, String startDate, String endDate,
            String details, String venue){
        timeslotEntity = new Timeslot();
        timeslotEntity.createTimeslot(title, startDate, endDate, details, venue);
        em.persist(timeslotEntity);
        em.flush();
        return timeslotEntity;
    }
    
    @Override
    public void createProjectGroup(SuperGroup superGroup, String name, double cost){
        System.out.println("Try to Add project group" + name);
        projectGroupEntity = new ProjectGroup();
        projectGroupEntity.createProjectGroup(superGroup, name, cost);
        em.persist(projectGroupEntity);
        superGroup.getProjectGroups().add(projectGroupEntity);
        em.merge(superGroup);
        em.flush();
    }
    
    @Override
    public GroupTimeslot createProjectGroupTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup projectGroup){
        groupTimeslot = new GroupTimeslot();
        groupTimeslot.createGroupTimeslot(date, timeFrom, timeEnd, title, details, venue, projectGroup);
        List<Student> students = (List<Student>) projectGroup.getGroupMembers();
        
        Iterator itr = students.iterator();
        while(itr.hasNext()){
            studentEntity = (Student) itr.next();
            //timeslotEntity = createTimeslot(title, timeFrom, timeEnd,details, venue);
            studentEntity.getGroupTimeslots().add(groupTimeslot);
            em.merge(studentEntity);
            System.out.println("Size: "+studentEntity.getGroupTimeslots().size());
        }
        projectGroup.getGroupTimeslots().add(groupTimeslot);
        em.merge(projectGroup);
        em.persist(groupTimeslot);
        em.flush();
        return groupTimeslot;
    }
    
    
    
    @Override
    public void createStudyPlan(String pickYear, String pickSem, Course course, Student student){
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.createStudyPlan(pickYear, pickSem, course, student);
        student.getStudyPlan().add(studyPlan);
        em.persist(studyPlan);
        em.merge(student);
        em.flush();
    }
    
//    @Override
//    public void createGrade(String moduleGrade, Module module, Student student){
//        Grade grade = new Grade();
//        grade.createGrade(moduleGrade, module, student);
//        student.getGrades().add(grade);
//        em.persist(grade);
//        em.merge(student);
//        em.flush();
//    }
    
    @Override
    public void dragAllNusMods(String url){
        
        try{
            String[][] arr = JsonReader.dragAllNusMods("");
            int length = arr.length;
            //System.out.println("The length of the arr is"+length);
            int index = 0;
            while(index<3432){
                //System.out.println(arr[index][0]+"  "+arr[index][1]+"  "+arr[index][2]+"  "+arr[index][3] +"  "+arr[index][4]);
                if(arr[index][2].length()>1000){
                    arr[index][2] = arr[index][1];
                }
                createCourse(arr[index][0],arr[index][1],arr[index][2],false,"","","","NUS",arr[index][3],arr[index][4]);
                createModule("2017","1",arr[index][5],arr[index][6],courseEntity);
                index++;
            }
            //(moduleCode, moduleTitle, moduleInfo ,false,"","", "", "NUS",Integer.parseInt(moduleCredit),workload);
        }
        catch(Exception e){
            System.out.println("there is exception");
        }
    }
    
    @Override
    public void getTimetable(String moduleCode){
        //String timetable = JsonReader.getTimetable(moduleCode);
        String timetable = JsonReader.getTimetable("IS4103");
        System.out.println(timetable);
    }
    
    @Override
    public void createLecturer(String username, String password, String name, String email, String school, Long contactNum, String salt, 
                String faculty){
        lecturerEntity = new Lecturer();
        lecturerEntity.createLecturer(username, hashPassword(password, salt), name, email, school, contactNum, salt,
                 faculty);
        em.persist(lecturerEntity);
        em.flush();
    }
    
    @Override
    public void createGuest(String username, String password, String name, String email, String school, Long contactNum, String salt){
        guestEntity = new Guest();
        guestEntity.createGuest(username, hashPassword(password, salt), name, email, school, contactNum, salt);
        em.persist(guestEntity);
        em.flush();
    }
    
    @Override
    public void createAdmin(String username, String password, String name, String email, String school, Long contactNum, String salt){
        Admin adminEntity = new Admin();
        adminEntity.createAdmin(username, hashPassword(password, salt), name, email, school, contactNum, salt);
        adminEntity.setApproval(true);
        em.persist(adminEntity);
        em.flush();
}

    @Override
    public void createCourse(String moduleCode, String moduleName, String moduleInfo, boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school, String moduleCredit, String workload) {
        courseEntity = new Course();
        courseEntity.createCourse(moduleCode, moduleName, moduleInfo, 
                discontinuedBool, discountinuedYear, discountinuedSem, 
                offeredSem, school,moduleCredit, workload);
        em.persist(courseEntity);
        em.flush();

    }
    
    
    @Override
    public void createModule(String takenYear, String takenSem, 
            String prerequisite, String preclusions, Course course) {
        Module module = new Module();
        module.createModule(takenYear, takenSem, prerequisite, preclusions, course);
        course.getModules().add(module);
        em.persist(module);
        em.merge(course);
        em.flush();
        //System.out.println("Try: get course's modules" + course.getModules().size());
        //System.out.println("Try: get course's modules" + course.getModules());
    }
    
    public void createLesson(String day, String timeFrom, String timeEnd, String type, String venue, Module module) {
        Lesson lesson = new Lesson();
        lesson.createLesson(day, timeFrom, timeEnd, type, venue, module);
        module.getLessons().add(lesson);
        em.persist(lesson);
        em.merge(module);
        em.flush();
    }

    
    @Override
    public boolean checkNewUser(String username) {
        if(findStudent(username) == null&&findAdmin(username) == null&&findLecturer(username) == null&&findGuest(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewAdmin(String username) {
        if(findAdmin(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewStudent(String username) {
        if(findStudent(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewLecturer(String username) {
        if(findLecturer(username) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkNewCourse(String moduleCode) {
        if(findCourse(moduleCode) == null){
            return true;
        }
        return false;
    }
    
    @Override
    public User findUser(String username){
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    
    @Override
    public Student findStudent(String username){
        studentEntity = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            studentEntity = (Student) q.getSingleResult();
//            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            studentEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return studentEntity;
    }
    
    @Override
    public Lecturer findLecturer(String username){
        
        lecturerEntity = null;
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
        catch(Exception e) {
            e.printStackTrace();
        }
        return lecturerEntity;
    }
    
    @Override
    public Admin findAdmin(String username){
        adminEntity = null;
        try{
            Query q = em.createQuery("SELECT a FROM Admin a WHERE a.username=:username");
            q.setParameter("username", username);
            adminEntity = (Admin) q.getSingleResult();
            System.out.println("Admin " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Admin " + username + " does not exist.");
            adminEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return adminEntity;
    }
    
    @Override
    public Guest findGuest(String username){

        guestEntity = null;
        try{
            Query q = em.createQuery("SELECT g FROM Guest g WHERE g.username=:username");
            q.setParameter("username", username);
            guestEntity = (Guest) q.getSingleResult();
            System.out.println("Guest " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Guest " + username + " does not exist.");
            guestEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return guestEntity;
    }
    
    @Override
    public Module findModule(String moduleCode, String takenYear, String takenSem){
        moduleEntity = null;
        courseEntity = null;
        Long courseId;
        try{
            courseEntity = this.findCourse(moduleCode);
            courseId = courseEntity.getId();
            Query q = em.createQuery("SELECT m FROM SchoolModule m "
                    + "WHERE m.course.id=:courseId AND m.takenYear=:takenYear "
                    + "AND m.takenSem=:takenSem");
            q.setParameter("courseId", courseId);
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            moduleEntity = (Module) q.getSingleResult();
            //System.out.println("Module: " + moduleCode + "with takenYear=" + 
            //        takenYear + ", takenSem= " + takenSem + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            moduleEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return moduleEntity;
    }
    
    @Override
    public Course findCourse(String moduleCode){
        courseEntity = null;
        try{
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            courseEntity = (Course) q.getSingleResult();
//            System.out.println("Course " + moduleCode + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            courseEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return courseEntity;
    }

    @Override
    public Lesson findLesson(String day, String timeFrom, String type, Module module){
        lessonEntity = null;
        try{
            Query q = em.createQuery("SELECT l FROM Lesson l WHERE l.day=:day AND l.timeFrom=:timeFrom AND l.type=:type AND l.module.id=:id");
            q.setParameter("day", day);
            q.setParameter("timeFrom", timeFrom);
            q.setParameter("type", type);
            q.setParameter("id", module.getId());
            lessonEntity = (Lesson) q.getSingleResult();
            System.out.println("Lesson " + module.getCourse().getModuleCode() + 
                    " for day " + day + ", timeFrom " + timeFrom + ", type " + 
                    type + " found.");
        }
        catch(NoResultException e){
            System.out.println("Lesson " + module.getCourse().getModuleCode() + 
                    " for day " + day + ", timeFrom " + timeFrom + ", type " + 
                    type + " does not exist.");
            lessonEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return lessonEntity;
    }
    

    
    /*@Override
    public String viewModule(String moduleCode) {
        String result;
        Module module = findModule(moduleCode);
        if (module == null) {
            return "Module not found!\n";
        } else {
            result = "Module Code: " + module.getCourse().getModuleCode() + "\n";
            result = result + "Modular Credits: " + module.getModularCredits() + "\n";
            result = result + "Workload: " + module.getWorkload() + "\n";
            result = result + "Registered Students: \n";
            Collection<Student> students = module.getStudents();
            if (students.isEmpty()) {
                result = result + "No registered students!";
            }
            for (Student student : students) {
                result = result + student.getUsername() + " " + student.getName() + " " + student.getEmail() + "\n";
            }
            return result;
        }
    }
    */
    
    @Override
    public boolean updateStudentEmail(String username, String email) {
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.username = :username");
        q.setParameter("username", username);
        studentEntity = (Student) q.getResultList().get(0);
        if (email.isEmpty()) {
            System.out.println("Error: Email is empty!");
            return false;
        }
        studentEntity.setEmail(email);
        em.merge(studentEntity);
        return true;
    }
    
    @Override
    public boolean updateStudentFaculty(String username, String faculty) {
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.faculty = :faculty");
        q.setParameter("faculty", faculty);
        studentEntity = (Student) q.getResultList().get(0);
        if (faculty.isEmpty()) {
            System.out.println("Error: faculty is empty!");
            return false;
        }
        studentEntity.setFaculty(faculty);
        em.merge(studentEntity);
        return true;
    }

    @Override
    public boolean updateStudentContact(String username, Long contactNum) {
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.contactNum = :contactNum");
        q.setParameter("contactNum", contactNum);
        studentEntity = (Student) q.getResultList().get(0);
        if (contactNum==0) {
            System.out.println("Error: no contact number is found!");
            return false;
        }
        studentEntity.setContactNum(contactNum);
        em.merge(studentEntity);
        return true;
    }
    
    @Override
    public void changePassword(String username, String newPassword){
        Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
        q.setParameter("username", username);
        userEntity = (User) q.getSingleResult();
        userEntity.setPassword(hashPassword(newPassword, userEntity.getSalt()));
        System.out.println("Password of " + username + " has been changed.");
        em.merge(userEntity);
        em.flush();
    }
    
    @Override
    public boolean checkPassword(String username, String password) {
        if(checkNewUser(username)==true){
            System.out.println("Error: This is a new user! Please register first!");
            return false;
        }else{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            if(userEntity.getPassword().equals(hashPassword(password, userEntity.getSalt()))){
                System.out.println("Password of " + username + " is correct.");
                return true;
            }
            else{
                 System.out.println("Password of " + username + " is wrong.");
            }
        }
        return false;
    }
    
    @Override
    public String resetPassword(String username){
        String newPass = genPass();
        if(checkNewUser(username)==true){
            System.out.println("Error: This is a new user! Please register first!");
        }else{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            userEntity.setPassword(hashPassword(newPass, userEntity.getSalt()));
            System.out.println("Password of " + username + " is reset.");
            em.merge(userEntity);
            em.flush();
            return newPass;
        }
        return null;
    }
    
    private String genPass(){
        Random rng = new Random();
        Integer gen = rng.nextInt(31958201);
        String newPass = gen.toString();
        
        return newPass;
    }
    
    private String hashPassword(String password, String salt){
        String genPass = null;
 
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pass = (password+salt).getBytes();
            md.update(pass);
            byte[] temp = md.digest(pass);
            
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < temp.length; i++){
                sb.append(Integer.toString((temp[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPass = sb.toString();
            
        }
        catch(Exception e){
            System.out.println("Failed to hash password.");
        }
        return genPass;
    }
    
    
    @Override
    public void setStudentTakingModules(Student student, Module module){
        student.getModules().add(module);
        em.merge(student);
        em.flush();
        
        module.getStudents().add(student);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void setStudentTakenModules(Student student, Module module, String grade) {
        Grade g = new Grade();
        g.createGrade(grade, module, student);
        student.getGrades().add(g);
        em.merge(student);
        em.flush();
        
        module.getStudents().add(student);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void setStudentLesson(Student student, Lesson lesson) {
        student.getLessons().add(lesson);
        em.merge(student);
        em.flush();
        
        lesson.getStudents().add(student);
        em.merge(lesson);
        em.flush();
    }

    @Override
    public void linkLecturerModule(String username, String moduleCode, String takenYear, String takenSem) {
        //To change body of generated methods, choose Tools | Templates.
        moduleEntity = null;
        lecturerEntity = null;
        
        moduleEntity = findModule(moduleCode, takenYear, takenSem);
        lecturerEntity = findLecturer(username);
        
        if(lecturerEntity==null||moduleEntity==null){
            System.out.println("LinkLecturerModule: cannot find module or lecturer");
            return;
        }
        
        lecturerEntity.getModules().add(moduleEntity);
        em.merge(moduleEntity);
        em.flush();
        moduleEntity.getLecturers().add(lecturerEntity);
        em.merge(lecturerEntity);
        em.flush();
        System.out.println("LinkLecturerModule: link finished");
    }
    
    @Override
    public void linkStudentModule(String username, String moduleCode, String takenYear, String takenSem){
        moduleEntity = null;
        studentEntity = null;
        
        moduleEntity = findModule(moduleCode, takenYear, takenSem);
        studentEntity = findStudent(username);
        
        if(studentEntity==null||moduleEntity==null){
            System.out.println("LinkStudentModule: cannot find module or student");
            return;
        }
        studentEntity.getModules().add(moduleEntity);
        em.merge(moduleEntity);
        em.flush();
        moduleEntity.getStudents().add(studentEntity);
        em.merge(studentEntity);
        em.flush();
        System.out.println("LinkStudentModule: link finished");
    }

    @Override
    public void linkStudentGroup(Student student, ProjectGroup projectGroup){
        //to make sure that students number in a project group will not be larger than the max number
        if(projectGroup.getGroupMembers().size()>=projectGroup.getSuperGroup().getMaxStudentNum()){
            System.out.println("This project Group " + projectGroup.getName() + " of " 
                    + projectGroup.getSuperGroup().getModule().getCourse().getModuleCode() 
                    +" is full.");
            return;
        }
        projectGroup.getGroupMembers().add(student);
        student.getProjectGroups().add(projectGroup);
        em.merge(projectGroup);
        em.merge(student);
        em.flush();
    }
    
    @Override
    public ProjectGroup findProjectgroup(String name, Module module) {
        try{
            Query q = em.createQuery("SELECT p FROM ProjectGroup p WHERE p.name = :name AND p.superGroup.id = :id");
            q.setParameter("name", name);
            q.setParameter("id", module.getSuperGroup().getId());
            this.projectGroupEntity = (ProjectGroup) q.getSingleResult();
            System.out.println("ProjectGroup " + name + " for " + module.getCourse().getModuleCode() +" found.");
        }
        catch(NoResultException e){
            System.out.println("ProjectGroup " + name + " for " + module.getCourse().getModuleCode() + " does not exist.");
            projectGroupEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return projectGroupEntity;
    }
    
    
    
    //date is of format dd-MM-yyyy
    @Override
    public Transaction createTransaction(double cost, String date, String activity, ProjectGroup group) {
        transactionEntity = new Transaction();
        transactionEntity.createTransaction(cost, date, activity, group);
        group.getTransactions().add(transactionEntity);
        em.persist(transactionEntity);
        em.merge(group);
        em.flush();
        return transactionEntity;
    }
    
    @Override
    public void createLedger(Student student, double ascCost, double pay, Transaction transaction) {
        ledgerEntity = new Ledger();
        ledgerEntity.createLedger(student, ascCost, pay, transaction);
        student.getLedgers().add(ledgerEntity);
        transaction.getLedgers().add(ledgerEntity);
        em.persist(ledgerEntity);
        em.merge(student);
        em.merge(transaction);
        em.flush();
    }
    
    @Override
    public void confirmGroupFormation(Module module) {
        if (module.getSuperGroup() != null)
            module.getSuperGroup().setConfirm(true);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void createSuperGroup(int numOfGroups, int avgStudentNum, int minStudentNum, int maxStudentNum, Module module){
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        superGroupEntity.setMaxStudentNum(maxStudentNum);
        superGroupEntity.setMinStudentNum(minStudentNum);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void createSuperGroup(int numOfGroups, int avgStudentNum, Module module) {
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void createSuperGroupWithMax(int numOfGroups, int avgStudentNum,  int maxStudentNum, Module module) {
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        superGroupEntity.setMaxStudentNum(maxStudentNum);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void createSuperGroupWithMin(int numOfGroups, int avgStudentNum, int minStudentNum,  Module module) {
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        superGroupEntity.setMinStudentNum(minStudentNum);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public List<Course> retrieveAllCourse() {
        Query query = em.createQuery("SELECT c FROM Course c");
        List<Course> courses = query.getResultList();
        return courses;
    }
}
