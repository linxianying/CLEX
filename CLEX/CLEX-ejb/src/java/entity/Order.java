/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author caoyu
 */
@Entity(name="CanteenOrder")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private HashMap<Item, Integer> orderItems;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date orderTime;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date takeoutTime;
    
    //false means havent fish
    private boolean finish;
    
    private double price;
    
    @ManyToOne
    private User orderer;
    
    @ManyToOne
    private Shop shop;

    public void createOrder(HashMap<Item, Integer> orderItems, User orderer, Shop shop, Date takeoutTime, double price) {
        this.orderItems = orderItems;
        this.orderer = orderer;
        this.shop = shop;
        this.takeoutTime = takeoutTime;
        Date current = new Date();
        this.orderTime = current;
        this.finish = false;
        this.price = price;
    }

    public HashMap<Item, Integer> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(HashMap<Item, Integer> orderItems) {
        this.orderItems = orderItems;
    }

    public User getOrderer() {
        return orderer;
    }

    public void setOrderer(User orderer) {
        this.orderer = orderer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getTakeoutTime() {
        return takeoutTime;
    }

    public void setTakeoutTime(Date takeoutTime) {
        this.takeoutTime = takeoutTime;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Order[ id=" + id + " ]";
    }
    
}
