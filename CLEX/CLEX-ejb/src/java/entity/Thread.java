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
public class Thread implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String username;

    @Column(length = 64, nullable = false)
    private String dateTime;

    @Column(length = 8000, nullable = false)
    private String content;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 32, nullable = false)
    private int upVote;

    @Column(length = 32, nullable = false)
    private int downVote;

    @Column(length = 32, nullable = false)
    private String tag; //KIV can give them many types (e.g "1": general, "2": reviews, "3": technical, "4": academic, "0": others

    @Column(length = 32, nullable = false)
    private boolean edited; //false = no (default), true = yes

    @Column(length = 32, nullable = false)
    private String editDateTime;

    @Column(length = 32, nullable = false)
    private String latestReplyDateTime;
        
    @Column(length = 32, nullable = false)
    private String school;

    @ManyToOne
    private User user;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "thread")
    private Collection<Reply> replies = new ArrayList<Reply>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "thread")
    private Collection<VoteThread> voteThreads = new ArrayList<VoteThread>();

    public Thread() {
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.dateTime = format.format(current);
        this.latestReplyDateTime = format.format(current);
    }

    public void createThread(String username, String content, String title, String tag, String school) {
        this.upVote = 0; //by default 0
        this.downVote = 0;
        this.username = username;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.edited = false;
        this.editDateTime = "";
        this.school = school;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public Collection<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Collection<Reply> replies) {
        this.replies = replies;
    }

    public Collection<VoteThread> getVoteThreads() {
        return voteThreads;
    }

    public void setVoteThreads(Collection<VoteThread> voteThreads) {
        this.voteThreads = voteThreads;
    }
    
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLatestReplyDateTime() {
        return latestReplyDateTime;
    }

    public void setLatestReplyDateTime(String latestReplyDateTime) {
        this.latestReplyDateTime = latestReplyDateTime;
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
        if (!(object instanceof Thread)) {
            return false;
        }
        Thread other = (Thread) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Thread[ id=" + id + " ]";
    }

}
