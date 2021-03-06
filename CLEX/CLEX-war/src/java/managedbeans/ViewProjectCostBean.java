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
import java.util.Date;
import javaClass.StudentBalance;
import javaClass.StudentCost;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import session.ClexSessionBeanLocal;
import session.ProjectCostSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "viewProjectCostBean")
@ViewScoped
public class ViewProjectCostBean {

    @EJB
    ClexSessionBeanLocal csbl;
    @EJB
    ProjectCostSessionBeanLocal pcsbl;

    FacesContext context;
    HttpSession session;

    private Module module;
    private ProjectGroup group;
    private Student student;
    private ArrayList<Transaction> transactions;
    private ArrayList<Transaction> sortedTransactions;
    //private List<Transaction> transactions;
    //private ArrayList<ComparableTransaction> sortedTransactions;
    private ArrayList<StudentBalance> balances;

    //for adding transactions
    private String activity;
    private double totalCost;
    private double cost;
    private Date date;
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

    //for displaying,retrieving
    private Transaction selectedTransaction;
    private double totalCostLabel;


    public ViewProjectCostBean() {
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        System.out.println("ViewProjectCostBean start initialization");
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        module = (Module) session.getAttribute("module");
        group = (ProjectGroup) session.getAttribute("projectGroup");
        student = (Student) session.getAttribute("user");
        groupMembers = group.getGroupMembers();
        sortedTransactions = pcsbl.getALLSortedTransactions(group);
        //System.out.println("sortedTransactions size: " + sortedTransactions.size());
        balances = pcsbl.getAllStudentBalance(group);
        paidBy = "Individual";
        splitBy = "Equally";
        this.setOriStudentCost();
        System.out.println("ViewProjectCostBean Finish initialization");
        selectedTransaction = null;
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Activity selected", (selectedTransaction.getActivity()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        System.out.println(selectedTransaction.getId());
    }

    public void totalCostAdd() {
        totalCost = 0.0;
        for (StudentCost sc : all) {
            totalCost += sc.getPay();
        }
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
//        System.out.println("Strat to add transaction");
//        System.out.println("activity=" + this.activity);
//        System.out.println("totalcost=" + this.totalCost);
//        System.out.println("individual payer ID = " + individualPayerId);
//
//        for (StudentCost sc: all) {
//            System.out.println(sc.toString());
//            System.out.println(" ");
//        }
        if (date == null) {
            date = new Date();
        }
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        if (addTransactionValidator()) {
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
            pcsbl.addTransaction(all, activity, totalCost, group, date);

            sortedTransactions = pcsbl.getALLSortedTransactions(group);
            //System.out.println("After add transaction: sortedTransactions size: " + sortedTransactions.size());
            balances = pcsbl.getAllStudentBalance(group);
            this.setOriStudentCost();
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", activity + " added.");
            context.addMessage(null, fmsg);
            activity = "";
            totalCost = 0.0;
            paidBy = "Individual";
            splitBy = "Equally";
            date = null;
        } else {
            System.out.println("Msg posted");
        }

    }

    public void deleteTransaction() {
        System.out.println("into delete");
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        pcsbl.deleteTransaction(selectedTransaction.getId(), group, selectedTransaction);
        fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", selectedTransaction.getActivity() + " deleted.");
        context.addMessage(null, fmsg);
        sortedTransactions = pcsbl.getALLSortedTransactions(group);
        //System.out.println("After add transaction: sortedTransactions size: " + sortedTransactions.size());
        balances = pcsbl.getAllStudentBalance(group);
        this.setOriStudentCost();
        activity = "";
        totalCost = 0.0;
        paidBy = "Individual";
        splitBy = "Equally";
        selectedTransaction = null;
    }

    public boolean addTransactionValidator() {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();

        double totalPaidBy = 0;
        double totalCostBy = 0;
        double difference = 0;
        double totalPercentage = 0;

        for (StudentCost sc : all) {
            totalPaidBy += sc.getPay();
            totalCostBy += sc.getCost();
        }

        if (paidBy.equals("Multiple People")) {

            if (totalPaidBy > totalCost) {
                difference = totalPaidBy - totalCost;
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong input", "The total amount paid is $"
                        + difference + " more than the total cost!");
                context.addMessage(null, fmsg);
                return false;
            } else if (totalPaidBy < totalCost) {
                difference = totalCost - totalPaidBy;
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong input", "The total amount paid is $"
                        + difference + " less than the total cost!");
                context.addMessage(null, fmsg);
                return false;
            }
        }

        if (splitBy.equals("Percentage") && (totalCostBy != 100.00)) {
            if (totalCostBy > 100) {
                difference = totalCostBy - 100;
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong input", "The total amount percentage cost is "
                        + difference + "% more than 100%!");
                context.addMessage(null, fmsg);
                return false;
            } else if (totalCostBy < 100) {
                difference = 100 - totalCostBy;
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong input", "The total amount percentage cost is "
                        + difference + " % less than 100%!");
                context.addMessage(null, fmsg);
                return false;
            }
        } else if (splitBy.equals("Exact Amount")) {
            if (totalCostBy > totalCost) {
                difference = totalCostBy - totalCost;
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong input", "The total amount cost by members is "
                        + difference + " dollars more than total cost!");
                context.addMessage(null, fmsg);
                return false;
            } else if (totalCostBy < totalCost) {
                difference = totalCost - totalCostBy;
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong input", "The total amount cost by members is "
                        + difference + " dollars less than total cost!");
                context.addMessage(null, fmsg);
                return false;
            }
        }

        return true;
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

    public ArrayList<Transaction> getSortedTransactions() {
        return sortedTransactions;
    }

    public void setSortedTransactions(ArrayList<Transaction> sortedTransactions) {
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

    public double getTotalCostLabel() {
        return totalCostLabel;
    }

    public void setTotalCostLabel(double totalCostLabel) {
        this.totalCostLabel = totalCostLabel;
    }

    public Transaction getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedTransaction(Transaction selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
