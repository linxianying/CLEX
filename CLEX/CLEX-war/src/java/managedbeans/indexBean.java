/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Answer;
import entity.Course;
import entity.Item;
import entity.Module;
import entity.Poll;
import entity.ProjectGroup;
import entity.Student;
import java.util.Iterator;
import entity.Transaction;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import session.AnnouncementSessionBeanLocal;
import session.ClassroomSessionBeanLocal;
import session.ClexSessionBeanLocal;
import session.OrderSessionBeanLocal;
import session.PRQuestionSessionBeanLocal;
import session.ScheduleSessionBeanLocal;
import session.StudyPlanSessionBeanLocal;
import session.ToDoListSessionBeanLocal;
import session.UserAccessControlBeanLocal;

/**
 *
 * @author caoyu
 */
@Named(value = "indexBean")
@Dependent
public class indexBean {
    @EJB
    private StudyPlanSessionBeanLocal cpsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ScheduleSessionBeanLocal sbl;
    @EJB
    private ClassroomSessionBeanLocal crsbl;
    @EJB
    private AnnouncementSessionBeanLocal asbl;
    @EJB
    private ToDoListSessionBeanLocal tdsbl;
    @EJB
    private PRQuestionSessionBeanLocal prqsbl;
    @EJB
    private OrderSessionBeanLocal osbl;
    @EJB
    private UserAccessControlBeanLocal uacbl; 
    
    public indexBean() {
        
    }
    
    private String genSalt(){
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();
        
        return salt;
    }
    
    public void testIcs(){
        sbl.loadIcsFile("");
    }
    
