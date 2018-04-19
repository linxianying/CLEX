package managedbeans;

import entity.Module;
import entity.Student;
import entity.Timeslot;
import entity.User;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.mindmap.DefaultMindmapNode;

import org.primefaces.model.mindmap.MindmapNode;
import session.AnnouncementSessionBeanLocal;
import session.ScheduleSessionBeanLocal;

/**
 *
 * @author Jeffrey
 */
@ManagedBean
@ViewScoped
public class LecturerMindmapBean implements Serializable {

    @EJB
    AnnouncementSessionBeanLocal asbl;
    @EJB
    ScheduleSessionBeanLocal sbl;
    private ScheduleModel eventModel = new DefaultScheduleModel();

    private ScheduleEvent event = new DefaultScheduleEvent();
    private MindmapNode root;
    private MindmapNode selectedNode;
    private ArrayList<String> allfolders = new ArrayList<String>();
    private ArrayList<String> allfolders2 = new ArrayList<String>();
    private ArrayList<String> listoffiles = new ArrayList<String>();
    private ArrayList<String> subNodeFolders = new ArrayList<String>();
    private ArrayList<String> allAssignmentFolders = new ArrayList<String>();
    private ArrayList<String> submittedfiles = new ArrayList<String>();
    FacesContext context;
    HttpSession session;

    private User userEntity;
    private String moduleCode;
    private String newfoldername;
    private String selectedFolder;
    Module moduleEntity;
    private String semester;
    private String year;
    private String schoolname;
    private Date endDate;
    private String displaydeadline;
    private Date nowDate;

    private StreamedContent downloadedFile;

    public LecturerMindmapBean() {

    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        moduleEntity = (Module) session.getAttribute("module");
        moduleCode = moduleEntity.getCourse().getModuleCode();
        semester = moduleEntity.getTakenSem();
        year = moduleEntity.getTakenYear();
        schoolname = userEntity.getSchool();
        root = new DefaultMindmapNode(moduleEntity.getCourse().getModuleCode(), moduleEntity.getCourse().getModuleCode(), "00acac", false);
        allfolders.clear();
        retrieveAllFolders();
        createSubNodes();
        selectedNode = root;
        allAssignmentFolders.clear();
        retrieveAllAssignments();
        nowDate = new Date();

    }

