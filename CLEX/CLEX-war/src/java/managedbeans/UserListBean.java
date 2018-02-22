/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.User;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import session.UserAccessControlBeanLocal;

/**
 *
 * @author eeren
 */
@ManagedBean
@ViewScoped
public class UserListBean implements Serializable {

    @EJB
    private UserAccessControlBeanLocal uacbl;
    
    private List<User> users;
    private List<User> filteredUsers;
    private List<String> allUserTypes;

    
    public UserListBean() {
    }
    
    @PostConstruct
    public void init() {
        users = uacbl.getAllUsers();    
        allUserTypes = uacbl.createUserTypes();
    }
    
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<String> getAllUserTypes() {
        return allUserTypes;
    }

    public void setAllUserTypes(List<String> allUserTypes) {
        this.allUserTypes = allUserTypes;
    }
    
}