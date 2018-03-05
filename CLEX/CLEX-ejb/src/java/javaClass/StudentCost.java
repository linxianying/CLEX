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
    private double cost;

    public StudentCost(Student student, double cost) {
        this.student = student;
        this.cost = cost;
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
    
}
