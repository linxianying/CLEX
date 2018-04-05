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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import session.MessageSessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@SessionScoped
public class PrivateMsgBean {

    @EJB
    private MessageSessionBeanLocal msbl;

    private User sender;
    private String sndUsername;

    private User receiver;
    private String rcvUsername;
    private String rcvName;

    private Conversation convo;
    private Long convoId;

    private Message msg;
    private String content;

    private List<Conversation> convoList;
    private List<Message> msgList;
    private int tabIndex;
    private boolean isUser;
    private boolean convoExist;

    FacesContext context;
    HttpSession session;

    public PrivateMsgBean() {
    }

    @PostConstruct
    public void init(){
        refresh();
    }
    
    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sndUsername = (String) session.getAttribute("username");
        tabIndex = 0;
        rcvUsername = null;
        content = null;
        convo = null;
        msgList = null;
        convoExist = false;
        convoList = (List) msbl.getConversationByUser(sndUsername);
    }
    /*
     public void sendNewMessage() throws IOException {
     FacesMessage fmsg = new FacesMessage();
     context = FacesContext.getCurrentInstance();
     session = (HttpSession) context.getExternalContext().getSession(true);
     sndUsername = (String) session.getAttribute("username");
     sender = msbl.findUser(sndUsername);

     if (sender != null) {
     receiver = msbl.findUser(rcvUsername);
     if (receiver != null) {
     if (startConversation(sndUsername, rcvUsername)) {
     fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent.", "");
     } else {
     fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to start conversation.", "Conversation already exists.");
     }
     } else {
     fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "The user you are sending to no longer exists.");
     }
     } else {
     fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "Please ensure you are logged in.");
     }
     context.addMessage(null, fmsg);
     }*/

    //From sendNewMessage
    public void startConversation() {
        if (msbl.findUser(rcvUsername) == null) {
            System.out.println("User does not exist. = " + rcvUsername);
        } else {
            rcvName = msbl.findUser(rcvUsername).getName();
            convoExist = true;
            if (msbl.checkUserInSameConversation(sndUsername, rcvUsername) != null) {
                convo = msbl.checkUserInSameConversation(sndUsername, rcvUsername);
                msgList = (List) convo.getMessages();
                System.out.println("Conversation between " + sndUsername + " and " + rcvUsername + " already exists.");
            } else {
                convo = msbl.createConversation(sndUsername, rcvUsername);
                msgList = (List) convo.getMessages();
                System.out.println("Conversation between " + sndUsername + " and " + rcvUsername + " created.");
            }
            refresh();
        }
    }

    public void selectConversation(String username) {
        if (username != null) {
            convoExist = true;
            rcvName = msbl.findUser(username).getName();
            rcvUsername = username;
            convo = msbl.checkUserInSameConversation(sndUsername, rcvUsername);
            msgList = (List) convo.getMessages();
            tabIndex = 1;
        }
    }

    public void sendMessage() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sndUsername = (String) session.getAttribute("username");
        sender = msbl.findUser(sndUsername);

        if (sender != null) {
            if (msbl.checkEmptyUserInConversation(convo.getId())) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "A user has already left conversation.");
            } else {
                if (msbl.createMessage(convo.getId(), sndUsername, content)) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent.", "");
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "");
                }
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "Please ensure you are logged in.");
        }
        msgList = (List) msbl.getMessageByConversation(convo.getId());
        content = null;
        context.addMessage(null, fmsg);
    }

    public void deleteConversation() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sndUsername = (String) session.getAttribute("username");
        sender = msbl.findUser(sndUsername);

        if (sender != null) {
            if (msbl.deleteConversation(sndUsername, convo.getId())) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Left conversation.", "");
                refresh();
                convo = null;
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete conversation.", "");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to leave conversation.", "Please ensure you are logged in.");
        }
        context.addMessage(null, fmsg);
    }

    public boolean getConversation(Long convoId) {
        msgList = (ArrayList<Message>) msbl.getMessageByConversation(convoId);
        if (convoId == null) {
            return false;
        }
        return true;
    }

    public void messageIsByUser(Message msg) {
        if (msg.getSentUser() == 1) {
            this.isUser = true;
        } else {
            this.isUser = false;
        }
    }

    /* DEBUGGING CODE
     public void test(){
     FacesMessage fmsg = new FacesMessage();
     context = FacesContext.getCurrentInstance();
     session = (HttpSession) context.getExternalContext().getSession(true);
     sndUsername = (String) session.getAttribute("username");
     sender = msbl.findUser(sndUsername);
        
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Conversation getConvo() {
        return convo;
    }

    public void setConvo(Conversation convo) {
        this.convo = convo;
    }

    public Long getConvoId() {
        return convoId;
    }

    public void setConvoId(Long convoId) {
        this.convoId = convoId;
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

    public List<Conversation> getConvoList() {
        return convoList;
    }

    public void setConvoList(List<Conversation> convoList) {
        this.convoList = convoList;
    }

    public List<Message> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Message> msgList) {
        this.msgList = msgList;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public String getSenderUsername() {
        return sndUsername;
    }

    public void setSenderUsername(String sndUsername) {
        this.sndUsername = sndUsername;
    }

    public String getRcvUsername() {
        return rcvUsername;
    }

    public void setRcvUsername(String rcvUsername) {
        this.rcvUsername = rcvUsername;
    }

    public String getRcvName() {
        return rcvName;
    }

    public void setRcvName(String rcvName) {
        this.rcvName = rcvName;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean getConvoExist() {
        return convoExist;
    }

    public void setConvoExist(boolean convoExist) {
        this.convoExist = convoExist;
    }
}
