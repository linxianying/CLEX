/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ProjectGroup;
import java.util.ArrayList;
import javaClass.StudentCost;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface ProjectCostSessionBeanLocal {

    public void addTransaction(ArrayList<StudentCost> all, String activity, double totalCost, ProjectGroup group);

    
}
