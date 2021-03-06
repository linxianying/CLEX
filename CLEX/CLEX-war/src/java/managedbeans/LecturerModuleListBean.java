/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Lesson;
import entity.Module;
import entity.Student;
import entity.User;
import static facebook4j.BackdatedTimeGranularity.year;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.AnnouncementSessionBeanLocal;
import session.ClassroomSessionBeanLocal;
import session.CourseMgmtBeanLocal;

@ManagedBean(name = "lecturerModuleListBean")
@SessionScoped

public class LecturerModuleListBean implements Serializable {

    @EJB
    CourseMgmtBeanLocal cmbl;
    @EJB
    AnnouncementSessionBeanLocal asbl;

    private ClassroomSessionBeanLocal crsbl;

    private List<Module> modules;
    private List<Module> filteredModules;
    //private List moduleCodes;
    private List<Lesson> lessons;
    private List<Lesson> filteredLessons;

    private ArrayList<Module> currentModules;
    private int currentYear;
    private int currentSem;
    private int numOfGroups;
    private int minStudentNum;
    private int maxStudentNum;

    private Lecturer lecturerEntity;
    private ArrayList<Student> students;
    private List<String> moduleCodes;

    private Module selectedModule;

    private String username;
    private List<String> daylist;
    private List<String> timelist;
    private String lecturerUser;

    FacesContext context;
    HttpSession session;

    private String prerequisite;
    private String preclusions;
    private String moduleCode;
    private String takenYear;
    private String takenSem;

