/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Item;
import entity.Shop;
import entity.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.OrderSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "orderBean")
@SessionScoped
public class OrderBean implements Serializable {
    
    public OrderBean() {
    }
    
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private OrderSessionBeanLocal osbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private User user;
    //for start a new order
    private Shop shop;
    private ArrayList<Item> avaliableItems;
    private HashMap<Item, Integer> orderItems;
    private Date takeoutTime;
    private double totalPrice;
    private ArrayList<Item> cartItems;
    private int changeQuantity;
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        username = (String) session.getAttribute("username");
        user = osbl.findUser(username);
        shop = (Shop) session.getAttribute("shop");
        avaliableItems = osbl.getAllAvaItems(shop.getUsername());
        orderItems = new HashMap<Item, Integer>();
        totalPrice = 0.0;
        this.refresh();
    }
    
    public void refresh() {
        if (orderItems!=null) 
            cartItems = new ArrayList<Item>(orderItems.keySet());
        totalPrice = this.calculateTotalPrice();
        
    }

    public void addItem(Item item) {
        orderItems.put(item, 1);
        this.refresh();
    }
    
    public void deletItem(Item item) {
        orderItems.remove(item);
        this.refresh();
    }
    
    public void updateItemQuantity(Item item) {
        System.out.println("change to quantity " + changeQuantity);
        orderItems.put(item, changeQuantity);
        this.refresh();
    }
    
    public double calculateTotalPrice() {
        for (Item item: cartItems)
            totalPrice += item.getPrice()*orderItems.get(item);
        return totalPrice;
    }
    
    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public OrderSessionBeanLocal getOsbl() {
        return osbl;
    }

    public void setOsbl(OrderSessionBeanLocal osbl) {
        this.osbl = osbl;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public ArrayList<Item> getAvaliableItems() {
        return avaliableItems;
    }

    public void setAvaliableItems(ArrayList<Item> avaliableItems) {
        this.avaliableItems = avaliableItems;
    }

    public HashMap<Item, Integer> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(HashMap<Item, Integer> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getTakeoutTime() {
        return takeoutTime;
    }

    public void setTakeoutTime(Date takeoutTime) {
        this.takeoutTime = takeoutTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Item> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<Item> cartItems) {
        this.cartItems = cartItems;
    }

    public int getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(int changeQuantity) {
        this.changeQuantity = changeQuantity;
    }
    
}