    public void createEntities() {
//        //create modules for all course from year 2014 sem1 - 2017 sem2
//        List<Course> courses = cpsbl.getAllCourses();
//        int year = 2014;
//        int sem = 1;
//        for (Course c: courses) {
//            do{
//                //create module for year and sem
//                csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", c);
//                //increase to next semester
//                if (sem == 1)
//                    sem++;
//                else {
//                    year++;
//                    sem--;
//                }
//            }while (year != 2018 || sem != 1);
//        }
        
        //create course, read from nusmods
        System.out.println("IndexBean:begin.");
        csbl.dragAllNusMods("try");
        System.out.println("IndexBean: drag module finished.");
        this.setModules();
        System.out.println("IndexBean: set module finished.");
        //create Users
        csbl.createAdmin("adminadmin", "123456", "Administrator", "admin@prism.com", "NUS", 90000000L, genSalt());
        
        csbl.createStudent("peterparker", "123456", "Peter Parker", "A0123451A", "peterparker@prism.com", "NUS", 90000001L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("peterparker");
        
        csbl.createStudent("brucebanner", "123456", "Bruce Banner", "A0123452B","brucebanner@prism.com", "NUS", 90000002L, genSalt(), "SOC", "IS","2015", "1", 0.0);
        uacbl.approveUser("brucebanner");
        
        csbl.createStudent("steverogers", "123456", "Steve Rogers", "A0123453C","steverogers@prism.com", "NUS", 90000003L, genSalt(), "SOC", "CS","2015", "2", 0.0);
        uacbl.approveUser("steverogers");
        
        csbl.createStudent("tonystark", "123456", "Tony Stark", "A0123454D","tonystark@prism.com", "NUS", 90000004L, genSalt(), "SOC", "IS","2015", "1", 0.0);
        uacbl.approveUser("tonystark");
        
        csbl.createStudent("nickfury", "123456", "Nick Fury", "A0123455E","nickfury@prism.com", "NUS", 90000005L, genSalt(), "SOC", "CS","2015", "2", 0.00);
        uacbl.approveUser("nickfury");
        
        csbl.createStudent("joseph", "123456", "Joseph Chan", "A0123456F","e0011012@u.nus.edu", "NUS", 90000006L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("joseph");
        
        csbl.createStudent("wenjie", "123456", "Wu Wenjie", "A0123457G","e0003941@u.nus.edu", "NUS", 90000007L, genSalt(), "SOC", "CS","2016", "1", 0.00);
        uacbl.approveUser("wenjie");
        
        csbl.createStudent("jeffrey", "123456", "Jeffrey Foo", "A0123458H","e0003912@u.nus.edu", "NUS", 90000008L, genSalt(), "SOC", "BA","2016", "2", 0.00);
        uacbl.approveUser("jeffrey");
        
        csbl.createStudent("caoyuu", "123456", "Cao Yu", "A0123459I","	e0012696@u.nus.edu", "NUS", 90000009L, genSalt(), "SOC", "IS","2016", "1", 0.00);
        uacbl.approveUser("caoyuu");
        
        csbl.createStudent("xianying", "123456", "Lin Xianying", "A0123461J","xianying@u.nus.edu", "NUS", 90000010L, genSalt(), "SOC", "EE","2016", "2", 0.00);
        uacbl.approveUser("xianying");
        
        csbl.createStudent("jeshua", "123456", "Jeshua Ang", "A0123462K","a0123970@u.nus.edu", "NUS", 90000011L, genSalt(), "SOC", "CEG","2016", "1", 0.00);
        uacbl.approveUser("jeshua");
        
        csbl.createStudent("eeeren", "123456", "Gwee Ee Ren", "A0123463L","e0011055@u.nus.edu", "NUS", 90000012L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("eeeren");
        
        csbl.createStudent("johnlee", "123456", "John Lee", "A0123464M","johnlee@gmail.com", "NUS", 90000013L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("johnlee");
        
        csbl.createStudent("steven", "123456", "Steven Tan", "A0123465N","steventan@gmail.com", "NUS", 90000014L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("steven");
        
        csbl.createStudent("garyneo", "123456", "Gary Neo", "A0123466O","garyneo@gmail.com", "NUS", 90000015L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("garyneo");
        
        csbl.createStudent("raygoh", "123456", "Ray Goh", "A0123467P", "raygoh@gmail.com", "NUS", 90000016L, genSalt(), "SOC", "CEG","2016", "1", 0.00);
        uacbl.approveUser("raygoh");
        
        csbl.createStudent("nicholas", "123456", "Nicholas See", "A0123468Q", "nicholas@gmail.com", "NUS", 90000017L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("nicholas");
        
        csbl.createStudent("ericteo", "123456", "Eric Teo", "A0123469R", "ericteo@gmail.com", "NUS", 90000018L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("ericteo");
        
        csbl.createStudent("maryjane", "123456", "Mary Jane", "A0123470S", "maryjane@gmail.com", "NUS", 90000019L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("maryjane");
        
        csbl.createStudent("matthew", "123456", "Sean Matthews", "A0123471T", "seanmatthews@gmail.com", "NUS", 90000020L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("matthew");
        
        csbl.createStudent("xavier", "123456", "Xavier", "A0123472U", "xavier@gmail.com", "NUS", 90000021L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("xavier");
        
        csbl.createStudent("ronnie", "123456", "Ronnie", "A0123473V", "ronnie@gmail.com", "NUS", 90000022L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("ronnie");
        
        csbl.createStudent("bernard", "123456", "Bernard Tey", "A0123474W", "bernard@gmail.com", "NUS", 90000023L, genSalt(), "SOC", "IS","2015", "1", 0.00);
        uacbl.approveUser("bernard");
        
        
        csbl.createLecturer("hsianghui", "123456", "Lek Hsiang Hui", "hsianghui@prism.com", "NUS", 86345278L, genSalt(), "SOC");
        uacbl.approveUser("hsianghui");
        
        csbl.createLecturer("dingyi", "123456", "Ding Yi", "dingyi@prism.com", "NUS", 92345678L, genSalt(), "SOC");
        uacbl.approveUser("dingyi");
        
        csbl.createLecturer("lifeng", "123456", "Zhou Li Feng", "zlf@prism.com", "NUS", 92342678L, genSalt(), "SOC");
        uacbl.approveUser("lifeng");
        
        csbl.createLecturer("lecturer", "123456", "Stephen Hawking", "stephen@prism.com", "NUS", 93345678L, genSalt(), "SOC");
        uacbl.approveUser("lecturer");
        
        
        csbl.createGuest("guesta", "123456", "Rory Lim", "rorylim@prism.com", "NUS", 90345678L, genSalt());
        uacbl.approveUser("guesta");
        
        csbl.createGuest("guestb", "123456", "Zhang Xiaozhong", "xiaozhong@prism.com", "NUS", 96645678L, genSalt());
        uacbl.approveUser("guestb");
        
        csbl.createGuest("guestc", "123456", "Zhong Mengjia", "mengjia@prism.com", "NUS", 97745678L, genSalt());
        uacbl.approveUser("guestc");
        
        csbl.createGuest("guestd", "123456", "Yang Xiaoge", "xiaoge@prism.com", "NUS", 90145781L, genSalt());
        uacbl.approveUser("guestd");
        
        csbl.createGuest("gueste", "123456", "Sun Junyi", "junyi@prism.com", "NUS", 90001678L, genSalt());
        uacbl.approveUser("gueste");
        
        
        //create Module basde on course
        //modules for 2015, sem 1
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("YIR3312"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("LAG2201"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("ES1102"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CL2280"));
        
        csbl.linkLecturerModule("lecturer", "CP3109", "2015", "1");
        csbl.linkLecturerModule("hsianghui", "YIR3312", "2015", "1");
        csbl.linkLecturerModule("dingyi", "LAG2201", "2015", "1");
        csbl.linkLecturerModule("lifeng", "ES1102", "2015", "1");
        csbl.linkLecturerModule("lecturer", "CL2280", "2015", "1");
        
        //modules for 2015, sem 2
        //csbl.createModule("2015", "2", "none", "none", csbl.findCourse("IS3106"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CL2281"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("PL1101E"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CM4254"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("YHU1210"));
        
        csbl.linkLecturerModule("lecturer", "IS3106", "2015", "2");
        csbl.linkLecturerModule("hsianghui", "CL2281", "2015", "2");
        csbl.linkLecturerModule("hsianghui", "PL1101E", "2015", "2");
        csbl.linkLecturerModule("dingyi", "CM4254", "2015", "2");
        csbl.linkLecturerModule("lecturer", "YHU1210", "2015", "2");
        
        //modules for 2016, sem 1
//        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CS1020"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("EC3551"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("FIN3103A"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("ACC1701X"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("MS3212"));
        
        csbl.linkLecturerModule("lecturer", "CS1020", "2016", "1");
        csbl.linkLecturerModule("hsianghui", "EC3551", "2016", "1");
        csbl.linkLecturerModule("dingyi", "FIN3103A", "2016", "1");
        csbl.linkLecturerModule("lifeng", "ACC1701X", "2016", "1");
        csbl.linkLecturerModule("lifeng", "MS3212", "2016", "1");
        
        //modules for 2016, sem 2
//        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CS2100"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("IE1111"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("PF3302"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("MA2101"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CL3551"));
        
        csbl.linkLecturerModule("lecturer", "CS2100", "2016", "2");
        csbl.linkLecturerModule("hsianghui", "IE1111", "2016", "2");
        csbl.linkLecturerModule("hsianghui", "PF3302", "2016", "2");
        csbl.linkLecturerModule("dingyi", "MA2101", "2016", "2");
        csbl.linkLecturerModule("lifeng", "CL3551", "2016", "2");
        
        //modules for 2017, sem 1
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("GER1000"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("RE2102"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("MLE2101"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("NM4227"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("LAL1201"));
        
        csbl.linkLecturerModule("lecturer", "GER1000", "2017", "1");
        csbl.linkLecturerModule("hsianghui", "RE2102", "2017", "1");
        csbl.linkLecturerModule("hsianghui", "MLE2101", "2017", "1");
        csbl.linkLecturerModule("lecturer", "NM4227", "2017", "1");
        csbl.linkLecturerModule("dingyi", "LAL1201", "2017", "1");
        
        //modules for 2017, sem 2, which is current year sem
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PS2240"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PC2193"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("SC3101"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("LAM3201"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("TIE2010"));
        
        csbl.linkLecturerModule("lecturer", "PS2240", "2017", "2");
        csbl.linkLecturerModule("hsianghui", "PC2193", "2017", "2");
        csbl.linkLecturerModule("hsianghui", "SC3101", "2017", "2");
        csbl.linkLecturerModule("lecturer", "LAM3201", "2017", "2");
        csbl.linkLecturerModule("lifeng", "TIE2010", "2017", "2");
        //modules for 2018, sem 1
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("ST2334"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("BT4212"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("CM1501"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("EE4101"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("IT1007"));
        
        
        //set each course a module for each semester, from year 2015-1 to 2017-2
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CP3109"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("YIR3312"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("YIR3312"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("YIR3312"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("YIR3312"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("LAG2201"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("LAG2201"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("LAG2201"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("LAG2201"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("ES1102"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("ES1102"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("ES1102"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("ES1102"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CL2280"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CL2280"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CL2280"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CL2280"));
        
        
//        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("IS3106"));
//        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("IS3106"));
//        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("IS3106"));
//        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("IS3106"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CL2281"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CL2281"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CL2281"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CL2281"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("PL1101E"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("PL1101E"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("PL1101E"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PL1101E"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CM4254"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CM4254"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CM4254"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CM4254"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("YHU1210"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("YHU1210"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("YHU1210"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("YHU1210"));
        
        
//        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CS1020"));
//        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CS1020"));
//        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CS1020"));
//        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CS1020"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("EC3551"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("EC3551"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("EC3551"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("EC3551"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("FIN3103A"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("FIN3103A"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("FIN3103A"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("FIN3103A"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("ACC1701X"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("ACC1701X"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("ACC1701X"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("ACC1701X"));
        
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("MS3212"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("MS3212"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("MS3212"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("MS3212"));
        
        
//        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CS2100"));
//        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CS2100"));
//        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CS2100"));
//        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CS2100"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("IE1111"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("IE1111"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("IE1111"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("IE1111"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("PF3302"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("PF3302"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("PF3302"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PF3302"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("MA2101"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("MA2101"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("MA2101"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("MA2101"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CL3551"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CL3551"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CL3551"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("CL3551"));
        
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("PS2240"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("PS2240"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("PS2240"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("PS2240"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("PC2193"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("PC2193"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("PC2193"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("PC2193"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("SC3101"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("SC3101"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("SC3101"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("SC3101"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("LAM3201"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("LAM3201"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("LAM3201"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("LAM3201"));
        
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("TIE2010"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("TIE2010"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("TIE2010"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("TIE2010"));
        
        //for test group formation function of lecturer
//        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("IS4231"));
        csbl.linkLecturerModule("hsianghui", "IS4231", "2017", "2");
        
        List<Course> courses = csbl.retrieveAllCourse();
        
        //for (Course each : courses) {
        //    Boolean success = (new File("/Applications/NetBeans/files/" + each.getModuleCode())).mkdirs();
        //    
        //}
        
        //set peer review form for hsianghui2 PC2193 TEST
        Date day = new Date();
        prqsbl.createPeerReviewQuestion("Test Peer Review Form", day, csbl.findModule("PC2193", "2017", "2"));
        
        
        System.out.println("IndexBean: create people, module finished.");
        
        
        //create lesson based on 2018 sem 1 courses
        csbl.createLesson("Monday", "12:00", "14:00", "LEC [1]", "COM1-0216",csbl.findModule("PS2240", "2017", "2"));
        csbl.createLesson("Monday", "14:00", "16:00", "LEC [2]", "COM1-0218",csbl.findModule("PS2240", "2017", "2"));
        csbl.createLesson("Monday", "14:00", "15:00", "TUT [1]", "COM1-0210",csbl.findModule("PS2240", "2017", "2"));
        csbl.createLesson("Tuesday", "10:00", "11:00", "TUT [2]", "COM1-0212",csbl.findModule("PS2240", "2017", "2"));
        
        csbl.createLesson("Tuesday", "10:00", "12:00", "LEC [1]", "COM1-0116",csbl.findModule("PC2193", "2017", "2"));
        csbl.createLesson("Wednesday", "10:00", "12:00", "LEC [2]", "COM1-0116",csbl.findModule("PC2193", "2017", "2"));
        csbl.createLesson("Friday", "12:00", "13:00", "TUT [1]", "COM1-0204",csbl.findModule("PC2193", "2017", "2"));
        csbl.createLesson("Monday", "14:00", "15:00", "TUT [2]", "COM1-0202",csbl.findModule("PC2193", "2017", "2"));
        
        csbl.createLesson("Tuesday", "12:30", "14:30", "LEC [1]", "COM1-0216",csbl.findModule("SC3101", "2017", "2"));
        csbl.createLesson("Tuesday", "18:00", "20:00", "LEC [2]", "COM2-0218",csbl.findModule("SC3101", "2017", "2"));
        csbl.createLesson("Monday", "13:00", "14:00", "TUT [1]", "COM1-0216",csbl.findModule("SC3101", "2017", "2"));
        csbl.createLesson("Thursday", "09:00", "10:00", "TUT [2]", "COM1-B116",csbl.findModule("SC3101", "2017", "2"));
        
        csbl.createLesson("Wednesday", "15:00", "18:00", "LEC [1]", "COM1-B108",csbl.findModule("LAM3201", "2017", "2"));
        csbl.createLesson("Tuesday", "12:00", "14:00", "LAB [1]", "COM2-0206",csbl.findModule("LAM3201", "2017", "2"));
        csbl.createLesson("Thursday", "12:00", "14:00", "LAB [2]", "COM2-0206",csbl.findModule("LAM3201", "2017", "2"));
        
        csbl.createLesson("Friday", "14:30", "17:30", "LEC [1]", "i3-0337",csbl.findModule("TIE2010", "2017", "2"));
        
        //set student "namename" with taken modules
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CP3109", "2015", "1"), "A");
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("YIR3312", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("LAG2201", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("ES1102", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CL2280", "2015", "1"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("brucebanner"), csbl.findModule("CP3109", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("nickfury"), csbl.findModule("CP3109", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("steverogers"), csbl.findModule("CP3109", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("tonystark"), csbl.findModule("CP3109", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("joseph"), csbl.findModule("CP3109", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("wenjie"), csbl.findModule("CP3109", "2015", "1"), "A");
        //csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("YIR3312", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("jeffrey"), csbl.findModule("LAG2201", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("caoyuu"), csbl.findModule("ES1102", "2015", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("eeeren"), csbl.findModule("CL2280", "2015", "1"), "A");
        
        
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("IS3106", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CL2281", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("PL1101E", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CM4254", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("YHU1210", "2015", "2"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("steverogers"), csbl.findModule("IS3106", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("nickfury"), csbl.findModule("CL2281", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("tonystark"), csbl.findModule("PL1101E", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("brucebanner"), csbl.findModule("CM4254", "2015", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("joseph"), csbl.findModule("YHU1210", "2015", "2"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CS1020", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("brucebanner"), csbl.findModule("CS1020", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("tonystark"), csbl.findModule("CS1020", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("steverogers"), csbl.findModule("CS1020", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("joseph"), csbl.findModule("CS1020", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("EC3551", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("FIN3103A", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("ACC1701X", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("MS3212", "2016", "1"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("xianying"), csbl.findModule("CS1020", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("xianying"), csbl.findModule("EC3551", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("xianying"), csbl.findModule("FIN3103A", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("wenjie"), csbl.findModule("ACC1701X", "2016", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("jeffrey"), csbl.findModule("MS3212", "2016", "1"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CS2100", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("tonystark"), csbl.findModule("CS2100", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("brucebanner"), csbl.findModule("CS2100", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("steverogers"), csbl.findModule("CS2100", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("IE1111", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("PF3302", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("MA2101", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("CL3551", "2016", "2"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("jeffrey"), csbl.findModule("CS2100", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("wenjie"), csbl.findModule("CS2100", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("joseph"), csbl.findModule("IE1111", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("jeshua"), csbl.findModule("PF3302", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("xianying"), csbl.findModule("MA2101", "2016", "2"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("eeeren"), csbl.findModule("CL3551", "2016", "2"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("GER1000", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("RE2102", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("MLE2101", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("NM4227", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("peterparker"), csbl.findModule("LAL1201", "2017", "1"), "A");
        
        csbl.setStudentTakenModules(csbl.findStudent("joseph"), csbl.findModule("GER1000", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("wenjie"), csbl.findModule("RE2102", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("eeeren"), csbl.findModule("MLE2101", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("jeshua"), csbl.findModule("NM4227", "2017", "1"), "A");
        csbl.setStudentTakenModules(csbl.findStudent("xianying"), csbl.findModule("LAL1201", "2017", "1"), "A");
        
        //Test accounts taking modules
        csbl.setStudentTakingModules(csbl.findStudent("peterparker"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("peterparker"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("peterparker"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("peterparker"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("peterparker"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nickfury"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nickfury"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nickfury"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nickfury"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nickfury"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("brucebanner"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("brucebanner"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("brucebanner"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("brucebanner"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("brucebanner"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("tonystark"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("tonystark"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("tonystark"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("tonystark"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("tonystark"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steverogers"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steverogers"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steverogers"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steverogers"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steverogers"), csbl.findModule("TIE2010", "2017", "2"));
        
        
        csbl.setStudentTakingModules(csbl.findStudent("joseph"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("caoyuu"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("wenjie"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeffrey"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("xianying"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("eeeren"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("wenjie"), csbl.findModule("TIE2010", "2017", "2"));
        
        csbl.setStudentTakingModules(csbl.findStudent("joseph"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("wenjie"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeffrey"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeshua"), csbl.findModule("SC3101", "2017", "2"));
        
        csbl.setStudentTakingModules(csbl.findStudent("joseph"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("wenjie"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeffrey"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeshua"), csbl.findModule("LAM3201", "2017", "2"));
        
        csbl.setStudentTakingModules(csbl.findStudent("ronnie"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("xianying"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeffrey"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeshua"), csbl.findModule("PS2240", "2017", "2"));
        
        csbl.setStudentTakingModules(csbl.findStudent("joseph"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("wenjie"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("xianying"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeshua"), csbl.findModule("PC2193", "2017", "2"));
        
        csbl.setStudentTakingModules(csbl.findStudent("joseph"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("xianying"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeffrey"), csbl.findModule("TIE2010", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeshua"), csbl.findModule("TIE2010", "2017", "2"));
        
        
        //for test group formation function of lecturer
        csbl.setStudentTakingModules(csbl.findStudent("peterparker"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("tonystark"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("brucebanner"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nickfury"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steverogers"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("joseph"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("wenjie"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeffrey"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("jeshua"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("xianying"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("caoyuu"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("eeeren"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("johnlee"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("steven"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("garyneo"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("raygoh"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("nicholas"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("ericteo"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("maryjane"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("matthew"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("xavier"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("ronnie"), csbl.findModule("IS4231", "2017", "2"));
        csbl.setStudentTakingModules(csbl.findStudent("bernard"), csbl.findModule("IS4231", "2017", "2"));
        
        //set student with lesson for 2017 sem 2 (current semester)
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Monday", "12:00", "LEC [1]", csbl.findModule("PS2240", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Monday", "14:00", "TUT [1]", csbl.findModule("PS2240", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Tuesday", "10:00", "LEC [1]", csbl.findModule("PC2193", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Friday", "12:00", "TUT [1]", csbl.findModule("PC2193", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Tuesday", "12:30", "LEC [1]", csbl.findModule("SC3101", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Thursday", "09:00", "TUT [2]", csbl.findModule("SC3101", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Wednesday", "15:00", "LEC [1]", csbl.findModule("LAM3201", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Thursday", "12:00", "LAB [2]", csbl.findModule("LAM3201", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("peterparker"),
                csbl.findLesson("Friday", "14:30", "LEC [1]", csbl.findModule("TIE2010", "2017", "2")));
        
        //create superGroup for current sem
        //No Group Formation needed for TIE2010
        csbl.createSuperGroup(3, 2, 2, 3, csbl.findModule("PS2240", "2017", "2"));
        //group formation has ended for module PS2240
        csbl.confirmGroupFormation(csbl.findModule("PS2240", "2017", "2"));
        csbl.createSuperGroup(3, 2, 2, 3, csbl.findModule("PC2193", "2017", "2"));
        csbl.createSuperGroup(3, 3, csbl.findModule("SC3101", "2017", "2"));
        csbl.createSuperGroup(4, 2, csbl.findModule("LAM3201", "2017", "2"));
        csbl.createSuperGroup(3, 2, csbl.findModule("CS1020", "2016", "1"));
        csbl.createSuperGroup(3, 1, csbl.findModule("GER1000", "2017", "1"));
        csbl.createSuperGroup(3, 3, csbl.findModule("CS2100", "2016", "2"));
        csbl.createSuperGroup(4, 2, csbl.findModule("CP3109", "2015", "1"));
        //csbl.createSuperGroup(5, 2, 3, csbl.findModule("TIE2010", "2017", "2"));
        
        //create projectGroup
        csbl.createProjectGroup(csbl.findModule("PS2240", "2017", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("PS2240", "2017", "2").getSuperGroup(), "N2",0.0);
        csbl.createProjectGroup(csbl.findModule("PS2240", "2017", "2").getSuperGroup(), "N3",0.0);
        csbl.createProjectGroup(csbl.findModule("PC2193", "2017", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("PC2193", "2017", "2").getSuperGroup(), "N2",0.0);
        csbl.createProjectGroup(csbl.findModule("PC2193", "2017", "2").getSuperGroup(), "N3",0.0);
        csbl.createProjectGroup(csbl.findModule("SC3101", "2017", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("SC3101", "2017", "2").getSuperGroup(), "N2",0.0);
        csbl.createProjectGroup(csbl.findModule("SC3101", "2017", "2").getSuperGroup(), "N3",0.0);
        csbl.createProjectGroup(csbl.findModule("LAM3201", "2017", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("LAM3201", "2017", "2").getSuperGroup(), "N2",0.0);
        csbl.createProjectGroup(csbl.findModule("LAM3201", "2017", "2").getSuperGroup(), "N3",0.0);
        
        csbl.createProjectGroup(csbl.findModule("CP3109", "2015", "1").getSuperGroup(), "N1",10.0);
        csbl.createProjectGroup(csbl.findModule("CP3109", "2015", "1").getSuperGroup(), "N2",22.0);
        csbl.createProjectGroup(csbl.findModule("CP3109", "2015", "1").getSuperGroup(), "N3",57.6);
        csbl.createProjectGroup(csbl.findModule("CS2100", "2016", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("CS2100", "2016", "2").getSuperGroup(), "N2",1.0);
        csbl.createProjectGroup(csbl.findModule("CS2100", "2016", "2").getSuperGroup(), "N3",0.0);
        csbl.createProjectGroup(csbl.findModule("GER1000", "2017", "1").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("GER1000", "2017", "1").getSuperGroup(), "N2",2.0);
        csbl.createProjectGroup(csbl.findModule("GER1000", "2017", "1").getSuperGroup(), "N3",0.9);
        
        
        //link student with projectgroup
        //student not join any project group for SC3101 and LAM3201
        csbl.linkStudentGroup(csbl.findStudent("peterparker"), csbl.findProjectgroup("N1", csbl.findModule("CS2100", "2016", "2")));
        csbl.linkStudentGroup(csbl.findStudent("peterparker"), csbl.findProjectgroup("N2", csbl.findModule("GER1000", "2017", "1")));
        csbl.linkStudentGroup(csbl.findStudent("peterparker"), csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("peterparker"), csbl.findProjectgroup("N2", csbl.findModule("PC2193", "2017", "2")));
        
        csbl.linkStudentGroup(csbl.findStudent("tonystark"), csbl.findProjectgroup("N1", csbl.findModule("SC3101", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("tonystark"), csbl.findProjectgroup("N1", csbl.findModule("LAM3201", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("tonystark"), csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("tonystark"), csbl.findProjectgroup("N1", csbl.findModule("PC2193", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("steverogers"), csbl.findProjectgroup("N1", csbl.findModule("CS2100", "2016", "2")));
        csbl.linkStudentGroup(csbl.findStudent("steverogers"), csbl.findProjectgroup("N2", csbl.findModule("GER1000", "2017", "1")));
        
        csbl.linkStudentGroup(csbl.findStudent("brucebanner"), csbl.findProjectgroup("N2", csbl.findModule("SC3101", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("brucebanner"), csbl.findProjectgroup("N2", csbl.findModule("LAM3201", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("brucebanner"), csbl.findProjectgroup("N2", csbl.findModule("PS2240", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("brucebanner"), csbl.findProjectgroup("N2", csbl.findModule("PC2193", "2017", "2")));
        
        csbl.linkStudentGroup(csbl.findStudent("nickfury"), csbl.findProjectgroup("N3", csbl.findModule("SC3101", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("nickfury"), csbl.findProjectgroup("N3", csbl.findModule("LAM3201", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("nickfury"), csbl.findProjectgroup("N3", csbl.findModule("PS2240", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("nickfury"), csbl.findProjectgroup("N3", csbl.findModule("PC2193", "2017", "2")));
        
        csbl.linkStudentGroup(csbl.findStudent("steverogers"), csbl.findProjectgroup("N1", csbl.findModule("SC3101", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("steverogers"), csbl.findProjectgroup("N1", csbl.findModule("LAM3201", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("steverogers"), csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("steverogers"), csbl.findProjectgroup("N1", csbl.findModule("PC2193", "2017", "2")));
        
        csbl.createProjectGroupTimeslot("", "2018-04-18 06:00", "2018-04-18 07:00", "Report", "Second System Release", "Cen Lib", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createProjectGroupTimeslot("", "2018-04-28 16:00", "2018-04-28 17:00", "Project", "First System Release", "Biz Lib", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createProjectGroupTimeslot("", "2018-04-21 12:00", "2018-04-21 13:00", "Group Meeting", "Discuss topic of project", "PGP", csbl.findProjectgroup("N2", csbl.findModule("PC2193", "2017", "2")));
        csbl.createProjectGroupTimeslot("", "2018-04-01 20:00", "2018-04-01 22:00", "Group Project", "Lab", "COM2", csbl.findProjectgroup("N2", csbl.findModule("PC2193", "2017", "2")));
        csbl.createProjectGroupTimeslot("", "2018-04-01 06:00", "2018-04-01 07:00", "Group Meeting", "Discussion", "Cen Lib", csbl.findProjectgroup("N2", csbl.findModule("PC2193", "2017", "2")));
        
        // date,  timeFrom,  timeEnd, title,  details,  venue,  projectGroup
        
        //create Transaction and ledger for PS2240 N1 group
        //date is of format dd-MM-yyyy
        Transaction t = new Transaction();
        t = csbl.createTransaction(43.7, "03-01-2018", "Dinner", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createLedger(csbl.findStudent("peterparker"), (43.7/3), 43.7, t);
        csbl.createLedger(csbl.findStudent("steverogers"), (43.7/3), 0.0, t);
        csbl.createLedger(csbl.findStudent("brucebanner"), (43.7/3), 0.0, t);
        
        t = csbl.createTransaction(15.6, "26-09-2017", "Lunch", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createLedger(csbl.findStudent("peterparker"), 4.0, 7.0, t);
        csbl.createLedger(csbl.findStudent("steverogers"), 7.2, 8.6, t);
        csbl.createLedger(csbl.findStudent("brucebanner"), 4.4, 0.0, t);
        
        t = csbl.createTransaction(6.3, "11-09-2017", "Print", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createLedger(csbl.findStudent("peterparker"), 2.1, 0.0, t);
        csbl.createLedger(csbl.findStudent("nickfury"), 2.1, 0.0, t);
        csbl.createLedger(csbl.findStudent("tonystark"), 2.1, 6.3, t);
        
        t = csbl.createTransaction(17.2, "03-12-2017", "Buy materials", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createLedger(csbl.findStudent("peterparker"), (17.2*0.3), 0.0, t);
        csbl.createLedger(csbl.findStudent("tonystark"), (17.2*0.1), 7.2, t);
        csbl.createLedger(csbl.findStudent("brucebanner"), (17.2*0.6), 10.0, t);
        
        t = csbl.createTransaction(3.9, "24-02-2018", "Print", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.createLedger(csbl.findStudent("peterparker"), 1.3, 3.9, t);
        csbl.createLedger(csbl.findStudent("steverogers"), 1.3, 0.0, t);
        csbl.createLedger(csbl.findStudent("brucebanner"), 1.3, 0.0, t);
        
        
        //create studyPlan, its relationship is set during create
        csbl.createStudyPlan("2018", "1", csbl.findCourse("CS2107"), csbl.findStudent("peterparker"));
        csbl.createStudyPlan("2018", "1", csbl.findCourse("CS3103"), csbl.findStudent("peterparker"));
        csbl.createStudyPlan("2018", "1", csbl.findCourse("CS3241"), csbl.findStudent("peterparker"));
        csbl.createStudyPlan("2018", "2", csbl.findCourse("EM1001"), csbl.findStudent("peterparker"));
        csbl.createStudyPlan("2018", "2", csbl.findCourse("LAF1201"), csbl.findStudent("peterparker"));
        
//        //create Grade, its relationship is set during create
//        csbl.createGrade("A", csbl.findModule("CP3109", "2015", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("YIR3312", "2015", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("LAG2201", "2015", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("ES1102", "2015", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("CL2280", "2015", "1"), csbl.findStudent("namename"));
//
//        csbl.createGrade("A", csbl.findModule("IS3106", "2015", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("CL2281", "2015", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("PL1101E", "2015", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("CM4254", "2015", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("YHU1210", "2015", "2"), csbl.findStudent("namename"));
//
//        csbl.createGrade("A", csbl.findModule("CS1020", "2016", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("EC3551", "2016", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("FIN3103A", "2016", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("ACC1701X", "2016", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("MS3212", "2016", "1"), csbl.findStudent("namename"));
//
//        csbl.createGrade("A", csbl.findModule("CS2100", "2016", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("IE1111", "2016", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("PF3302", "2016", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("MA2101", "2016", "2"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("CL3551", "2016", "2"), csbl.findStudent("namename"));
//
//        csbl.createGrade("A", csbl.findModule("GER1000", "2017", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("RE2102", "2017", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("MLE2101", "2017", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("NM4227", "2017", "1"), csbl.findStudent("namename"));
//        csbl.createGrade("A", csbl.findModule("LAL1201", "2017", "1"), csbl.findStudent("namename"));
        
        
        //csbl.createGrade("A", csbl.findModule("PS2240", "2017", "2"), csbl.findStudent("namename"));
        //csbl.createGrade("A", csbl.findModule("PC2193", "2017", "2"), csbl.findStudent("namename"));
        //csbl.createGrade("A", csbl.findModule("SC3101", "2017", "2"), csbl.findStudent("namename"));
        //csbl.createGrade("A", csbl.findModule("LAM3201", "2017", "2"), csbl.findStudent("namename"));
        //csbl.createGrade("A", csbl.findModule("TIE2010", "2017", "2"), csbl.findStudent("namename"));
        //sbl.createTimeslot("namename","2018-02-22", "14:00", "16:00", "IS3106", "LT17", "Lecture");
        //sbl.createTimeslot("namename","2018-01-12", "23:00", "23:50", "CS2100", "LT19", "Lecture");
        //sbl.createTimeslot("namename","2018-01-29", "10:00", "11:00", "Lab of CS3228", "COM2-201", "Lab");
        //sbl.createTimeslot("namename","2018-02-12", "11:00", "13:00", "Tutorial CS1010J", "COM1-203", "Tutorial");
        //sbl.createTimeslot("namename","2018-02-27", "09:00", "10:00", "Go out with Caoyu", "Orchard", "Go shopping");
        //sbl.createTimeslot("namename","2018-02-28", "20:00", "21:10", "Hang out with Susu", "Vivo City", "Buy clothes");
        //sbl.createTimeslot("guesta","2018-03-28", "18:00", "19:00", "Go with Su", "Beijing", "Buy clothes");
        //sbl.createTimeslot("dddddd","2018-02-28", "06:00", "07:00", "Homework", "Home", "Finish IS4103 homework");
        //sbl.createTimeslot("bbbbbb","2018-04-28", "11:00", "12:00", "Travel with Su", "Chongqing", "Buy sunglasses and eat hotpot");
        sbl.createTimeslot("lecturer", "Eat with Caoyu","2018-04-28 06:00", "2018-04-28 07:00", "Bugis", "Eat hotpot");
        sbl.createTimeslot("peterparker", "Go for IS3106", "2018-04-22 16:00", "2018-04-22 17:00", "LT17", "Lecture");
        sbl.createTimeslot("peterparker", "Go for CS2100","2018-04-26 16:00", "2018-04-26 17:00", "LT19", "Lecture");
        sbl.createTimeslot("peterparker", "Lab for CS3228","2018-04-21 11:00", "2018-04-21 12:00", "COM2-201", "Lab");
        sbl.createTimeslot("peterparker", "Tutorial for CS1010J", "2018-04-10 10:00", "2018-04-10 11:00",  "COM1-203", "Tutorial");
        sbl.createTimeslot("peterparker", "Go out with Caoyu", "2018-04-06 18:00", "2018-04-06 19:30", "Orchard", "Go shopping with Caoyu");
        sbl.createTimeslot("peterparker", "Hang out with Susu", "2018-04-11 19:00", "2018-04-11 20:10",  "Vivo City", "Buy clothes with Susu");
        sbl.createTimeslot("lecturer", "IS3106", "2018-04-22 16:00", "2018-04-22 17:00",  "LT17", "Lecture");
        sbl.createTimeslot("lecturer", "CS2100", "2018-04-26 16:00", "2018-04-26 17:00", "LT19", "Lecture");
        sbl.createTimeslot("lecturer", "CS3228", "2018-04-21 11:00", "2018-04-21 12:00",  "COM2-201", "Lab");
        sbl.createTimeslot("lecturer", "CS1010J", "2018-04-20 10:00", "2018-04-20 11:00",  "COM1-203", "Tutorial");
        sbl.createTimeslot("lecturer", "MA Introduction", "2018-04-16 18:00", "2018-04-16 19:30",  "LT27", "Introduce Math to bridging course students");
        sbl.createTimeslot("lecturer", "Meeting", "2018-04-11 09:00", "2018-04-11 10:10",  "Central Libriary", "Meeting with IS4103 group T02");
        sbl.createTimeslot("hsianghui", "IS4103", "2018-04-01 16:00", "2018-04-01 17:00",  "COM1-203", "Lecture");
        //sbl.createTimeslot("guesta", "Go with Su", "2018-03-18 22:00", "2018-03-18 23:00",  "Beijing", "Buy clothes");
        
        crsbl.createPoll("CS2100", "2016", "2", "2016-08-27", "Computer Organization", 0.25, "type1", "Do you like this course?");
        crsbl.createPoll("CS2100", "2016", "2", "2016-09-27", "Computer Organization", 0.49, "type1", "How do you find the lecturer?");
        crsbl.createPoll("CS2100", "2016", "2", "2016-10-17", "MIPS", 0.31, "type2", "How much do you know MIPS?");
        crsbl.createPoll("CS1020", "2016", "1", "2016-01-30", "Java OOP", 0.70, "type1", "Do you feel familiar with four OOP concepts?");
        crsbl.createPoll("CS1020", "2016", "1", "2016-02-30", "Java OOP", 0.40, "type2", "Do you understand inheritance?");
        crsbl.createPoll("CS1020", "2016", "1", "2016-03-30", "Java OOP", 0.29, "type3", "Do you understand abstract?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-02-27", "Final report", 0.38, "type1", "How is your final report going?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-11-27", "Physics", 0.31, "type1", "Do you like this course?");
        crsbl.createPoll("LAL1201", "2017", "1", "2017-02-17", "Language Learning Curve", 0.18, "type", "Do you find it difficult to learn new languages?");
        crsbl.createPoll("CS2100", "2016", "2", "2016-08-27", "Computer Organization", 0.38, "type", "Do you like this course?");
        crsbl.createPoll("CP3109", "2015", "1", "2015-01-29", "Project", 0.11, "type", "Do you think this course bring you something interesting");
        crsbl.createPoll("CS1020", "2016", "1", "2016-03-20", "Java OOP", 0.27, "type", "Do you feel familiar with four OOP concepts?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-01-27", "First lecture", 0.53, "type2", "How is the lecturer?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-02-27", "Second lecture", 0.13, "type3", "How is the tutorial?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-03-27", "Assignment", 0.78, "type4", "How is the assignment?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-10-27", "Physics", 0.39, "type1", "Do you understand the concepts");
        crsbl.createPoll("LAL1201", "2017", "1", "2017-04-17", "Language Learning Curve", 0.27, "type1", "How is your learning experience?");
        crsbl.createPoll("IE1111", "2016", "2", "2017-10-17", "IE", 0.38, "type2", "How is your learning experience?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-11-17", "topic1", 0.64, "type1", "How is your learning experience?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-11-27", "topic2", 0.42, "type2", "How is your learning experience?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-12-07", "topic3", 0.71, "type3", "How is your learning experience?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-12-11", "topic3", 0.38, "type4", "How is your learning experience?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-12-17", "topic4", 0.10, "type5", "How is your learning experience?");
        ArrayList<Answer> ans = new ArrayList<Answer>();
        
        ans.add(crsbl.createAnswer("A.Very Likely"));
        ans.add(crsbl.createAnswer("B.Likely"));
        ans.add(crsbl.createAnswer("C.Not Likely"));
        ans.add(crsbl.createAnswer("D.Never"));
        
        ArrayList<Answer> ans1 = new ArrayList<Answer>();
        ans1.add(crsbl.createAnswer("A.Understand all the materials"));
        ans1.add(crsbl.createAnswer("B.Understand some parts of the materials"));
        ans1.add(crsbl.createAnswer("C.Not Understand"));
        crsbl.createUnfinishedPoll("PC2193", "2017", "2", "2018-01-01", "topic4", 0.0, "type5", "Would you like to take next level physics course again?", ans, 0);
        crsbl.createUnfinishedPoll("PC2193", "2017", "2", "2018-02-01", "topic1", 0.0, "type1", "Do you understand this week's new concepts?", ans1, 0);
        
        
        
        asbl.createLecturerAnnc("lecturer", "CS2100 Lecture 5 cancelled", "Lecture 5, which is on next Friday is cancelled", "CS2100");
        asbl.createLecturerAnnc("lecturer", "IS3106 Lecture 6 cancelled", "Lecture 6, which is on next Monday is cancelled", "IS3106");
        asbl.createLecturerAnnc("lecturer", "PS2240 Tutorial 5 cancelled", "Tutorial 3, which is on next Friday is cancelled", "PS2240");
        asbl.createLecturerAnnc("hsianghui", "PC2193 Lecture 5 cancelled", "PC2193 Lecture 6, which is on next Friday is cancelled", "PC2193");
        asbl.createLecturerAnnc("hsianghui", "PC2193 Lab 3 cancelled", "PC2193 Lab 3, which is on this Thursday is cancelled", "PC2193");
        //asbl.createAdminAnnc("admin", "CIT Recruitment for System Engineer", "Interested candidates are advised to read the detailed requirements and apply for the above position at this following link: https://nuscareers.taleo.net/careersection/nusep/jobdetail.ftl?job=007LB Thank you! Admin Wong", "1");
        
        tdsbl.createTask("peterparker", "2018-01-17", "2018-01-21 12:00", "IS4103 Lab 1", "IS4103 Lab Week 1", "finished");
        tdsbl.createTask("peterparker", "2018-02-17", "2018-03-21 12:00", "IS4103 Lab 2", "IS4103 Lab Week 2", "finished");
        tdsbl.createTask("peterparker", "2018-03-17", "2018-03-21 21:00", "IS4103 Lab 3", "IS4103 Lab Week 3", "unfinished");
        tdsbl.createTask("peterparker", "2018-04-17", "2018-04-21 23:59", "IS4103 Lab 4", "IS4103 Lab Week 4", "unfinished");
        tdsbl.createTask("peterparker", "2018-03-18", "2018-03-19 02:00", "IS4103 Lab 5", "IS4103 Lab Week 5", "finished");
        tdsbl.createTask("peterparker", "2018-03-19", "2018-03-20 02:00", "IS4103 Lab 6", "IS4103 Lab Week 6", "finished");
        tdsbl.createTask("peterparker", "2018-03-20", "2018-03-21 02:00", "IS4103 Lab 7", "IS4103 Lab Week 7", "finished");
        tdsbl.createTask("peterparker", "2018-01-17", "2018-05-21 02:00", "IS4103 Lab 8", "IS4103 Lab Week 8", "finished");
        tdsbl.createTask("brucebanner", "2018-01-17", "2018-01-21 12:00", "IS4103 Lab 1", "IS4103 Lab Week 4", "finished");
        tdsbl.createTask("brucebanner", "2018-02-17", "2018-02-21 17:00", "IS4103 Lab 2", "IS4103 Lab Week 4", "finished");
        tdsbl.createTask("brucebanner", "2018-03-17", "2018-03-21 23:00", "IS4103 Lab 3", "IS4103 Lab Week 4", "finished");
        tdsbl.createTask("brucebanner", "2018-04-17", "2018-04-21 13:00", "IS4103 Lab 4", "IS4103 Lab Week 4", "unfinished");
        tdsbl.createTask("brucebanner", "2018-05-17", "2018-05-21 09:00", "IS4103 Lab 5", "IS4103 Lab Week 4", "unfinished");
        
        
        tdsbl.createGroupTask("2018-04-07", "2018-04-22 23:59", "PS2240 Group Meeting",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")),
                getProjectUserName(csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2"))));
        
        tdsbl.createGroupTask("2018-04-07", "2018-04-12 12:59", "PS2240 - Group Talk",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")),
                getProjectUserName(csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2"))));
        tdsbl.createGroupTask("2018-04-07", "2018-04-12 12:59", "PS2240 - Group Lecture",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")),
                getProjectUserName(csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2"))));
        tdsbl.createGroupTask("2018-04-07", "2018-04-12 12:59", "GER1000 - Group Lecture",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N2", csbl.findModule("GER1000", "2017", "1")),
                getProjectUserName(csbl.findProjectgroup("N2", csbl.findModule("GER1000", "2017", "1"))));
        tdsbl.createGroupTask("2018-04-07", "2018-04-11 10:59", "GER1000 - Group Discussion",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N2", csbl.findModule("GER1000", "2017", "1")),
                getProjectUserName(csbl.findProjectgroup("N2", csbl.findModule("GER1000", "2017", "1"))));
        tdsbl.createGroupTask("2018-04-07", "2018-04-12 12:59", "SC3101 - Group Report",
                "Group Task Test details", "unfinished", csbl.findProjectgroup("N1", csbl.findModule("SC3101", "2017", "2")),
                getProjectUserName(csbl.findProjectgroup("N1", csbl.findModule("SC3101", "2017", "2"))));
        System.out.println("IndexBean: task, poll, answer finished.");
        this.testShop();
        System.out.println("IndexBean: test shop finished.");
    }
    
    public String[] getProjectUserName(ProjectGroup p){
        if(p.getGroupMembers()==null){
            System.out.println("This project group is empty");
            return null;
        }
        Iterator itr = p.getGroupMembers().iterator();
        String[] name = new String[p.getGroupMembers().size()];
        System.out.println("This project group size is " + p.getGroupMembers().size());
        int index = 0;
        Student s;
        while(itr.hasNext()){
            s =(Student) itr.next();
            name[index] = s.getUsername();
            index++;
        }
        return name;
    }
    
    public void setModules() {
        int year = 2014;
        int sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS1103"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS1105"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS2101"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS2102"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS2103"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS3103"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS3106"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS3150"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS3240"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS3261"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4010"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4100"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4103"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4204"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4228"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4231"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4234"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4240"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("IS4301"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS1010"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS1010J"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS1010S"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS1020"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS1231"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2010"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2030"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2040"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2100"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2101"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2102"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2103"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2104"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2105"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2106"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2107"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2108"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2220"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS2309"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3103"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3201"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3202"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3205"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3210"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3216"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3219"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3230"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3235"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3240"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3241"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
        year = 2014;
        sem = 1;
        do{
            //create module for year and sem
            csbl.createModule(Integer.toString(year), Integer.toString(sem), "none", "none", csbl.findCourse("CS3244"));
            //increase to next semester
            if (sem == 1)
                sem++;
            else {
                year++;
                sem--;
            }
        }while (year != 2018 || sem != 1);
        
    }
    
    public void testShop() {
        osbl.createShop("The Terrace", "Noodles", "terrace1", "123456", "NUS");
        osbl.createItem(osbl.findShop("terrace1"), "Ban Mian", 3.2);
        osbl.createItem(osbl.findShop("terrace1"), "Tom	Yam Mian", 3.2);
        osbl.createItem(osbl.findShop("terrace1"), "Dumplings", 4.0);
        osbl.createShop("The Terrace", "Korean", "terrace2", "123456", "NUS");
        osbl.createItem(osbl.findShop("terrace2"), "Pork", 4.5);
        osbl.createItem(osbl.findShop("terrace2"), "Chicken", 4.5);
        osbl.createItem(osbl.findShop("terrace2"), "Beef", 4.5);
        osbl.createItem(osbl.findShop("terrace2"), "Chicken soup", 5.0);
        osbl.createItem(osbl.findShop("terrace2"), "Kimchi Soup", 4.0);
        osbl.createItem(osbl.findShop("terrace2"), "Kimchi fried rice", 4.5);
        osbl.createShop("Pines Food Court", "Noodles", "pines1", "123456", "NUS");
        osbl.createItem(osbl.findShop("pines1"), "Dan Dan Mian",4.5);
        osbl.createItem(osbl.findShop("pines1"), "ChongQing Xiao Mian", 4.5);
        osbl.createItem(osbl.findShop("pines1"), "Beef noodle", 4.5);
        osbl.createItem(osbl.findShop("pines1"), "Dumplings", 5.0);
        osbl.createItem(osbl.findShop("pines1"), "Wontons", 4.5);
        osbl.createItem(osbl.findShop("pines1"), "Add Vegetables", 1.0);
        osbl.createItem(osbl.findShop("pines1"), "Take away", 0.5);
        
        //create orders
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d1 = new Date();
        Date d2 = new Date();
        Date d3 = new Date();
        Date d4 = new Date();
        Date d5 = new Date();
        Date d6 = new Date();
        try {
            d1 = sdf.parse("21/03/2017 12:20:00");
            d2 = sdf.parse("27/01/2016 14:50:00");
            d3 = sdf.parse("03/11/2017 19:17:20");
            d4 = sdf.parse("07/03/2018 10:00:00");
            d5 = sdf.parse("07/03/2017 16:20:00");
            d6 = sdf.parse("21/09/2016 15:30:00");
        } catch (ParseException ex) {
            Logger.getLogger(indexBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        HashMap<Item, Integer> test = new HashMap<Item, Integer>();
        test.put(osbl.findItemByName(osbl.findShop("pines1"), "Wontons"), 1);
        test.put(osbl.findItemByName(osbl.findShop("pines1"), "Dumplings"), 2);
        osbl.createOrder(csbl.findStudent("caoyuu"), test, osbl.findShop("pines1"), d1, 14.5);
        
        test.clear();
        test.put(osbl.findItemByName(osbl.findShop("terrace2"), "Kimchi fried rice"), 1);
        osbl.createOrder(csbl.findStudent("caoyuu"), test, osbl.findShop("terrace2"), d2, 4.5);
        
        test.clear();
        test.put(osbl.findItemByName(osbl.findShop("terrace1"), "Ban Mian"), 2);
        test.put(osbl.findItemByName(osbl.findShop("terrace1"), "Dumplings"), 1);
        osbl.createOrder(csbl.findStudent("caoyuu"), test, osbl.findShop("terrace1"), d3, 10.4);
        
        
    }
}
