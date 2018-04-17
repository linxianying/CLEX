/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Jeffrey
 */
@ManagedBean
@ViewScoped
public class LecturerWhiteboardBean {

    FacesContext context;
    HttpSession session;

    private String activityname;
    private User userEntity;
    private String moduleCode;
    private List<String> list;
    Module moduleEntity;
    private List<String> itemlist;

    private ArrayList<String> ongoinglist;
    private ArrayList<String> ongoingstudent;
    private String semester;
    private String year;
    private String schoolname;
    private StreamedContent file;
    private boolean displayStudentlist;
    private String selectedDrawing;
    private String selectedStudent;
    private String selectedActivity;

    public LecturerWhiteboardBean() {

    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        moduleEntity = (Module) session.getAttribute("module");
        moduleCode = moduleEntity.getCourse().getModuleCode();
        semester = moduleEntity.getTakenSem();
        year = moduleEntity.getTakenYear();
        schoolname = userEntity.getSchool();
        getAllOngoingActivities(forGetActivities());
        getAllCompletedActivities(forGetActivities());
        displayStudentlist = false;
    }

    public void selectActivity(String activityname) {
        selectedDrawing = "";
        selectedStudent = "";
        selectedActivity = activityname;
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + selectedActivity + "/";
        path = path.replaceAll("\\\\", "/");
        String stu;
        List<String> items;
        items = listFiles2(path);
        ongoingstudent = new ArrayList<String>(items.size());
        for (int i = 0; i < items.size(); i++) {
            stu = items.get(i);
            stu = stu.replaceAll(".txt", "");
            ongoingstudent.add(stu);
        }

        displayStudentlist = true;
    }

    public void selectStudent(String studentName) {
        selectedStudent = studentName;
        selectedDrawing = retrieveStudentDrawing(selectedStudent);
    }

    public void updateDrawing() {
        selectedDrawing = retrieveStudentDrawing(selectedStudent);
    }

    public List<String> forGetActivities() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/";
        path = path.replaceAll("\\\\", "/");
        List<String> items;
        items = findFoldersInDirectory(path);
        return items;

    }

    public void getAllCompletedActivities(List<String> items) {
        list = new ArrayList<String>(items.size());
        String activity;
        for (int i = 0; i < items.size(); i++) {
            activity = items.get(i);
            if (checkActivity(activity) == true) {
                list.add(activity);
            }
        }
    }

    public void getAllOngoingActivities(List<String> items) {
        ongoinglist = new ArrayList<String>(items.size());
        String activity;
        for (int i = 0; i < items.size(); i++) {
            activity = items.get(i);
            if (checkActivity(activity) == false) {
                ongoinglist.add(activity);
            }
        }
    }

    public String retrieveStudentDrawing(String studentName) {
        String studentDrawing = "";
        String extension = ".txt";
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + selectedActivity + "/";
        path = path.replaceAll("\\\\", "/");
        path = path + studentName + extension;
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                studentDrawing = sCurrentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return studentDrawing;
    }

    public void createActivity() throws IOException {
        String path3 = session.getServletContext().getRealPath("/");
        String path4 = session.getServletContext().getRealPath("/");
        int pathlength3 = path3.length();
        pathlength3 = pathlength3 - 10;
        path3 = path3.substring(0, pathlength3);
        path3 = path3 + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + activityname + "/";
        path3 = path3.replaceAll("\\\\", "/");
        int pathlength4 = path4.length();
        pathlength4 = pathlength4 - 10;
        path4 = path4.substring(0, pathlength4);
        path4 = path4 + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + activityname + "/submitted/";
        path4 = path4.replaceAll("\\\\", "/");
        Path folder3 = Paths.get(path3);
        Path folder4 = Paths.get(path4);
        if (!Files.exists(folder3)) {
            Files.createDirectories(folder3);
        }
        if (!Files.exists(folder4)) {
            Files.createDirectories(folder4);
        }
        getAllOngoingActivities(forGetActivities());
        activityname = "";
    }

    public List<String> getAllItemsInFolder(String foldername) throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + foldername + "/submitted/";
        path = path.replaceAll("\\\\", "/");
        List<String> items = new ArrayList<String>();
        items = listFiles(path);
        return items;
    }

    public List<String> listFiles(String foldername) {
        File directory = new File(foldername);
        String[] fList = directory.list();
        List<String> filesInDirectory = new ArrayList<String>(fList.length);
        for (String file : fList) {
            if (file.endsWith(".png")) {
                filesInDirectory.add(file);
            }
        }
        return filesInDirectory;
    }

    public List<String> listFiles2(String foldername) {
        File directory = new File(foldername);
        String[] fList = directory.list();
        List<String> filesInDirectory = new ArrayList<String>(fList.length);
        for (String file : fList) {
            if (file.endsWith(".txt")) {
                filesInDirectory.add(file);
            }
        }
        return filesInDirectory;
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
            foldersInDirectory.add(directoryAsFile.getName());
        }
        return foldersInDirectory;
    }

    public void closeActivity(String foldername) throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        Path file = Files.createTempFile(folder, foldername + "-Closed", ".prism");
        Files.move(file, Paths.get(path, foldername + "-Closed.prism"));
        getAllOngoingActivities(forGetActivities());
        getAllCompletedActivities(forGetActivities());
    }

    public boolean checkActivity(String foldername) {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        path = path + foldername + "-Closed.prism";
        Path folder = Paths.get(path);
        if (Files.exists(folder)) {
            return true;
        } else {
            return false;
        }
    }

    public void removeFolder(String foldername) throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        File folder1 = new File(path);
        deleteFolder(folder1); //remove all items inside and the folder itself
        getAllCompletedActivities(forGetActivities());
        getAllOngoingActivities(forGetActivities());
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", foldername + " has been removed.");
        context.addMessage(null, fmsg);
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void downloadallfiles(String foldername) {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Activities/" + foldername + "/submitted/";
        path = path.replaceAll("\\\\", "/");
        List<String> items = new ArrayList<String>();
        List<String> itemswithpath = new ArrayList<String>();
        items = listFiles(path);
        for (int i = 0; i < items.size(); i++) {
            itemswithpath.add(path + items.get(i));
        }

        String zipFile = System.getProperty("user.home") + "/Desktop/" + foldername + ".zip";
        zipFile = zipFile.replaceAll("\\\\", "/");

        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < itemswithpath.size(); i++) {
                File srcFile = new File(itemswithpath.get(i));
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();

        } catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }

    public String getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(String selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    public boolean isDisplayStudentlist() {
        return displayStudentlist;
    }

    public void setDisplayStudentlist(boolean displayStudentlist) {
        this.displayStudentlist = displayStudentlist;
    }

    public String getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(String selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public ArrayList<String> getOngoinglist() {
        return ongoinglist;
    }

    public void setOngoinglist(ArrayList<String> ongoinglist) {
        this.ongoinglist = ongoinglist;
    }

    public ArrayList<String> getOngoingstudent() {
        return ongoingstudent;
    }

    public void setOngoingstudent(ArrayList<String> ongoingstudent) {
        this.ongoingstudent = ongoingstudent;
    }

    public String getSelectedDrawing() {
        return selectedDrawing;
    }

    public void setSelectedDrawing(String selectedDrawing) {
        this.selectedDrawing = selectedDrawing;
    }

    public StreamedContent getFile() {
        return file;
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

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Module getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(Module moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public List<String> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<String> itemlist) {
        this.itemlist = itemlist;
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
