/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.ProjectGroup;
import entity.Student;
import entity.Module;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javaClass.StudentCost;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@Named(value = "projectCostBean")
@SessionScoped
public class ProjectCostBean implements Serializable {
    
    ClexSessionBeanLocal csbl;
    
    FacesContext context;
    HttpSession session;
    
    private Module module;
    private ProjectGroup group;
    
    private String activity;
    private double cost;
    private Collection<Student> groupMembers;
    private Student individualPayer;
    private ArrayList<StudentCost> payers;
    private ArrayList<StudentCost> payees;
    private String splitBy;
    private String paidBy;

    private boolean multiple;
    private boolean individual;
    
    
    public ProjectCostBean() {
    }
    
    @PostConstruct
    public void init() {
        //for test only
        //group = csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2"));
        //groupMembers = group.getGroupMembers();
        
        
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        group = (ProjectGroup) session.getAttribute("projectGroup");
        groupMembers = group.getGroupMembers();
        module = (Module) session.getAttribute("module");
        paidBy = "Individual";
        this.setOriPayers();
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

    public ProjectGroup getGroup() {
        return group;
    }

    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public Collection<Student> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(Collection<Student> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public Student getIndividualPayer() {
        return individualPayer;
    }

    public void setIndividualPayer(Student individualPayer) {
        this.individualPayer = individualPayer;
    }

    public String getSplitBy() {
        return splitBy;
    }

    public void setSplitBy(String splitBy) {
        this.splitBy = splitBy;
    }

    public String getPaidBy() {
        //System.out.println("Get paidby:" + paidBy);
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        //System.out.println("Set paidby:" + paidBy);
        this.paidBy = paidBy;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ArrayList<StudentCost> getPayers() {
        return payers;
    }

    public void setPayers(ArrayList<StudentCost> payers) {
        this.payers = payers;
    }

    public ArrayList<StudentCost> getPayees() {
        return payees;
    }

    public void setPayees(ArrayList<StudentCost> payees) {
        this.payees = payees;
    }
    
    //in case of multiple payers, set each student with the amount they pay
    public void setOriPayers() {
        payers = new ArrayList<StudentCost>();
        for (Student s: this.groupMembers) {
            StudentCost studentCost = new StudentCost(s, 0.0);
            payers.add(studentCost);
        }
    }
    
    //check there is one payer or multiple
    public void checkNumOfPayer() {
        // if there is more than one payer
        System.out.println("paidby:" + paidBy);
    }
    
    
}
