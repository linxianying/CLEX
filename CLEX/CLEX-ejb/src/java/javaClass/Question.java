/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;

import java.io.Serializable;

/**
 *
 * @author caoyu
 */
public class Question implements Serializable{
    private String question;
    //type of question, can be open/ rating/ ranking
    private String type;
    //if the question type is rating, levelOfrating decides how many levels in total, 1-5 or 1-7 etc
    private int levelOfRating;

    public Question(String question, String type) {
        this.question = question;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevelOfRating() {
        return levelOfRating;
    }

    public void setLevelOfRating(int levelOfRating) {
        this.levelOfRating = levelOfRating;
    }
    
    
}
