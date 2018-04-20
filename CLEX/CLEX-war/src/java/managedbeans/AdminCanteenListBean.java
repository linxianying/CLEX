/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Item;
import entity.Shop;
import entity.User;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.OrderSessionBeanLocal;

/**
 *
 * @author lin
 */
@ManagedBean(name = "adminCanteenListBean")
@ViewScoped
public class AdminCanteenListBean {

    /**
     * Creates a new instance of AdminCanteenListBean
     */
    @EJB
    OrderSessionBeanLocal osbl;
    @EJB
    ClexSessionBeanLocal csbl;
    
    ArrayList<Shop> allShops;
    ArrayList<Shop> filteredShops;
    ArrayList<Item> allItems;
    ArrayList<Item> filteredItems;
    
    FacesContext context;
    HttpSession session;
    
    String username;
    User user;
    
    public AdminCanteenListBean() {
    }
    
    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        user = csbl.findUser(username);
        allShops = osbl.getAllShops(user);
        allItems = osbl.getAllItems(user);
        
    }

    public OrderSessionBeanLocal getOsbl() {
        return osbl;
    }

    public void setOsbl(OrderSessionBeanLocal osbl) {
        this.osbl = osbl;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ArrayList<Shop> getAllShops() {
        return allShops;
    }

    public void setAllShops(ArrayList<Shop> allShops) {
        this.allShops = allShops;
    }

    public ArrayList<Shop> getFilteredShops() {
        return filteredShops;
    }

    public void setFilteredShops(ArrayList<Shop> filteredShops) {
        this.filteredShops = filteredShops;
    }

    public ArrayList<Item> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<Item> allItems) {
        this.allItems = allItems;
    }

    public ArrayList<Item> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(ArrayList<Item> filteredItems) {
        this.filteredItems = filteredItems;
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
    
}
