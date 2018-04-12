/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Conversation;
import entity.Message;
import entity.User;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author eeren
 */
@Local
public interface MessageSessionBeanLocal {

    public Conversation createConversation(String username1, String username2);
    public boolean createMessage(Long convoId, String username, String rcvUsername, String message);

    public boolean deleteConversation(String username, Long convoId);

    public void setReadMsgCount(Long convoId, String username);
    public boolean checkReadStatus(Long convoId, String username);
    
    public Conversation checkUserInSameConversation(String username1, String username2);
    public boolean checkEmptyUserInConversation(Long convoId);

    public Collection<Conversation> getConversationByUser(String username);
    public Collection<Message> getMessageByConversation(Long id);
    public Collection<User> getAllUsers();
            
    public User findUser(String username);
    
}
