/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

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

    public WhiteboardBean() {
    }

    public void init() {

    }

    public void undo() {
        currentDrawing = previousDrawing;
    }

    public void onInput(ValueChangeEvent e) {
        
        previousDrawing = currentDrawing;
        currentDrawing = e.getNewValue().toString();
        
    }

    public void cleardrawing() {
        System.out.println("pre before: " + previousDrawing);
        System.out.println("curr before: " + currentDrawing);
        currentDrawing = "";
        previousDrawing = "";
        System.out.println("pre after: " + previousDrawing);
        System.out.println("curr after: " + currentDrawing);
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
}
