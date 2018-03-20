/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Conversation;
import entity.Message;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import session.MessageSessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class PrivateMsgBean {

    @EJB
    private MessageSessionBeanLocal msbl;

    private User sender;
    private String sUsername;
    
    private User receiver;
    private String rUsername;
    
    private Conversation convo;
    private Long cId;
    
    private Message msg;
    private String content;
    
    private ArrayList<Conversation> convoList;
    private ArrayList<Message> msgList;
    
    FacesContext context;
    HttpSession session;
    
    public PrivateMsgBean() {
    }
    
    @PostConstruct
    public void init(){
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sUsername = (String) session.getAttribute("username");
        convoList = (ArrayList) msbl.getConversationByUser(sUsername);
    }
    
    public void sendNewMessage() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sUsername = (String) session.getAttribute("username");
        sender = msbl.findUser(sUsername);

        if(sender != null){
            receiver = msbl.findUser(rUsername);
            if(receiver != null){
                if(startConversation(sUsername, rUsername, content)){
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent.", "");
                }
                else{
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to start conversation.", "Conversation already exists.");
                }
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "The user you are sending to no longer exists.");
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }
    
    //From sendNewMessage
    public boolean startConversation(String sUsername, String rUsername, String content) {
        
        if(msbl.checkUserInSameConversation(sUsername, rUsername)){
            System.out.println("Conversation between " + sUsername + " and " + rUsername + " already exists.");
            return false;
        }
    
        if(msbl.createConversation(sUsername, rUsername, content)){
            System.out.println("Conversation between " + sUsername + " and " + rUsername + " created.");
            return true;
        }
        
        System.out.println("Failed to start conversation");
        return false;
    }
    
    public void sendMessage() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sUsername = (String) session.getAttribute("username");
        sender = msbl.findUser(sUsername);

        if(sender != null){
            if(msbl.checkEmptyUserInConversation(cId)){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "A user has already left conversation.");
            }
            else{
                if(msbl.createMessage(cId, sUsername, content)){
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent.", "");
                }
                else{
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "");
                }
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }
    
    public void deleteConversation() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sUsername = (String) session.getAttribute("username");
        sender = msbl.findUser(sUsername);

        if(sender != null){
            if(msbl.deleteConversation(sUsername, cId)){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Left conversation.", "");
            }
            else{
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete conversation.", "");
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to leave conversation.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }

    /* DEBUGGING CODE
    public void test(){
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sUsername = (String) session.getAttribute("username");
        sender = msbl.findUser(sUsername);
        
        List<Conversation> user1convos = (List) sender.getConversations();
        List<User> users;
        List<Message> user1messages;
        List<Conversation> user2convos;
        List<Message> user2messages;
        
        for(int i=0; i<user1convos.size(); i++){
            System.out.println("TestprintUser1Convo" + i + ": " + user1convos.get(i).getStartDateTime());
            users = (List) user1convos.get(i).getUsers();
            
            if(users.size() == 1){
                if(users.get(0) != null){
                    user1messages = (List) user1convos.get(i).getMessages();
                    for(int j=0; j<user1messages.size(); j++){
                        System.out.println("Testuser1(" + j + "): " + user1messages.get(j).getDateTime());
                    }
                }
            }
            
            if(users.size() == 2){
                if(users.get(1) != null){
                    user2convos = (List) users.get(1).getConversations();
                    user2messages = (List) user2convos.get(i).getMessages();
                    for(int k=0; k<user2messages.size(); k++){
                        System.out.println("Testuser2(" + k + "): " + user2messages.get(k).getDateTime());
                    } 
                }
            }
        }
    }
    */
    
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getsUsername() {
        return sUsername;
    }

    public void setsUsername(String sUsername) {
        this.sUsername = sUsername;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getrUsername() {
        return rUsername;
    }

    public void setrUsername(String rUsername) {
        this.rUsername = rUsername;
    }

    public Conversation getConvo() {
        return convo;
    }

    public void setConvo(Conversation convo) {
        this.convo = convo;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Conversation> getConvoList() {
        return convoList;
    }

    public void setConvoList(ArrayList<Conversation> convoList) {
        this.convoList = convoList;
    }

    public ArrayList<Message> getMsgList() {
        return msgList;
    }

    public void setMsgList(ArrayList<Message> msgList) {
        this.msgList = msgList;
    }
    
    
}
