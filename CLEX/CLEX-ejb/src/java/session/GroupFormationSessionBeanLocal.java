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
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface GroupFormationSessionBeanLocal {

    public ArrayList<ProjectGroup> getAllProjectGroups(Module module);

    public boolean joinGroup(Student student, ProjectGroup group);

    public ProjectGroup findProjectGroup(Long id);
    
}
