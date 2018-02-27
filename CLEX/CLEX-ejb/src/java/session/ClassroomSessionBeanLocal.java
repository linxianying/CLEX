/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Module;
import entity.Poll;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface ClassroomSessionBeanLocal {
    public Module findModule(String moduleCode, String takenYear, String takenSem);
    public Poll createPoll(String moduleCode, String takenYear, String takenSem, 
            String datetime, String topic, int count, String type, String content);
}
