/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Shop;
import entity.User;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import session.ClexSessionBeanLocal;
import session.OrderSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@SessionScoped
@ManagedBean(name = "shopLoginBean")
public class ShopLoginBean {
    
    public ShopLoginBean() {
    }
    
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private OrderSessionBeanLocal osbl;
    
    private Shop shop;
    private String username;
    private String password;
    
    public void doLogin() throws IOException {
        FacesMessage fmsg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);

        shop = osbl.findShop(username);
        
        if (shop == null) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Shop '" + username + " does not exists.", "Please double check!");
            context.addMessage(null, fmsg);
            username = "";
            password = "";
        }
        else if (osbl.checkShopPassword(username, password)){
            session.setAttribute("shop", shop);
            session.setAttribute("username", username);
            context.getExternalContext().redirect("shopMain.xhtml");
        }
        else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password.", "Please double check your password? ");
            context.addMessage(null, fmsg);
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
