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
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface GroupFormationSessionBeanLocal {

//    public ArrayList<ProjectGroup> getAllProjectGroups(Module module);

    public boolean joinGroup(Long studentId, Long groupId, Long oriGroupId);

    public ProjectGroup findProjectGroup(Long id);

    public void changeStudentGroup(Long studentId, Long toGroupId, Long fromGroupId);

    public SuperGroup createSuperGroup(int numOfGroups, int avgStudentNum, int minStudentNum, int maxStudentNum, Module module);

    public SuperGroup createSuperGroup(int numOfGroups, int avgStudentNum, Module module);

    public SuperGroup createSuperGroupWithMax(int numOfGroups, int avgStudentNum, int maxStudentNum, Module module);

    public SuperGroup createSuperGroupWithMin(int numOfGroups, int avgStudentNum, int minStudentNum, Module module);

    public SuperGroup findSuperGroup(Long id);

    public void closeGroupFormation(Long superGroupId);

    public void deleteProjectGroup(Long projectGroupId);

    public ArrayList<ProjectGroup> getAllProjectGroups(Long superGroupId);

    public Student findStudent(Long id);

    public String checkStudentGroup(Long studentId, Long superGroupId);

    public Long checkStudentGroupId(Long studentId, Long superGroupId);

    public void setStudentGroup(Long studentId, Long projectGroupId);

    public void addProjectGroup(Long superGroupId, String name);

    public void leaveGroup(Long studentId, Long groupId);

    public Module findModule(Long id);

    public int autoAssign(Long moduleId, Long superGroupId);

    public ProjectGroup createProjectGroup(Long superGroupId, String name);

    public void autoAssignAll(Long moduleId, Long superGroupId, int avg);

    public void joinStudentGroup(Long studentId, Long groupId);

    public ArrayList<Student> getStudentNoGroup(Module module);
    
    public void setDeadline(Long Id, String deadline);
    
}
