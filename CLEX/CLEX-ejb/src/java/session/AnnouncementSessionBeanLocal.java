/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Announcement;
import entity.User;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author eeren
 */
@Local
public interface AnnouncementSessionBeanLocal {
    
    public void createLecturerAnnc(String username, String title, String message, String type);    
    public void createAdminAnnc(String username, String title, String message, String audience);

    public void editAnnc(String title, String message, Long id);
    
    public boolean deleteAnnc(String username, Long id);
    
    public Collection<Announcement> getAllAnnc();
    public Collection<Announcement> getAnncByUser(String username);
    public Collection<Announcement> getAnncByModule(String moduleCode);
    public Collection<Announcement> getAnncByAudience(String audience);
}
