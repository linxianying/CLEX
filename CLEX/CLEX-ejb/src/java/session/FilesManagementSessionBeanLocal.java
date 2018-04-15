/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Files;
import javax.ejb.Local;

/**
 *
 * @author lin
 */
@Local
public interface FilesManagementSessionBeanLocal {
    public Files createFile(String username, Long id) ;
    public Files getFile(Long id);
}
