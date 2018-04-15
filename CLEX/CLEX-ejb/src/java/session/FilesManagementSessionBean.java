/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Files;
import entity.Module;
import entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author lin
 */
@Stateless
public class FilesManagementSessionBean implements FilesManagementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    private User userEntity;
    private Files fileEntity;
    private Module moduleEntity;
    
    
    @Override
    public Files createFile(String username, Long id) {
        moduleEntity = findModule(id);

        if (moduleEntity == null) {
            System.out.println("Module cannot be found.");
            return null;
        }

        fileEntity = new Files();

        System.out.println("Id: " + fileEntity.getId());
        fileEntity.createFile(username);
        fileEntity.setModule(moduleEntity);
        moduleEntity.getFileLists().add(fileEntity);

        em.merge(userEntity);
        em.persist(fileEntity);
        em.flush();

        System.out.println("File " + fileEntity.getId() + " created.");
        return fileEntity;
    }
    
    public Module findModule(Long id) {
        moduleEntity = null;
        try {
            Query q = em.createQuery("SELECT c FROM Module c WHERE c.id=:id");
            q.setParameter("id", id);
            moduleEntity = (Module) q.getSingleResult();
            System.out.println("Module " + id + " found.");
        } catch (NoResultException e) {
            System.out.println("Module " + id + " does not exist.");
            moduleEntity = null;
        }
        return moduleEntity;
    }
    
    @Override
    public Files getFile(Long id) {
        fileEntity = em.find(Files.class, id);
        fileEntity.getModule().getId();
        return fileEntity;
    }
}
