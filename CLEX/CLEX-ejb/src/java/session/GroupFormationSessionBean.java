/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public ArrayList<ProjectGroup> projectGroups;
    
    
    @Override
    public ArrayList<ProjectGroup> getAllProjectGroups(Module module) {
        projectGroups = new ArrayList<ProjectGroup>();
        Collection<ProjectGroup> all = module.getSuperGroup().getProjectGroups();
        for (ProjectGroup pg: all) 
            projectGroups.add(pg);
        return projectGroups;
    }

    
    
}
