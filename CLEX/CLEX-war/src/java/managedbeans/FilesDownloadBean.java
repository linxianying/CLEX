/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Files;
import entity.Module;
import entity.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.AnnouncementSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.FilesManagementSessionBeanLocal;

/**
 *
 * @author lin
 */
@Named(value = "filesDownloadBean")
@SessionScoped
public class FilesDownloadBean {

    /**
     * Creates a new instance of FilesDownloadBean
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
    private Files file;
    private List<Files> files;
    private StreamedContent streamedContent;
    private int num = 0;
    
    FacesContext context;
    HttpSession session;
    
    public FilesDownloadBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username"); 
        module = (Module) session.getAttribute("module");
        id = module.getId();
        user = asbl.findUser(username);
        module = fmsbl.findModule(id);
        files = module.getFileLists();
        num = files.size();
        System.out.println("FileDownloadBean: num of files" + num);
    }
    
     public StreamedContent retrieveStreamedContent() {
        
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        String filePath = (String)session.getAttribute("downloadFilePath");
        
        if(filePath != null && filePath.trim().length() > 0)
        {
            try
            {            
                FileInputStream stream = new FileInputStream(new File(filePath));
                streamedContent = new DefaultStreamedContent(stream, "image/png", file.getName());
            }
            catch(FileNotFoundException ex)
            {
                
            }
        }
        
        return streamedContent;
    }
     
     public StreamedContent getFile() {
        
        String filePath = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("downloadFilePath");
        
        if(filePath != null && filePath.trim().length() > 0)
        {
            try
            {            
                FileInputStream stream = new FileInputStream(new File(filePath));
                streamedContent = new DefaultStreamedContent(stream, "image/png", file.getName());
            }
            catch(FileNotFoundException ex)
            {
                
            }
        }
        
        return streamedContent;
    }
    
     
    public void handleDownload(ActionEvent event) {
        try {
            id = (Long) event.getComponent().getAttributes().get("fileId");
            file = fmsbl.getFile(id);

            String filePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1")
                    + System.getProperty("file.separator") + 
                    module.getCourse().getModuleCode() + 
                    System.getProperty("file.separator") + file.getName();
            System.out.println(filePath);
            session.setAttribute("downloadFilePath", filePath);
            
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public List<Files> getFiles() {
        return files;
    }

    public void setFiles(List<Files> files) {
        this.files = files;
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


    public void setFile(Files file) {
        this.file = file;
    }

    public StreamedContent getStreamedContent() {
        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    
    
    
    
}
