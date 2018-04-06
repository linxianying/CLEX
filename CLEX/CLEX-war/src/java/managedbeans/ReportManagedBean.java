/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Student;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import session.ClexSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "reportManagedBean")
@RequestScoped
public class ReportManagedBean {
    
    @Resource(name = "jasperReportDataSource")
    private DataSource jasperReportDataSource;

    @EJB
    private ClexSessionBeanLocal csbl;
    
    private String username;
    private Student student;
    FacesContext context;
    HttpSession session;
    FacesMessage fmsg;
    /**
     * Creates a new instance of ReportManagedBean
     */
    
    public ReportManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        username = (String) session.getAttribute("username");
        student = csbl.findStudent(username);
    }
    
    public void generateReport(ActionEvent event){        
        try{
            
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/CLEX-war/jasperreport/nuslogo.png");
            parameters.put("CAP", student.getCap());
            parameters.put("username",username);
            parameters.put("name", student.getName());

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/Cherry.jasper");                
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, jasperReportDataSource.getConnection());
        }
        catch(JRException ex){
            ex.printStackTrace();
        }
        catch(SQLException ex){   
            ex.printStackTrace();
        }
        catch(IOException ex){
        }
    }
}
