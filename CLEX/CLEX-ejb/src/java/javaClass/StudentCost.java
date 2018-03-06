/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;

import entity.Student;

/**
 *
 * @author caoyu
 */
public class StudentCost {
    private Student student;
    //how much this student spend
    private double cost;
    //how much this student pay
    private double pay;

    public StudentCost(Student student, double cost, double pay) {
        this.student = student;
        this.cost = cost;
        this.pay = pay;
    }
    
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }
    
    @Override
    public String toString(){
        return (student.getName() + ", pay " + pay + ", cost " + cost);
    }
}
