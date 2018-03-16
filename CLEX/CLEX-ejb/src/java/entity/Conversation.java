/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author eeren
 */
@Entity
public class Conversation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String startDateTime;
    
    @Column(length = 32, nullable = false)
    private int sentMsgCount1;
    
    @Column(length = 32, nullable = false)
    private int readMsgCount1; // (sent2 - read1) = no. of unread for user1
    
    @Column(length = 32, nullable = false)
    private int sentMsgCount2;
    
    @Column(length = 32, nullable = false)
    private int readMsgCount2; // (sent1 - read2) = no. of unread for user2
    
    @ManyToMany(cascade={CascadeType.ALL}, mappedBy="conversations")
    private Collection<User> users = new ArrayList<User>(2); //one-to-one only
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="conversation")
    private Collection<Message> messages = new ArrayList<Message>();
    
    public Conversation(){
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.startDateTime = format.format(current);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getSentMsgCount1() {
        return sentMsgCount1;
    }

    public void setSentMsgCount1(int sentMsgCount1) {
        this.sentMsgCount1 = sentMsgCount1;
    }

    public int getReadMsgCount1() {
        return readMsgCount1;
    }

    public void setReadMsgCount1(int readMsgCount1) {
        this.readMsgCount1 = readMsgCount1;
    }

    public int getSentMsgCount2() {
        return sentMsgCount2;
    }

    public void setSentMsgCount2(int sentMsgCount2) {
        this.sentMsgCount2 = sentMsgCount2;
    }

    public int getReadMsgCount2() {
        return readMsgCount2;
    }

    public void setReadMsgCount2(int readMsgCount2) {
        this.readMsgCount2 = readMsgCount2;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conversation)) {
            return false;
        }
        Conversation other = (Conversation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Conversation[ id=" + id + " ]";
    }
    
}
