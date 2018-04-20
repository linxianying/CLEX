/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author lin
 */
@Entity(name="BasicUser")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 512, nullable = false)
    private String password;
    
    @Column(length = 64, nullable = false)
    private String username;
    
    @Column(length = 32, nullable = false)
    private String userType;
    
    @Column(length = 64, nullable = false)
    private String school;
    
    @Column(length = 32, nullable = false)
    private String email;
    
    @Column(length = 32, nullable = false)
    private Long contactNum;
    
    @Column(length = 64, nullable = false)
    private String name;
    
    @Column(length = 32, nullable = false)
    private int viewAnncCount; //by default = 0, used for announcement notification
    
    private String salt;
    
    @Column(length = 32, nullable = false)
    private boolean approval; //false - not approved, true - approved
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="user")
    private Collection<Announcement> announcements = new ArrayList<Announcement>();
    
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="BasicUser_Conversation")
    private Collection<Conversation> conversations = new ArrayList<Conversation>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="user")
    private Collection<Thread> threads = new ArrayList<Thread>();

    @OneToMany(cascade={CascadeType.ALL}, mappedBy="user")
    private Collection<Vote> votes = new ArrayList<Vote>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="user")
    private Collection<Reply> replys = new ArrayList<Reply>();
    
    @OneToMany(cascade={CascadeType.ALL})
    private Collection<Timeslot> timeslots = new ArrayList<Timeslot>();
    
    @OneToMany(cascade={CascadeType.ALL})
    private Collection<Task> tasks = new ArrayList<Task>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "orderer")
    private List<Order> orders = new ArrayList<Order>();
    
    public void createUser(String username, String password, String name, String email, String userType, String school, Long contactNum, String salt) {
        this.username = username;
        this.userType = userType;
        this.school = school;
        this.contactNum = contactNum;
        this.name = name;
        this.password = password;
        this.email = email;
        this.viewAnncCount = 0;
        this.salt = salt;
        //for easier test <disabled>
        //this.approval = true; 
        this.approval = false;
    }
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Collection<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Collection<Announcement> announcements) {
        this.announcements = announcements;
    }

    public Collection<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Collection<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Collection<Thread> getThreads() {
        return threads;
    }

    public void setThreads(Collection<Thread> threads) {
        this.threads = threads;
    }

    public Collection<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Collection<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
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

    public int getViewAnncCount() {
        return viewAnncCount;
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

    public void setViewAnncCount(int viewAnncCount) {
        this.viewAnncCount = viewAnncCount;
    }
          
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
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
