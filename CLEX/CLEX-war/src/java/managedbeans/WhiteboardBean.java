/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jeffrey
 */
@ManagedBean
@SessionScoped
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

    private static final String URL_DATA_PNG_BASE64_PREFIX = "data:image/png;base64,";

    public WhiteboardBean() {
    }

    @PostConstruct
    public void init() {
        canvascolor = "#ffffff";
        drawingcolor = "#000000";
        canvasheight = "800";
        canvaswidth = "1000";
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
        path = path + "web/resources/image/";
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
            System.out.println("Try Delete and rename");
            Files.delete(Paths.get(path + filename + extension));
            Files.move(file, Paths.get(path + filename + extension));
        } else {
            System.out.println("Try rename");
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
}
