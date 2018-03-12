/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javaClass.Question;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author caoyu
 */
@Entity
public class PeerReviewQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    //questions for a spesific group member
    private ArrayList<Question> individualQuestions;
    //questions for the whole group or all group members
    private ArrayList<Question> groupQuestions;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deadline;
    
    @OneToOne(mappedBy = "peerReviewQuestion")
    private Module module;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "question")
    private Collection<PeerReviewAnswer> answer;

    
    public void createPeerReviewQuestion(String title, Date deadline, Module module) {
        this.title = title;
        this.deadline = deadline;
        this.module = module;
        //pre settled question
        Question q = new Question("how do you rate this group member's cooperativeness", "rating");
        q.setLevelOfRating(5);
        individualQuestions.add(q);
        q = new Question("how do you rate this group member's punctuality", "rating");
        q.setLevelOfRating(5);
        individualQuestions.add(q);
        q = new Question("how do you rate this group member's commitment to the project", "rating");
        q.setLevelOfRating(5);
        individualQuestions.add(q);
        q = new Question("how do you rate this group member's contribution", "rating");
        q.setLevelOfRating(7);
        individualQuestions.add(q);
        q = new Question("what are the advantages of this group member", "open");
        individualQuestions.add(q);
        q = new Question("what are the advantages of this group member", "open");
        individualQuestions.add(q);
        q = new Question("what do you think of this project", "open");
        groupQuestions.add(q);
        q = new Question("ny other comment for this group", "open");
        groupQuestions.add(q);
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Collection<PeerReviewAnswer> getAnswer() {
        return answer;
    }

    public void setAnswer(Collection<PeerReviewAnswer> answer) {
        this.answer = answer;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public ArrayList<Question> getIndividualQuestions() {
        return individualQuestions;
    }

    public void setIndividualQuestions(ArrayList<Question> individualQuestions) {
        this.individualQuestions = individualQuestions;
    }

    public ArrayList<Question> getGroupQuestions() {
        return groupQuestions;
    }

    public void setGroupQuestions(ArrayList<Question> groupQuestions) {
        this.groupQuestions = groupQuestions;
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
        if (!(object instanceof PeerReviewQuestion)) {
            return false;
        }
        PeerReviewQuestion other = (PeerReviewQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PeerreviewQuestion[ id=" + id + " ]";
    }
    
}
