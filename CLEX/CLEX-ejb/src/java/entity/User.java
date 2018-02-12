/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author lin
 */
@Entity(name="BasicUser")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String password;
    private String username;
    private String userType;
    private String school;
    private String email;
    private Long contactNum;
    private String name;
    

    @OneToMany(cascade={CascadeType.ALL}, mappedBy="user")
    private Collection<Thread> threads = new ArrayList<Thread>();

    @OneToMany(cascade={CascadeType.PERSIST})
    private Collection<Vote> votes = new ArrayList<Vote>();
    
    @OneToMany(cascade={CascadeType.PERSIST})
    private Collection<Reply> replys = new ArrayList<Reply>();
    
    
    public void createUser(String username, String password, String name, String email, String userType, String school, Long contactNum) {
        this.username = username;
        this.userType = userType;
        this.school = school;
        this.contactNum = contactNum;
        this.name = name;
        this.password = password;
        this.email = email;
    }
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Collection<Thread> getThreads() {
        return threads;
    }

    public void setThreads(Collection<Thread> threads) {
        this.threads = threads;
    }

    public Collection<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Collection<Vote> votes) {
        this.votes = votes;
    }

    public Collection<Reply> getReplys() {
        return replys;
    }

    public void setReplys(Collection<Reply> replys) {
        this.replys = replys;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public String getSchool() {
        return school;
    }

    public String getEmail() {
        return email;
    }

    public Long getContactNum() {
        return contactNum;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactNum(Long contactNum) {
        this.contactNum = contactNum;
    }

    public void setName(String name) {
        this.name = name;
    }
          
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.User[ id=" + id + " ]";
    }
    
}
