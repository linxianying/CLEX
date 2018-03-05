/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author caoyu
 */
@Entity
public class GroupTimeslot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 64, nullable = false)
    private String date;
    
    @Column(length = 64, nullable = false)
    private String timeFrom;
    
    @Column(length = 64, nullable = false)
    private String timeEnd;
    
    @Column(length = 32, nullable = false)
    private String title;
    
    @Column(length = 32, nullable = false)
    private String details;
    
    @Column(length = 32, nullable = false)
    private String venue;
    
    @ManyToOne
    private ProjectGroup projectGroup = new ProjectGroup();

    public void createGroupTimeslot(String date, String timeFrom, String timeEnd, 
                String title, String details, String venue, ProjectGroup projectGroup) {
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeEnd = timeEnd;
        this.details = details;
        this.title = title;
        this.venue = venue;
        this.projectGroup = projectGroup;
    }

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        this.projectGroup = projectGroup;
    }
    
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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
        if (!(object instanceof GroupTimeslot)) {
            return false;
        }
        GroupTimeslot other = (GroupTimeslot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GroupTimeslot[ id=" + id + " ]";
    }
    
}
