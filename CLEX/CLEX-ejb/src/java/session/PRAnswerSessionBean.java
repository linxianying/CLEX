/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Module;
import entity.PeerReviewAnswer;
import entity.PeerReviewQuestion;
import entity.ProjectGroup;
import entity.Student;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
public class PRAnswerSessionBean implements PRAnswerSessionBeanLocal {
    @PersistenceContext
    EntityManager em;
    
    private Student reviewer;
    private ProjectGroup group;
    private PeerReviewAnswer answers;
    private PeerReviewQuestion question;
    private ArrayList<Question> individualQuestions;
    private ArrayList<Question> groupQuestions;
    private Collection<Student> groupMembers;
    private HashMap<Student,ArrayList<String>> indAnswers;
    
    @Override
    public Student findStudent(String username){
        reviewer = null;
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            reviewer = (Student) q.getSingleResult();
//            System.out.println("Student " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            reviewer = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return reviewer;
    }
    
    @Override
    public ProjectGroup findProjectGroup(Long id) {
        try{
            Query q = em.createQuery("SELECT p FROM ProjectGroup p WHERE p.id = :id");
            q.setParameter("id", id);
            this.group = (ProjectGroup) q.getSingleResult();
//            System.out.println("ProjectGroup " + group.getName() + " for "
//                    + group.getSuperGroup().getModule().getCourse().getModuleCode() +" found.");
        }
        catch(NoResultException e){
            System.out.println("ProjectGroup " + group.getName() + " for "
                    + group.getSuperGroup().getModule().getCourse().getModuleCode() + " does not exist.");
            group = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return group;
    }
    
    @Override
    public void submitPRForm(ArrayList<Student> groupMembers, ArrayList<ArrayList<String>> individualAnswers, ArrayList<ArrayList<String>> groupAnswers, PeerReviewAnswer answers) {
        indAnswers = new HashMap<Student,ArrayList<String>>();
        for (int i=0; i<groupMembers.size(); i++) {
            indAnswers.put(groupMembers.get(i),individualAnswers.get(i));
        }
        answers.setIndAnswers(indAnswers);
        answers.setGrpAnswers(groupAnswers);
        answers.setSubmit(true);
        em.merge(answers);
    }
    
    @Override
    public PeerReviewQuestion findPRQuestionById(Long id) {
        try{
            Query q = em.createQuery("SELECT q FROM PeerReviewQuestion q WHERE q.id = :id");
            q.setParameter("id", id);
            question = (PeerReviewQuestion) q.getSingleResult();
        }
        catch(NoResultException e){
            System.out.println("PRQuestionSessionBean: PeerReviewQuestion does not exist.");
            question = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return question;
    }
    
    public boolean checkPRFormSubmit(Student student, Module module) {
        try{
            Query q = em.createQuery("SELECT q FROM PeerReviewAnswer q WHERE q.reviewer.id =:studentId AND q.question.id =:questionId");
            q.setParameter("studentId", student.getId());
            q.setParameter("questionId", module.getPeerReviewQuestion().getId());
            answers = (PeerReviewAnswer) q.getSingleResult();
            return answers.isSubmit();
        }
        catch(NoResultException e){
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

