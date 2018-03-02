/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import java.util.Random;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import session.ClassroomSessionBeanLocal;
import session.ClexSessionBeanLocal;
import session.ScheduleSessionBeanLocal;
import session.StudyPlanSessionBeanLocal;

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
   
    /**
     * Creates a new instance of indexBean
     */
    public indexBean() {
        
    }
    
    private String genSalt(){
        Random rng = new Random();
        Integer gen = rng.nextInt(13371337);
        String salt = gen.toString();
        
        return salt;
    }
    
    public void testIcs(){
        sbl.loadIcsFile();
    }
    
    public void createEntities() {
        //create Users
        csbl.createStudent("namename", "123456", "LinXianying", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 5.0);
        csbl.createStudent("aaaaaa", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        csbl.createStudent("bbbbbb", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "CS","2015", "2", 0.0);
        csbl.createStudent("cccccc", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        csbl.createStudent("dddddd", "123456", "name", "email@email.com", "NUS", 65345455L, genSalt(), "soc", "CS","2015", "2", 0.0);
        csbl.createStudent("eeeeee", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        csbl.createStudent("ffffff", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "CS","2016", "1", 0.0);
        csbl.createStudent("gggggg", "123456", "name", "email@email.com", "NUS", 12345378L, genSalt(), "soc", "BA","2016", "2", 0.0);
        csbl.createStudent("hhhhhh", "123456", "name", "email@email.com", "NUS", 12345278L, genSalt(), "soc", "IS","2016", "1", 0.0);
        csbl.createStudent("iiiiii", "123456", "name", "email@email.com", "NUS", 12845678L, genSalt(), "soc", "EE","2016", "2", 0.0);
        csbl.createStudent("jjjjjj", "123456", "name", "email@email.com", "NUS", 12345278L, genSalt(), "soc", "CEG","2016", "1", 0.0);
        csbl.createLecturer("lecturer", "123456", "Zhou Lifeng", "zlf@email.com", "NUS", 12345278L, genSalt(), "soc");
        csbl.createLecturer("hsianghui2", "123456", "LekHsiangHui2", "hsianghui@email.com", "NUS", 92345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui3", "123456", "LekHsiangHui3", "hh3@email.com", "NUS", 12342678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui4", "123456", "LekHsiangHui4", "hh4@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createGuest("guesta", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestb", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestc", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestd", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("gueste", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        
        //create course, read from nusmods
        csbl.dragAllNusMods("try");
        
        //create Module basde on course
        //modules for 2015, sem 1
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("YIR3312"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("LAG2201"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("ES1102"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CL2280"));
        
        csbl.linkLecturerModule("lecturer", "CP3109", "2015", "1");
        csbl.linkLecturerModule("hsianghui2", "YIR3312", "2015", "1");
        csbl.linkLecturerModule("hsianghui3", "LAG2201", "2015", "1");
        csbl.linkLecturerModule("hsianghui2", "ES1102", "2015", "1");
        csbl.linkLecturerModule("hsianghui3", "CL2280", "2015", "1");
        //modules for 2015, sem 2
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("IS3106"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CL2281"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("PL1101E"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CM4254"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("YHU1210"));
        
        csbl.linkLecturerModule("lecturer", "IS3106", "2015", "2");
        csbl.linkLecturerModule("hsianghui2", "CL2281", "2015", "2");
        csbl.linkLecturerModule("hsianghui3", "PL1101E", "2015", "2");
        csbl.linkLecturerModule("hsianghui4", "CM4254", "2015", "2");
        csbl.linkLecturerModule("hsianghui4", "YHU1210", "2015", "2");
        //modules for 2016, sem 1
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CS1020"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("EC3551"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("FIN3103A"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("ACC1701X"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("MS3212"));
        
        csbl.linkLecturerModule("lecturer", "CS1020", "2016", "1");
        csbl.linkLecturerModule("hsianghui2", "EC3551", "2016", "1");
        csbl.linkLecturerModule("hsianghui3", "FIN3103A", "2016", "1");
        csbl.linkLecturerModule("hsianghui4", "ACC1701X", "2016", "1");
        csbl.linkLecturerModule("hsianghui4", "MS3212", "2016", "1");
        //modules for 2016, sem 2
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CS2100"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("IE1111"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("PF3302"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("MA2101"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CL3551"));
        
        csbl.linkLecturerModule("lecturer", "CS2100", "2016", "2");
        csbl.linkLecturerModule("hsianghui2", "IE1111", "2016", "2");
        csbl.linkLecturerModule("hsianghui3", "PF3302", "2016", "2");
        csbl.linkLecturerModule("hsianghui4", "MA2101", "2016", "2");
        csbl.linkLecturerModule("hsianghui4", "CL3551", "2016", "2");
        
        //modules for 2017, sem 1
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("GER1000"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("RE2102"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("MLE2101"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("NM4227"));
        //csbl.createModule("2017", "1", "none", "none", csbl.findCourse("LAL1201"));
        
        csbl.linkLecturerModule("lecturer", "GER1000", "2017", "1");
        csbl.linkLecturerModule("hsianghui2", "RE2102", "2017", "1");
        csbl.linkLecturerModule("hsianghui3", "MLE2101", "2017", "1");
        csbl.linkLecturerModule("hsianghui4", "NM4227", "2017", "1");
        csbl.linkLecturerModule("hsianghui4", "LAL1201", "2017", "1");
        
        //modules for 2017, sem 2, which is current year sem
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PS2240"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PC2193"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("SC3101"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("LAM3201"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("TIE2010"));
        
        
        //create superGroup for current sem
        //No superGroup for TIE2010
        csbl.createSuperGroup(3, 2, 3, csbl.findModule("PS2240", "2017", "2"));
        csbl.createSuperGroup(3, 1, 3, csbl.findModule("PC2193", "2017", "2"));
        csbl.createSuperGroup(3, 3, 3, csbl.findModule("SC3101", "2017", "2"));
        csbl.createSuperGroup(4, 2, 3, csbl.findModule("LAM3201", "2017", "2"));
        //csbl.createSuperGroup(5, 2, 3, csbl.findModule("TIE2010", "2017", "2"));
        
        //create projectGroup 
        //no project group created for LAM3201
        csbl.createProjectGroup(csbl.findModule("PS2240", "2017", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("PS2240", "2017", "2").getSuperGroup(), "N2",0.0);
        csbl.createProjectGroup(csbl.findModule("PS2240", "2017", "2").getSuperGroup(), "N3",0.0);
        csbl.createProjectGroup(csbl.findModule("PC2193", "2017", "2").getSuperGroup(), "N1",0.0);
        csbl.createProjectGroup(csbl.findModule("SC3101", "2017", "2").getSuperGroup(), "N1",0.0);
        
        //link student with projectgroup
        //student not join any project group for SC3101
        csbl.linkStudentGroup(csbl.findStudent("namename"), csbl.findProjectgroup("N1", csbl.findModule("PS2240", "2017", "2")));
        csbl.linkStudentGroup(csbl.findStudent("namename"), csbl.findProjectgroup("N1", csbl.findModule("PC2193", "2017", "2")));
        
        csbl.linkLecturerModule("lecturer", "PS2240", "2017", "2");
        csbl.linkLecturerModule("hsianghui2", "PC2193", "2017", "2");
        csbl.linkLecturerModule("hsianghui3", "SC3101", "2017", "2");
        csbl.linkLecturerModule("hsianghui4", "LAM3201", "2017", "2");
        csbl.linkLecturerModule("hsianghui4", "TIE2010", "2017", "2");
        //modules for 2018, sem 1
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("ST2334"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("BT4212"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("CM1501"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("EE4101"));
        //csbl.createModule("2018", "1", "none", "none", csbl.findCourse("IT1007"));
        
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
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CP3109", "2015", "1"));
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("YIR3312", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("LAG2201", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("ES1102", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CL2280", "2015", "1"));
        
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("CP3109", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("CP3109", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("cccccc"), csbl.findModule("CP3109", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("dddddd"), csbl.findModule("CP3109", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("eeeeee"), csbl.findModule("CP3109", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("CP3109", "2015", "1"));
        //csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("YIR3312", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("cccccc"), csbl.findModule("LAG2201", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("dddddd"), csbl.findModule("ES1102", "2015", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("eeeeee"), csbl.findModule("CL2280", "2015", "1"));
        
        
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("IS3106", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CL2281", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("PL1101E", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CM4254", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("YHU1210", "2015", "2"));
        
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("IS3106", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("CL2281", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("gggggg"), csbl.findModule("PL1101E", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("eeeeee"), csbl.findModule("CM4254", "2015", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("YHU1210", "2015", "2"));
        
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CS1020", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("CS1020", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("jjjjjj"), csbl.findModule("CS1020", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("CS1020", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("cccccc"), csbl.findModule("CS1020", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("EC3551", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("FIN3103A", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("ACC1701X", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("MS3212", "2016", "1"));
        
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("CS1020", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("EC3551", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("FIN3103A", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("ACC1701X", "2016", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("dddddd"), csbl.findModule("MS3212", "2016", "1"));
      
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CS2100", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("CS2100", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("dddddd"), csbl.findModule("CS2100", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("gggggg"), csbl.findModule("CS2100", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("IE1111", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("PF3302", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("MA2101", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CL3551", "2016", "2"));
        
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("CS2100", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("IE1111", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("jjjjjj"), csbl.findModule("PF3302", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("ffffff"), csbl.findModule("MA2101", "2016", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("CL3551", "2016", "2"));

        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("GER1000", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("RE2102", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("MLE2101", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("NM4227", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("LAL1201", "2017", "1"));
        
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("GER1000", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("jjjjjj"), csbl.findModule("RE2102", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("eeeeee"), csbl.findModule("MLE2101", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("dddddd"), csbl.findModule("NM4227", "2017", "1"));
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("LAL1201", "2017", "1"));

        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("TIE2010", "2017", "2"));
        
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("iiiiii"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("gggggg"), csbl.findModule("PS2240", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("hhhhhh"), csbl.findModule("PC2193", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("aaaaaa"), csbl.findModule("SC3101", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("bbbbbb"), csbl.findModule("LAM3201", "2017", "2"));
        csbl.setStudentTakenModules(csbl.findStudent("cccccc"), csbl.findModule("TIE2010", "2017", "2"));
        
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("ST2334", "2018", "1"));
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("BT4212", "2018", "1"));
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("CM1501", "2018", "1"));
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("EE4101", "2018", "1"));
        //csbl.setStudentTakenModules(csbl.findStudent("namename"), csbl.findModule("IT1007", "2018", "1"));

        //set student with lesson for 2017 sem 2 (current semester)
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Monday", "12:00", "LEC [1]", csbl.findModule("PS2240", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Monday", "14:00", "TUT [1]", csbl.findModule("PS2240", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Tuesday", "10:00", "LEC [1]", csbl.findModule("PC2193", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Friday", "12:00", "TUT [1]", csbl.findModule("PC2193", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Tuesday", "12:30", "LEC [1]", csbl.findModule("SC3101", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Thursday", "09:00", "TUT [2]", csbl.findModule("SC3101", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Wednesday", "15:00", "LEC [1]", csbl.findModule("LAM3201", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Thursday", "12:00", "LAB [2]", csbl.findModule("LAM3201", "2017", "2")));
        csbl.setStudentLesson(csbl.findStudent("namename"), 
                csbl.findLesson("Friday", "14:30", "LEC [1]", csbl.findModule("TIE2010", "2017", "2")));
        
        //create studyPlan, its relationship is set during create
        csbl.createStudyPlan("2018", "1", csbl.findCourse("CS2107"), csbl.findStudent("namename"));
        csbl.createStudyPlan("2018", "1", csbl.findCourse("CS3103"), csbl.findStudent("namename"));
        csbl.createStudyPlan("2018", "1", csbl.findCourse("CS3241"), csbl.findStudent("namename"));
        csbl.createStudyPlan("2018", "2", csbl.findCourse("EM1001"), csbl.findStudent("namename"));
        csbl.createStudyPlan("2018", "2", csbl.findCourse("LAF1201"), csbl.findStudent("namename"));
        
        //create Grade, its relationship is set during create
        csbl.createGrade("A", csbl.findModule("CP3109", "2015", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("YIR3312", "2015", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("LAG2201", "2015", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("ES1102", "2015", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("CL2280", "2015", "1"), csbl.findStudent("namename"));

        csbl.createGrade("A", csbl.findModule("IS3106", "2015", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("CL2281", "2015", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("PL1101E", "2015", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("CM4254", "2015", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("YHU1210", "2015", "2"), csbl.findStudent("namename"));

        csbl.createGrade("A", csbl.findModule("CS1020", "2016", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("EC3551", "2016", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("FIN3103A", "2016", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("ACC1701X", "2016", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("MS3212", "2016", "1"), csbl.findStudent("namename"));
        
        csbl.createGrade("A", csbl.findModule("CS2100", "2016", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("IE1111", "2016", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("PF3302", "2016", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("MA2101", "2016", "2"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("CL3551", "2016", "2"), csbl.findStudent("namename"));

        csbl.createGrade("A", csbl.findModule("GER1000", "2017", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("RE2102", "2017", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("MLE2101", "2017", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("NM4227", "2017", "1"), csbl.findStudent("namename"));
        csbl.createGrade("A", csbl.findModule("LAL1201", "2017", "1"), csbl.findStudent("namename"));

        
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
        //sbl.createTimeslot("hsianghui3","2018-02-28", "16:00", "17:00", "Eat with Caoyu", "Bugis", "Eat hotpot");
        
        
        crsbl.createPoll("CS2100", "2016", "2", "2016-08-27", "Computer Organization", 0.35, "type", "Do you like this course?");
        
        crsbl.createPoll("CS1020", "2016", "1", "2016-01-30", "Java OOP Concepts", 0.70, "type", "Do you feel familiar with four OOP concepts?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-02-27", "Final report", 0.38, "type", "How is your final report going?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-11-27", "Physics", 0.31, "type", "Do you like this course?");
        crsbl.createPoll("LAL1201", "2017", "1", "2017-02-17", "Language Learning Curve", 0.18, "type", "Do you find it difficult to learn new languages?");
        crsbl.createPoll("CS2100", "2016", "2", "2016-08-27", "Computer Organization", 0.38, "type", "Do you like this course?");
        crsbl.createPoll("CP3109", "2015", "1", "2015-01-29", "Project", 0.11, "type", "Do you think this course bring you something interesting");
        crsbl.createPoll("CS1020", "2016", "1", "2016-03-20", "Java OOP Concepts", 0.27, "type", "Do you feel familiar with four OOP concepts?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-01-27", "First lecture", 0.53, "type", "How is the lecturer?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-02-27", "Second lecture", 0.13, "type", "How is the tutorial?");
        crsbl.createPoll("GER1000", "2017", "1", "2017-03-27", "Assignment", 0.78, "type", "How is the assignment?");
        crsbl.createPoll("PC2193", "2017", "2", "2017-10-27", "Physics", 0.39, "type", "Do you understand the concepts");
        crsbl.createPoll("LAL1201", "2017", "1", "2017-04-17", "Language Learning Curve", 0.27, "type", "How is your learning experience?");
        crsbl.createPoll("IE1111", "2016", "2", "2017-10-17", "IE", 0.38, "type", "How is your learning experience?");
        
        
        
    }
}
