/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.User;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private String semester;
    private String year;
    private String schoolname;
    private StreamedContent file;

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

        getAllActivities();
    }

    public void createActivity() throws IOException {
        String path = session.getServletContext().getRealPath("/");
        String path2 = session.getServletContext().getRealPath("/");
        String path3 = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/";
        path = path.replaceAll("\\\\", "/");

        int pathlength2 = path2.length();
        pathlength2 = pathlength2 - 10;
        path2 = path2.substring(0, pathlength2);
        path2 = path2 + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/";
        path2 = path2.replaceAll("\\\\", "/");

        int pathlength3 = path3.length();
        pathlength3 = pathlength3 - 10;
        path3 = path3.substring(0, pathlength2);
        path3 = path3 + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + activityname + "/";
        path3 = path3.replaceAll("\\\\", "/");

        Path folder1 = Paths.get(path);
        Path folder2 = Paths.get(path2);
        Path folder3 = Paths.get(path3);

        if (!Files.exists(folder1)) {
            Files.createDirectory(folder1);
        }
        if (!Files.exists(folder2)) {
            Files.createDirectory(folder2);
        }
        if (!Files.exists(folder3)) {
            Files.createDirectory(folder3);
        }
        getAllActivities();
        activityname = "";
    }

    public List<String> getAllItemsInFolder(String foldername) throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");

        List<String> items = new ArrayList<String>();
        items = listFiles(path);
        return items;
    }

    public List<String> listFiles(String foldername) {
        System.out.println(foldername);
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

    public void getAllActivities() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester;
        path = path.replaceAll("\\\\", "/");
        list = findFoldersInDirectory(path);
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
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        Path file = Files.createTempFile(folder, foldername + "-Closed", ".txt");
        Files.move(file, Paths.get(path, foldername + "-Closed.txt"));
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

    public void removeFolder(String foldername) throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        File folder1 = new File(path);
        deleteFolder(folder1); //remove all items inside and the folder itself
        getAllActivities();
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
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + foldername + "/";
        path = path.replaceAll("\\\\", "/");
        List<String> items = new ArrayList<String>();
        List<String> itemswithpath = new ArrayList<String>();
        items = listFiles(path);
        for (int i = 0; i < items.size(); i++) {
            System.out.println(path + items.get(i));
            itemswithpath.add(path + items.get(i));
        }

        String zipFile = System.getProperty("user.home") + "/Desktop/" + foldername + ".zip";
        zipFile = zipFile.replaceAll("\\\\", "/");
        System.out.println(zipFile);

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
