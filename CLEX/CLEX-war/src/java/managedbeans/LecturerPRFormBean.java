/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.PeerReviewQuestion;
import entity.Student;
import java.util.ArrayList;
import java.util.Date;
import javaClass.Question;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@Named(value = "lecturerPRFormBean")
@SessionScoped

public class LecturerPRFormBean {
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private PRQuestionSessionBeanLocal prqsbl;
            
    FacesContext context;
    HttpSession session;
    
    private Lecturer lecturer;
    private String username;
    private Module module;
    private PeerReviewQuestion question;
    
    private ArrayList<Question> individualQuestions;
    private String[] individualAnswer;
    private ArrayList<Question> groupQuestions;
    private String[] groupAnswer;
    
    public LecturerPRFormBean() {
    }
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
//        lecturer = (Lecturer) session.getAttribute("user");
//        username = lecturer.getUsername();
//        module = (Module) session.getAttribute("module");
        
        System.out.println("start init");
        //for test purpose only
        module = csbl.findModule("PS2240", "2017", "2");
        Date day = new Date();
        prqsbl.createPeerReviewQuestion("test PR form", day, module);
        question = module.getPeerReviewQuestion();
        individualQuestions = question.getIndividualQuestions();
        individualAnswer = new String[individualQuestions.size()];
        groupQuestions = question.getGroupQuestions();
        groupAnswer = new String[groupQuestions.size()];

    }

    
    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
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

    public ArrayList<Question> getIndividualQuestions() {
        return individualQuestions;
    }

    public void setIndividualQuestions(ArrayList<Question> individualQuestions) {
        this.individualQuestions = individualQuestions;
    }

    public ArrayList<Question> getGroupQuestions() {
        return groupQuestions;
    }

    public void setGroupQuestions(ArrayList<Question> groupQuestions) {
        this.groupQuestions = groupQuestions;
    }

    public PRQuestionSessionBeanLocal getPrqsbl() {
        return prqsbl;
    }

    public void setPrqsbl(PRQuestionSessionBeanLocal prqsbl) {
        this.prqsbl = prqsbl;
    }

    public PeerReviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(PeerReviewQuestion question) {
        this.question = question;
    }

    public String[] getIndividualAnswer() {
        return individualAnswer;
    }

    public void setIndividualAnswer(String[] individualAnswer) {
        this.individualAnswer = individualAnswer;
    }

    public String[] getGroupAnswer() {
        return groupAnswer;
    }

    public void setGroupAnswer(String[] groupAnswer) {
        this.groupAnswer = groupAnswer;
    }
    
    
    public int[] getRating(Question q) {
        int[] rating = new int[q.getLevelOfRating()];
        for (int index=1; index <= q.getLevelOfRating(); index++)
            rating[index-1] = index;
        return rating;
    }
    
    
    public void testUpdatePRForm() {
        System.out.println("Strat to update");
        
    }
    
}
