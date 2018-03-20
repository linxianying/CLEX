/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Module;
import entity.PeerReviewQuestion;
import java.util.ArrayList;
import java.util.Date;
import javaClass.Question;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface PRQuestionSessionBeanLocal {

    public PeerReviewQuestion findPRQuestion(Module module);

    public void createPeerReviewQuestion(String title, Date deadline, Module module);

    public void updatePRForm(PeerReviewQuestion question, ArrayList<Question> individualQuestions, ArrayList<Question> groupQuestions);
    
}
