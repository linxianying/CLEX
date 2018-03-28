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
    public ArrayList<ProjectGroup> projectGroups;
    
    
    @Override
    public ProjectGroup findProjectGroup(Long id) {
        try{
            Query q = em.createQuery("SELECT p FROM ProjectGroup p WHERE p.id = :id");
            q.setParameter("id", id);
            this.group = (ProjectGroup) q.getSingleResult();
            System.out.println("ProjectGroup " + group.getName() + " for " 
                    + group.getSuperGroup().getModule().getCourse().getModuleCode() +" found.");
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
    public ArrayList<ProjectGroup> getAllProjectGroups(Module module) {
        projectGroups = new ArrayList<ProjectGroup>();
        Collection<ProjectGroup> all = module.getSuperGroup().getProjectGroups();
        for (ProjectGroup pg: all) 
            projectGroups.add(pg);
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
    public void changeStudentGroup(Student student, ProjectGroup toGroup, ProjectGroup fromGroup) {
        fromGroup.getGroupMembers().remove(student);
        toGroup.getGroupMembers().add(student);
        student.getProjectGroups().remove(fromGroup);
        student.getProjectGroups().add(toGroup);
        em.merge(toGroup);
        em.merge(fromGroup);
        em.merge(student);
        em.flush();
    }
    

}
