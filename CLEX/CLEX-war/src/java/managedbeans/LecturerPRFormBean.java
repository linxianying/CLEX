/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.PeerReviewQuestion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javaClass.Question;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.ReorderEvent;
import session.ClexSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "lecturerPRFormBean")
@SessionScoped

public class LecturerPRFormBean implements Serializable{
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
    private ArrayList<Question> groupQuestions;
    private String title;
    private Date deadline;
    
    private Question newQuestion;
    private String addQuestion;
    //indQuestion or grQuestion
    private String addType;
    //rating/ ranking/ open
    private String questionType;
    private int levelOfRating;
    
    private String test;
    
    private String hasDeadline;
    private Date newDeadline;
    
    public LecturerPRFormBean() {
    }
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        lecturer = (Lecturer) session.getAttribute("user");
        username = lecturer.getUsername();
        module = (Module) session.getAttribute("managedModule");
        
        //for test purpose only
//        module = csbl.findModule("PS2240", "2017", "2");
//        Date day = new Date();
//        prqsbl.createPeerReviewQuestion("test PR form", day, module);
        question = module.getPeerReviewQuestion();
        individualQuestions = question.getIndividualQuestions();
        groupQuestions = question.getGroupQuestions();
        title = question.getTitle();
        deadline = question.getDeadline();
        hasDeadline = "no";
        System.out.println("Finish init");
        
    }

    
    public int[] getRating(Question q) {
        int[] rating = new int[q.getLevelOfRating()];
        for (int index=1; index <= q.getLevelOfRating(); index++)
            rating[index-1] = index;
        return rating; 
    }
    
    public void addQuestion() {
        newQuestion = new Question(addQuestion, questionType);
        if (questionType.equals("rating")) {
            newQuestion.setLevelOfRating(levelOfRating);
        }
        prqsbl.addPRQuestion(question, newQuestion, addType);
        
        question = module.getPeerReviewQuestion();
        individualQuestions = question.getIndividualQuestions();
        groupQuestions = question.getGroupQuestions();
        System.out.println("group size = " + groupQuestions.size());
    }
    
    //the value will be changed automatically
    public void editIndQuestion(int indIndex){
//        System.out.println("change question " + indIndex);
//        System.out.println(individualQuestions.get(0).getQuestion());
        prqsbl.editIndQuestion(question, individualQuestions);
    }
    
    public void editGrQuestion(int indIndex){
//        System.out.println("change question " + indIndex);
//        System.out.println(groupQuestions.get(0).getQuestion());
        prqsbl.editGrQuestion(question, groupQuestions);
    }
    
    public void deleteIndQuestion(int indIndex) {
        prqsbl.deleteIndQuestion(question, indIndex);
//        individualQuestions.remove(indIndex);
    }
    
    public void deleteGrQuestion(int grIndex) {
        System.out.println("index=" + grIndex);
        System.out.println("question number is " + groupQuestions.get(grIndex).getQuestion());
        prqsbl.deleteGrQuestion(question, grIndex);
//        groupQuestions.remove(indIndex);
    }
    
    public void editTitle(){
        prqsbl.editTitle(question, title);
    }
    
    public void reset() {
        System.out.println("into reset");
        prqsbl.createPeerReviewQuestion("Sample Peer Review Form", null, module);
        question = module.getPeerReviewQuestion();
        individualQuestions = question.getIndividualQuestions();
        groupQuestions = question.getGroupQuestions();
        title = question.getTitle();
    }
    
    public void onIndRowReorder(ReorderEvent event) {
        prqsbl.setIndQuestion(question, individualQuestions);
    }
    
    public void onGrRowReorder(ReorderEvent event) {
        prqsbl.setGrQuestion(question, groupQuestions);
    }
    
    public void startPR() {
        if (hasDeadline.equals("no"))
            newDeadline = null;
        prqsbl.startPR(module, newDeadline);
        FacesMessage message = new FacesMessage();
        message.setSummary("Success");
        message.setDetail("Peer Review Session successfully starts!");
        addMessage(message);
    }
    
    public void stopPR() {
        prqsbl.stopPR(module);
        FacesMessage message = new FacesMessage();
        message.setSummary("Success");
        message.setDetail("Peer Review Session successfully stops!");
        addMessage(message);
    }
    
    public void testUpdatePRForm() {
        System.out.println("Strat to update");
    }
    
    public void test() {
//        this.questionType = 
        System.out.println("Question type= " + this.questionType);
        
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

    public PeerReviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(PeerReviewQuestion question) {
        this.question = question;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Question getNewQuestion() {
        return newQuestion;
    }

    public void setNewQuestion(Question newQuestion) {
        this.newQuestion = newQuestion;
    }

    public String getAddQuestion() {
        return addQuestion;
    }

    public void setAddQuestion(String addQuestion) {
        this.addQuestion = addQuestion;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getLevelOfRating() {
        return levelOfRating;
    }

    public void setLevelOfRating(int levelOfRating) {
        this.levelOfRating = levelOfRating;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getHasDeadline() {
        return hasDeadline;
    }

    public void setHasDeadline(String hasDeadline) {
        this.hasDeadline = hasDeadline;
    }

    public Date getNewDeadline() {
        return newDeadline;
    }

    public void setNewDeadline(Date newDeadline) {
        this.newDeadline = newDeadline;
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    
}
