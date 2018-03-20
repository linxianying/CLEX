/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Admin;
import entity.Guest;
import entity.Lecturer;
import entity.Student;
import entity.User;
import java.security.MessageDigest;
import java.util.ArrayList;
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
public class ProfileSessionBean implements ProfileSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private User userEntity;
    private Student studentEntity;
    private Lecturer lecturerEntity;
    private Guest guestEntity;
    private Admin adminEntity;

    @Override
    public void editStudent(String username, String name, String email, Long contactNum, String faculty, String major, String matricYear, String matricSem) {
        studentEntity = findStudent(username);

        studentEntity.setName(name);
        studentEntity.setEmail(email);
        studentEntity.setContactNum(contactNum);
        studentEntity.setFaculty(faculty);
        studentEntity.setMajor(major);
        studentEntity.setMatricYear(matricYear);
        studentEntity.setMatricSem(matricSem);

        em.merge(studentEntity);
        em.flush();
    }

    @Override
    public void editLecturer(String username, String name, String email, String school, Long contactNum, String faculty) {
        lecturerEntity = findLecturer(username);

        lecturerEntity.setName(name);
        lecturerEntity.setEmail(email);
        lecturerEntity.setContactNum(contactNum);
        lecturerEntity.setFaculty(faculty);

        em.merge(lecturerEntity);
        em.flush();
    }

    @Override
    public void editGuest(String username, String name, String email, String school, Long contactNum) {
        guestEntity = findGuest(username);

        guestEntity.setName(name);
        guestEntity.setEmail(email);
        guestEntity.setContactNum(contactNum);

        em.merge(guestEntity);
        em.flush();
    }

    @Override
    public void editAdmin(String username, String name, String email, String school, Long contactNum) {
        adminEntity = findAdmin(username);

        adminEntity.setName(name);
        adminEntity.setEmail(email);
        adminEntity.setContactNum(contactNum);

        em.merge(adminEntity);
        em.flush();
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
        q.setParameter("username", username);
        userEntity = (User) q.getSingleResult();
        userEntity.setPassword(hashPassword(newPassword, userEntity.getSalt()));
        System.out.println("Password of " + username + " has been changed.");
        em.merge(userEntity);
        em.flush();
    }

    @Override
    public boolean checkPassword(String username, String password) {
        Query q = em.createQuery("SELECT u FROM BasicUser u WHERE u.username = :username");
        q.setParameter("username", username);
        userEntity = (User) q.getSingleResult();
        if (userEntity.getPassword().equals(hashPassword(password, userEntity.getSalt()))) {
            System.out.println("Password of " + username + " is correct.");
            return true;
        } else {
            System.out.println("Password of " + username + " is wrong.");
        }
        return false;
    }

    private String hashPassword(String password, String salt) {
        String genPass = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pass = (password + salt).getBytes();
            md.update(pass);
            byte[] temp = md.digest(pass);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < temp.length; i++) {
                sb.append(Integer.toString((temp[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPass = sb.toString();

        } catch (Exception e) {
            System.out.println("Failed to hash password.");
        }
        return genPass;
    }

    public Student findStudent(String username) {
        studentEntity = null;
        try {
            Query q = em.createQuery("SELECT u FROM Student u WHERE u.username=:username");
            q.setParameter("username", username);
            studentEntity = (Student) q.getSingleResult();
            System.out.println("Student " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("Student " + username + " does not exist.");
            studentEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentEntity;
    }

    public Lecturer findLecturer(String username) {
        lecturerEntity = null;
        try {
            Query q = em.createQuery("SELECT l FROM Lecturer l WHERE l.username=:username");
            q.setParameter("username", username);
            lecturerEntity = (Lecturer) q.getSingleResult();
            System.out.println("Lecturer " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("Lecturer " + username + " does not exist.");
            lecturerEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lecturerEntity;
    }

    public Guest findGuest(String username) {
        guestEntity = null;
        try {
            Query q = em.createQuery("SELECT g FROM Guest g WHERE g.username=:username");
            q.setParameter("username", username);
            guestEntity = (Guest) q.getSingleResult();
            System.out.println("Guest " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("Guest " + username + " does not exist.");
            guestEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return guestEntity;
    }

    public Admin findAdmin(String username) {
        adminEntity = null;
        try {
            Query q = em.createQuery("SELECT a FROM Admin a WHERE a.username=:username");
            q.setParameter("username", username);
            adminEntity = (Admin) q.getSingleResult();
            System.out.println("Admin " + username + " found.");
        } catch (NoResultException e) {
            System.out.println("Admin " + username + " does not exist.");
            guestEntity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminEntity;
    }

    @Override
    public List<String> getSchoolFaculty(String school) {
        List<String> facultylist = new ArrayList<String>();
        if (school.equals("NUS")) {
            facultylist.add(0, "Faculty of Arts and Social Sciences");
            facultylist.add(1, "School of Computing");
            facultylist.add(2, "Business School");
            facultylist.add(3, "Faculty of Dentistry");
            facultylist.add(4, "School of Design and Environment");
            facultylist.add(5, "Duke–NUS Medical School");
            facultylist.add(6, "Faculty of Engineering");
            facultylist.add(7, "School for Integrative Sciences and Engineering ");
            facultylist.add(8, "Faculty of Law");
            facultylist.add(9, "Yong Loo Lin School of Medicine");
            facultylist.add(10, "Yong Siew Toh Conservatory of Music");
            facultylist.add(11, "Saw Swee Hock School of Public Health");
            facultylist.add(12, "Lee Kuan Yew School of Public Policy ");
            facultylist.add(13, "Faculty of Science");
            facultylist.add(14, "Yale-NUS College");
        } else if (school.equals("NTU")) {
            facultylist.add(0, "School of Chemical and Biomedical Engineering");
            facultylist.add(1, "School of Civil and Environmental Engineering");
            facultylist.add(2, "School of Computer Science and Engineering");
            facultylist.add(3, "School of Electrical and Electronic Engineering");
            facultylist.add(4, "School of Materials Science and Engineering");
            facultylist.add(5, "School of Mechanical and Aerospace Engineering");
            facultylist.add(6, "School of Art, Design and Media");
            facultylist.add(7, "School of Humanities");
            facultylist.add(8, "School of Social Sciences");
            facultylist.add(9, "Wee Kim Wee School of Communication and Information");
            facultylist.add(10, "Nanyang Business School");
            facultylist.add(11, "School of Biological Sciences ");
            facultylist.add(12, "School of Physical and Mathematical Sciences ");
            facultylist.add(13, "The Asian School of the Environment");
            facultylist.add(14, "Lee Kong Chian School of Medicine");
            facultylist.add(15, "College of Professional and Continuing Education");
            facultylist.add(16, "Interdisciplinary Graduate School");
            facultylist.add(17, "National Institute of Education");
            facultylist.add(18, "S. Rajaratnam School of International Studies");
        } else if (school.equals("SUSS")) {
            facultylist.add(0, "School of Arts and Social Sciences");
            facultylist.add(1, "School of Business");
            facultylist.add(2, "S R Nathan School of Human Development");
            facultylist.add(3, "School of Law");
            facultylist.add(4, "School of Science and Technology");
        } else if (school.equals("SMU")) {
            facultylist.add(0, "School of Accountancy");
            facultylist.add(1, "Lee Kong Chian School of Business");
            facultylist.add(2, "School of Economics");
            facultylist.add(3, "School of Information Systems");
            facultylist.add(4, "School of Law");
            facultylist.add(5, "School of Social Sciences");
            facultylist.add(6, "Executive Development");
            facultylist.add(7, "SMU Academy | Human Capital, Management & Leadership");
        } else if (school.equals("SUTD")) {
            facultylist.add(0, "SUTD");
        } else if (school.equals("SIT")) {
            facultylist.add(0, "School of Arts and Social Sciences");
            facultylist.add(1, "School of Computing");
            facultylist.add(2, "School of Business");
            facultylist.add(3, "School of Engineering");
            facultylist.add(4, "School of Design and Environment");
            facultylist.add(5, "School of Science");
        } else if (school.equals("NP")) {
            facultylist.add(0, "School of Business & Accountancy");
            facultylist.add(1, "School of Design & Environment");
            facultylist.add(2, "School of Engineering");
            facultylist.add(3, "School of Film & Media Studies");
            facultylist.add(4, "School of Health Sciences");
            facultylist.add(5, "School of Humanities & Social Sciences");
            facultylist.add(6, "School of InfoComm Technology");
            facultylist.add(7, "School of Life Sciences & Chemical Technology");
            facultylist.add(8, "School of Interdisciplinary Studies");
        } else if (school.equals("SP")) {
            facultylist.add(0, "School of Architecture and the Built Environment");
            facultylist.add(1, "SP Business School");
            facultylist.add(2, "School of Chemical and Life Sciences");
            facultylist.add(3, "School of Communication, Arts and Social Sciences");
            facultylist.add(4, "SP Design School");
            facultylist.add(5, "School of Digital Media and Infocomm Technology");
            facultylist.add(6, "School of Electrical and Electronic Engineering");
            facultylist.add(7, "School of Mechanical and Aeronautical Engineering");
            facultylist.add(8, "School of Mathematics and Science");
            facultylist.add(9, "Singapore Maritime Academy");
            facultylist.add(10, "Department of Educational Development");
        } else if (school.equals("NYP")) {
            facultylist.add(0, "School of Business Management");
            facultylist.add(1, "School Of Design");
            facultylist.add(2, "School of Chemical & Life Sciences");
            facultylist.add(3, "School Of Health Sciences");
            facultylist.add(4, "School of Engineering");
            facultylist.add(5, "School of Information Technology");
            facultylist.add(6, "School Of Interactive & Digital Media");
        } else if (school.equals("TP")) {
            facultylist.add(0, "School of Business");
            facultylist.add(1, "School Of Design");
            facultylist.add(2, "School of Applied Science");
            facultylist.add(3, "School of Informatics & IT");
            facultylist.add(4, "School of Engineering");
            facultylist.add(5, "School of Humanities & Social Sciences");
        } else if (school.equals("RP")) {
            facultylist.add(0, "School of Management and Communication");
            facultylist.add(1, "School of Applied Science");
            facultylist.add(2, "School of Sports, Health and Leisure");
            facultylist.add(3, "School of Hospitality");
            facultylist.add(4, "School of Engineering");
            facultylist.add(5, "School of Infocomm");
            facultylist.add(6, "School of Technology for the Arts");
        }
        return facultylist;

    }
}
