/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package session;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.SuperGroup;
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
 * @author caoyu
 */
@Stateless
public class GroupFormationSessionBean implements GroupFormationSessionBeanLocal {
    
    @PersistenceContext
            EntityManager em;
    private Student student;
    private String username;
    private ProjectGroup group;
    private ArrayList<ProjectGroup> projectGroups;
    private SuperGroup superGroupEntity;
    private ProjectGroup toGroup;
    private ProjectGroup fromGroup;
    
    @Override
    public Student findStudent(Long id){
        student = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.id=:id");
            q.setParameter("id", id);
            student = (Student) q.getSingleResult();
//            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("GroupFormationSessionBean: findStudent: Student does not exist.");
            student = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return student;
    }
    
    @Override
    public ProjectGroup findProjectGroup(Long id) {
        try{
            Query q = em.createQuery("SELECT p FROM ProjectGroup p WHERE p.id = :id");
            q.setParameter("id", id);
            this.group = (ProjectGroup) q.getSingleResult();
//            System.out.println("ProjectGroup " + group.getName() + " for "
//                    + group.getSuperGroup().getModule().getCourse().getModuleCode() +" found.");
        }
        catch(NoResultException e){
            System.out.println("ProjectGroup " + group.getName() + " for "
                    + group.getSuperGroup().getModule().getCourse().getModuleCode() + " does not exist.");
            group = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return group;
    }
    
    @Override
    public SuperGroup findSuperGroup(Long id) {
        try{
            Query q = em.createQuery("SELECT sg FROM SuperGroup sg WHERE sg.id = :id");
            q.setParameter("id", id);
            this.superGroupEntity = (SuperGroup) q.getSingleResult();
        }
        catch(NoResultException e){
            System.out.println("superGroup for " + group.getSuperGroup().getModule().getCourse().getModuleCode() + " does not exist.");
            superGroupEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return superGroupEntity;
    }
    
//    @Override
//    public ArrayList<ProjectGroup> getAllProjectGroups(Module module) {
//        projectGroups = new ArrayList<ProjectGroup>();
//        Collection<ProjectGroup> all = module.getSuperGroup().getProjectGroups();
//        for (ProjectGroup pg: all)
//            projectGroups.add(pg);
//        return projectGroups;
//    }
    
    @Override
    public ArrayList<ProjectGroup> getAllProjectGroups(Long superGroupId) {
        projectGroups = new ArrayList<ProjectGroup>();
        List<ProjectGroup> all = new ArrayList<ProjectGroup>();
        try{
            Query q = em.createQuery("SELECT g FROM ProjectGroup g WHERE g.superGroup.id = :superGroupId");
            q.setParameter("superGroupId", superGroupId);
            all = (List<ProjectGroup>) q.getResultList();
            for (ProjectGroup pg: all)
                projectGroups.add(pg);
        }
        catch(NoResultException e){
            System.out.println("GroupFormationSessionBean:getAllProjectGroups: no projectgroups found" );
            projectGroups = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return projectGroups;
    }
    
    //join the student to the group unless the group is full return false
    @Override
    public boolean joinGroup(Student student, ProjectGroup group) {
        if (!this.checkGroupStatus(group))
            return false;
        else {
            student.getProjectGroups().add(group);
            group.getGroupMembers().add(student);
            em.merge(student);
            em.merge(group);
            em.flush();
            System.out.println("gf Session bean: joinGroup: " + student.getName()
                    + " successfully joins group " + group.getName() + " for "
                    + group.getSuperGroup().getModule().getCourse().getModuleCode());
            return true;
        }
    }
    
    //called by joinGroup method
    //in case consistency problem, check whether the project group is full or not
    //return false if it is full, else true
    private boolean checkGroupStatus(ProjectGroup group) {
        boolean check = false;
        int max = group.getSuperGroup().getMaxStudentNum();
        if (group.getGroupMembers().size() < max) {
            check = true;
            System.out.println("gf Session bean: checkGroupStatus: proejctGroup "
                    + group.getName() + " for "
                    + group.getSuperGroup().getModule().getCourse().getModuleCode() + " is not full yet");
        }
        return check;
    }
    
    @Override
    public void changeStudentGroup(Long studentId, Long toGroupId, Long fromGroupId) {
        student = this.findStudent(studentId);
        fromGroup = this.findProjectGroup(fromGroupId);
        toGroup = this.findProjectGroup(toGroupId);
        fromGroup.getGroupMembers().remove(student);
        toGroup.getGroupMembers().add(student);
        student.getProjectGroups().remove(fromGroup);
        student.getProjectGroups().add(toGroup);
        em.merge(toGroup);
        em.merge(fromGroup);
        em.merge(student);
        em.flush();
    }
    
    @Override
    public SuperGroup createSuperGroup(int numOfGroups, int avgStudentNum, int minStudentNum, int maxStudentNum, Module module){
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        superGroupEntity.setMaxStudentNum(maxStudentNum);
        superGroupEntity.setMinStudentNum(minStudentNum);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
        return superGroupEntity;
    }
    
    @Override
    public SuperGroup createSuperGroup(int numOfGroups, int avgStudentNum, Module module) {
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
        return superGroupEntity;
    }
    
    @Override
    public SuperGroup createSuperGroupWithMax(int numOfGroups, int avgStudentNum,  int maxStudentNum, Module module) {
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        superGroupEntity.setMaxStudentNum(maxStudentNum);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
        return superGroupEntity;
    }
    
    @Override
    public SuperGroup createSuperGroupWithMin(int numOfGroups, int avgStudentNum, int minStudentNum,  Module module) {
        superGroupEntity = new SuperGroup();
        superGroupEntity.createSuperGroup(numOfGroups, avgStudentNum, module);
        superGroupEntity.setMinStudentNum(minStudentNum);
        em.persist(superGroupEntity);
        module.setSuperGroup(superGroupEntity);
        em.merge(module);
        em.flush();
        return superGroupEntity;
    }
    
    @Override
    public void closeGroupFormation(Long superGroupId) {
        superGroupEntity = this.findSuperGroup(superGroupId);
        superGroupEntity.setConfirm(true);
        em.merge(superGroupEntity);
        em.flush();
    }
    
    @Override
    public void addProjectGroup(Long superGroupId, String name){
        superGroupEntity = this.findSuperGroup(superGroupId);
        group = new ProjectGroup();
        group.createProjectGroup(superGroupEntity, name, 0.0);
        em.persist(group);
        if (superGroupEntity.getProjectGroups() != null)
            superGroupEntity.getProjectGroups().add(group);
        else {
            Collection<ProjectGroup> all = new ArrayList<ProjectGroup>();
            all.add(group);
            superGroupEntity.getProjectGroups().add(group);
        }
        em.merge(superGroupEntity);
        em.flush();
    }
    
    @Override
    public void deleteProjectGroup(Long projectGroupId){
        group = this.findProjectGroup(projectGroupId);
        if (group.getGroupMembers() != null && !group.getGroupMembers().isEmpty()) {
            System.out.println("Error: groupFormationSessionBean: deleteProjectGroup: The group has related students");
        }
        superGroupEntity = group.getSuperGroup();
        superGroupEntity.getProjectGroups().remove(group);
        em.merge(superGroupEntity);
        em.remove(group);
        em.flush();
    }
    
    //check whether a student has a group for this module or not
    //return the group number or none if the student is not in any group
    @Override
    public String checkStudentGroup(Long studentId, Long superGroupId) {
        superGroupEntity = this.findSuperGroup(superGroupId);
        student = this.findStudent(studentId);
        for (ProjectGroup pg: superGroupEntity.getProjectGroups()) {
            if (pg.getGroupMembers().contains(student)) {
                return pg.getName();
            }
        }
        return "none";
    }
    
    //check whether a student has a group for this module or not
    //return the group id or null if the student is not in any group
    @Override
    public Long checkStudentGroupId(Long studentId, Long superGroupId) {
        superGroupEntity = this.findSuperGroup(superGroupId);
        student = this.findStudent(studentId);
        for (ProjectGroup pg: superGroupEntity.getProjectGroups()) {
            if (pg.getGroupMembers().contains(student)) {
                return pg.getId();
            }
        }
        return null;
    }
    
    //set a student with no group to a group
    @Override
    public void setStudentGroup(Long studentId, Long projectGroupId){
        student = this.findStudent(studentId);
        group = this.findProjectGroup(projectGroupId);
        if (group.getGroupMembers() == null){
            Collection<Student> all = new ArrayList<Student>();
            all.add(student);
            group.setGroupMembers(all);
        }
        else { 
            group.getGroupMembers().add(student);
        }
        student.getProjectGroups().add(group);
        em.merge(group);
        em.merge(student);
        em.flush();
    }
}
