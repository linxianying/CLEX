/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;  
import javax.persistence.Column;

/**
 *
 * @author lin
 */
@Entity
public class Attendence implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 32, nullable = false)
    private Long studentId;
    
    @Column(length = 32, nullable = false)
    private String moduleCode;
    private List<String> attendStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void createAttendence(Long studentId, String moduleCode, String attendence) {
        this.studentId = studentId;
        this.moduleCode = moduleCode ;
        this.attendStatus.add(attendence);
    }
    
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public List<String> getAttendStatus() {
        return attendStatus;
    }

    public void setAttendStatus(List<String> attendStatus) {
        this.attendStatus = attendStatus;
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
        if (!(object instanceof Attendence)) {
            return false;
        }
        Attendence other = (Attendence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Attendence[ id=" + id + " ]";
    }
    
}
