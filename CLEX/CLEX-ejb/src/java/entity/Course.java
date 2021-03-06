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
import javax.persistence.OneToMany;

/**
 *
 * @author lin
 */
@Entity
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    @Column(length = 64, nullable = false)
    private String moduleCode;
    
    @Column(length = 256, nullable = false)
    private String moduleName;
    
    @Column(length = 2048)
    private String moduleInfo;
    
    @Column(length = 64)
    private boolean discontinuedBool;
    
    @Column(length = 64)
    private String discountinuedYear;
    
    @Column(length = 64)
    private String discountinuedSem;
    
    @Column(length = 256)
    private String offeredSem;
    
    @Column(length = 256)
    private String school;
    
    @Column(length = 128)
    private String workload;
    
    @Column(length = 32)
    private String modularCredits;
    
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="course")
    private Collection<Module> modules = new ArrayList<Module>();    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void createCourse(String moduleCode, String moduleName, String moduleInfo ,boolean discontinuedBool,
            String discountinuedYear, String discountinuedSem, String offeredSem, String school, String modularCredits
            ,String workload) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.moduleInfo = moduleInfo;
        this.workload = workload;
        this.discontinuedBool = discontinuedBool;
        this.discountinuedYear = discountinuedYear;
        this.discountinuedSem = discountinuedSem;
        this.offeredSem = offeredSem;
        this.school = school;
        this.modularCredits=modularCredits;
    }
    
    public Collection<Module> getModules() {
        return modules;
    }

    public void setModules(Collection<Module> modules) {
        this.modules = modules;
    }

    public String getModularCredits() {
        return modularCredits;
    }

    public void setModularCredits(String modularCredits) {
        this.modularCredits = modularCredits;
    }


    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public boolean isDiscontinuedBool() {
        return discontinuedBool;
    }

    public void setDiscontinuedBool(boolean discontinuedBool) {
        this.discontinuedBool = discontinuedBool;
    }

    public String getDiscountinuedYear() {
        return discountinuedYear;
    }

    public void setDiscountinuedYear(String discountinuedYear) {
        this.discountinuedYear = discountinuedYear;
    }

    public String getDiscountinuedSem() {
        return discountinuedSem;
    }

    public void setDiscountinuedSem(String discountinuedSem) {
        this.discountinuedSem = discountinuedSem;
    }

    public String getOfferedSem() {
        return offeredSem;
    }

    public void setOfferedSem(String offeredSem) {
        this.offeredSem = offeredSem;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
    
    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
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
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Course[ id=" + id + " ]";
    }
    
}
