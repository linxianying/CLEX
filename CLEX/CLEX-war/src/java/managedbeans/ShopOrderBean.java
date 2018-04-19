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
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
@ManagedBean(name = "shopOrderBean")
@SessionScoped
public class ShopOrderBean implements Serializable {

    public ShopOrderBean() {
    }
    
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private OrderSessionBeanLocal osbl;
    
    FacesContext context;
    HttpSession session;
    
    private String username;
    private Shop shop;
    //for history orders
    private ArrayList<Order> orders;
    private ArrayList<Order> filteredOrders;
    private double totalPrice;
    //for current orders
    private ArrayList<Order> currentOrders;
    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        //for test purpose
        //username = (String) session.getAttribute("username");
        username = "pines1";
        shop = osbl.findShop(username);
        this.refresh();
        System.out.println("shopOrderBean finish init");
    }
    
    public void refresh() {
        orders = osbl.getShopFinishOrder(username);
        totalPrice = osbl.getAllOrderPrice(username);
        currentOrders = osbl.getShopUnfinishOrder(username);
        items = osbl.getAllItems(username);
    }

    public ArrayList<Item> getOrderItems(Order order) {
        ArrayList<Item> items = new ArrayList<Item>(order.getOrderItems().keySet());
        return items;
    }
    
    public void finishOrder(Long orderId) {
        System.out.println("try to finish " + orderId);
        osbl.endOrder(orderId);
        this.refresh();
    }
    
    public void changeShopName(String newName) {
        osbl.changeShopName(shop.getUsername(), newName);
    }
    public void editItemName(Item item, String newName){
        osbl.changeItemName(item, newName);
    }
    
    public void editItemPrice(Item item, double newPrice){
        osbl.changeItemPrice(item, newPrice);  
    }
    
    public void editItemAvailability(Item item, boolean avai){
        osbl.changeItemAvailability(item, avai);  
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
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

    public ArrayList<Order> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(ArrayList<Order> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Order> getFilteredOrders() {
        return filteredOrders;
    }

    public void setFilteredOrders(ArrayList<Order> filteredOrders) {
        this.filteredOrders = filteredOrders;
    }

    public ArrayList<Item> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(ArrayList<Item> filteredItems) {
        this.filteredItems = filteredItems;
    }
    
    
}
