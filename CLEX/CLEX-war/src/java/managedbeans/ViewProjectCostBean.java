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
import java.util.Collection;
import javaClass.ComparableTransaction;
import javaClass.StudentBalance;
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
@ManagedBean(name = "viewProjectCostBean")
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
    
    private ArrayList<StudentBalance> balances;
    
    //for adding transactions
    private String activity;
    private double totalCost;
    private double cost;
    //private Map<Long, Student> groupMembers;
    private Collection<Student> groupMembers;
    private Student individualPayer;
    private String individualPayerUsername;
    private Long individualPayerId;
    private ArrayList<StudentCost> all;
    //private ArrayList<StudentCost> payees;
    private String splitBy;
    private String paidBy;

    private boolean multiple;
    private boolean individual;
    
    public ViewProjectCostBean() {
    }
    
    @PostConstruct
    public void init() {
        System.out.println("ViewProjectCostBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        module = (Module) session.getAttribute("module");
        group = (ProjectGroup) session.getAttribute("projectGroup");
        student = (Student) session.getAttribute("user");
        groupMembers = group.getGroupMembers();
        sortedTransactions = pcsbl.getSortedTransactions(group);
        //System.out.println("sortedTransactions size: " + sortedTransactions.size());
        balances = pcsbl.getAllStudentBalance(group);
        paidBy = "Individual";
        splitBy = "Equally";
        this.setOriStudentCost();
        System.out.println("ViewProjectCostBean Finish initialization");
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

    public ArrayList<StudentBalance> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<StudentBalance> balances) {
        this.balances = balances;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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

    public ArrayList<StudentCost> getAll() {
        return all;
    }

    public void setAll(ArrayList<StudentCost> all) {
        this.all = all;
    }

    public String getSplitBy() {
        return splitBy;
    }

    public void setSplitBy(String splitBy) {
        this.splitBy = splitBy;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
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
    
    //in case of multiple payers and payees, set each student with the amount they pay
    public void setOriStudentCost() {
        all = new ArrayList<StudentCost>();
        for (Student s: this.getGroupMembers()) {
            StudentCost studentCost = new StudentCost(s, 0.0, 0.0);
            all.add(studentCost);
        }
    }
    
    
    public void addTransaction() {
        System.out.println("Strat to add transaction");
        System.out.println("activity=" + this.activity);
        System.out.println("totalcost=" + this.totalCost);
        System.out.println("individual payer ID = " + individualPayerId);

        for (StudentCost sc: all) {
            System.out.println(sc.toString());
            System.out.println(" ");
        }
        //if paid by individual
        if (paidBy.equals("Individual")) {
            // find the individualPayer in the payer arrayList and assign total cost to it
            System.out.println("It is paid by individual");
            for (StudentCost sc: all) {
                if (sc.getStudent().getId().equals(individualPayerId)) {
                    System.out.println(sc.getStudent().getName() + "find");
                    sc.setPay(totalCost);
                }
            }
        }
        // no need to set anything if paid by multiple people
        //if split equally 
        if (splitBy.equals("Equally")) {
            cost = totalCost/getGroupMembers().size();
            for (StudentCost sc: all) 
                sc.setCost(cost);
        }
        //if split by percentage 
        else if (splitBy.equals("Percentage")) {
            for (StudentCost sc: all) {
                cost = totalCost*sc.getCost()/100;
                sc.setCost(cost);
            }
        }
        System.out.println("activity=" + this.activity);
        System.out.println("totalcost=" + this.totalCost);
        System.out.println("all=" + this.all.toString());
        pcsbl.addTransaction(all, activity, totalCost, group);
        
        //initialize 
        for (StudentCost sc: all) {
            sc.setPay(0.0);
            sc.setCost(0.0);
        }
        
        sortedTransactions = pcsbl.getSortedTransactions(group);
        //System.out.println("After add transaction: sortedTransactions size: " + sortedTransactions.size());
        balances = pcsbl.getAllStudentBalance(group);
        this.setOriStudentCost();
        activity = null;
        totalCost = 0.0;
        paidBy = "Individual";
        splitBy = "Equally";
    }
    
    public void deleteTransaction(Long deletedTransactionId) {
        //System.out.println("into delete");
        Transaction t = pcsbl.findTransactionById(deletedTransactionId);
        pcsbl.deleteTransaction(deletedTransactionId, group, t);
        
        sortedTransactions = pcsbl.getSortedTransactions(group);
        //System.out.println("After add transaction: sortedTransactions size: " + sortedTransactions.size());
        balances = pcsbl.getAllStudentBalance(group);
        this.setOriStudentCost();
        activity = null;
        totalCost = 0.0;
        paidBy = "Individual";
        splitBy = "Equally";
    }
    
}
