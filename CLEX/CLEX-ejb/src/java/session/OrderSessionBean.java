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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author caoyu
 */
@Stateless
public class OrderSessionBean implements OrderSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    private User userEntity;
    private Order order;
    private Shop shop;
    private Item item;
    private ArrayList<Shop> shops;
    
    @Override
    public User findUser(String username){
        try{
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    
    @Override
    public ArrayList<Shop> getAllShops(User user) {
        String school = user.getSchool();
        shops = new ArrayList<Shop>();
        List<Shop> all = new ArrayList<Shop>();
        try{
            Query q = em.createQuery("SELECT s FROM Shop s WHERE s.school = :school");
            q.setParameter("school", school);
            all = (List<Shop>) q.getResultList();
            System.out.println("Shop for school " + school + " found.");
            for (Shop s: all)
                shops.add(s);
        }
        catch(NoResultException e){
            System.out.println("Shop for school " + school  + " does not exist.");
            shops = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return shops;
    }
    
    @Override
    public ArrayList<Item> getAllItems(User user) {
        String school = user.getSchool();
        ArrayList<Item> items = new ArrayList<Item>();
        List<Item> all = new ArrayList<Item>();
        try{
            Query q = em.createQuery("SELECT i FROM Item i WHERE i.shop.school = :school");
            q.setParameter("school", school);
            all = (List<Item>) q.getResultList();
            for (Item s: all)
                items.add(s);
        }
        catch(NoResultException e){
            System.out.println("No item for school " + school  + " found");
            items = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return items;
    }
    
    @Override
    public void createShop(String canteen, String name, String username, String password, String school, String email, String telephone, boolean approve){
        shop = new Shop();
        String salt = genSalt();
        shop.createShop(canteen, name, hashPassword(password, salt), username, school, salt,  email, telephone, approve );
        em.persist(shop);
    }
    
    @Override
    public Shop findShop(String username){
        try{
            Query q = em.createQuery("SELECT s FROM Shop s WHERE s.username = :username");
            q.setParameter("username", username);
            shop = (Shop) q.getSingleResult();
            System.out.println("Shop " + username + " found.");
        }
        catch(NoResultException e){
            System.out.println("Shop " + username + " does not exist.");
            shop = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return shop;
    }
    
    //sort order 
    @Override
    public void changeShopName(String username, String newName) {
        shop = this.findShop(username);
        shop.setName(newName);
        em.merge(shop);
    }
    
    //get all orders sort by order date
    @Override
    public ArrayList<Order> getShopOrders(String username) {
        shop = this.findShop(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.shop.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("Shop " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getOrderTime().compareTo(o2.getOrderTime());}
            });
        return orders;
    }
    
    //get all finished orders sort by order date
    @Override
    public ArrayList<Order> getShopFinishOrder(String username) {
        shop = this.findShop(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.shop.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                if (o.isFinish())
                    orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("Shop " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getOrderTime().compareTo(o2.getOrderTime());}
            });
        return orders;
    }
    
    //get all unfinished orders sort by order date
    @Override
    public ArrayList<Order> getShopUnfinishOrder(String username) {
        shop = this.findShop(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.shop.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                if (!o.isFinish())
                    orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("Shop " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getOrderTime().compareTo(o2.getOrderTime());}
            });
        return orders;
    }
    
    //get all unfinished orders sort by takeout date
    @Override
    public ArrayList<Order> getShoptodoOrder(String username) {
        shop = this.findShop(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.shop.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                if (!o.isFinish())
                    orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("Shop " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getTakeoutTime().compareTo(o2.getTakeoutTime());}
            });
        return orders;
    }
    
    //calculated all orders' total price
    @Override
    public double getAllOrderPrice(String username) {
        shop = this.findShop(username);
        double totalPrice = 0.0;
        for (Order o: this.getShopOrders(username))
            totalPrice += o.getPrice();
        return totalPrice;
    }
    
    @Override
    public void createItem(Shop shop, String name, double price) {
        item = new Item();
        item.createItem(shop, name, price);
        em.persist(item);
        shop.getItems().add(item);
        em.merge(shop);
        em.flush();
    }
    
    @Override
    public Item findItem(Long id) {
        try{
            Query q = em.createQuery("SELECT i FROM Item i WHERE i.id = :id");
            q.setParameter("id", id);
            item = (Item) q.getSingleResult();
            System.out.println(item.getName() + "found.");
        }
        catch(NoResultException e){
            System.out.println("item  does not exist.");
            item = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return item;
    }
    
    // just use for indexBean to test
    @Override
    public Item findItemByName(Shop shop, String itemName) {
        try{
            Query q = em.createQuery("SELECT i FROM Item i WHERE i.shop.id =:id AND i.name =:name");
            q.setParameter("id", shop.getId());
            q.setParameter("name", itemName);
            item = (Item) q.getSingleResult();
            System.out.println(item.getName() + "found.");
        }
        catch(NoResultException e){
            System.out.println("item  does not exist.");
            item = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return item;
    }
    
    //get all items in a shop
    @Override
    public ArrayList<Item> getAllItems(String username) {
        shop = this.findShop(username);
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item i: shop.getItems())
            items.add(i);
        return items;
    }
    
    //get all avaliable items in a shop
    @Override
    public ArrayList<Item> getAllAvaItems(String username) {
        shop = this.findShop(username);
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item i: shop.getItems())
            if (i.isAvailable())
                items.add(i);
        return items;
    }
    
    //get all unavaliable items in a shop
    @Override
    public ArrayList<Item> getAllUnavaItems(String username) {
        shop = this.findShop(username);
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item i: shop.getItems())
            if (!i.isAvailable())
                items.add(i);
        return items;
    }
    
    //set Item avaliable
    @Override
    public void setItemAva(Long id) {
        item = this.findItem(id);
        item.setAvailable(true);
        em.merge(item);
        em.flush();
    }
    
    //change Item unavaliable
    @Override
    public void setItemUnava(Long id) {
        item = this.findItem(id);
        item.setAvailable(false);
        em.merge(item);
        em.flush();
    }
    
    @Override
    public void createOrder(User user, HashMap<Item, Integer> orderItems, Shop shop, Date takeoutTime, double price) {
        order = new Order();
        order.createOrder(orderItems, user, shop, takeoutTime, price);
        em.persist(order);
        user.getOrders().add(order);
        shop.getOrders().add(order);
        em.merge(user);
        em.merge(shop);
        em.flush();
    }
    
    @Override
    public void endOrder(Long orderId) {
        order = this.findOrder(orderId);
        order.setFinish(true);
        em.merge(order);
        em.flush();
    }
    
    @Override
    public Order findOrder(Long id) {
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.id = :id");
            q.setParameter("id", id);
            order = (Order) q.getSingleResult();
            System.out.println("Order found.");
        }
        catch(NoResultException e){
            System.out.println("Order does not exist.");
            order = null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return order;
    }
    
    //get students' all orders sort by order date
    @Override
    public ArrayList<Order> getUserOrders(String username) {
        userEntity = this.findUser(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.orderer.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("User " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getOrderTime().compareTo(o2.getOrderTime());}
            });
        return orders;
    }
    
        //get students' all orders sort by takeout date
    @Override
    public ArrayList<Order> getUserOrdersByTakeout(String username) {
        userEntity = this.findUser(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.orderer.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("User " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getTakeoutTime().compareTo(o2.getTakeoutTime());}
            });
        return orders;
    }
    
    //get users' all finished orders sort by order date
    @Override
    public ArrayList<Order> getUserFinishOrder(String username) {
        userEntity = this.findUser(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.orderer.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                if (o.isFinish())
                    orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("User " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getOrderTime().compareTo(o2.getOrderTime());}
            });
        return orders;
    }
    
    //get all users' unfinished orders sort by order date
    @Override
    public ArrayList<Order> getUserUnfinishOrder(String username) {
        userEntity = this.findUser(username);
        List<Order> all = new ArrayList<Order>();
        ArrayList<Order> orders = new ArrayList<Order>();
        
        try{
            Query q = em.createQuery("SELECT o FROM CanteenOrder o WHERE o.orderer.username = :username");
            q.setParameter("username", username);
            all = q.getResultList();
            for (Order o: all)
                if (!o.isFinish())
                    orders.add(o);
        }
        catch(NoResultException e){
            System.out.println("User " + username + " has no order");
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        Collections.sort(orders, new Comparator<Order>() {
                @Override
                public int compare (Order o1, Order o2) {
                        return o1.getOrderTime().compareTo(o2.getOrderTime());}
            });
        return orders;
    }
    

    
    //calculated all orders' total price
    @Override
    public double getUserAllOrderPrice(String username) {
        userEntity = this.findUser(username);
        double totalPrice = 0.0;
        for (Order o: this.getUserOrders(username))
            totalPrice += o.getPrice();
        return totalPrice;
    }
    
    @Override
    public boolean changeItemName(Item item, String newName){
        item.setName(newName);
        em.merge(item);
        em.flush();
        return true;
    }
    
    @Override
    public boolean changeItemNameById(Long id, String newName){
        Item itemEntity = em.find(Item.class, id);
        //lazy fectching
        itemEntity.getShop().getId();
        return true;
    }
    
    @Override
    public boolean changeItemPrice(Item item, double newPrice){
        item.setPrice(newPrice);
        em.merge(item);
        em.flush();
        return true;
    }
    
    @Override
    public boolean changeItemAvailability(Item item, boolean avail){
        item.setAvailable(avail);
        em.merge(item);
        em.flush();
        return true;
    }
    
    @Override
    public boolean checkShopPassword(String username, String password) {
            Query q = em.createQuery("SELECT s FROM Shop s WHERE s.username = :username");
            q.setParameter("username", username);
            shop = (Shop) q.getSingleResult();
            if(shop.getPassword().equals(hashPassword(password, shop.getSalt()))){
                System.out.println("Password of shop " + username + " is correct.");
                return true;
            }
            else{
                 System.out.println("Password of shop " + username + " is wrong.");
            }
        return false;
    }
    
    private String genSalt() {
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();

        return salt;
    }
    
    private String hashPassword(String password, String salt){
        String genPass = null;
 
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pass = (password+salt).getBytes();
            md.update(pass);
            byte[] temp = md.digest(pass);
            
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < temp.length; i++){
                sb.append(Integer.toString((temp[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPass = sb.toString();
            
        }
        catch(Exception e){
            System.out.println("Failed to hash password.");
        }
        return genPass;
    }
}
