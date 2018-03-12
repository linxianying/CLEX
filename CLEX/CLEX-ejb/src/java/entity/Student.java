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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author lin
 */
@Entity
public class Student extends User implements Serializable {
    @OneToMany(mappedBy = "reviewer")
    private List<PeerReviewAnswer> peerReviewAnswers;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 32)
    private String faculty;
    
    @Column(length = 32)
    private String major;
    
    @Column(length = 32)
    private String matricYear = "";
    
    @Column(length = 32)
    private String matricSem = "";
    //private String currentYear;
    @Column(length = 32)
    private double cap = 0.0;
    
    @ManyToMany(cascade={CascadeType.PERSIST}, mappedBy="students")
    @JoinTable(name="Student_Module")
    private Collection<Module> modules = new ArrayList<Module>();
    
    @ManyToMany(cascade={CascadeType.PERSIST}, mappedBy="students")
    @JoinTable(name="Student_Lesson")
    private Collection<Lesson> lessons = new ArrayList<Lesson>();

    @OneToMany(cascade={CascadeType.ALL}, mappedBy="student")
    private Collection<Grade> grades = new ArrayList<Grade>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="student")
    private Collection<Ledger> ledgers = new ArrayList<Ledger>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="student")
    private Collection<StudyPlan> studyPlan = new ArrayList<StudyPlan>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="student")
    private Collection<Task> tasks = new ArrayList<Task>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="student")
    private Collection<IndividualGroupTask> IndividualGroupTasks = new ArrayList<IndividualGroupTask>();
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable(name="Student_ProjectGroup")
    private Collection<ProjectGroup> projectGroups = new ArrayList<ProjectGroup>();
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable(name="Student_GroupTimeSlot")
    private Collection<GroupTimeslot> groupTimeslots = new ArrayList<GroupTimeslot>();
    
    @OneToMany(mappedBy = "reviewer")
    private Collection<PeerReviewAnswer> asReviewer;
    
    @OneToMany(mappedBy = "reviewee")
    private Collection<PeerReviewAnswer> asReviewee;
    
    public void createStudent(String username, String password, String name, 
                String email, String school, Long contactNum, String salt,
                String faculty, String major, String matricYear, String matricSem, 
                double cap){
        super.createUser(username, password, name, email, "Student", school, contactNum, salt);
        this.faculty = faculty;
        this.major = major;
        this.matricYear = matricYear;
        this.matricSem = matricSem;
        this.cap = cap;
    }
    
    public Collection<ProjectGroup> getProjectGroups() {
        return projectGroups;
    }

    public void setProjectGroups(Collection<ProjectGroup> projectGroups) {
        this.projectGroups = projectGroups;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Collection<StudyPlan> getStudyPlan() {
        return studyPlan;
    }

    public void setStudyPlan(Collection<StudyPlan> studyPlan) {
        this.studyPlan = studyPlan;
    }

    public Collection<GroupTimeslot> getGroupTimeslots() {
        return groupTimeslots;
    }

    public void setGroupTimeslots(Collection<GroupTimeslot> groupTimeslots) {
        this.groupTimeslots = groupTimeslots;
    }
    
    public Collection<Ledger> getLedgers() {
        return ledgers;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public void setLedgers(Collection<Ledger> ledgers) {
        this.ledgers = ledgers;
    }
    
    public Collection<Module> getModules() {
        return modules;
    }

    public void setModules(Collection<Module> modules) {
        this.modules = modules;
    }

    public Collection<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Collection<Grade> grades) {
        this.grades = grades;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMatricYear() {
        return matricYear;
    }

    public void setMatricYear(String matricYear) {
        this.matricYear = matricYear;
    }

    public String getMatricSem() {
        return matricSem;
    }

    public void setMatricSem(String matricSem) {
        this.matricSem = matricSem;
    }

    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }

    public Collection<IndividualGroupTask> getIndividualGroupTasks() {
        return IndividualGroupTasks;
    }

    public void setIndividualGroupTasks(Collection<IndividualGroupTask> IndividualGroupTasks) {
        this.IndividualGroupTasks = IndividualGroupTasks;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PeerReviewAnswer> getPeerReviewAnswers() {
        return peerReviewAnswers;
    }

    public void setPeerReviewAnswers(List<PeerReviewAnswer> peerReviewAnswers) {
        this.peerReviewAnswers = peerReviewAnswers;
    }

    public Collection<PeerReviewAnswer> getAsReviewer() {
        return asReviewer;
    }

    public void setAsReviewer(Collection<PeerReviewAnswer> asReviewer) {
        this.asReviewer = asReviewer;
    }

    public Collection<PeerReviewAnswer> getAsReviewee() {
        return asReviewee;
    }

    public void setAsReviewee(Collection<PeerReviewAnswer> asReviewee) {
        this.asReviewee = asReviewee;
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
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.getName()+", username: " + super.getUsername();
    }
    
}
