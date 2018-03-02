/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
public class ProjectSessionBean implements ProjectSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private Student student;
    private String username;
    private ArrayList<Module> takingModules;
    private ProjectGroup projectGroup;
    
    @Override
    public ArrayList<Module> getTakingModules(String username) {
        Collection<Module> all = new ArrayList<Module>();
        takingModules = new ArrayList<Module>();
        this.student = findStudent(username);
        all = this.student.getModules();
        int currentSem = 0;
        int numOfSemTaken = 0;
        //get current year and sem
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        System.out.println("Current Year is : " + currentYear);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        System.out.println("Current Month is : " + now.get(Calendar.MONTH));
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
            numOfSemTaken++;
        }
        else {
            currentSem = 1;
        }
        //add modules for this semster to takingModules
        for (Module m : all) {
            if (Integer.parseInt(m.getTakenYear()) == currentYear) {
                if (Integer.parseInt(m.getTakenSem()) == currentSem){
                    takingModules.add(m);
                }
            }
        }
        System.out.println("StudyPlanSessionbean: getTakenModules: student:" 
                + username + "'s takingModules:" + takingModules.size());
        return takingModules;
    }
    
    @Override
    public Student findStudent(String username) {
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
        this.username = username;
        this.student = u;
        return student;
    }
    
    //check the status of a student's module group
    //return a type to indicate the status of the group 
    @Override
    public String checkModuleGroup(Student student, Module module) {
        String type = "none";
        //the module has not been enabled by the lecturer to be able to form group
        if(module.getSuperGroup() == null) {
            type = "noSuperGroup";
            System.out.println("No Super group for " + module.getCourse().getModuleCode());
        }
        //there is superGroup but the student has not linked to a group
        else if (!(checkStudentProjectGroup(student, module))) {
            type = "formGroup";
            System.out.println("Not join project group for " + module.getCourse().getModuleCode());
        }
        //there is superGroup and the student has linked to a group
        else {
            type = "hasGroup";
            System.out.println("Has a project group for " + module.getCourse().getModuleCode());
        }
        return type;
    }
    
    //call by checkModuleGroup
    //check whether a student has erolled in a project group of certain module
    @Override
    public boolean checkStudentProjectGroup(Student student, Module module) {
        boolean has= false;
        Collection<ProjectGroup> allGroups = new ArrayList<ProjectGroup>();
        allGroups = student.getProjectGroups();
        System.out.println("project sb: checkStudentProjectGroup: " + allGroups.size());
        for (ProjectGroup pg: allGroups) {
            if (pg.getSuperGroup().getModule().getId().equals(module.getId()))
                has = true;
        }
        return has;
    }
    
    //get a certain project group of a certain module of the student
    @Override
    public ProjectGroup getStudentProjectGroup(Student student, Module module) {
        Collection<ProjectGroup> allGroups = new ArrayList<ProjectGroup>();
        allGroups = student.getProjectGroups();
        System.out.println("project sb: checkStudentProjectGroup: " + allGroups.size());
        for (ProjectGroup pg: allGroups) {
            if (pg.getSuperGroup().getModule().getId().equals(module.getId()))
                this.projectGroup = pg;
            }
        return projectGroup;
    }

}
