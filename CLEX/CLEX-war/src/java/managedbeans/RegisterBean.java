
package managedbeans;

import entity.User;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
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

@SessionScoped
@ManagedBean
public class RegisterBean implements Serializable{
    
    @EJB
    private ClexSessionBeanLocal csbl;
    
    private User userEntity;
    private String username;
    private String password;
    private String name;    
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

    /*/My plan is to use register for user class and then use the other entities (Lecturer, student etc.)
    for admin to approve/*/
    public void register(){
        if(csbl.checkNewUser(username) == true){
            csbl.createStudent(username, password, name, email, school, contactNum, genSalt(), 
                 faculty, major, matricYear, matricSem, currentYear, cap);
        }
        else{
            //return error message
        }
    }
    
    private String genSalt(){
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();
        
        return salt;
    }
    
//----------------------------------------------------------------
    //For testing only
    public void testRegister(){
        if(csbl.checkNewUser("namename") == true){
            csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1","2017", 0.0);
        }
    }
}