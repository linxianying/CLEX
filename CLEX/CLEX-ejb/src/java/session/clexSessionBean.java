/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;

/**
 *
 * @author lin
 */
@Stateless
public class clexSessionBean implements clexSessionBeanLocal {

    @Override
    public void testMethod() {
        System.out.println("test");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
