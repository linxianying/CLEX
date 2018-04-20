/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Item;
import entity.Shop;
import entity.User;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
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
        Date date = new Date();
        takeoutTime = new Date(date.getTime()+300000);
        this.refresh();
        System.out.println("orderBean finish render");
    }
    
    public void refresh() {
//        System.out.println("orderItems" + orderItems);
        if (orderItems!=null) {
            cartItems = new ArrayList<Item>(orderItems.keySet());
        }
        totalPrice = this.calculateTotalPrice();
//        System.out.println("total price:" + totalPrice);
    }
    
    public void addItem(Item item) {
//        System.out.println("Add item " + item.getName());
        orderItems.put(item, 1);
        this.refresh();
    }
    
    public void deletItem(Item item) {
        orderItems.remove(item);
        this.refresh();
    }
    
    public void updateItemQuantity(Item item, int newQuantity) {
//        System.out.println("change item quantity " + item.getName());
//        System.out.println("new quantity: " + newQuantity + ", " + orderItems.get(item).toString());
        orderItems.put(item, newQuantity);
        this.refresh();
    }
    
    public double calculateTotalPrice() {
        totalPrice = 0.0;
//        double add = 0.0;
        for (Item item: cartItems) {
//            System.out.println("new price: " + item.getPrice());
//            System.out.println("quantity: " + Integer.valueOf(orderItems.get(item)));
//            add = item.getPrice()*orderItems.get(item);
//            System.out.println("add: " + add);
            totalPrice += item.getPrice()*orderItems.get(item);
        }
        return totalPrice;
    }
    
    public void placeOrder() throws IOException {
        context = FacesContext.getCurrentInstance();
        FacesMessage fmsg = new FacesMessage();
        if (this.takeoutTime.compareTo(new Date()) < 0) {
           fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail to place order","You are choosing a date time before now");
            context.addMessage(null, fmsg); 
        }
        if (cartItems.isEmpty()) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail to place order","Nothing inside your cart currently!");
            context.addMessage(null, fmsg);
        }
        else {
            osbl.createOrder(user, orderItems, shop, takeoutTime, totalPrice);
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Congradulation!","Successfully place order!");
            context.addMessage(null, fmsg);
            orderItems = new HashMap<Item, Integer>();
            cartItems = null;
            totalPrice = 0.0;
            session = (HttpSession) context.getExternalContext().getSession(true);
            if (this.user.getUserType().equals("Student")) {
                context.getExternalContext().redirect("studentOrder.xhtml");
            }
            else if (this.user.getUserType().equals("Lecturer")) {
                context.getExternalContext().redirect("lecturerOrder.xhtml");
            }
            else if (this.user.getUserType().equals("Guest")) {
                context.getExternalContext().redirect("guestOrder.xhtml");
            }
        }
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
        return Double.valueOf(new DecimalFormat("0.00").format(totalPrice));
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
