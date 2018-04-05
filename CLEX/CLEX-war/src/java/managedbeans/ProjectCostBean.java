/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.ProjectGroup;
import entity.Student;
import entity.Module;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javaClass.StudentCost;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.ProjectCostSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "projectCostBean")
@SessionScoped
public class ProjectCostBean implements Serializable {

    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ProjectCostSessionBeanLocal pcsbl;

    FacesContext context;
    HttpSession session;

    private Module module;
    private ProjectGroup group;
    private Student student;

    private String activity;
    private double totalCost;
    private double cost;
    //private Map<Long, Student> groupMembers;
    private Collection<Student> groupMembers;
//    private ArrayList<Student> testGroupMembers;
//    private ArrayList<String> testGroupMembersName;
    private Student individualPayer;
    private String individualPayerUsername;
    private Long individualPayerId;
    private ArrayList<StudentCost> all;
    //private ArrayList<StudentCost> payees;
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
        refresh();

    }

    public void refresh() {
        System.out.println("ProjectCostBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        group = (ProjectGroup) session.getAttribute("projectGroup");
        groupMembers = group.getGroupMembers();
        //testGroupMembers = new ArrayList<Student>();
        //testGroupMembersName = new ArrayList<String>();
//        this.setOriStudentCost();
        module = (Module) session.getAttribute("module");
        paidBy = "Individual";
        splitBy = "Equally";
        this.setOriStudentCost();
        System.out.println(" ProjectCostBean Finish initialization");
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ProjectCostSessionBeanLocal getPcsbl() {
        return pcsbl;
    }

    public void setPcsbl(ProjectCostSessionBeanLocal pcsbl) {
        this.pcsbl = pcsbl;
    }

    public ArrayList<StudentCost> getAll() {
        return all;
    }

    public void setAll(ArrayList<StudentCost> all) {
        this.all = all;
    }

    public Collection<Student> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(Collection<Student> groupMembers) {
        this.groupMembers = groupMembers;
    }

    //in case of multiple payers and payees, set each student with the amount they pay
    public void setOriStudentCost() {
        all = new ArrayList<StudentCost>();
        for (Student s : this.getGroupMembers()) {
            StudentCost studentCost = new StudentCost(s, 0.0, 0.0);
            all.add(studentCost);
        }
    }

    public void addTransaction() {
        System.out.println("Strat to add transaction");
        System.out.println("activity=" + this.activity);
        System.out.println("totalcost=" + this.totalCost);
        System.out.println("individual payer ID = " + individualPayerId);

        for (StudentCost sc : all) {
            System.out.println(sc.toString());
            System.out.println(" ");
        }
        //if paid by individual
        if (paidBy.equals("Individual")) {
            // find the individualPayer in the payer arrayList and assign total cost to it
            System.out.println("It is paid by individual");
            for (StudentCost sc : all) {
                if (sc.getStudent().getId().equals(individualPayerId)) {
                    System.out.println(sc.getStudent().getName() + "find");
                    sc.setPay(totalCost);
                }
            }
        }
        // no need to set anything if paid by multiple people
        //if split equally 
        if (splitBy.equals("Equally")) {
            cost = totalCost / getGroupMembers().size();
            for (StudentCost sc : all) {
                sc.setCost(cost);
            }
        } //if split by percentage 
        else if (splitBy.equals("Percentage")) {
            for (StudentCost sc : all) {
                cost = totalCost * sc.getCost() / 100;
                sc.setCost(cost);
            }
        }
        System.out.println("activity=" + this.activity);
        System.out.println("totalcost=" + this.totalCost);
        System.out.println("all=" + this.all.toString());
        //pcsbl.addTransaction(all, activity, totalCost, group, date);

        //initialize 
        for (StudentCost sc : all) {
            sc.setPay(0.0);
            sc.setCost(0.0);
        }
//        try {
//            context.getExternalContext().redirect("viewProjectCost.xhtml");
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
        refresh();
    }

    //for test purpose
    //check there is one payer or multiple
    public void checkNumOfPayer() {
        // if there is more than one payer
        System.out.println("paidby:" + paidBy);
    }

    /*
     @return the groupMembers
     
     public Map<Long, Student> getGroupMembers() {
     return groupMembers;
     }

   
     * @param groupMembers the groupMembers to set
     public void setGroupMembers(Map<Long, Student> groupMembers) {
     this.groupMembers = groupMembers;
     }
     */
    public String getIndividualPayerUsername() {
        return individualPayerUsername;
    }

    public void setIndividualPayerUsername(String individualPayerUsername) {
        this.individualPayerUsername = individualPayerUsername;
    }

    public Long getIndividualPayerId() {
        return individualPayerId;
    }

    public void setIndividualPayerId(Long individualPayerId) {
        this.individualPayerId = individualPayerId;
    }

}
