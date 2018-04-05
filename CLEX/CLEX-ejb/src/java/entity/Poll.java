/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author lin
 */
@Entity
public class Poll implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 32, nullable = false)
    private String datetime;
    
    @Column(length = 32, nullable = false)
    private String topic;
    
    @Column(length = 32, nullable = false)
    private String type;
    
    @Column(length = 256, nullable = false)
    private String content;
    
    @Column(length = 32)
    private double correctRate;
    
    @Column(length = 32)
    private String status;
    
    private ArrayList<String> answers = new ArrayList<String>();
    
    private int correctAns;
    
    private int total;
    private int correct;
    
    @ManyToOne
    private Module module;


    public void createPoll(String datetime, String topic, double correctRate, String type, 
                String content){
        this.datetime = datetime;
        this.topic = topic;
        this.type = type;
        this.correctRate = correctRate;
        this.content = content;
        this.status = "finished";
    }
    
    public void createPoll(String datetime, String topic, double correctRate, String type, 
                String content, String status){
        this.datetime = datetime;
        this.topic = topic;
        this.type = type;
        this.correctRate = correctRate;
        this.content = content;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public double getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(double correctRate) {
        this.correctRate = correctRate;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
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
        if (!(object instanceof Poll)) {
            return false;
        }
        Poll other = (Poll) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "entity.Poll[ id=" + id + " ]";
    }
    
}
