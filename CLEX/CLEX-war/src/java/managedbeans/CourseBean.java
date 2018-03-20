/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Lecturer;
import entity.Module;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.poseidon.domain.Theme;
import session.CourseMgmtBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class CourseBean implements Serializable {

    @EJB
    CourseMgmtBeanLocal cmbl;

    //Others
    private String lecturerUser;

    private ArrayList<User> lecturerlist;
    private ArrayList<Module> modulelist;

    public CourseBean() {

    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        modulelist = (ArrayList<Module>) cmbl.getAllModules();
        lecturerlist = (ArrayList<User>) cmbl.getLecturerName();
    }

    public Collection<Lecturer> getAssignedLecturer(Module moduleEntity) {
        List<Lecturer> users = new ArrayList<Lecturer>();
        List<Lecturer> lecturers = (List) moduleEntity.getLecturers();
        for (int i = 0; i < lecturers.size(); i++) {
            users.add(lecturers.get(i));
        }
        return users;
    }

    public void assignLecturer(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempModuleCode = moduleEntity.getCourse().getModuleCode();
        String temptakenYear = moduleEntity.getTakenYear();
        String temptakenSem = moduleEntity.getTakenSem();
        System.out.println("Assigning " + lecturerUser + " to " + tempModuleCode);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkExistingModule(tempModuleCode, temptakenYear, temptakenSem) == true) {
            if (cmbl.linkLecturerToModule(tempModuleCode, temptakenYear, temptakenSem, lecturerUser) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lecturer " + lecturerUser + " assigned to " + tempModuleCode + ".");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.update("panel1:moduletable");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error.", "Failed to assign lecturer.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exists");
            context.addMessage(null, fmsg);
        }
    }

    public void unassignLecturer(Module moduleEntity) throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String tempModuleCode = moduleEntity.getCourse().getModuleCode();
        String temptakenYear = moduleEntity.getTakenYear();
        String temptakenSem = moduleEntity.getTakenSem();
        System.out.println("Unassigning " + lecturerUser + " from " + tempModuleCode);
        RequestContext reqcontext = RequestContext.getCurrentInstance();
        if (cmbl.checkExistingModule(tempModuleCode, temptakenYear, temptakenSem) == true) {
            if (cmbl.removeLecturerFromModule(tempModuleCode, temptakenYear, temptakenSem, lecturerUser) == true) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Lecturer " + lecturerUser + " unassigned fom " + tempModuleCode + ".");
                context.addMessage(null, fmsg);
                refresh();
                reqcontext.update("panel1:moduletable");
            } else {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error.", "Failed to unassign lecturer.");
                context.addMessage(null, fmsg);
            }
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Module does not exists");
            context.addMessage(null, fmsg);
        }
    }

    public String getLecturerUser() {
        return lecturerUser;
    }

    public void setLecturerUser(String lecturerUser) {
        this.lecturerUser = lecturerUser;
    }

    public ArrayList<User> getLecturerlist() {
        return lecturerlist;
    }

    public void setLecturerlist(ArrayList<User> lecturerlist) {
        this.lecturerlist = lecturerlist;
    }

    public ArrayList<Module> getModulelist() {
        return modulelist;
    }

    public void setModulelist(ArrayList<Module> modulelist) {
        this.modulelist = modulelist;
    }

}
