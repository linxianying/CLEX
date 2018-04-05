/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Conversation;
import entity.Message;
import entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eeren
 */
@Stateless
public class MessageSessionBean implements MessageSessionBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    
    User userEntity;
    Conversation convoEntity;
    Message msgEntity;
    
    Collection<Conversation> conversations;
    Collection<Message> messages;
    
    @Override
    public Conversation createConversation(String username1, String username2){
        User user1 = findUser(username1);
        User user2 = findUser(username2);
        
        if(user1 == null){
            System.out.println("User1 " + user1.getUsername() +" not found.");
        }
        
        if(user2 == null){
            System.out.println("User2 " + user2.getUsername() +" not found.");
        }
        
        Conversation convo = new Conversation();
        convo.startConversation();
   
        em.persist(convo);
        em.flush();
        
        convo.getUsers().add(user1);
        convo.getUsers().add(user2);

        user1.getConversations().add(convo);
        user2.getConversations().add(convo);
        
        em.merge(convo);
        em.merge(user1);
        em.merge(user2);
        em.flush();
        
        return convo;
    }
    
    @Override
    public boolean createMessage(Long convoId, String username, String message){
        userEntity = findUser(username);
        convoEntity = findConversation(convoId);
        List<User> userList = (List<User>) convoEntity.getUsers();
        
        if(userEntity == null){
            System.out.println("User not logged in.");
            return false;
        }
        
        if(userList.size() <= 1){
            System.out.println("Unable to create message, a user has already left conversation.");
            return false;
        }
        
        Message msg = new Message();
        int sentUser; //1: user1, 2: user2
        
        if(Objects.equals(userEntity.getId(), userList.get(0).getId())){
            sentUser = 1;
            convoEntity.setSentMsgCount1(convoEntity.getSentMsgCount1() + 1);
        }
        else if(Objects.equals(userEntity.getId(), userList.get(1).getId())){
            sentUser = 2;
            convoEntity.setSentMsgCount2(convoEntity.getSentMsgCount2() + 1);
        }
        else{
            System.out.println("User not in conversation.");
            return false;
        }
        
        msg.createMessage(message, sentUser);
        msg.setConversation(convoEntity);
        
        convoEntity.getMessages().add(msg);
        
        em.merge(convoEntity);
        em.persist(msg);
        em.flush();

        return true;
    }

    @Override
    public boolean deleteConversation(String username, Long convoId){
        userEntity = findUser(username);
        convoEntity = findConversation(convoId);
        List<User> userList = (List<User>) convoEntity.getUsers();
        
        if(userEntity == null){
            System.out.println("User not logged in.");
            return false;
        }

        if(userList.size() == 2){
            //Only 1 side delete, retain convo
            if(convoEntity.getUsers().remove(userEntity)){
                userEntity.getConversations().remove(convoEntity);
                em.merge(userEntity);
                em.merge(convoEntity);
                em.flush();
                em.clear();
                
                System.out.println("Removed user from conversation.");
                return true;
            }
        }
        else{
            if(convoEntity.getUsers().remove(userEntity)){
                //Both side delete, remove convo
                userEntity.getConversations().remove(convoEntity);
                em.merge(userEntity);
                em.remove(convoEntity);
                em.flush();
                em.clear();
                
                System.out.println("Conversation deleted.");
                return true;
            }
        }
        
        System.out.println("Failed to delete conversation.");
        return false;
    }
    
    @Override
    public void setReadMsgCount(Long convoId, String username, int readCount) {
        userEntity = findUser(username);
        convoEntity = findConversation(convoId);
        List<User> userList = (List<User>) convoEntity.getUsers();
    
        if(Objects.equals(userEntity.getId(), userList.get(0).getId())){
            convoEntity.setReadMsgCount1(convoEntity.getReadMsgCount1() + 1);
        }
        else if(Objects.equals(userEntity.getId(), userList.get(1).getId())){
            convoEntity.setReadMsgCount2(convoEntity.getReadMsgCount2() + 1);
        }
        
        em.merge(convoEntity);
        em.flush();
    }
   
    //For starting conversations
    @Override
    public Conversation checkUserInSameConversation(String username1, String username2){
        User user1 = findUser(username1);
        User user2 = findUser(username2);
        Conversation convo = null;
        List<Conversation> convoList = (List) user1.getConversations();
        
        for(int i=0; i<convoList.size(); i++){
            if(user2.getConversations().contains(convoList.get(i))){
                convo = convoList.get(i);
            }
        }
        
        return convo;
    }
    
    //For stopping messages from being sent if a user left convo
    @Override
    public boolean checkEmptyUserInConversation(Long convoId){
        convoEntity = findConversation(convoId);
        List<User> userList = (List<User>) convoEntity.getUsers();
        
        if(userList.size() <= 1){
            return true;
        }
        
        return false;
    }
    
    @Override
    public Collection<Conversation> getConversationByUser(String username) {
        userEntity = findUser(username);
        conversations = userEntity.getConversations();
        return conversations;
    }
    
    @Override
    public Collection<Message> getMessageByConversation(Long id) {
        convoEntity = findConversation(id);
        messages = convoEntity.getMessages();
        return messages;
    }
    
    @Override
    public Collection<User> getAllUsers() {
        Collection<User> users = new ArrayList<User>();
        Query q = em.createQuery("Select u FROM BasicUser u");
        for (Object o : q.getResultList()) {
            userEntity = (User) o;
            users.add(userEntity);
        }
        return users;
    }
            
    @Override
    public User findUser(String username) {
        userEntity = null;
        try {
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    
    public Conversation findConversation(Long id) {
        convoEntity = null;
        try {
            Query q = em.createQuery("SELECT c FROM Conversation c WHERE c.id = :id");
            q.setParameter("id", id);
            convoEntity = (Conversation) q.getSingleResult();
            System.out.println("Conversation " + id + " found.");
        } catch (NoResultException e) {
            System.out.println("Conversation " + id + " does not exist.");
            convoEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convoEntity;
    }
    
    public Message findMessage(Long id) {
        msgEntity = null;
        try {
            Query q = em.createQuery("SELECT m FROM Message m WHERE m.id = :id");
            q.setParameter("id", id);
            msgEntity = (Message) q.getSingleResult();
            System.out.println("Message " + id + " found.");
        } catch (NoResultException e) {
            System.out.println("Message " + id + " does not exist.");
            msgEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgEntity;
    }
    
}
