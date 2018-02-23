
package managedbeans;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import session.ClexSessionBeanLocal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eeren
 */

@RequestScoped
@ManagedBean
public class RegisterBean implements Serializable{
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    @NotNull 
    private String username;
    @Size(min = 6, max = 12)
    private String password;
    private String name;    
    @Size(min = 4, max = 20)
    @Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
    private String email;
    private String userType;
    private String school;
    private Long contactNum;
    private String faculty;
    private String major;
    private String matricYear;
    private String matricSem;
    private String currentYear;
    private double cap;
    
    public RegisterBean(){
        
    }
    
    /*/My plan is to use register for user class and then use the other entities (Lecturer, student etc.)
    for admin to approve/*/
    public void register(){
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();

        if(csbl.checkNewUser(username) == true){
            if(password.length()>=6&&!username.equals("")&&!email.equals("")){
                if(userType.equals("1")){ //Student
                    csbl.createStudent(username, password, name, email, school, contactNum, genSalt(), 
                    faculty, major, matricYear, matricSem, currentYear, cap);
                }
                else if(userType.equals("2")){ //Lecturer
                    csbl.createLecturer(username, password, name, email, school, contactNum, genSalt(), 
                    faculty);                    
                }
                else{ //Guest
                    csbl.createGuest(username, password, name, email, school, contactNum, genSalt());                    
                }
            }
        }
        else{
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User '" + username + "' already exists.", "");
            context.addMessage(null, fmsg);
        }
    }
    
    private String genSalt(){
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();
        
        return salt;
    }
    
    public void submit(ActionEvent event){
        System.err.println("RegisterBean.submit(): username: " + username);
        System.err.println("RegisterBean.submit(): name: " + name);
        System.err.println("RegisterBean.submit(): email: " + email);
        System.err.println("RegisterBean.submit(): userType: " + userType);
    }
    
//----------------------------------------------------------------
    //For testing only
    public void testRegisterStudent() throws IOException{
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2017", 0.0);
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'namename' already exists.", ""));
        }
    }
    
    public void testRegisterLecturer() throws IOException{
        if(csbl.checkNewUser("hsianghui") == true){
            csbl.createLecturer("hsianghui", "123456", "LekHsiangHui", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'hsianghui' already exists.", ""));
        }
    }
    
    public void testRegisterGuest() throws IOException{
        if(csbl.checkNewUser("aguest") == true){
            csbl.createGuest("aguest", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'aguest' already exists.", ""));
        }
    }

    public void registerAdmin() throws IOException{
        if(csbl.checkNewUser("theadmin") == true){
            csbl.createAdmin("theadmin", "123", "admin", "admin", "NUS", 12345678L, genSalt());
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'admin' already exists.", ""));
        }       
    }

    public void testusercase() throws IOException{
        csbl.createStudent("aaaaaa", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2016", 0.0);
        csbl.createStudent("bbbbbb", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "2","2016", 0.0);
        csbl.createStudent("cccccc", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2017", 0.0);
        csbl.createStudent("dddddd", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "2","2017", 0.0);
        csbl.createStudent("eeeeee", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2018", 0.0);
        csbl.createStudent("ffffff", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "1","2016", 0.0);
        csbl.createStudent("gggggg", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "2","2016", 0.0);
        csbl.createStudent("hhhhhh", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "1","2017", 0.0);
        csbl.createStudent("iiiiii", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "2","2017", 0.0);
        csbl.createStudent("jjjjjj", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "1","2018", 0.0);
        csbl.createLecturer("hsianghui2", "123456", "LekHsiangHui2", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui3", "123456", "LekHsiangHui3", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui4", "123456", "LekHsiangHui4", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createGuest("guesta", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestb", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestc", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestd", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("gueste", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
    }
    
    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Long getContactNum() {
        return contactNum;
    }

    public void setContactNum(Long contactNum) {
        this.contactNum = contactNum;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMatricYear() {
        return matricYear;
    }

    public void setMatricYear(String matricYear) {
        this.matricYear = matricYear;
    }

    public String getMatricSem() {
        return matricSem;
    }

    public void setMatricSem(String matricSem) {
        this.matricSem = matricSem;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }
}