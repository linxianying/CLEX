/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Item;
import entity.Order;
import entity.Shop;
import entity.User;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.OrderSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "userOrderBean")
@SessionScoped
public class UserOrderBean implements Serializable {
    
    public UserOrderBean() {
    }
    
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private OrderSessionBeanLocal osbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private User user;
    //for history orders
    private ArrayList<Order> orders;
    private double totalPrice;
    //for current orders
    private ArrayList<Order> currentOrders;
    private ArrayList<Order> filteredCurrentOrders;
    private Shop shop;
    //for new orders
    private ArrayList<Shop> shops;
    private List<Shop> filteredShops;
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        username = (String) session.getAttribute("username");
        user = osbl.findUser(username);
        shops = osbl.getAllShops(user);
        this.refresh();
    }
    
    public void refresh() {
        //default get orders sort by order date
        orders = osbl.getUserFinishOrder(username);
        totalPrice = osbl.getUserAllOrderPrice(username);
        currentOrders = osbl.getUserUnfinishOrder(username);
    }
    
    public void goNewOrderPage(Shop selectShop) throws IOException {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("shop", selectShop);
        System.out.println(this.user.getUserType());
        if (this.user.getUserType().equals("Student")) {
            context.getExternalContext().redirect("studentNewOrder.xhtml");
        }
        else if (this.user.getUserType().equals("Lecturer")) {
            context.getExternalContext().redirect("lecturerNewOrder.xhtml");
        }
        else if (this.user.getUserType().equals("Guest")) {
            context.getExternalContext().redirect("guestNewOrder.xhtml");
        }else if (this.user.getUserType().equals("Admin")) {
            context.getExternalContext().redirect("adminNewOrder.xhtml");
        }
    }

    public ArrayList<Item> getOrderItems(Order order) {
        ArrayList<Item> items = new ArrayList<Item>(order.getOrderItems().keySet());
        return items;
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

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public ArrayList<Order> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(ArrayList<Order> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public void setShops(ArrayList<Shop> shops) {
        this.shops = shops;
    }

    public List<Shop> getFilteredShops() {
        return filteredShops;
    }

    public void setFilteredShops(List<Shop> filteredShops) {
        this.filteredShops = filteredShops;
    }

    public ArrayList<Order> getFilteredCurrentOrders() {
        return filteredCurrentOrders;
    }

    public void setFilteredCurrentOrders(ArrayList<Order> filteredCurrentOrders) {
        this.filteredCurrentOrders = filteredCurrentOrders;
    }

    
}
