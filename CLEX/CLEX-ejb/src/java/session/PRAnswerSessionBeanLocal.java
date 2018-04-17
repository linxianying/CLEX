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
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface PRAnswerSessionBeanLocal {

    public Student findStudent(String username);

    public ProjectGroup findProjectGroup(Long id);
    
    public void submitPRForm(ArrayList<Student> groupMembers, ArrayList<ArrayList<String>> individualAnswers, ArrayList<ArrayList<String>> groupAnswers, PeerReviewAnswer answers);

    public PeerReviewQuestion findPRQuestionById(Long id);

    public boolean checkPRFormSubmit(Student student, Module module);

    public ArrayList<ArrayList<String>> getIndAnswers(ArrayList<Student> groupMembers, PeerReviewAnswer answers);

}
