package managedbeans;

import entity.Module;
import entity.User;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;

import org.primefaces.model.mindmap.MindmapNode;

/**
 *
 * @author Jeffrey
 */
@ManagedBean
@ViewScoped
public class LecturerMindmapBean implements Serializable {

    private MindmapNode root;

    private MindmapNode selectedNode;
    private ArrayList<String> allWeeks = new ArrayList<String>();
    FacesContext context;
    HttpSession session;

    private User userEntity;
    private String moduleCode;

    Module moduleEntity;
    private String semester;
    private String year;
    private String schoolname;

    public LecturerMindmapBean() {

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
        root = new DefaultMindmapNode(moduleEntity.getCourse().getModuleCode(), moduleEntity.getCourse().getModuleCode(), "00acac", false);
        allWeeks.clear();
        retrieveAllWeeks();
        createSubNodes();
    }

    public void createSubNodes() {
        for (int i = 0; i < allWeeks.size(); i++) {
            MindmapNode subnode = new DefaultMindmapNode(allWeeks.get(i), allWeeks.get(i), "62f442", true);
            root.addNode(subnode);
            System.out.println(allWeeks.get(i));
        }
    }

    public void retrieveAllWeeks() {
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/";
        path = path.replaceAll("\\\\", "/");
        List<String> items;
        items = findFoldersInDirectory(path);
        String tempweek;
        for (int i = 0; i < items.size(); i++) {
            tempweek = items.get(i);
            allWeeks.add(tempweek);
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

    public MindmapNode getRoot() {
        return root;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect(SelectEvent event) {

        MindmapNode node = (MindmapNode) event.getObject();
        if (!node.getLabel().equals(moduleEntity.getCourse().getModuleCode())) {
            String path = session.getServletContext().getRealPath("/");
            int pathlength = path.length();
            pathlength = pathlength - 10;
            path = path.substring(0, pathlength);
            path = path + "web/serverfiles/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/Materials/" + node.getLabel() + "/";
            path = path.replaceAll("\\\\", "/");
            ArrayList<String> subFolders = new ArrayList<String>();
            List<String> items;
            items = findFoldersInDirectory(path);
            String tempweek;
            for (int i = 0; i < items.size(); i++) {
                tempweek = items.get(i);
                subFolders.add(tempweek);
            }
            for (int i = 0; i < subFolders.size(); i++) {
                node.addNode(new DefaultMindmapNode(subFolders.get(i), subFolders.get(i), "82c542", true));

            }
        }

    }

    public ArrayList<String> getAllWeeks() {
        return allWeeks;
    }

    public void setAllWeeks(ArrayList<String> allWeeks) {
        this.allWeeks = allWeeks;
    }

    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();
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

}
