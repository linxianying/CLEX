/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    
    @Column(length = 256, nullable = false)
    private String content;
    
    @Column(length = 64, nullable = false)
    private String title;
    
    @Column(length = 32, nullable = false)
    private int upVote;
    
    @Column(length = 32, nullable = false)
    private int downVote;
    
    @ManyToOne
    private User user;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="thread")
    private Collection<Reply> replies = new ArrayList<Reply>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="thread")
    private Collection<VoteThread> voteThreads = new ArrayList<VoteThread>();

    public void createThread(String username, String content, String dateTime, String title, 
                int upVote, int downVote, User user){
        this.username = username;
        this.content = content;
        this.dateTime = dateTime;
        this.upVote = upVote;
        this.downVote = downVote;
        this.title = title;
        this.user = user;
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
