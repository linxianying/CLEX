/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
public class PeerReviewAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private HashMap<Student,ArrayList<String>> indAnswers = new HashMap<Student,ArrayList<String>>();
    //there are ranking questions whose answer is an AL<String username>; others answer is the get(question index).get(0)
    private ArrayList<ArrayList<String>> grpAnswers = new ArrayList<ArrayList<String>>();
    //whether the student submit or not, decides whether the lecturer can view or not
    private boolean submit;
    @ManyToOne
    private Student reviewer;
//    
//    @ManyToOne
//    private Student reviewee;
    
    @ManyToOne
    private PeerReviewQuestion question;
    
    @ManyToOne
    private ProjectGroup projectGroup;

    public void createPeerReviewAnswer(Student reviewer, PeerReviewQuestion question, ProjectGroup projectGroup) {
        this.reviewer = reviewer;
        this.question = question;
        this.projectGroup = projectGroup;
        submit=false;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getReviewer() {
        return reviewer;
    }

    public void setReviewer(Student reviewer) {
        this.reviewer = reviewer;
    }

    public PeerReviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(PeerReviewQuestion question) {
        this.question = question;
    }

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        this.projectGroup = projectGroup;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public ArrayList<ArrayList<String>> getGrpAnswers() {
        return grpAnswers;
    }

    public void setGrpAnswers(ArrayList<ArrayList<String>> grpAnswers) {
        this.grpAnswers = grpAnswers;
    }

    public HashMap<Student, ArrayList<String>> getIndAnswers() {
        return indAnswers;
    }

    public void setIndAnswers(HashMap<Student, ArrayList<String>> indAnswers) {
        this.indAnswers = indAnswers;
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
        if (!(object instanceof PeerReviewAnswer)) {
            return false;
        }
        PeerReviewAnswer other = (PeerReviewAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PeerReviewAnswer[ id=" + id + " ]";
    }
    
}
