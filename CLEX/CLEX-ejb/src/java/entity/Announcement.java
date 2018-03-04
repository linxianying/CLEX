/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author eeren
 */
@Entity
public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String title;
    private String message;
    private String createdDate;
    private String type; //Input "admin": admin announcement, "<modulecode>": lecturer announcement
    private String audience; //Input "1": all, "2": students only, "3": lecturers only, "4": guests only, "5": 2&3 (admin can use any, lecturer's default use 2)
    
    @ManyToOne
    private User user;
    
    public Announcement(){
        Date current = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.createdDate = format.format(current);
    }
    
    public void lecturerCreate(String title, String message, String type){
        this.title = title;
        this.message = message;
        this.type = type; //module code
        this.audience = "0"; // can only send to students
    }
    
    public void adminCreate(String title, String message, String audience){
        this.title = title;
        this.message = message;
        this.type = "admin"; //by default, admin
        this.audience = audience; //selectable by admin
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Announcement[ id=" + id + " ]";
    }
    
}
