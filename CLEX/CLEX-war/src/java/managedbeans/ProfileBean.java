/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Student;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.ProfileSessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean(name = "profileBean")
@SessionScoped
public class ProfileBean implements Serializable {

    @EJB
    private ClexSessionBeanLocal csbl;
    
    @EJB
    private ProfileSessionBeanLocal psbl;

    private User userEntity;
    private Student studentEntity;

    private String username;
    private String password;
    private String userType;

    //All users
    private String school;
    private String email;
    private String contactNum; //remember to parse back to long
    private String name;

    //Students and lecturer
    private String faculty;

    //Students only
    private String major;
    private String matricYear;
    private String matricSem;
    private double cap;

    //New Values
    private String newName;
    private String newEmail;
    private String newContactNum;
    private String newPassword1;
    private String newPassword2; //for confirm password
    private String oldPassword;

    HttpSession session;
    
    FacesMessage fmsg = new FacesMessage();
    FacesContext context = FacesContext.getCurrentInstance();

    public ProfileBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        userEntity = (User) session.getAttribute("user");
        username = userEntity.getUsername();
        userType = userEntity.getUserType();

        school = userEntity.getSchool();
        email = userEntity.getEmail();
        contactNum = Long.toString(userEntity.getContactNum());
        name = userEntity.getName();

        //Only implemented for students, can use this for other user type as well
        if (userType.equals("Student")) {
            studentEntity = (Student) userEntity;

            faculty = studentEntity.getFaculty();
            major = studentEntity.getMajor();
            matricYear = studentEntity.getMatricYear();
            matricSem = studentEntity.getMatricSem();
            cap = studentEntity.getCap();
        }
    }
    
    public void editStudentProfile() throws IOException{     
        Long contactNo = Long.parseLong(contactNum);
        
        //System.out.println("name: " + name);
        psbl.editStudent(username, name, email, contactNo, faculty, major, matricYear, matricSem);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Successfully Updated", ""));
    }
    
    public void editLecturerProfile() throws IOException{     
        Long contactNo = Long.parseLong(contactNum);
        
        //System.out.println("name: " + name);
        psbl.editLecturer(username, name, email, school, contactNo, faculty);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("lecturerProfile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Successfully Updated", ""));
    }
    
    public void editGuestProfile() throws IOException{     
        Long contactNo = Long.parseLong(contactNum);
        
        //System.out.println("name: " + name);
        psbl.editGuest(username, name, email, school, contactNo);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("guestProfile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Successfully Updated", ""));
    }
    
    public void editAdminProfile() throws IOException{     
        Long contactNo = Long.parseLong(contactNum);
        
        //System.out.println("name: " + name);
        psbl.editAdmin(username, name, email, school, contactNo);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("adminProfile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Successfully Updated", ""));
    }
    
    public void editPassword() throws IOException{
        if(!newPassword1.equals(newPassword2)){
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 2 new passwords are not the same", ""));
        }
        else{
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);
            
            if (temp == true){
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
            }
            else{
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
            }
        }
    }
    
    public void editLecturerPassword() throws IOException{
        if(!newPassword1.equals(newPassword2)){
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 2 new passwords are not the same", ""));
        }
        else{
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);
            
            if (temp == true){
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                FacesContext.getCurrentInstance().getExternalContext().redirect("lecturerProfile.xhtml");
            }
            else{
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
            }
        }
    }
    
    public void editGuestPassword() throws IOException{
        if(!newPassword1.equals(newPassword2)){
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 2 new passwords are not the same", ""));
        }
        else{
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);
            
            if (temp == true){
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                //FacesContext.getCurrentInstance().getExternalContext().redirect("guestProfile.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password has been successfully updated", ""));
            }
            else{
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current password is incorrect", ""));
            }
        }
    }
    
    public void editAdminPassword() throws IOException{
        if(!newPassword1.equals(newPassword2)){
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 2 new passwords are not the same", ""));
        }
        else{
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);
            
            if (temp == true){
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                FacesContext.getCurrentInstance().getExternalContext().redirect("adminProfile.xhtml");
            }
            else{
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
            }
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }

    /**
     * @return the newName
     */
    public String getNewName() {
        return newName;
    }

    /**
     * @param newName the newName to set
     */
    public void setNewName(String newName) {
        this.newName = newName;
    }

    /**
     * @return the newEmail
     */
    public String getNewEmail() {
        return newEmail;
    }

    /**
     * @param newEmail the newEmail to set
     */
    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    /**
     * @return the newContactNum
     */
    public String getNewContactNum() {
        return newContactNum;
    }

    /**
     * @param newContactNum the newContactNum to set
     */
    public void setNewContactNum(String newContactNum) {
        this.newContactNum = newContactNum;
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the newPassword1
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * @param newPassword1 the newPassword1 to set
     */
    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    /**
     * @return the newPassword2
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * @param newPassword2 the newPassword2 to set
     */
    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

}
