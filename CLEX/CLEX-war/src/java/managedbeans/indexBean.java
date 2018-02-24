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
import session.ClexSessionBeanLocal;
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
    
    public void createEntities() {
        //create Users
        csbl.createStudent("aaaaaa", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        csbl.createStudent("bbbbbb", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "2", 0.0);
        csbl.createStudent("cccccc", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        csbl.createStudent("dddddd", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "2", 0.0);
        csbl.createStudent("eeeeee", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2015", "1", 0.0);
        csbl.createStudent("ffffff", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "1", 0.0);
        csbl.createStudent("gggggg", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "2", 0.0);
        csbl.createStudent("hhhhhh", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "1", 0.0);
        csbl.createStudent("iiiiii", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "2", 0.0);
        csbl.createStudent("jjjjjj", "123456", "name", "email@email.com", "NUS", 12345678L, genSalt(), "soc", "IS","2016", "1", 0.0);
        csbl.createLecturer("hsianghui2", "123456", "LekHsiangHui2", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui3", "123456", "LekHsiangHui3", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createLecturer("hsianghui4", "123456", "LekHsiangHui4", "email@email.com", "NUS", 12345678L, genSalt(), "soc");
        csbl.createGuest("guesta", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestb", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestc", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("guestd", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        csbl.createGuest("gueste", "123456", "someguest", "email@email.com", "NUS", 12345678L, genSalt());
        
        //create course ?
        
        //create Module basde on course
        //modules for 2015, sem 1
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CP3109"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("YIR3312"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("LAG2201"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("ES1102"));
        csbl.createModule("2015", "1", "none", "none", csbl.findCourse("CL2280"));
        //modules for 2015, sem 2
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("IS3106"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CL2281"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("PL1101E"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("CM4254"));
        csbl.createModule("2015", "2", "none", "none", csbl.findCourse("YHU1210"));
        //modules for 2016, sem 1
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("CS1020"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("EC3551"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("FIN3103A"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("ACC1701X"));
        csbl.createModule("2016", "1", "none", "none", csbl.findCourse("MS3212"));
        //modules for 2016, sem 2
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CS2100"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("IE1111"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("PF3302"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("MA2101"));
        csbl.createModule("2016", "2", "none", "none", csbl.findCourse("CL3551"));
        
        //modules for 2017, sem 1
        csbl.createModule("2017", "1", "none", "none", csbl.findCourse("GER1000"));
        csbl.createModule("2017", "1", "none", "none", csbl.findCourse("RE2102"));
        csbl.createModule("2017", "1", "none", "none", csbl.findCourse("MLE2101"));
        csbl.createModule("2017", "1", "none", "none", csbl.findCourse("NM4227"));
        csbl.createModule("2017", "1", "none", "none", csbl.findCourse("LAL1201"));
        
        //modules for 2017, sem 2
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PS2240"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("PC2193"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("SC3101"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("LAM3201"));
        csbl.createModule("2017", "2", "none", "none", csbl.findCourse("TIE2010"));
        //modules for 2018, sem 1
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("ST2334"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("BT4212"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("CM1501"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("EE4101"));
        csbl.createModule("2018", "1", "none", "none", csbl.findCourse("IT1007"));
        //create lesson
        //csbl.createLeson("Monday", null, null, null, null);
        
    }
}
