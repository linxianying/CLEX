/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.PeerReviewAnswer;
import entity.PeerReviewQuestion;
import entity.ProjectGroup;
import entity.Student;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import javaClass.Question;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.PRAnswerSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "lecturerViewPRBean")
@SessionScoped
public class LecturerViewPRBean implements Serializable {
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private PRQuestionSessionBeanLocal prqsbl;
    @EJB
    private PRAnswerSessionBeanLocal prasbl;
    @EJB
    private ProjectSessionBeanLocal psbl;
    
    public LecturerViewPRBean() {
    }
    
    FacesContext context;
    HttpSession session;
    
    private Student student;
    private Lecturer lecturer;
    private String username;
    private Module module;
    private ProjectGroup group;
    private PeerReviewQuestion questions;
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
        lecturer = (Lecturer) session.getAttribute("user");
        username = lecturer.getUsername();
        module = (Module) session.getAttribute("managedModule");
        questions = module.getPeerReviewQuestion();
        student = (Student) session.getAttribute("managedStudent");
        answers = (PeerReviewAnswer) session.getAttribute("managedPRAnswer");
        group = psbl.getStudentProjectGroup(student, module);
        groupMembers = new ArrayList<Student>();
        for (Student s: group.getGroupMembers())
            groupMembers.add(s);
        individualQuestions = questions.getIndividualQuestions();
        individualAnswers = prasbl.getIndAnswers(groupMembers, answers);
        groupQuestions = questions.getGroupQuestions();
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

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
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

    public PeerReviewQuestion getQuestions() {
        return questions;
    }

    public void setQuestions(PeerReviewQuestion questions) {
        this.questions = questions;
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
