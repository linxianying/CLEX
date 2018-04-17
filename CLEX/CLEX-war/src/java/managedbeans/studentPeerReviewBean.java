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
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import javaClass.Question;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.ReorderEvent;
import session.ClexSessionBeanLocal;
import session.PRAnswerSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "studentPeerReviewBean")
@SessionScoped
public class studentPeerReviewBean implements Serializable {
    
    public studentPeerReviewBean() {
    }
    
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private PRQuestionSessionBeanLocal prqsbl;
    @EJB
    private PRAnswerSessionBeanLocal prasbl;
    
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
    private String test;
    private ArrayList<String> a;
    
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
        //get individualAnswers, if null create one with size = individual questions' size
        individualAnswers = new ArrayList<ArrayList<String>>();
        this.iniIndividualAnswers();
        
        groupQuestions = question.getGroupQuestions();
        //get groupAnswers, if null create one with size = group questions' size
        groupAnswers = new ArrayList<ArrayList<String>>();
        this.iniGroupAnswers();
        
//        a = new ArrayList<String>();
//        a.add("t1");
//        a.add("t2");
//        a.add("t3");
//        a.add("t4");
    }
    
    public void refresh() {
    }
    
    public void iniIndividualAnswers() {
        individualAnswers = new ArrayList<ArrayList<String>>();
        for (int n=0; n< groupMembers.size(); n++) {
            ArrayList<String> inner = new ArrayList<String>();
            for (int i=0; i< individualQuestions.size(); i++) {
                if (individualQuestions.get(i).getType().equals("rating"))
                    inner.add("select");
                else if (individualQuestions.get(i).getType().equals("open"))
                    inner.add("");
            }
            individualAnswers.add(inner);
        }
    }
    
    public void iniGroupAnswers() {
        groupAnswers = new ArrayList<ArrayList<String>>();
        for (int i=0; i< groupQuestions.size(); i++) {
            ArrayList<String> inner = new ArrayList<String>();
            if (groupQuestions.get(i).getType().equals("rating")) {
                inner.add("select");
                groupAnswers.add(inner);
            }
            else if (groupQuestions.get(i).getType().equals("open")) {
                inner.add("");
                groupAnswers.add(inner);
            }
            else if (groupQuestions.get(i).getType().equals("ranking")) {
                for (Student s: groupMembers)
                    inner.add(s.getUsername());
                groupAnswers.add(inner);
            }
        }
    }
    
    public int[] getRating(Question q) {
        int[] rating = new int[q.getLevelOfRating()];
        for (int index=1; index <= q.getLevelOfRating(); index++)
            rating[index-1] = index;
        return rating;
    }
    
    public String getStudentInfo(String username) {
        return prqsbl.getStudentInfo(username);
    }
    
    public void submitPRForm( ) {
//        System.out.println("test: " + test);
//        for (int i=0; i< a.size(); i++)
//            System.out.println(i+". " + a.get(i));
//        System.out.println("individual: " + individualAnswers.size());
//        for (int i=0; i< this.groupMembers.size(); i++) {
//            System.out.println("For student " + groupMembers.get(i).getName());
//            for (int n=0; n< this.individualQuestions.size(); n++)
//            System.out.println(n+". " + individualAnswers.get(i).get(n));
//        }
//        System.out.println("Group: " + groupAnswers);
//        for (int i=0; i< groupAnswers.size(); i++) {
//            if (groupAnswers.get(i).size() == 1)
//                System.out.println(i+". " + groupAnswers.get(i).get(0));
//            else {
//                for (String s: groupAnswers.get(i))
//                    System.out.println(s);
//            }
//        }
        prasbl.submitPRForm(groupMembers, individualAnswers, groupAnswers, answers);
    }
    
    public void onRowReorder(ReorderEvent event) {
//        System.out.println("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
//        System.out.println(groupAnswers.get(2));
    }
    
    public ArrayList<String> findGrpAnswerString(int index) {
        return groupAnswers.get(index);
    }
    
    public void testUpdatePRForm() {
        System.out.println("Strat to update");
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
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public ProjectGroup getGroup() {
        return group;
    }
    
    public void setGroup(ProjectGroup group) {
        this.group = group;
    }
    
    public PeerReviewAnswer getAnswers() {
        return answers;
    }
    
    public void setAnswers(PeerReviewAnswer answers) {
        this.answers = answers;
    }
    
    public ArrayList<ArrayList<String>> getGroupAnswers() {
        return groupAnswers;
    }
    
    public void setGroupAnswers(ArrayList<ArrayList<String>> groupAnswers) {
        this.groupAnswers = groupAnswers;
    }
    
    public String getTest() {
        return test;
    }
    
    public void setTest(String test) {
        this.test = test;
    }
    
    public ArrayList<String> getA() {
        return a;
    }
    
    public void setA(ArrayList<String> a) {
        this.a = a;
    }
    
    public ArrayList<Student> getGroupMembers() {
        return groupMembers;
    }
    
    public void setGroupMembers(ArrayList<Student> groupMembers) {
        this.groupMembers = groupMembers;
    }
    
    public ArrayList<ArrayList<String>> getIndividualAnswers() {
        return individualAnswers;
    }
    
    public void setIndividualAnswers(ArrayList<ArrayList<String>> individualAnswers) {
        this.individualAnswers = individualAnswers;
    }
    
    
}