    public void addAssignment() throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/Assignments/" + newfoldername + "/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
            String path2 = path + "Submissions/";
            Path folder2 = Paths.get(path2);
            Files.createDirectories(folder2);
        }
        refresh();
        newfoldername = "";
    }

    public void removeAssignment() throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/Assignments/" + selectedFolder + "/";
        path = path.replaceAll("\\\\", "/");
        File folder1 = new File(path);
        deleteFolder(folder1); //remove all items inside and the folder itself
        refresh();
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", selectedFolder + " has been removed.");
        context.addMessage(null, fmsg);
    }

    public String getDeadline() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/Assignments/" + selectedNode.getLabel() + "/";
        path = path.replaceAll("\\\\", "/");
        File directory = new File(path);
        String[] fList = directory.list();
        String deadline = "FALSE";
        for (String file : fList) {
            if (file.endsWith(".prism")) {
                deadline = file;
            }
        }
        if (deadline.equals("FALSE")) {
            return "No deadline has been set yet.";
        } else {
            deadline = deadline.replaceAll(".prism", "");
            deadline = deadline.replace(".", ":");
            System.out.println(deadline);
            deadline = "The submission deadline for this assignment is: " + deadline;
            return deadline;
        }
    }

    public void createFolder() throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/" + newfoldername + "/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
        refresh();
        newfoldername = "";
    }

    public void removeFolder() throws IOException {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/" + selectedFolder + "/";
        path = path.replaceAll("\\\\", "/");
        File folder1 = new File(path);
        deleteFolder(folder1); //remove all items inside and the folder itself
        refresh();
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", selectedFolder + " has been removed.");
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

    public void setDeadline() throws IOException {
        List<Timeslot> t = new ArrayList<Timeslot>();
        List<Student> students = (List<Student>) moduleEntity.getStudents();
        String assignmentname = moduleCode + " - " + selectedFolder;
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/Assignments/" + selectedFolder + "/";
        path = path.replaceAll("\\\\", "/");
        Path folder = Paths.get(path);
        File directory = new File(path);
        String[] fList = directory.list();
        String deadlinefile = "";
        for (String file : fList) {
            if (file.endsWith(".prism")) {
                deadlinefile = file;
            }
        }
        String path2 = path + deadlinefile;
        File checkfile = new File(path2);
        if (checkfile.exists()) {
            checkfile.delete();
            for (int i = 0; i < students.size(); i++) {
                System.out.println("Get timeslot for student: " + students.get(i).getName());
                t = (List<Timeslot>) students.get(i).getTimeslots();
                for(int x =0;x<t.size();x++) {
                    System.out.println(t.get(x).getTitle());
                    if(t.get(x).getTitle().equals(assignmentname)) {
                        User user1 = (User) students.get(i);
                        sbl.deleteTimeslot(t.get(x).getId(), user1);
                        System.out.println("Delete timeslot for" + students.get(i).getName());
                    }
                }
                System.out.println("-------------------------");
            }
        }
        
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH.mm");
        String date = DATE_FORMAT.format(endDate);
        Path file = Files.createTempFile(folder, date, ".prism");
        Files.move(file, Paths.get(path, date + ".prism"));
        date = date.replace(".", ":");
        enterAnnouncement(date);

        for (int i = 0; i < students.size(); i++) {
            addEvent(students.get(i), assignmentname, endDate);
        }
    }

    public void addEvent(Student student, String assignmentname, Date endDate) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = df.format(endDate);
        Timeslot timeslot = sbl.createTimeslot(student.getUsername(), assignmentname, date, date, assignmentname + " due on " + date, "NIL");
        eventModel.addEvent(new DefaultScheduleEvent(assignmentname, toCalendar(date), toCalendar(date), timeslot));
        event = new DefaultScheduleEvent();
    }

    public void enterAnnouncement(String date) {
        String title = moduleCode + " - Assignment: " + selectedFolder;
        String message = "The submission date for this assignment is on " + date + " and it has been marked on your calendar." + "\nEnsure that you complete and submit your work on time.";
        asbl.createLecturerAnnc(userEntity.getUsername(), title, message, moduleCode);
    }

    public void createSubNodes() {
        ArrayList<String> templist = new ArrayList<String>();
        MindmapNode subnode;
        for (int i = 0; i < allfolders.size(); i++) {
            if (allfolders.get(i).equals("Assignments")) {
                subnode = new DefaultMindmapNode(allfolders.get(i), allfolders.get(i), "ff6666", true);
            } else {
                subnode = new DefaultMindmapNode(allfolders.get(i), allfolders.get(i), "ccff66", true);
            }
            root.addNode(subnode);
            String path = session.getServletContext().getRealPath("/");
            int pathlength = path.length();
            pathlength = pathlength - 10;
            path = path.substring(0, pathlength);
            path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/" + allfolders.get(i) + "/";
            path = path.replaceAll("\\\\", "/");
            templist = (ArrayList<String>) findFoldersInDirectory(path);
            MindmapNode subsubnode;
            if (!templist.isEmpty()) {
                for (int x = 0; x < templist.size(); x++) {

                    if (templist.get(x).equals("Lecture Notes")) {
                        subsubnode = new DefaultMindmapNode(templist.get(x), templist.get(x), "62f442", true);
                    } else if (templist.get(x).equals("Tutorial")) {
                        subsubnode = new DefaultMindmapNode(templist.get(x), templist.get(x), "0099ff", true);
                    } else if (templist.get(x).equals("Lab")) {
                        subsubnode = new DefaultMindmapNode(templist.get(x), templist.get(x), "ff6600", true);
                    } else {
                        subsubnode = new DefaultMindmapNode(templist.get(x), templist.get(x), "0099ff", true);
                    }
                    subnode.addNode(subsubnode);
                }
            }
        }
    }

    public void retrieveAllAssignments() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/Assignments";
        path = path.replaceAll("\\\\", "/");
        List<String> items;
        items = findFoldersInDirectory(path);
        String tempname;
        for (int i = 0; i < items.size(); i++) {
            tempname = items.get(i);
            allAssignmentFolders.add(tempname);
        }
    }

    public void getSubmittedFiles(MindmapNode node) {
        submittedfiles.clear();
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-"
                + semester + "/Materials/Assignments/" + node.getLabel() + "/Submissions/";
        path = path.replaceAll("\\\\", "/");
        ArrayList<String> items = (ArrayList<String>) listFiles(path);
        for (int i = 0; i < items.size(); i++) {
            submittedfiles.add(items.get(i));
        }
    }

    public void retrieveAllFolders() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/";
        path = path.replaceAll("\\\\", "/");
        List<String> items;
        items = findFoldersInDirectory(path);
        String tempname;
        for (int i = 0; i < items.size(); i++) {
            tempname = items.get(i);
            allfolders.add(tempname);
        }
        retrieveAllFolders2();
    }

    public void retrieveAllFolders2() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/";
        path = path.replaceAll("\\\\", "/");
        List<String> items;
        items = findFoldersInDirectory(path);
        String tempname;
        for (int i = 0; i < items.size(); i++) {
            tempname = items.get(i);
            if (!tempname.equals("Assignments")) {
                allfolders2.add(tempname);
            }
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
            foldersInDirectory.add(directoryAsFile.getName());
        }
        return foldersInDirectory;
    }

    public void onNodeSelect(SelectEvent event) throws IOException {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        listoffiles.clear();
        selectedNode = (MindmapNode) event.getObject();
        displaydeadline = "";
        MindmapNode node = (MindmapNode) event.getObject();
        if (node.getLabel().equals(moduleCode)) {
            refresh();
        } else if (node.getChildren().isEmpty()) {
            refreshlistoffiles(node);
            if (node.getParent().getLabel().equals("Assignments")) {
                getSubmittedFiles(node);
                displaydeadline = getDeadline();
            }
        }
    }

    public void refreshlistoffiles(MindmapNode node) {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-"
                + semester + "/Materials/" + node.getParent().getLabel() + "/" + node.getLabel() + "/";
        path = path.replaceAll("\\\\", "/");
        ArrayList<String> items = (ArrayList<String>) listFiles(path);
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).endsWith(".prism")) {
                listoffiles.add(items.get(i));
            }
        }
    }

    public List<String> listFiles(String foldername) {
        File directory = new File(foldername);
        String[] fList = directory.list();
        List<String> filesInDirectory = new ArrayList<String>(fList.length);
        for (String file : fList) {
            if (!file.equals("Submissions")) {
                filesInDirectory.add(file);
            }
        }
        return filesInDirectory;
    }

    public void deleteFile(String filename) {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-"
                + semester + "/Materials/" + selectedNode.getParent().getLabel() + "/" + selectedNode.getLabel() + "/" + filename;
        path = path.replaceAll("\\\\", "/");
        File file = new File(path);
        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
        listoffiles.clear();
        refreshlistoffiles(selectedNode);
    }

    public void downloadAllFile() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-"
                + semester + "/Materials/" + selectedNode.getParent().getLabel() + "/" + selectedNode.getLabel() + "/";
        path = path.replaceAll("\\\\", "/");
        List<String> items = new ArrayList<String>();
        List<String> itemswithpath = new ArrayList<String>();
        items = listFiles(path);
        for (int i = 0; i < items.size(); i++) {
            itemswithpath.add(path + items.get(i));
        }
        String zipFile = System.getProperty("user.home") + "/Desktop/"
                + moduleCode + "-" + selectedNode.getParent().getLabel() + "-" + selectedNode.getLabel() + ".zip";
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

    public void downloadFile(String filename) {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-"
                + semester + "/Materials/" + selectedNode.getParent().getLabel() + "/" + selectedNode.getLabel() + "/" + filename;
        path = path.replaceAll("\\\\", "/");
        String zipFile = System.getProperty("user.home") + "/Desktop/"
                + moduleCode + "-" + selectedNode.getParent().getLabel() + "-" + selectedNode.getLabel() + ".zip";
        zipFile = zipFile.replaceAll("\\\\", "/");
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(path);
            FileInputStream fis = new FileInputStream(srcFile);
            zos.putNextEntry(new ZipEntry(srcFile.getName()));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
            zos.close();
        } catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        extension = "." + extension;
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-"
                + semester + "/Materials/" + selectedNode.getParent().getLabel() + "/" + selectedNode.getLabel() + "/";
        path = path.replaceAll("\\\\", "/");
        System.out.println("path " + path);
        Path folder = Paths.get(path);
        Path file = Files.createTempFile(folder, event.getFile().getFileName(), extension);

        try (InputStream input = event.getFile().getInputstream()) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println("File successfully saved in " + file);
        FacesMessage message = new FacesMessage("File(s) successfully uploaded!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        if (Files.exists(Paths.get(path + event.getFile().getFileName()))) {
            System.out.println("Try Delete and rename");
            Files.delete(Paths.get(path + event.getFile().getFileName()));
        } else {
            System.out.println("Try rename");
            Files.move(file, Paths.get(path + event.getFile().getFileName()));
        }
        listoffiles.clear();
        refreshlistoffiles(selectedNode);
    }

