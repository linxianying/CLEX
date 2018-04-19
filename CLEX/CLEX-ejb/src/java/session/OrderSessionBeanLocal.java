/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Item;
import entity.Order;
import entity.Shop;
import entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.Local;

/**
 *
 * @author caoyu
 */
@Local
public interface OrderSessionBeanLocal {

    public User findUser(String username);

    public void createItem(Shop shop, String name, double price);

    public void createShop(String canteen, String name, String username, String password, String school);

    public Shop findShop(String username);

    public Item findItem(Long id);

    public void changeShopName(String username, String newName);

    public ArrayList<Order> getShoptodoOrder(String username);

    public ArrayList<Order> getShopUnfinishOrder(String username);

    public ArrayList<Order> getShopFinishOrder(String username);

    public ArrayList<Order> getShopOrders(String username);

    public void setItemUnava(Long id);

    public void setItemAva(Long id);

    public ArrayList<Item> getAllUnavaItems(String username);

    public ArrayList<Item> getAllAvaItems(String username);

    public ArrayList<Item> getAllItems(String username);

    public void createOrder(User user, HashMap<Item, Integer> orderItems, Shop shop, Date takeoutTime, double price);

    public double getAllOrderPrice(String username);

    public Order findOrder(Long id);

    public ArrayList<Order> getUserOrders(String username);

    public ArrayList<Order> getUserFinishOrder(String username);

    public ArrayList<Order> getUserUnfinishOrder(String username);

    public ArrayList<Order> getUserOrdersByTakeout(String username);

    public double getUserAllOrderPrice(String username);

    public Item findItemByName(Shop shop, String itemName);

    public void endOrder(Long orderId);

    public ArrayList<Shop> getAllShops(User user);
    
}