    public LecturerModuleListBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        lecturerEntity = (Lecturer) session.getAttribute("user");
        username = lecturerEntity.getUsername();
        this.setCurrentYearSem();
        refresh();
    }

    public void refresh() {
        modules = (List) cmbl.getModulesFromLecturer(username);
        lessons = (List) cmbl.getLessonsFromLecturer(username);
        moduleCodes = asbl.getModuleCodeByLecturer(username);
        timelist = (List) cmbl.getAllTimings();
        daylist = (List) cmbl.getAllDays();
        currentModules = cmbl.getCurrentModulesFromLecturer(username, Integer.toString(currentYear), Integer.toString(currentSem));
    }

    public void setCurrentYearSem() {
        Calendar now = Calendar.getInstance();
        currentYear = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
        } else {
            currentSem = 1;
        }
    }

    public void viewModule(Module module) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        session.setAttribute("user", lecturerEntity);
        session.setAttribute("username", username);
        session.setAttribute("module", module);
        session.setAttribute("moduleCode", module.getCourse().getModuleCode());
        session.setAttribute("pickSem", module.getTakenSem());
        session.setAttribute("pickYear", module.getTakenYear());
        context.getExternalContext().redirect("lecturerModuleInfo.xhtml");
    }

    public void lectEditModule() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        lecturerUser = (String) session.getAttribute("username");
        System.out.println("Code: " + moduleCode + " Year: " + takenYear + " Sem: " + takenSem + " Prereq: " + prerequisite + " Preclu: " + preclusions);

        if (cmbl.checkExistingModule(moduleCode, takenYear, takenSem) == true) {
            if (cmbl.checkLectTeachModule(lecturerUser, moduleCode, takenYear, takenSem)) {
                cmbl.editModule(takenYear, takenSem, prerequisite, preclusions, moduleCode);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", moduleCode + " has been updated.");
                context.addMessage(null, fmsg);
                refresh();
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", "You are not authorised to edit this module.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exist.");
            context.addMessage(null, fmsg);
        }
    }

    public void lectEditLesson(Lesson lessonEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        lecturerUser = (String) session.getAttribute("username");
        String tempModCode = lessonEntity.getModule().getCourse().getModuleCode();
        String tempTakenYear = lessonEntity.getModule().getTakenYear();
        String tempTakenSem = lessonEntity.getModule().getTakenSem();
        String tempDay = lessonEntity.getDay();
        String tempTimeStart = lessonEntity.getTimeFrom();
        String tempTimeEnd = lessonEntity.getTimeEnd();
        String tempType = lessonEntity.getType();
        String tempVenue = lessonEntity.getVenue();
        if (cmbl.checkExistingLesson(tempModCode, tempTakenYear, tempTakenSem, tempDay, tempTimeStart, tempTimeEnd) == true) {
            if (cmbl.checkLectTeachModule(lecturerUser, tempModCode, tempTakenYear, tempTakenSem)) {
                cmbl.editLesson(tempDay, tempTimeStart, tempTimeEnd, tempType, tempVenue, tempModCode, tempTakenYear, tempTakenSem);
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", tempModCode + " has been updated.");
                context.addMessage(null, fmsg);
                refresh();
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", "You are not authorised to edit this lesson.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Lesson does not exist.");
            context.addMessage(null, fmsg);
        }
    }

    public void formGroup(Module module) {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("managedModule", module);
        try {
            context.getExternalContext().redirect("lecturerModuleGroup.xhtml");
            //System.out.println("Form group "+module.getCourse().getModuleCode()+": # of groups:"+numOfGroups+", max:"+maxStudentNum + ", min:" +minStudentNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //add the peer review template to this module
    public void startPR(Module module) {
        cmbl.addPRForm(module);
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("managedModule", module);
        try {
            context.getExternalContext().redirect("lecturerUpdatePRform.xhtml");
            //System.out.println("Form group "+module.getCourse().getModuleCode()+": # of groups:"+numOfGroups+", max:"+maxStudentNum + ", min:" +minStudentNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goPRPage(Module module) {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("managedModule", module);
//        try {
//        context.getExternalContext().redirect("lecturerUpdatePRform.xhtml");
//        //System.out.println("Form group "+module.getCourse().getModuleCode()+": # of groups:"+numOfGroups+", max:"+maxStudentNum + ", min:" +minStudentNum);
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
    }

    public void manageMaterials(Module module) throws IOException {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("module", module);
        User userEntity = (User) session.getAttribute("user");
        String schoolname = userEntity.getSchool();
        String moduleCode = module.getCourse().getModuleCode();
        String year = module.getTakenYear();
        String semester = module.getTakenSem();
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        if (Files.exists(folder)) {
            //do nothing
        } else {
            Files.createDirectories(folder);
            String path2 = path + "Assignments/";
            Path folder2 = Paths.get(path2);
            if (Files.exists(folder2)) {
                //do nothing
            } else {
                Files.createDirectories(folder2);
            }
            for (int i = 1; i < 14; i++) {
                String temppath = path + "Week " + Integer.toString(i) + "/";
                Path tempfolder = Paths.get(temppath);
                if (Files.exists(tempfolder)) {
                    //do nothing
                } else {
                    Files.createDirectories(tempfolder);
                    String lecture = temppath + "Lecture Notes/";
                    String tutorial = temppath + "Tutorial/";
                    String lab = temppath + "Lab/";
                    Path tempfolder1 = Paths.get(lecture);
                    Path tempfolder2 = Paths.get(tutorial);
                    Path tempfolder3 = Paths.get(lab);
                    Files.createDirectories(tempfolder1);
                    Files.createDirectories(tempfolder2);
                    Files.createDirectories(tempfolder3);
                }
            }

        }

        context.getExternalContext().redirect("lecturerMindmap.xhtml");
    }

    public void manageActivities(Module module) throws IOException {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("module", module);
        User userEntity = (User) session.getAttribute("user");
        String schoolname = userEntity.getSchool();
        String moduleCode = module.getCourse().getModuleCode();
        String year = module.getTakenYear();
        String semester = module.getTakenSem();
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        if (Files.exists(folder)) {
            //do nothing
        } else {
            Files.createDirectories(folder);
        }
        context.getExternalContext().redirect("createWhiteboardActivity.xhtml");
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Module> getFilteredModules() {
        return filteredModules;
    }

    public void setFilteredModules(List<Module> filteredModules) {
        this.filteredModules = filteredModules;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Lesson> getFilteredLessons() {
        return filteredLessons;
    }

    public void setFilteredLessons(List<Lesson> filteredLessons) {
        this.filteredLessons = filteredLessons;
    }

    public Lecturer getLecturerEntity() {
        return lecturerEntity;
    }

    public void setLecturerEntity(Lecturer lecturerEntity) {
        this.lecturerEntity = lecturerEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }

    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
    }

    public ClassroomSessionBeanLocal getCrsbl() {
        return crsbl;
    }

    public void setCrsbl(ClassroomSessionBeanLocal crsbl) {
        this.crsbl = crsbl;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
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

    public List<String> getModuleCodes() {
        return moduleCodes;
    }

    public void setModuleCodes(List<String> moduleCode) {
        this.moduleCodes = moduleCode;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }

    public List<String> getDaylist() {
        return daylist;
    }

    public void setDaylist(List<String> daylist) {
        this.daylist = daylist;
    }

    public List<String> getTimelist() {
        return timelist;
    }

    public void setTimelist(List<String> timelist) {
        this.timelist = timelist;
    }

    public String getLecturerUser() {
        return lecturerUser;
    }

    public void setLecturerUser(String lecturerUser) {
        this.lecturerUser = lecturerUser;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getPreclusions() {
        return preclusions;
    }

    public void setPreclusions(String preclusions) {
        this.preclusions = preclusions;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getTakenYear() {
        return takenYear;
    }

    public void setTakenYear(String takenYear) {
        this.takenYear = takenYear;
    }

    public String getTakenSem() {
        return takenSem;
    }

    public void setTakenSem(String takenSem) {
        this.takenSem = takenSem;
    }

    public AnnouncementSessionBeanLocal getAsbl() {
        return asbl;
    }

    public void setAsbl(AnnouncementSessionBeanLocal asbl) {
        this.asbl = asbl;
    }

    public ArrayList<Module> getCurrentModules() {
        return currentModules;
    }

    public void setCurrentModules(ArrayList<Module> currentModules) {
        this.currentModules = currentModules;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentSem() {
        return currentSem;
    }

    public void setCurrentSem(int currentSem) {
        this.currentSem = currentSem;
    }

    public int getNumOfGroups() {
        return numOfGroups;
    }

    public void setNumOfGroups(int numOfGroups) {
        this.numOfGroups = numOfGroups;
    }

    public int getMinStudentNum() {
        return minStudentNum;
    }

    public void setMinStudentNum(int minStudentNum) {
        this.minStudentNum = minStudentNum;
    }

    public int getMaxStudentNum() {
        return maxStudentNum;
    }

    public void setMaxStudentNum(int maxStudentNum) {
        this.maxStudentNum = maxStudentNum;
    }

}
