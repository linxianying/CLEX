/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author caoyu
 */
@Entity
public class Shop implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 64)
    private String canteen;
    
    @Column(length = 64)
    private String name;
    
    @Column(length = 512, nullable = false)
    private String password;
    
    @Column(length = 64, nullable = false)
    private String username;
    
    @Column(length = 64, nullable = false)
    private String email;
    
    @Column(length = 64, nullable = false)
    private String telephone;
    
    @Column(nullable = false)
    private boolean approve;
    
    @Column(length = 64, nullable = false)
    private String school;
    
    private String salt;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "shop")
    private List<Item> items = new ArrayList<Item>();
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "shop")
    private List<Order> orders = new ArrayList<Order>();   

    //Nus canteen list: The Terrace, The Deck, Techno Edge, Frontier, Pines Food Court, Foodclique, Fine Food, Flavours
    public void createShop(String canteen, String name, String password, 
            String username, String school, String salt, String email, String telephone, boolean approve) {
        this.canteen = canteen;
        this.name = name;
        this.password = password;
        this.username = username;
        this.school = school;
        this.salt = salt;
        this.telephone = telephone;
        this.email = email;
        this.approve = approve;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCanteen() {
        return canteen;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shop)) {
            return false;
        }
        Shop other = (Shop) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Shop[ id=" + id + " ]";
    }
    
    
}
