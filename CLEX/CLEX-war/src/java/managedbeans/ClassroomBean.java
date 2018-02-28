/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;


import entity.Lecturer;
import entity.Module;
import entity.Poll;
import entity.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import session.ClassroomSessionBeanLocal;
import session.ClexSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "classroomBean")
@RequestScoped

public class ClassroomBean {
    @Resource(name = "clexDataSource")
    private DataSource clexDataSource;
    
    /**
     * Creates a new instance of ClassroomBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ClassroomSessionBeanLocal crsbl;
    
    private Lecturer lecturerEntity;
    private String username;
    private String password;
    private String userType;
    private ArrayList<Poll> polls ;
    private ArrayList<Module> modules;
    
    
    public ClassroomBean() {
    }
    
    public void testViewPolls(){
        polls = crsbl.testViewPolls();
    }
    
    public void viewModules(){
        lecturerEntity = csbl.findLecturer("lecturer");
        modules = crsbl.viewModules(lecturerEntity);
    }
    
    public void generateReport(ActionEvent event){
        
        try{
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:1527/CLEX-war/jasperreports/cherry.jpg");

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreports/pollsreport.jasper");                
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, clexDataSource.getConnection());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
    }

    public DataSource getClexDataSource() {
        return clexDataSource;
    }

    public void setClexDataSource(DataSource clexDataSource) {
        this.clexDataSource = clexDataSource;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ClassroomSessionBeanLocal getCrsbl() {
        return crsbl;
    }

    public void setCrsbl(ClassroomSessionBeanLocal crsbl) {
        this.crsbl = crsbl;
    }

    public Lecturer getLecturerEntity() {
        return lecturerEntity;
    }

    public void setLecturerEntity(Lecturer lecturerEntity) {
        this.lecturerEntity = lecturerEntity;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
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

    public ArrayList<Poll> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Poll> polls) {
        this.polls = polls;
    }
    
    
}
