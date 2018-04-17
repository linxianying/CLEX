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
import java.util.Date;
import java.util.List;
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
    
    private Student student;
    private PeerReviewQuestion question;
    private ArrayList<Question> individualQuestions;
    private ArrayList<Question> groupQuestions;
    private PeerReviewAnswer answer;
    private Student reviewer;
    private ProjectGroup group;
    private ArrayList<PeerReviewAnswer> answers;
    
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
        Long id = module.getPeerReviewQuestion().getId();
        return this.findPRQuestionById(id);
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
    
    @Override
    public void reorederIndQuestion(int fromIndex, int toIndex, PeerReviewQuestion question){
        individualQuestions = question.getIndividualQuestions();
        individualQuestions.add(toIndex, individualQuestions.remove(fromIndex));
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void reorederGrQuestion(int fromIndex, int toIndex, PeerReviewQuestion question){
        groupQuestions = question.getGroupQuestions();
        groupQuestions.add(toIndex, groupQuestions.remove(fromIndex));
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void setGrQuestion(PeerReviewQuestion question, ArrayList<Question> groupQuestions) {
        question.setGroupQuestions(groupQuestions);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void setIndQuestion(PeerReviewQuestion question, ArrayList<Question> individualQuestions) {
        question.setIndividualQuestions(individualQuestions);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void startPR(Module module, Date newDeadline) {
        question = module.getPeerReviewQuestion();
        question.setStatus("start");
        if (newDeadline != null)
            question.setDeadline(newDeadline);
        em.merge(question);
        em.flush();
    }
    
    @Override
    public void stopPR(Module module) {
        question = module.getPeerReviewQuestion();
        question.setStatus("end");
        em.merge(question);
        em.flush();
    }
    
    
    @Override
    public PeerReviewAnswer getPRAnswer(Module module, Student student, ProjectGroup group) {
        question = this.findPRQuestion(module);
        try{
            Query q = em.createQuery("SELECT q FROM PeerReviewAnswer q WHERE q.reviewer.id =:studentId AND q.question.id =:questionId AND q.projectGroup.id=:groupId");
            q.setParameter("studentId", student.getId());
            q.setParameter("questionId", module.getPeerReviewQuestion().getId());
            q.setParameter("groupId", group.getId());
//            Query q = em.createQuery("Select a From PeerReviewAnswer a Where a.reviewer.id=: studentId and a.question.id=: questionId and a.projectGroup.id=:groupId");
//            q.setParameter("studentId", student.getId());
//            q.setParameter("questionId", student.getId());
//            q.setParameter("groupId", student.getId());
            answer = (PeerReviewAnswer) q.getSingleResult();
        }
        catch(NoResultException e){
            System.out.println("Create a new Answer");
            answer = this.createPeerReviewAnswer(student.getUsername(), group.getId(), module);
            System.out.println("Finish create a new Answer " + answer);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return answer;
    }
    
    
    @Override
    public PeerReviewAnswer createPeerReviewAnswer(String username, Long groupId, Module module) {
        reviewer = this.findStudent(username);
        group = this.findProjectGroup(groupId);
        question = this.findPRQuestion(module);
        answer = new PeerReviewAnswer();
        answer.createPeerReviewAnswer(reviewer, question, group);
        reviewer.getPeerReviewAnswers().add(answer);
        question.getAnswer().add(answer);
        group.getAnswers().add(answer);
        em.persist(answer);
        em.merge(reviewer);
        em.merge(question);
        em.merge(group);
        em.flush();
        return answer;
    }
    
    @Override
    public ArrayList<PeerReviewAnswer> getAllSubmittedAnswer(Module module){
        answers = new ArrayList<PeerReviewAnswer>();
        List<PeerReviewAnswer> all = new ArrayList<PeerReviewAnswer>();
        try{
            Query q = em.createQuery("SELECT a FROM PeerReviewAnswer a WHERE a.question.module = :id");
            q.setParameter("id", module.getId());
            all = q.getResultList();
            for (PeerReviewAnswer a:all )
                answers.add(a);
        }
        catch(NoResultException e){
            System.out.println("PRQuestionSessionBean: getAllSubmittedAnswer: No student submit a pr form for " + module.getCourse().getModuleCode());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return answers;
    }
    
    @Override
    public String getStudentInfo(String username){
        try{
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            student = (Student) q.getSingleResult();
        }
        catch(NoResultException e){
            System.out.println("Student " + username + " does not exist.");
            student = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return student.getName() + ", " + student.getStudentId();
    }
}
