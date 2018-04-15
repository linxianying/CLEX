/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import session.AnnouncementSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.FilesManagementSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "filesUploadBean")
@ViewScoped
public class FilesUploadBean implements Serializable {

    /**
     * Creates a new instance of FilesUploadBean
     */
    @EJB
    AnnouncementSessionBeanLocal asbl;
    @EJB
    CourseMgmtBeanLocal cmbl;
    @EJB
    FilesManagementSessionBeanLocal fmsbl;
    
    private Long id;
    private Module module;
    private User user;
    private String username;
    
    FacesContext context;
    HttpSession session;
    
    public FilesUploadBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username"); 
        module = (Module) session.getAttribute("module");
        id = module.getId();
        id = (Long) session.getAttribute("id");
        user = asbl.findUser(username);

    }
    
    public void handleFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + 
                    System.getProperty("file.separator") + module.getCourse().getModuleCode() + System.getProperty("file.separator") + 
                    event.getFile().getFileName();

            System.err.println("handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputstream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
    }

    public AnnouncementSessionBeanLocal getAsbl() {
        return asbl;
    }

    public void setAsbl(AnnouncementSessionBeanLocal asbl) {
        this.asbl = asbl;
    }

    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }

    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
    }

    public FilesManagementSessionBeanLocal getFmsbl() {
        return fmsbl;
    }

    public void setFmsbl(FilesManagementSessionBeanLocal fmsbl) {
        this.fmsbl = fmsbl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    
}
