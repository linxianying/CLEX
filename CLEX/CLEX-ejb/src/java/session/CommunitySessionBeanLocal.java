/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Reply;
import entity.Thread;
import entity.User;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface CommunitySessionBeanLocal {

    public void createThread(String username, String content, String title);
    public void createReply(Long threadId, String content, String username);
    
}
