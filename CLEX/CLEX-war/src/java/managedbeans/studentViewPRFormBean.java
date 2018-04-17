/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Module;
import entity.PeerReviewAnswer;
import entity.PeerReviewQuestion;
import entity.ProjectGroup;
import entity.Student;
import java.util.ArrayList;
import javaClass.Question;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.PRAnswerSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "studentViewPRFormBean")
@SessionScoped
public class studentViewPRFormBean {
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private PRQuestionSessionBeanLocal prqsbl;
    @EJB
    private PRAnswerSessionBeanLocal prasbl;
    
    public studentViewPRFormBean() {
    }
    
    FacesContext context;
    HttpSession session;
    
    private Student student;
    private String username;
    private Module module;
    private ProjectGroup group;
    private PeerReviewQuestion question;
    private PeerReviewAnswer answers;
    private ArrayList<Student> groupMembers;
    private ArrayList<Question> individualQuestions;
    private ArrayList<ArrayList<String>> individualAnswers;
    private ArrayList<Question> groupQuestions;
    private ArrayList<ArrayList<String>> groupAnswers;
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        username = (String) session.getAttribute("username");
        student = csbl.findStudent(username);
        group = (ProjectGroup) session.getAttribute("PRProjectGroup");
        module = group.getSuperGroup().getModule();
        question = module.getPeerReviewQuestion();
        answers = prqsbl.getPRAnswer(module, student, group);
        
        groupMembers = new ArrayList<Student>();
        for (Student s: group.getGroupMembers())
            groupMembers.add(s);
        
        individualQuestions = question.getIndividualQuestions();
        individualAnswers = prasbl.getIndAnswers(groupMembers, answers);
        groupQuestions = question.getGroupQuestions();
        groupAnswers = answers.getGrpAnswers();
    }

    public String getStudentInfo(String username) {
        return prqsbl.getStudentInfo(username);
    }
    
    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public PRQuestionSessionBeanLocal getPrqsbl() {
        return prqsbl;
    }

    public void setPrqsbl(PRQuestionSessionBeanLocal prqsbl) {
        this.prqsbl = prqsbl;
    }

    public PRAnswerSessionBeanLocal getPrasbl() {
        return prasbl;
    }

    public void setPrasbl(PRAnswerSessionBeanLocal prasbl) {
        this.prasbl = prasbl;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ProjectGroup getGroup() {
        return group;
    }

    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

    public PeerReviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(PeerReviewQuestion question) {
        this.question = question;
    }

    public PeerReviewAnswer getAnswers() {
        return answers;
    }

    public void setAnswers(PeerReviewAnswer answers) {
        this.answers = answers;
    }

    public ArrayList<Student> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(ArrayList<Student> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public ArrayList<Question> getIndividualQuestions() {
        return individualQuestions;
    }

    public void setIndividualQuestions(ArrayList<Question> individualQuestions) {
        this.individualQuestions = individualQuestions;
    }

    public ArrayList<ArrayList<String>> getIndividualAnswers() {
        return individualAnswers;
    }

    public void setIndividualAnswers(ArrayList<ArrayList<String>> individualAnswers) {
        this.individualAnswers = individualAnswers;
    }

    public ArrayList<Question> getGroupQuestions() {
        return groupQuestions;
    }

    public void setGroupQuestions(ArrayList<Question> groupQuestions) {
        this.groupQuestions = groupQuestions;
    }

    public ArrayList<ArrayList<String>> getGroupAnswers() {
        return groupAnswers;
    }

    public void setGroupAnswers(ArrayList<ArrayList<String>> groupAnswers) {
        this.groupAnswers = groupAnswers;
    }
    
    
}
