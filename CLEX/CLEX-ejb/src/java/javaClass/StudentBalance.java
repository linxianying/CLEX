/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;

import entity.Student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author caoyu
 */
public class StudentBalance implements Comparable<StudentBalance>{
    private Student student;
    //how much this student owes or is owed, negative means the student owes, positive means the stdudent is owed
    private double totalAmount;
    //the map of payees or payers of this person and associated amount
    private HashMap<Student, Double> payees;

    public StudentBalance(Student student, double totalAmount) {
        this.student = student;
        this.totalAmount = totalAmount;
        payees = new HashMap<Student, Double>();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public HashMap<Student, Double> getPayees() {
        return payees;
    }

    public void setPayees(HashMap<Student, Double> payees) {
        this.payees = payees;
    }

    public ArrayList<HashMap.Entry<Student, Double>> getBalances() {
         Set<HashMap.Entry<Student, Double>> balances = payees.entrySet();
    return new ArrayList<HashMap.Entry<Student, Double>>(balances);
    }
    
    @Override
    public String toString() {
        return (student.getName() + " with asscociated cost " + totalAmount);
    }
    
    @Override
    public int compareTo(StudentBalance balance) {
        if (this.getTotalAmount() == 0.0 || balance.getTotalAmount() == 0.0){
            System.out.println("StudentBalance: compareTo: zero balance encountered" );
        }
        return Double.compare(totalAmount, balance.getTotalAmount());
    }
    
    
}
