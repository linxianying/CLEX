package managedbeans;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import session.ClexSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.ProfileSessionBeanLocal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author eeren
 */
@ViewScoped
@ManagedBean
public class RegisterBean implements Serializable {

    @EJB
    ClexSessionBeanLocal csbl;
    @EJB
    CourseMgmtBeanLocal cmbl;
    @EJB
    ProfileSessionBeanLocal psbl;

    private User userEntity;
    @NotNull
    private String username;
    @Size(min = 6, max = 12)
    private String password;
    private String password1;
    private String name;
    @Size(min = 4, max = 32)
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
    private boolean agree = false;
    private List<String> schoollist;
    private String studentId;
    private List<String> yearlist;
    private List<String> facultylist;

    public RegisterBean() {

    }

    @PostConstruct
    public void init() {
        schoollist = cmbl.getAllSchools();
        yearlist = cmbl.getYearList();
        school = "NUS";
        facultylist = psbl.getSchoolFaculty(school);
    }

    public void onSchoolChange(String school) {
        facultylist = psbl.getSchoolFaculty(school);
    }

    public void doBack() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "redirecting to login", "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, fmsg);
        context.getExternalContext().redirect("login.xhtml");
    }

    public void register() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if (agree == false) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must agree to our Terms &amp; Conditions", "");
            context.addMessage(null, fmsg);
        } else {
            if (csbl.checkNewUser(username) == true) {
                if (password.length() >= 6 && !username.equals("") && !email.equals("")) {
                    if (password.equals(password1)) {
                        if (userType.equals("1")) { //Student
                            csbl.createStudent(username, password, name, studentId, email, school, contactNum, genSalt(),
                                    faculty, major, matricYear, matricSem, 0.0);
                            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Student '" + username + "' successfully created.", "We hope you will like PRISM.");
                        } else if (userType.equals("2")) { //Lecturer
                            csbl.createLecturer(username, password, name, email, school, contactNum, genSalt(),
                                    faculty);
                            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lecturer '" + username + "' successfully created.", "We hope you will like PRISM.");
                        } else { //Guest
                            csbl.createGuest(username, password, name, email, school, contactNum, genSalt());
                            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Guest '" + username + "' successfully created.", "We hope you will like PRISM.");
                        }
                        context.addMessage(null, fmsg);
                        context.getExternalContext().getFlash().setKeepMessages(true);
                        username = "";
                        password = "";
                        name = "";
                        email = "";
                        school = "";
                        contactNum = null;
                        context.getExternalContext().redirect("login.xhtml"); //redirect will not show success
                    } else {
                        fmsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "The password are inconsistent", "Please enter again.");
                        username = "";
                        context.addMessage(null, fmsg);
                    }
                }
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "User'" + username + "' already exists.", "Please choose another username.");
                username = "";
                context.addMessage(null, fmsg);
            }
        }
    }

    private String genSalt() {
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();

        return salt;
    }

    public void submit(ActionEvent event) {
        System.err.println("RegisterBean.submit(): username: " + username);
        System.err.println("RegisterBean.submit(): name: " + name);
        System.err.println("RegisterBean.submit(): email: " + email);
        System.err.println("RegisterBean.submit(): userType: " + userType);
    }

//----------------------------------------------------------------
    //For testing only
    public void testRegisterStudent() throws IOException {
        if (csbl.checkNewUser("namename") == true) {
            csbl.createStudent("namename", "123456", "LinXianying", "A0000M", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS", "2015", "1", 0.0);
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'namename' already exists.", ""));
        }
    }

    public void testRegisterLecturer() throws IOException {
        if (csbl.checkNewUser("hsianghui") == true) {
            csbl.createLecturer("hsianghui", "123456", "LekHsiangHui", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'hsianghui' already exists.", ""));
        }
    }

    public void testRegisterGuest() throws IOException {
        if (csbl.checkNewUser("aguest") == true) {
            csbl.createGuest("aguest", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'aguest' already exists.", ""));
        }
    }

    public void registerAdmin() throws IOException {
        if (csbl.checkNewUser("theadmin") == true) {
            csbl.createAdmin("theadmin", "123", "admin", "admin", "NUS", 12345678L, genSalt());
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User 'admin' already exists.", ""));
        }
    }

    public void testusercase() throws IOException {
        csbl.createStudent("aaaaaa", "123456", "Su Xinran", "A0001M", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS", "2015", "1", 0.0);
        csbl.createStudent("bbbbbb", "123456", "Cao Yu", "A0002M", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "CS", "2015", "2", 0.0);
        csbl.createStudent("cccccc", "123456", "Joseph", "A0003M", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS", "2015", "1", 0.0);
        csbl.createStudent("dddddd", "123456", "Wenjie", "A0004M", "email@email.com", "NUS", 65345455L, genSalt(), "soc", "CS", "2015", "2", 4.8);
        csbl.createStudent("eeeeee", "123456", "EE Ren", "A0005M", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS", "2015", "1", 4.3);
        csbl.createStudent("ffffff", "123456", "Lifeng Zhou", "A0006M", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "CS", "2016", "1", 4.2);
        csbl.createStudent("gggggg", "123456", "Fan Weiguang", "A0007M", "email@email.com", "NUS", 12345378L, genSalt(), "soc", "BA", "2016", "2", 3.9);
        csbl.createStudent("hhhhhh", "123456", "Duan Yichen", "A0008M", "email@email.com", "NUS", 12345278L, genSalt(), "soc", "IS", "2016", "1", 4.7);
        csbl.createStudent("iiiiii", "123456", "Luo Yuyang", "A0009M", "email@email.com", "NUS", 12845678L, genSalt(), "soc", "EE", "2016", "2", 3.5);
        csbl.createStudent("jjjjjj", "123456", "Yang Ming", "A0010M", "email@email.com", "NUS", 12345278L, genSalt(), "soc", "CEG", "2016", "1", 1.6);
        csbl.createLecturer("hsianghui2", "123456", "LekHsiangHui2", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui3", "123456", "LekHsiangHui3", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui4", "123456", "LekHsiangHui4", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createGuest("guesta", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestb", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestc", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestd", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("gueste", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
    }

    public List<String> getFacultylist() {
        return facultylist;
    }

    public void setFacultylist(List<String> facultylist) {
        this.facultylist = facultylist;
    }

    public List<String> getYearlist() {
        return yearlist;
    }

    public void setYearlist(List<String> yearlist) {
        this.yearlist = yearlist;
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

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public List<String> getSchoollist() {
        return schoollist;
    }

    public void setSchoollist(List<String> schoollist) {
        this.schoollist = schoollist;
    }

    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
