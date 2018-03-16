/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Announcement;
import entity.Lecturer;
import entity.Module;
import entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eeren
 */
@Stateless
public class AnnouncementSessionBean implements AnnouncementSessionBeanLocal {

    @PersistenceContext
    EntityManager em;

    private User userEntity;
    private Announcement anncEntity;
    private Collection<Announcement> anncs;

    //Check announcement entity for more details on how to categorise announcements
    @Override
    public void createLecturerAnnc(String username, String title, String message, String type) {
        userEntity = findUser(username);
        Announcement annc = new Announcement();

        annc.lecturerCreate(title, message, type);
        annc.setUser(userEntity);
        userEntity.getAnnouncements().add(annc);

        em.merge(userEntity);
        em.persist(annc);
        em.flush();
    }

    @Override
    public void createAdminAnnc(String username, String title, String message, String audience) {
        userEntity = findUser(username);
        Announcement annc = new Announcement();

        annc.adminCreate(title, message, audience);
        annc.setUser(userEntity);
        userEntity.getAnnouncements().add(annc);

        em.merge(userEntity);
        em.persist(annc);
        em.flush();
    }

    @Override
    public void editAnnc(String title, String message, Long id) {
        anncEntity = findAnnc(id);

        anncEntity.setTitle(title);
        anncEntity.setMessage(message);

        em.merge(anncEntity);
        em.flush();
    }

    @Override
    public boolean deleteAnnc(String username, Long id) {
        userEntity = findUser(username);
        anncEntity = findAnnc(id);
        anncs = userEntity.getAnnouncements();

        if (anncs.remove(anncEntity) == true) {
            em.merge(userEntity);
            em.remove(anncEntity);
            em.flush();
            return true;
        }

        return false;
    }

    @Override
    public void setViewAnncCount(String username, int viewCount) {
        userEntity = findUser(username);
        
        userEntity.setViewAnncCount(viewCount);
        
        em.merge(userEntity);
        em.flush();
    }
    
    @Override
    public List getModuleCodeByLecturer(String username) {
        List<String> moduleCodes = new ArrayList();
        Lecturer lecturerEntity = (Lecturer) findUser(username);
        List<Module> modules = (List) lecturerEntity.getModules();
        for (int i = 0; i < modules.size(); i++) {
            moduleCodes.add(modules.get(i).getCourse().getModuleCode());
        }
        return moduleCodes;
    }

    @Override
    public Collection<Announcement> getAllAnnc() {
        List<Announcement> announcements = new ArrayList<Announcement>();
        Query q = em.createQuery("Select a FROM Announcement a");
        for (Object o : q.getResultList()) {
            anncEntity = (Announcement) o;
            announcements.add(anncEntity);
        }
        return announcements;
    }

    @Override
    public Collection<Announcement> getAnncByUser(String username) {
        List<Announcement> announcements = new ArrayList<Announcement>();
        Query q = em.createQuery("Select a FROM Announcement a");
        for (Object o : q.getResultList()) {
            anncEntity = (Announcement) o;
            if (anncEntity.getUser().getUsername().equals(username)) {
                announcements.add(anncEntity);
            }
        }
        return announcements;
    }

    @Override
    public Collection<Announcement> getAnncByModule(String moduleCode) {
        List<Announcement> announcements = new ArrayList<Announcement>();
        Query q = em.createQuery("Select a FROM Announcement a");
        for (Object o : q.getResultList()) {
            anncEntity = (Announcement) o;
            if (anncEntity.getType().equals(moduleCode)) {
                announcements.add(anncEntity);
            }
        }
        return announcements;
    }

    @Override
    public Collection<Announcement> getAnncByAudience(String audience) {
        List<Announcement> announcements = new ArrayList<Announcement>();
        Query q = em.createQuery("Select a FROM Announcement a");
        for (Object o : q.getResultList()) {
            anncEntity = (Announcement) o;
            if (anncEntity.getAudience().equals(audience)) {
                announcements.add(anncEntity);
            }
        }
        return announcements;
    }

    @Override
    public User findUser(String username) {
        userEntity = null;
        try {
            Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
            q.setParameter("username", username);
            userEntity = (User) q.getSingleResult();
            System.out.println("User " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("User " + username + " does not exist.");
            userEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }

    @Override
    public Announcement findAnnc(Long id) {
        anncEntity = null;
        try {
            Query q = em.createQuery("SELECT a FROM Announcement a WHERE a.id=:id");
            q.setParameter("id", id);
            anncEntity = (Announcement) q.getSingleResult();
            System.out.println("Announcement found.");
        } catch (NoResultException e) {
            System.out.println("Announcement does not exist.");
            anncEntity = null;
        }
        return anncEntity;
    }
    
    @Override
    public List<Announcement> sortAnncByDate(List<Announcement> anncList){
        Collections.sort(anncList, new Comparator<Announcement>() {
            public int compare(Announcement annc1, Announcement annc2) {
                return annc2.getCreatedDate().compareTo(annc1.getCreatedDate());
            }
        });
        return anncList;
    }
}
