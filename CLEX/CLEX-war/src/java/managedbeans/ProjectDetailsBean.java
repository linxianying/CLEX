/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.Student;
import entity.User;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.PRAnswerSessionBeanLocal;
import session.ProjectSessionBeanLocal;

/**
 *
 * @author Joseph
 */
@ManagedBean(name = "projectDetailsBean")
@RequestScoped
public class ProjectDetailsBean {

    /**
     * Creates a new instance of ProjectDetailsBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectSessionBeanLocal psbl;
    @EJB
    private PRAnswerSessionBeanLocal prasbl;
    
    FacesContext context;
    HttpSession session;

    private String username;
    private Student student;
    private Module module;
    private List<String> list;

    private String activityname;
    private User userEntity;
    private String moduleCode;
    private String semester;
    private String year;
    private String schoolname;

    //for test
    private boolean check;

    public ProjectDetailsBean() {
    }

    @PostConstruct
    public void init() {
        //for test purpose
        //check = false;
        //module = csbl.findModule("PS2240", "2017", "2");
        //for test purpose only
        //this.username="namename";
        //this.student = csbl.findStudent(username);
        System.out.println("ProjectDetailsBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        module = (Module) session.getAttribute("module");
        moduleCode = module.getCourse().getModuleCode();
        semester = module.getTakenSem();
        year = module.getTakenYear();
        schoolname = userEntity.getSchool();
        setUsername((String) session.getAttribute("username"));
        setStudent(csbl.findStudent(getUsername()));

        System.out.println("ProjectDetailsBean Finish initialization");
        getAllActivities();
    }

    public void getAllActivities() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester;
        path = path.replaceAll("\\\\", "/");
        Path check = Paths.get(path);
        if (Files.exists(check)) {
            list = findFoldersInDirectory(path);
        } else {
            //do nothing
        }
    }

    public List<String> findFoldersInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        FileFilter directoryFileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        List<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            if (checkActivity(directoryAsFile.getName())) {
                //dont add
            } else {
                foldersInDirectory.add(directoryAsFile.getName());
            }
        }
        return foldersInDirectory;
    }

    public boolean checkActivity(String foldername) {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        path = path + foldername + "-Closed.txt";
        Path folder = Paths.get(path);
        if (Files.exists(folder)) {
            return true;
        } else {
            return false;
        }
    }

    public void enterActivity(String foldername) {
        System.out.println("Selected Activity: " + foldername);
        try {
            session.setAttribute("module", module);
            session.setAttribute("activity", foldername);
            context.getExternalContext().redirect("whiteboard.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkPRFormSubmit(Module m) {
        return prasbl.checkPRFormSubmit(student,m);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
}
