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
    private ArrayList<Question> individualQuestions;
    private ArrayList<Question> groupQuestions;
    
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
    
    @Override
    public void addPRQuestion(PeerReviewQuestion question, Question newQuestion, String addType) {
        if (addType.equals("indQuestion")) {
            individualQuestions = question.getIndividualQuestions();
            individualQuestions.add(newQuestion);
        }
        else if (addType.equals("grQuestion")) {
            groupQuestions = question.getGroupQuestions();
            groupQuestions.add(newQuestion);
        }
        em.merge(question);
        em.flush();
    }
    
    
    @Override
    public void editIndQuestion(PeerReviewQuestion question, ArrayList<Question> individualQuestions) {
        question.setIndividualQuestions(individualQuestions);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void editGrQuestion(PeerReviewQuestion question, ArrayList<Question> groupQuestions) {
        question.setGroupQuestions(groupQuestions);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void deleteIndQuestion(PeerReviewQuestion question, int indIndex) {
        question.getIndividualQuestions().remove(indIndex);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void deleteGrQuestion(PeerReviewQuestion question, int indIndex) {
        question.getGroupQuestions().remove(indIndex);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void editTitle(PeerReviewQuestion question, String title){
        question.setTitle(title);
        em.merge(question);
        em.flush();
    }
    
    
    
    
    
    
    public void testUpdatePRForm() {
        
    }
}
