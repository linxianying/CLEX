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
import entity.Guest;
import entity.Lecturer;
import entity.Task;
import entity.User;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Collection;
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
    private Student studentEntity;
    private Lecturer lecturerEntity;
    private Guest guestEntity;
    private Course courseEntity;
    
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void createStudent(String username, String password, String name, String email, String school, Long contactNum, String salt, 
                String faculty, String major, String matricYear, String matricSem, String currentYear, double cap){
        studentEntity = new Student();
        studentEntity.createStudent(username, hashPassword(password, salt), name, email, school, contactNum, salt,
                 faculty, major, matricYear, matricSem, currentYear, cap);
        em.persist(studentEntity);
        em.flush();
    }
    
    @Override
    public void apiTest(String url){
        
        try{
            JsonReader.test("http://api.nusmods.com/2015-2016/1/moduleList.json");
        }
        catch(Exception e){
            System.out.println("there is exception");
        }
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
    public void createCourse(String moduleCode, String moduleName, String moduleInfo ,boolean discontinuedBool,
        String discountinuedYear, String discountinuedSem, String offeredSem, String school) {
        courseEntity = new Course();
        courseEntity.createCourse(moduleCode, moduleName, moduleInfo, 
                discontinuedBool, discountinuedYear, discountinuedSem, 
                offeredSem, school);
        em.persist(courseEntity);
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
        return userEntity;
    }
    
    @Override
    public Student findStudent(String username){
        Student u = new Student();
        u = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            u = (Student) q.getSingleResult();
            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            u = null;
        }
        return u;
    }
    
    @Override
    public Lecturer findLecturer(String username){
        Lecturer l = new Lecturer();
        l = null;
        try{
            Query q = em.createQuery("SELECT l FROM Lecturer l WHERE l.username=:username");
            q.setParameter("username", username);
            l = (Lecturer) q.getSingleResult();
            System.out.println("Lecturer " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Lecturer " + username + " does not exist.");
            l = null;
        }
        return l;
    }
    
    @Override
    public Admin findAdmin(String username){
        Admin a = new Admin();
        a = null;
        try{
            Query q = em.createQuery("SELECT a FROM Admin a WHERE a.username=:username");
            q.setParameter("username", username);
            a = (Admin) q.getSingleResult();
            System.out.println("Admin " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Admin " + username + " does not exist.");
            a = null;
        }
        return a;
    }
    
    @Override
    public Guest findGuest(String username){
        Guest g = new Guest();
        g = null;
        try{
            Query q = em.createQuery("SELECT g FROM Guest g WHERE g.username=:username");
            q.setParameter("username", username);
            g = (Guest) q.getSingleResult();
            System.out.println("Guest " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Guest " + username + " does not exist.");
            g = null;
        }
        return g;
    }
    
    @Override
    public Module findModule(String moduleCode, String takenYear, String takenSem){
        Module m = new Module();
        m = null;
        Course c = new Course();
        c = null;
        Long courseId;
        try{
            c = this.findCourse(moduleCode);
            courseId = c.getId();
            Query q = em.createQuery("SELECT m FROM SCHOOLMODULE m "
                    + "WHERE m.COURSE_ID=:courseId AND m.TAKENYEAR=:takenYear "
                    + "AND m.TAKENSEM=:takenSem");
            q.setParameter("courseId", courseId);
            q.setParameter("takenYear", takenYear);
            q.setParameter("takenSem", takenSem);
            m = (Module) q.getSingleResult();
            System.out.println("Module: " + moduleCode + "with takenYear=" + 
                    takenYear + ", takenSem= " + takenSem + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            m = null;
        }
        return m;
    }
    
    @Override
    public Course findCourse(String moduleCode){
        Course c = new Course();
        c = null;
        try{
            Query q = em.createQuery("SELECT c FROM Course c WHERE c.moduleCode=:moduleCode");
            q.setParameter("moduleCode", moduleCode);
            c = (Course) q.getSingleResult();
            System.out.println("Course " + moduleCode + " found.");
        }
        catch(NoResultException e){
            System.out.println("Course " + moduleCode + " does not exist.");
            c = null;
        }
        return c;
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
    public String removeTask(Long taskId) {
        Task task = findTask(taskId);
        if (task==null) {
            return "Task not found!\n";
        }
        else{
            task = em.find(Task.class, taskId);
            em.remove(task);
            em.flush();
            em.clear();
        }
        return "Tutorial is sucessfully deleted!\n";
    }
    
    @Override
    public Task findTask(Long taskId){
        Task t = new Task();
        t = null;
        try{
            Query q = em.createQuery("SELECT t FROM Task t WHERE t.taskId=:taskId");
            q.setParameter("taskId", taskId);
            t = (Task) q.getSingleResult();
            System.out.println("Task " + taskId + " found.");
        }
        catch(NoResultException e){
            System.out.println("Task " + taskId + " does not exist.");
            t = null;
        }
        return t;
    }

    private double capCulculator(Student student){
        double cap = 0.0;
        int num = 0;
        Collection<Grade> grades = student.getGrades();
        for (Grade grade : grades) {
            String tempGrade = grade.getModuleGrade();
            if(tempGrade.equals("A")||tempGrade.equals("A+")){
                cap = cap + 5;
                num++;
            }else if(tempGrade.equals("A-")){
                cap = cap + 4.5;
                num++;
            }else if(tempGrade.equals("B+")){
                cap = cap + 4;
                num++;
            }else if(tempGrade.equals("B")){
                cap = cap + 3.5;
                num++;
            }else if(tempGrade.equals("B-")){
                cap = cap + 3;
                num++;
            }else if(tempGrade.equals("C+")){
                cap = cap + 2.5;
                num++;
            }else if(tempGrade.equals("C")){
                cap = cap + 2;
                num++;
            }else if(tempGrade.equals("C-")){
                cap = cap + 1.5;
                num++;
            }else if(tempGrade.equals("D+")){
                cap = cap + 1.0;
                num++;
            }else if(tempGrade.equals("D")){
                cap = cap + 0.5;
                num++;
            }else{
                num++;
            }
        }
        student.setCap((cap/num));
        return (cap/num); 
    }
    
}
