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

    public void addPRQuestion(PeerReviewQuestion question, Question newQuestion, String addType);

    public void editIndQuestion(PeerReviewQuestion question, ArrayList<Question> individualQuestions);

    public void editGrQuestion(PeerReviewQuestion question, ArrayList<Question> groupQuestions);

    public void deleteIndQuestion(PeerReviewQuestion question, int indIndex);

    public void deleteGrQuestion(PeerReviewQuestion question, int indIndex);

    public void editTitle(PeerReviewQuestion question, String title);

    public void reorederIndQuestion(int fromIndex, int toIndex, PeerReviewQuestion question);

    public void reorederGrQuestion(int fromIndex, int toIndex, PeerReviewQuestion question);

    public void setGrQuestion(PeerReviewQuestion question, ArrayList<Question> groupQuestions);

    public void setIndQuestion(PeerReviewQuestion question, ArrayList<Question> individualQuestions);

    public void startPR(Module module, Date newDeadline);

    public void stopPR(Module module);

    public PeerReviewAnswer getPRAnswer(Module module, Student student, ProjectGroup group);

    public Student findStudent(String username);

    public ProjectGroup findProjectGroup(Long id);

    public ArrayList<PeerReviewAnswer> getAllSubmittedAnswer(Module module);
    
    public PeerReviewAnswer createPeerReviewAnswer(String username, Long groupId, Module module);

    public String getStudentInfo(String username);

    public PeerReviewQuestion findPRQuestionById(Long id);
    
}
