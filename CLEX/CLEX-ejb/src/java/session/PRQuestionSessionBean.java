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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author caoyu
 */
@Stateless
public class PRQuestionSessionBean implements PRQuestionSessionBeanLocal {
    @PersistenceContext
    EntityManager em;
    
    private PeerReviewQuestion question;
    
    @Override
    public void createPeerReviewQuestion(String title, Date deadline, Module module) {
        question = new PeerReviewQuestion();
        question.createPeerReviewQuestion(title, deadline, module);
        em.persist(question);
        module.setPeerReviewQuestion(question);
        em.merge(module);
        em.flush();
    }
    
    @Override
    public void updatePRForm(PeerReviewQuestion question, ArrayList<Question> individualQuestions, ArrayList<Question> groupQuestions) {
        question.setIndividualQuestions(individualQuestions);
        question.setGroupQuestions(groupQuestions);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public PeerReviewQuestion findPRQuestion(Module module) {
        try{
            Query q = em.createQuery("Select q From PeerReviewQuestion Where q.module.id=: id");
            q.setParameter("id", module.getId());
            question = (PeerReviewQuestion) q.getSingleResult();
        }
        catch(NoResultException e){
            System.out.println("PeerReviewQuestion for module " + module.getCourse().getModuleCode() + " does not exist.");
            question = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return question;
    }
    
    public void testUpdatePRForm() {
        
    }
}