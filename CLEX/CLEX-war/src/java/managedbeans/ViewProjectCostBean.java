/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Module;
import entity.ProjectGroup;
import entity.Student;
import entity.Transaction;
import java.util.ArrayList;
import javaClass.ComparableTransaction;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.ProjectCostSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@Named(value = "viewProjectCostBean")
@SessionScoped
public class ViewProjectCostBean {
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectCostSessionBeanLocal pcsbl;
    
    FacesContext context;
    HttpSession session;
    
    private Module module;
    private ProjectGroup group;
    private Student student;
    private ArrayList<Transaction> transactions;
    private ArrayList<ComparableTransaction> sortedTransactions;
    
    
    
    
    public ViewProjectCostBean() {
    }
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        group = (ProjectGroup) session.getAttribute("projectGroup");
        student = (Student) session.getAttribute("user");
        group = (ProjectGroup) session.getAttribute("projectGroup");
        
        sortedTransactions = pcsbl.getSortedTransactions(group);
        System.out.println("Finish initialization");
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ProjectCostSessionBeanLocal getPcsbl() {
        return pcsbl;
    }

    public void setPcsbl(ProjectCostSessionBeanLocal pcsbl) {
        this.pcsbl = pcsbl;
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ProjectGroup getGroup() {
        return group;
    }

    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<ComparableTransaction> getSortedTransactions() {
        return sortedTransactions;
    }

    public void setSortedTransactions(ArrayList<ComparableTransaction> sortedTransactions) {
        this.sortedTransactions = sortedTransactions;
    }
    
    
}
