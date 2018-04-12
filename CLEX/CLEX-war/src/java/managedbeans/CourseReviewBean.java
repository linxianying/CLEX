/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Course;
import entity.Module;
import javax.ejb.EJB;
import entity.Thread;
import entity.Reply;
import entity.Student;
import entity.User;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.CommunitySessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@RequestScoped
public class CourseReviewBean {
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    @EJB
    private CommunitySessionBeanLocal cmsbl;
    
    private User userEntity;
    private Student studentEntity;
    private String username;
    
    private Course courseEntity;
    
    private List<Module> takingModules;
    private Module moduleEntity;
    
    private String moduleCode;
    private String moduleName;
    private String moduleYear;
    private String moduleSem;
    
    private String content;
    
    private List<Thread> moduleReviews;
    private Thread threadEntity;
    private Long threadId;
    private String threadTitle;
    
    private Reply replyEntity;
    private Long replyId;
    
    FacesContext context;
    HttpSession session;
    
    public CourseReviewBean() {
    }
    
    public void refresh(){
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        username = (String) session.getAttribute("username");
        studentEntity = csbl.findStudent(username);
        takingModules = (List) studentEntity.getModules();
    }
    
    public void createReview(){
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        if(userEntity != null){
            if(content.equals("")){
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contents needed.", "Please fill up the content field.");
            }
            else{
                threadTitle = createReviewTitle();
                threadEntity = cmsbl.getExistingReview(threadTitle, username);
                if(threadEntity != null){
                    if(cmsbl.createReply(threadEntity.getId(), username, content)){
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Review created.");
                    }
                    else{
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create reply.", "Please ensure you are logged in.");                       
                    }
                }
                else{
                    createNewReview(threadTitle, content, fmsg);
                }
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to review module.", "Please ensure you are logged in.");
        }
    }
    
    public void createNewReview(String threadTitle, String content, FacesMessage fmsg){
        if(cmsbl.createThread(username, content, threadTitle, "Course Review", courseEntity.getSchool())){
           fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Review created.");
        }
        else{
           fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create reply.", "Please ensure you are logged in.");                       
        }
    }
    
    public String createReviewTitle(){
        //Example title: "IS4103" "<Name>" - Year "2017" Sem "1" Review
        return moduleCode + " " + moduleName + " - Year " + moduleYear + " Sem " + moduleSem; 
    }
    
    public void getModuleReviews(String moduleCode){
        moduleCode = this.moduleCode;
        Course courseEntity = csbl.findCourse(moduleCode);
        moduleName = courseEntity.getModuleName();
        System.out.println("Searching reviews for " + moduleCode + " " + moduleName + "...");
        moduleReviews = cmsbl.searchThreadByTitle(moduleCode + " " + moduleName, courseEntity.getSchool());
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public Student getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(Student studentEntity) {
        this.studentEntity = studentEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Course getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(Course courseEntity) {
        this.courseEntity = courseEntity;
    }

    public List<Module> getTakingModules() {
        return takingModules;
    }

    public void setTakingModules(List<Module> takingModules) {
        this.takingModules = takingModules;
    }

    public Module getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(Module moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleYear() {
        return moduleYear;
    }

    public void setModuleYear(String moduleYear) {
        this.moduleYear = moduleYear;
    }

    public String getModuleSem() {
        return moduleSem;
    }

    public void setModuleSem(String moduleSem) {
        this.moduleSem = moduleSem;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Thread> getModuleReviews() {
        return moduleReviews;
    }

    public void setModuleReviews(List<Thread> moduleReviews) {
        this.moduleReviews = moduleReviews;
    }

    public Thread getThreadEntity() {
        return threadEntity;
    }

    public void setThreadEntity(Thread threadEntity) {
        this.threadEntity = threadEntity;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public Reply getReplyEntity() {
        return replyEntity;
    }

    public void setReplyEntity(Reply replyEntity) {
        this.replyEntity = replyEntity;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }
    
    
}
