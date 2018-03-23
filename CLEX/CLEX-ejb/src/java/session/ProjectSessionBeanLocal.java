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
public interface ProjectSessionBeanLocal {

    /**
     *
     * @param student
     * @return
     */
    

    public String checkModuleGroup(Student student, Module module);

    public boolean checkStudentProjectGroup(Student student, Module module);

    public ProjectGroup getStudentProjectGroup(Student student, Module module);

    public ArrayList<Module> getTakingModules(Student student);


    
}