//    public Date getInitialDate() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
//        return calendar.getTime();
//    }
//
    public static Date toCalendar(String date) {
        Calendar t = Calendar.getInstance();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d = df.parse(date);
            t.setTime(d);
        } catch (Exception e) {
            System.err.println(e);
        }
        return t.getTime();
    }

    public String getDisplaydeadline() {
        return displaydeadline;
    }

    public void setDisplaydeadline(String displaydeadline) {
        this.displaydeadline = displaydeadline;
    }

    public MindmapNode getRoot() {
        return root;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public StreamedContent getDownloadedFile() {
        return downloadedFile;
    }

    public ArrayList<String> getAllfolders() {
        return allfolders;
    }

    public void setAllfolders(ArrayList<String> allfolders) {
        this.allfolders = allfolders;
    }

    public ArrayList<String> getSubNodeFolders() {
        return subNodeFolders;
    }

    public void setSubNodeFolders(ArrayList<String> subNodeFolders) {
        this.subNodeFolders = subNodeFolders;
    }

    public ArrayList<String> getListoffiles() {
        return listoffiles;
    }

    public void setListoffiles(ArrayList<String> listoffiles) {
        this.listoffiles = listoffiles;
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

    public Module getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(Module moduleEntity) {
        this.moduleEntity = moduleEntity;
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

    public String getNewfoldername() {
        return newfoldername;
    }

    public void setNewfoldername(String newfoldername) {
        this.newfoldername = newfoldername;
    }

    public String getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(String selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public ArrayList<String> getAllAssignmentFolders() {
        return allAssignmentFolders;
    }

    public void setAllAssignmentFolders(ArrayList<String> allAssignmentFolders) {
        this.allAssignmentFolders = allAssignmentFolders;
    }

    public ArrayList<String> getSubmittedfiles() {
        return submittedfiles;
    }

    public void setSubmittedfiles(ArrayList<String> submittedfiles) {
        this.submittedfiles = submittedfiles;
    }

    public ArrayList<String> getAllfolders2() {
        return allfolders2;
    }

    public void setAllfolders2(ArrayList<String> allfolders2) {
        this.allfolders2 = allfolders2;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public Date getNowDate() {
        return nowDate;
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = nowDate;
    }

}
