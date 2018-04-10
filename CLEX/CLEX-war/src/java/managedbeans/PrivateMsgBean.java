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
    private String searchUsername;
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
    public void init() {
        refresh();
    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        sndUsername = (String) session.getAttribute("username");
        tabIndex = 0;
        searchUsername = null;
        rcvUsername = null;
        content = null;
        convo = null;
        msgList = null;
        convoExist = false;
        convoList = (List) msbl.getConversationByUser(sndUsername);
    }

    public String getRcvrUsername(Conversation convo) {
        List<User> userList = (List<User>) convo.getUsers();
        String rUsername = null;
        for (User u : userList) {
            if (!(sndUsername.equals(u.getUsername()))) {
                rUsername = u.getUsername();
                rcvName = u.getName();
            }
        }

        if (rUsername == null) {
            msgList = (List) convo.getMessages();
            for (int i = 0; i < msgList.size(); i++) {
                String owner = msgList.get(i).getMsgOwner();
                if (!sndUsername.equals(owner)) {
                    rUsername = owner;
                    rcvName = msbl.findUser(rUsername).getName();
                    break;
                }
            }
        }
        return rUsername;
    }

    public String getRcvName(Conversation convo) {
        msgList = (List) convo.getMessages();
        if (rcvUsername != null) {
            rcvName = msbl.findUser(rcvUsername).getName();
        } else {
            for (int i = 0; i < msgList.size(); i++) {
                Message temp = msgList.get(i);
                if (sndUsername.equals(temp.getMsgOwner())) {
                    rcvUsername = temp.getMsgReceiver();
                    rcvName = msbl.findUser(rcvUsername).getName();
                    break;
                }
            }
        }

        return rcvName;
    }

    public List<User> getUsers() {
        return (List) msbl.getAllUsers();
    }

    public void startConversation() {
        if (msbl.findUser(searchUsername) == null) {
            System.out.println("User does not exist. = " + searchUsername);
        } else {
            if (msbl.checkUserInSameConversation(sndUsername, searchUsername) != null) {
                convo = msbl.checkUserInSameConversation(sndUsername, searchUsername);
                msgList = (List) convo.getMessages();
                System.out.println("Conversation between " + sndUsername + " and " + searchUsername + " already exists.");
            } else {
                convo = msbl.createConversation(sndUsername, searchUsername);
                msgList = (List) convo.getMessages();
                System.out.println("Conversation between " + sndUsername + " and " + searchUsername + " created.");

            }
            rcvName = msbl.findUser(searchUsername).getName();
            refresh();
        }
    }

    public void selectConversation(Conversation selectedConvo) {
        convoExist = true;
        rcvUsername = getRcvrUsername(selectedConvo);
        convo = selectedConvo;
        msgList = (List) convo.getMessages();
        tabIndex = 1;
    }

    public boolean messageIsByUser(Message msg) {
        if (sndUsername.equals(msg.getMsgOwner())) {
            this.isUser = false;
        } else {
            this.isUser = true;
        }

        return isUser;
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
                if (msbl.createMessage(convo.getId(), sndUsername, rcvUsername, content)) {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent.", "");
                } else {
                    fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "");
                }
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to send message.", "Please ensure you are logged in.");
        }

        rcvName = getRcvName(convo);
        content = null;
        convoList = (List) msbl.getConversationByUser(sndUsername);
        convo = msbl.checkUserInSameConversation(sndUsername, rcvUsername);
        msgList = (List) msbl.getMessageByConversation(convo.getId());
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
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete conversation.", "");
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to leave conversation.", "Please ensure you are logged in.");
        }

        refresh();
        context.addMessage(null, fmsg);
    }

    public boolean getConversation(Long convoId) {
        msgList = (ArrayList<Message>) msbl.getMessageByConversation(convoId);
        if (convoId == null) {
            return false;
        }
        return true;
    }

    public boolean checkEmptyUser() {
        System.out.println(msbl.checkEmptyUserInConversation(convo.getId()));
        return msbl.checkEmptyUserInConversation(convo.getId());
    }

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

    public String getSearchUsername() {
        return searchUsername;
    }

    public void setSearchUsername(String searchUsername) {
        this.searchUsername = searchUsername;
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
