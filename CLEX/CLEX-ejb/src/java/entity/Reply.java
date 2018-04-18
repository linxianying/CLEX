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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author lin
 */
@Entity
public class Reply implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long threadId;
    
    @Column(length = 32, nullable = false)
    private String dateTime;
    
    @Column(length = 8000, nullable = false)
    private String content;
    
    @Column(length = 32, nullable = false)
    private int upVote;
    
    @Column(length = 32, nullable = false)
    private int downVote;

    @Column(length = 32, nullable = false)
    private boolean edited; //false = no (default), true = yes
    
    @Column(length = 32, nullable = false)
    private String editDateTime;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Thread thread;

    @OneToMany(cascade={CascadeType.ALL}, mappedBy="reply")
    private Collection<VoteReply> voteReplies = new ArrayList<VoteReply>();
        
    public Reply(){
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.dateTime = format.format(current);
    }
    
    public void createReply(Long threadId, String content){
        this.threadId = threadId;
        this.upVote = 0; //by default 0
        this.downVote = 0;
        this.content = content;
        this.edited = false;
        this.editDateTime = "";
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public String getEditDateTime() {
        return editDateTime;
    }

    public void setEditDateTime(String editDateTime) {
        this.editDateTime = editDateTime;
    }

    public Collection<VoteReply> getVoteReplies() {
        return voteReplies;
    }

    public void setVoteReplies(Collection<VoteReply> voteReplies) {
        this.voteReplies = voteReplies;
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
        if (!(object instanceof Reply)) {
            return false;
        }
        Reply other = (Reply) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reply[ id=" + id + " ]";
    }
    
}
