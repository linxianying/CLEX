/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;

import entity.Ledger;
import entity.ProjectGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author caoyu
 */
public class ComparableTransaction implements Comparable<ComparableTransaction>{
    private double cost;
    private Date date;
    private String activity;
    private ProjectGroup projectGroup;
    private Collection<Ledger> ledgers;

    public ComparableTransaction(double cost, Date date, String activity, ProjectGroup projectGroup, Collection<Ledger> ledgers) {
        this.cost = cost;
        this.date = date;
        this.activity = activity;
        this.projectGroup = projectGroup;
        this.ledgers = ledgers;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        this.projectGroup = projectGroup;
    }

    public Collection<Ledger> getLedgers() {
        return ledgers;
    }

    public void setLedgers(Collection<Ledger> ledgers) {
        this.ledgers = ledgers;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return ("Date: " + formatter.format(date) + ", Activity: " + activity);
    }
    
    @Override
    public int compareTo(ComparableTransaction t) {
        if (this.getDate() == null || t.getDate() == null){
            System.out.println("ComparableTransaction: compareTo: date attribute values null" );
            return 0;
        }
        return this.getDate().compareTo(t.getDate());
    }
    
}
