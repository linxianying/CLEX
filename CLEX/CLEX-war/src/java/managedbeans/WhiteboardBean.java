/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jeffrey
 */
@ManagedBean
@ViewScoped
public class WhiteboardBean {

    FacesContext context;
    HttpSession session;

    private String currentDrawing;
    private String previousDrawing;
    private String imagevalue;

    private String drawingcolor;

    private String canvascolor;
    private String canvasheight;
    private String canvaswidth;

    private String activityname;
    private String moduleCode;
    private String semester;
    private String year;
    private String schoolname;

    private User userEntity;
    Module moduleEntity;

    private static final String URL_DATA_PNG_BASE64_PREFIX = "data:image/png;base64,";

    public WhiteboardBean() {
    }

    @PostConstruct
    public void init() {
        drawingcolor = "#000000";
        canvascolor = "#ffffff";
        canvasheight = "800";
        canvaswidth = "1200";
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        userEntity = (User) session.getAttribute("user");
        moduleEntity = (Module) session.getAttribute("module");
        activityname = session.getAttribute("activity").toString();
        moduleCode = moduleEntity.getCourse().getModuleCode();
        semester = moduleEntity.getTakenSem();
        year = moduleEntity.getTakenYear();
        schoolname = userEntity.getSchool();
    }

    public void enlargewidth() {
        int width = Integer.parseInt(canvaswidth);
        if (width > 1600) {
            System.out.println("maximum width");
        } else {
            width = width + 200;
            canvaswidth = Integer.toString(width);
        }
    }

    public void enlargeheight() {
        System.out.println(canvasheight);
        int height = Integer.parseInt(canvasheight);
        if (height > 1200) {
            System.out.println("maximum height");
        } else {
            height = height + 200;
            canvasheight = Integer.toString(height);
        }
    }

    public void shrinkwidth() {
        int width = Integer.parseInt(canvaswidth);
        if (width < 800) {
            System.out.println("minimum width");
        } else {
            width = width - 200;
            canvaswidth = Integer.toString(width);
        }
    }

    public void shrinkheight() {
        int height = Integer.parseInt(canvasheight);
        if (height < 800) {
            System.out.println("minimum height");
        } else {
            height = height - 200;
            canvasheight = Integer.toString(height);
        }
    }

    public void save() throws IOException {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        String filename = session.getAttribute("username").toString();
        String extension = ".png";
        String path = session.getServletContext().getRealPath("/");
        int pathlength = path.length();
        pathlength = pathlength - 10;
        path = path.substring(0, pathlength);
        path = path + "web/resources/school/" + schoolname + "/" + moduleCode + "/" + year + "-" + semester + "/" + activityname + "/";
        path = path.replaceAll("\\\\", "/");
        System.out.println("path " + path);
        Path folder = Paths.get(path);
        Path file = Files.createTempFile(folder, filename, extension);
        System.out.println("File name: " + file.getFileName().toString());
        String encoded = imagevalue.substring(URL_DATA_PNG_BASE64_PREFIX.length());
        System.out.println("encoded: " + encoded);
        byte[] decoded = Base64.getDecoder().decode(encoded);
        try (InputStream input = new ByteArrayInputStream(decoded)) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }
        if (Files.exists(Paths.get(path + filename + extension))) {
            Files.delete(Paths.get(path + filename + extension));
            Files.move(file, Paths.get(path + filename + extension));
        } else {
            Files.move(file, Paths.get(path + filename + extension));
        }

    }

    public void undo() {
        currentDrawing = previousDrawing;
    }

    public void onInput(ValueChangeEvent e) {

        previousDrawing = currentDrawing;
        currentDrawing = e.getNewValue().toString();

    }

    public void cleardrawing() {
        currentDrawing = "";
        previousDrawing = "";
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public Module getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(Module moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getCurrentDrawing() {
        return currentDrawing;
    }

    public void setCurrentDrawing(String currentDrawing) {
        this.currentDrawing = currentDrawing;
    }

    public String getPreviousDrawing() {
        return previousDrawing;
    }

    public void setPreviousDrawing(String previousDrawing) {
        this.previousDrawing = previousDrawing;
    }

    public String getImagevalue() {
        return imagevalue;
    }

    public void setImagevalue(String imagevalue) {
        this.imagevalue = imagevalue;
    }

    public String getDrawingcolor() {
        return drawingcolor;
    }

    public void setDrawingcolor(String drawingcolor) {
        this.drawingcolor = drawingcolor;
    }

    public String getCanvascolor() {
        return canvascolor;
    }

    public void setCanvascolor(String canvascolor) {
        this.canvascolor = canvascolor;
    }

    public String getCanvasheight() {
        return canvasheight;
    }

    public void setCanvasheight(String canvasheight) {
        this.canvasheight = canvasheight;
    }

    public String getCanvaswidth() {
        return canvaswidth;
    }

    public void setCanvaswidth(String canvaswidth) {
        this.canvaswidth = canvaswidth;
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
