/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Student;
import entity.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import static org.primefaces.component.contextmenu.ContextMenu.PropertyKeys.event;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import session.ClexSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.ProfileSessionBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean(name = "profileBean")
@SessionScoped
public class ProfileBean implements Serializable {

    @EJB
    private ProfileSessionBeanLocal psbl;

    @EJB
    CourseMgmtBeanLocal cmbl;

    @EJB
    private ClexSessionBeanLocal csbl;

    private User userEntity;
    private Student studentEntity;
    private Lecturer lecturerEntity;

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
    private List<String> facultylist;
    private List<String> yearlist;

    HttpSession session;
    HttpServletRequest request;
    FacesMessage fmsg = new FacesMessage();
    FacesContext context = FacesContext.getCurrentInstance();
    private UploadedFile uploadedFile;
    private byte[] data;

    public ProfileBean() {
    }

    public void setNoCache() {
        HttpServletResponse response = (HttpServletResponse) FacesContext
                .getCurrentInstance().getExternalContext().getResponse();
        response.setHeader("Cache-Control", "no-cache, no-store");
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public boolean checkpic(String username) {

        context = FacesContext.getCurrentInstance();
        String filename = username;

        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/profile/";
        path = path.replaceAll("\\\\", "/");
        if (Files.exists(Paths.get(path + filename + ".png"))) {
            return true;
        } else if (Files.exists(Paths.get(path + filename + ".jpeg"))) {
            return true;
        } else if (Files.exists(Paths.get(path + filename + ".jpg"))) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkpicpng(String username) {

        context = FacesContext.getCurrentInstance();
        String filename = username;

        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/profile/";
        path = path.replaceAll("\\\\", "/");
        if (Files.exists(Paths.get(path + filename + ".png"))) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkpicjpg(String username) {

        context = FacesContext.getCurrentInstance();
        String filename = username;

        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/profile/";
        path = path.replaceAll("\\\\", "/");
        if (Files.exists(Paths.get(path + filename + ".jpg"))) {
            return true;
        } else {
            return false;
        }

    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        username = (String) session.getAttribute("username");
        userEntity = (User) session.getAttribute("user");
        userType = userEntity.getUserType();
        yearlist = cmbl.getYearList();
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
        } else if (userType.equals("Lecturer")) {
            lecturerEntity = (Lecturer) userEntity;

            faculty = lecturerEntity.getFaculty();
        }
        facultylist = psbl.getSchoolFaculty(school);
    }

    public void oncapture(CaptureEvent captureEvent) throws IOException {
        data = captureEvent.getData();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        String filename = "temp-" + username;
        String extension = ".jpg";
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/profile/";
        path = path.replaceAll("\\\\", "/");
        System.out.println("path " + path);
        Path folder = Paths.get(path);
        Path file = Files.createTempFile(folder, filename, extension);

        try (InputStream input = new ByteArrayInputStream(data)) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }
        if (Files.exists(Paths.get(path + username + ".png"))) {
            System.out.println("Try Delete and rename");
            Files.delete(Paths.get(path + username + ".png"));
        }
        if (Files.exists(Paths.get(path + username + extension))) {
            System.out.println("Try Delete and rename");
            Files.delete(Paths.get(path + username + extension));
            Files.move(file, Paths.get(path + username + extension));
        } else {
            System.out.println("Try rename");
            Files.move(file, Paths.get(path + username + extension));
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (userEntity.getUserType().equals("Lecturer")) {
            context.getExternalContext().redirect("lecturerProfile.xhtml");
        } else if (userEntity.getUserType().equals("Student")) {
            context.getExternalContext().redirect("profile.xhtml");
        } else if (userEntity.getUserType().equals("Admin")) {
            context.getExternalContext().redirect("adminProfile.xhtml");
        } else {
            context.getExternalContext().redirect("guestProfile.xhtml");
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        String filename = username;
        String extension = ".png";

        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/profile/";
        path = path.replaceAll("\\\\", "/");
        System.out.println("path " + path);

        Path folder = Paths.get(path);
        Path file = Files.createTempFile(folder, filename, extension);
        System.out.println("File name: " + file.getFileName().toString());

        try (InputStream input = event.getFile().getInputstream()) {
            System.out.println("File size: " + event.getFile().getSize());
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("File successfully saved in " + file);
        FacesMessage message = new FacesMessage("Succes!", event.getFile().getFileName() + " uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

        if (Files.exists(Paths.get(path + username + ".jpg"))) {
            System.out.println("Try Delete and rename");
            Files.delete(Paths.get(path + username + ".jpg"));
        }
        if (Files.exists(Paths.get(path + username + extension))) {
            System.out.println("Try Delete and rename");
            Files.delete(Paths.get(path + username + extension));
            Files.move(file, Paths.get(path + username + extension));
        } else {
            System.out.println("Try rename");
            Files.move(file, Paths.get(path + username + extension));
        }

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (userEntity.getUserType().equals("Lecturer")) {
            context.getExternalContext().redirect("lecturerProfile.xhtml");
        } else if (userEntity.getUserType().equals("Student")) {
            context.getExternalContext().redirect("profile.xhtml");
        } else if (userEntity.getUserType().equals("Admin")) {
            context.getExternalContext().redirect("adminProfile.xhtml");
        } else {
            context.getExternalContext().redirect("guestProfile.xhtml");
        }

    }

    public void editStudentProfile() throws IOException {
        Long contactNo = Long.parseLong(contactNum);

        //System.out.println("name: " + name);
        psbl.editStudent(username, name, email, contactNo, faculty, major, matricYear, matricSem);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Profile updated"));
    }

    public void editLecturerProfile() throws IOException {
        Long contactNo = Long.parseLong(contactNum);

        //System.out.println("name: " + name);
        psbl.editLecturer(username, name, email, school, contactNo, faculty);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("lecturerProfile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Profile updated"));
    }

    public void editGuestProfile() throws IOException {
        Long contactNo = Long.parseLong(contactNum);

        //System.out.println("name: " + name);
        psbl.editGuest(username, name, email, school, contactNo);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("guestProfile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Profile updated"));
    }

    public void editAdminProfile() throws IOException {
        Long contactNo = Long.parseLong(contactNum);

        //System.out.println("name: " + name);
        psbl.editAdmin(username, name, email, school, contactNo);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("adminProfile.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Profile updated"));
    }

    public void editPassword() throws IOException {
        if (!newPassword1.equals(newPassword2)) {
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Passwords are not the same"));
        } else {
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);

            if (temp == true) {
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                //FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Password updated"));
            } else {
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Current password is incorrect"));
            }
        }
    }

    public void editLecturerPassword() throws IOException {
        if (!newPassword1.equals(newPassword2)) {
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Passwords are not the same"));
        } else {
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);

            if (temp == true) {
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                //FacesContext.getCurrentInstance().getExternalContext().redirect("lecturerProfile.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Password updated"));
            } else {
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Current password is incorrect"));
            }
        }
    }

    public void editGuestPassword() throws IOException {
        if (!newPassword1.equals(newPassword2)) {
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Passwords are not the same"));
        } else {
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);

            if (temp == true) {
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                //FacesContext.getCurrentInstance().getExternalContext().redirect("guestProfile.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Password updated"));
            } else {
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Current password is incorrect"));
            }
        }
    }

    public void editAdminPassword() throws IOException {
        if (!newPassword1.equals(newPassword2)) {
            // Passwords don't match
            // Need help for implementation error message on UI for passwords mismatch
            //fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm Password does not match", "");
            System.out.println("Passwords Mismatch");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Passwords are not the same"));
        } else {
            Boolean temp;
            temp = psbl.checkPassword(username, oldPassword);

            if (temp == true) {
                //System.out.println("here");
                psbl.changePassword(username, newPassword2);
                //FacesContext.getCurrentInstance().getExternalContext().redirect("adminProfile.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password has been successfully updated", ""));
            } else {
                //throw error: Wrong password
                // Need help for implementation error message on UI for passwords mismatch
                System.out.println("Wrong password with DB");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current password is incorrect", ""));
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

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
