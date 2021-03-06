/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entity.Answer;
import entity.Course;
import entity.Lecturer;
import entity.Module;
import entity.Poll;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import session.ClassroomSessionBeanLocal;
import session.ClexSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.StudyPlanSessionBeanLocal;

/**
 *
 * @author lin
 */
//@Named(value = "classroomBean")
@ManagedBean
@RequestScoped

public class ClassroomBean  { //implements Serializable

    @Resource(name = "clexDataSource")
    private DataSource clexDataSource;

    /**
     * Creates a new instance of ClassroomBean
     */
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
    private ClassroomSessionBeanLocal crsbl;
    @EJB
    private CourseMgmtBeanLocal cmbl;
    @EJB
    private StudyPlanSessionBeanLocal cpsbl;

    private Lecturer lecturerEntity;
    private String username;
    private String password;
    private String userType;
    private ArrayList<Poll> polls;
    private ArrayList<Poll> filteredPolls;
    private ArrayList<Module> modules;

    private String addPollType;
    private String addPollTopic;
    private String addPollContent;
    private String addPollCorrectRate;
    private String addModuleCode;
    private String addErrorMsg;
    private String addPickYear;
    private String addPickSem;
    private boolean addButton;
    private boolean finishedOrNot;
    private String finish;
    private int currentYear;
    private int currentSem;
    private ArrayList<Answer> ans = new ArrayList<Answer>();

    private ArrayList<Answer> anslist = new ArrayList<Answer>();
    private ArrayList<String> answers = new ArrayList<String>();

    private ArrayList<Module> currentModules;
    int num;
    private int total;
    private int rightAns;
    private boolean[] str = new boolean[20];
    private List<Course> courses;
    private String answer;

    FacesContext context;
    HttpSession session;
    FacesMessage fmsg;

    private BarChartModel barModel;
    //private PieChartModel pieModel1;
    private BarChartModel barModelByType;

    public ClassroomBean() {
    }

    @PostConstruct
    public void init() {
        addModuleCode = null;
        addPickYear = null;
        addPickSem = null;
        addErrorMsg = null;
        addButton = true;
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        lecturerEntity = csbl.findLecturer(username);
        //System.out.println("Lecturer Name: " + username);
        this.setCurrentYearSem();
        if (lecturerEntity != null) {
            modules = crsbl.viewModules(lecturerEntity);
            polls = crsbl.viewPolls(lecturerEntity);
            currentModules = cmbl.getCurrentModulesFromLecturer(username, Integer.toString(currentYear), Integer.toString(currentSem));

        }
        createBarModel();
        num = 1;
        anslist = makeAnsList();
        if (!ans.isEmpty()) {
            System.out.println("ans " + ans.size());
        } else {
            System.out.println("ans empty: " + ans.size());
        }
        System.out.println("anslist " + anslist.size());
        System.out.println("ClassroomBean: initial finished");

        //modules = cmbl.getModulesFromLecturer(username);
    }

    public void removePoll(Poll p) {
        setCurrentYearSem();

        boolean deletePoll = crsbl.removePoll(p.getModule().getCourse().getModuleCode(),
                p.getModule().getTakenYear(), p.getModule().getTakenSem(), p.getId());
        System.out.println("delete poll is " + deletePoll);
        if (deletePoll) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Poll '" + p.getId() + " is deleted.", "Successfuly");
            context.addMessage(null, fmsg);
        } else {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Poll '" + p.getId() + " is not deleted.", "Unsuccessfuly");
            context.addMessage(null, fmsg);
        }
        refresh();
    }

    public void setCurrentYearSem() {
        Calendar now = Calendar.getInstance();
        currentYear = now.get(Calendar.YEAR);
        // month starts from 0 to 11
        int currentMonth = now.get(Calendar.MONTH);
        if (currentMonth < 6) {
            currentSem = 2;
            currentYear--;
        } else {
            currentSem = 1;
        }
    }

    public void testViewPolls() {
        polls = crsbl.testViewPolls();
    }

    public void viewModules() {
        //lecturerEntity = csbl.findLecturer("lecturer");
        lecturerEntity = csbl.findLecturer(username);
        if (lecturerEntity != null) {
            modules = crsbl.viewModules(lecturerEntity);
        }
    }

    public void generateReport(ActionEvent event) {

        try {
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/CLEX-war/jasperreport/cherry.jpg");

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/pollsreport.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, clexDataSource.getConnection());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void checkModule() {

        if (cpsbl.findCourse(addModuleCode) == null) {
            addErrorMsg = "Course " + addModuleCode + " does not exist";
            this.addButton = false;
            //}else if(crsbl.findModule(addModuleCode, addPickYear, addPickSem) == null){
            //    addErrorMsg = "Module " + addModuleCode + "AY" + addPickYear + " Sem" + addPickSem + " does not exist";
            //    this.addButton = false;
        } else {
            addErrorMsg = null;
            this.addButton = true;
        }
    }

    public void addPoll() {
        fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Poll p = crsbl.createPoll(addModuleCode, currentYear + "", currentSem + "", dateFormat.format(date),
                addPollTopic, Double.parseDouble(addPollCorrectRate), addPollType, addPollContent);
        //System.out.println("addPollType:"+addPollType+"//////////////////////Topic:"+addPollTopic);
        if (p == null) {
            System.out.println("ClassroomBean: Create Poll failed ");
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Poll creation failed. Please check the module details", "Unsuccessfuly");
            context.addMessage(null, fmsg);
        } else {
            System.out.println("ClassroomBean: New Poll id is " + p.getId());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Poll '" + p.getId() + " is created.", "Successfuly");
            context.addMessage(null, fmsg);
        }
        refresh();

    }

    public void addUnfinishedPoll() {
//        for (int i = 0; i < answers.size(); i++) {
//            ans.add(crsbl.createAnswer(answers.get(i)));
//        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Poll p = crsbl.createUnfinishedPoll(addModuleCode, currentYear + "", currentSem + "", dateFormat.format(date),
                addPollTopic, 0.0, addPollType, addPollContent, ans, 0);
//        System.out.println("addType:" + addPollType + "//////////////////////ans:" + ans.get(0));
        if (p == null) {
            System.out.println("ClassroomBean: Create Poll failed ");
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Poll creation failed. Please check the module details", "Unsuccessfuly");
            context.addMessage(null, fmsg);
        } else {
            System.out.println("ClassroomBean: New Poll id is " + p.getId());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Poll '" + p.getId() + " is created.", "Successfuly");
            context.addMessage(null, fmsg);
            refresh();
        }
    }

    public void addAnswer() {
        // adding answer to arraylist<String> then loop thru in addUnfinishedPoll to add to ans arraylist<Answer>
//        System.out.println("adding answer: " + answer);
//        System.out.println("before answers size " + answers.size());
//        answers.add(answer);
//        System.out.println("after answers size " + answers.size());

        // use answerentity and add directly to the ans arraylist<Answer>
        Answer answerEntity = crsbl.createAnswer(answer);
        System.out.println("adding answer: " + answerEntity.getAnswer());
//        System.out.println("before ans size " + ans.size());
        ans.add(crsbl.createAnswer(answer));
        System.out.println("after ans size " + ans.size());
//        for(int i=0;i<ans.size();i++) {
//            System.out.println("ans " + i + ". " + ans.get(i).getAnswer());
//        }

    }

    public ArrayList<Answer> makeAnsList() {
        int temp = 0;

        ArrayList<Answer> templist = new ArrayList<Answer>();
        Answer answerEntity;
        for (int i = 0; i < num; i++) {
            answerEntity = crsbl.createAnswer(String.valueOf(temp));
            templist.add(answerEntity);
            temp++;
        }
        return templist;

    }

    public void stopPoll(Poll p) {
        crsbl.endPoll(p);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Poll is finished",
                "Please check in the classroom ");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void createBarModel() {
        barModel = initPollBarModel();
        barModelByType = initPollBarModelByType();

        barModelByType.setTitle("Poll Analysis By Type");
        barModelByType.setLegendPosition("ne");

        barModel.setTitle("Poll Analysis By Topic");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Poll Topic");

        Axis xAxis1 = barModelByType.getAxis(AxisType.X);
        xAxis1.setLabel("Poll Type");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Correct Rate");

        Axis yAxis1 = barModelByType.getAxis(AxisType.Y);
        yAxis1.setLabel("Correct Rate");

        yAxis.setMin(0);
        yAxis.setMax(1);

        yAxis1.setMin(0);
        yAxis1.setMax(1);
    }

    private BarChartModel initPollBarModel() {
        BarChartModel model = new BarChartModel();

        if (!polls.isEmpty()) {
            ChartSeries p = new ChartSeries();
            p.setLabel("PollsByTopic");
            for (Poll pl : polls) {
                p.set("" + pl.getTopic(), crsbl.getCorrectRateByTopic(polls, pl.getTopic()));
            }

            model.addSeries(p);

            return model;
        }
        return null;
    }

    private BarChartModel initPollBarModelByType() {
        BarChartModel model = new BarChartModel();

        if (!polls.isEmpty()) {

            ChartSeries p1 = new ChartSeries();
            p1.setLabel("PollsByType");
            for (Poll pl : polls) {
                p1.set("" + pl.getType(), crsbl.getCorrectRateByType(polls, pl.getType()));
            }
            model.addSeries(p1);

            return model;
        }
        return null;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public DataSource getClexDataSource() {
        return clexDataSource;
    }

    public void setClexDataSource(DataSource clexDataSource) {
        this.clexDataSource = clexDataSource;
    }

    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }

    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }

    public ClassroomSessionBeanLocal getCrsbl() {
        return crsbl;
    }

    public void setCrsbl(ClassroomSessionBeanLocal crsbl) {
        this.crsbl = crsbl;
    }

    public Lecturer getLecturerEntity() {
        return lecturerEntity;
    }

    public void setLecturerEntity(Lecturer lecturerEntity) {
        this.lecturerEntity = lecturerEntity;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ArrayList<Poll> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Poll> polls) {
        this.polls = polls;
    }

    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }

    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
    }

    public String getAddPollType() {
        return addPollType;
    }

    public void setAddPollType(String addPollType) {
        this.addPollType = addPollType;
    }

    public String getAddPollTopic() {
        return addPollTopic;
    }

    public void setAddPollTopic(String addPollTopic) {
        this.addPollTopic = addPollTopic;
    }

    public String getAddPollContent() {
        return addPollContent;
    }

    public void setAddPollContent(String addPollContent) {
        this.addPollContent = addPollContent;
    }

    public String getAddPollCorrectRate() {
        return addPollCorrectRate;
    }

    public void setAddPollCorrectRate(String addPollCorrectRate) {
        this.addPollCorrectRate = addPollCorrectRate;
    }

    public StudyPlanSessionBeanLocal getCpsbl() {
        return cpsbl;
    }

    public void setCpsbl(StudyPlanSessionBeanLocal cpsbl) {
        this.cpsbl = cpsbl;
    }

    public String getAddModuleCode() {
        return addModuleCode;
    }

    public void setAddModuleCode(String addModuleCode) {
        this.addModuleCode = addModuleCode;
    }

    public boolean isFinishedOrNot() {
        return finishedOrNot;
    }

    public void setFinishedOrNot(boolean finishedOrNot) {
        this.finishedOrNot = finishedOrNot;
    }

    public FacesMessage getFmsg() {
        return fmsg;
    }

    public void setFmsg(FacesMessage fmsg) {
        this.fmsg = fmsg;
    }

    public String getAddErrorMsg() {
        return addErrorMsg;
    }

    public void setAddErrorMsg(String addErrorMsg) {
        this.addErrorMsg = addErrorMsg;
    }

    public String getAddPickYear() {
        return addPickYear;
    }

    public void setAddPickYear(String addPickYear) {
        this.addPickYear = addPickYear;
    }

    public String getAddPickSem() {
        return addPickSem;
    }

    public void setAddPickSem(String addPickSem) {
        this.addPickSem = addPickSem;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public boolean isAddButton() {
        return addButton;
    }

    public void setAddButton(boolean addButton) {
        this.addButton = addButton;
    }

    public ArrayList<Poll> getFilteredPolls() {
        return filteredPolls;
    }

    public void setFilteredPolls(ArrayList<Poll> filteredPolls) {
        this.filteredPolls = filteredPolls;
    }

    public BarChartModel getBarModelByType() {
        return barModelByType;
    }

    public void setBarModelByType(BarChartModel barModelByType) {
        this.barModelByType = barModelByType;
    }

    public ArrayList<Answer> getAns() {
        return ans;
    }

    public void setAns(ArrayList<Answer> ans) {
        this.ans = ans;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRightAns() {
        return rightAns;
    }

    public void setRightAns(int rightAns) {
        this.rightAns = rightAns;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        System.out.println("num is set to " + num);
//        ans = null;
        anslist = null;
        anslist = makeAnsList();
//        ans = makeAnsList();
//        ArrayList<Answer> anslist = new ArrayList<Answer>(num);
//        this.setAns(anslist);
//        System.out.println("ans list size" + this.ans.size());
//        if(ans.size()<num){
//            for(int i = 0;i<num; i++){
//                Answer answer = new Answer();
//                answer.createAnswer("");
//                ans.add(answer);
//            }
//        }
    }
//    public void setNum(String number) {
//        this.num = Integer.parseInt(number);
//        System.out.println("num is set to "+num);
//        if(ans.size()<num){
//            for(int i = 0;i<num; i++){
//                Answer answer = new Answer();
//                answer.createAnswer("");
//                ans.add(answer);
//            }
//        }
//    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void addMessage() {
        String summary = finishedOrNot ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    public boolean[] getStr() {
        return str;
    }

    public void setStr(boolean[] str) {
        this.str = str;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentSem() {
        return currentSem;
    }

    public void setCurrentSem(int currentSem) {
        this.currentSem = currentSem;
    }

    public ArrayList<Module> getCurrentModules() {
        return currentModules;
    }

    public void setCurrentModules(ArrayList<Module> currentModules) {
        this.currentModules = currentModules;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Answer> getAnslist() {
        return anslist;
    }

    public void setAnslist(ArrayList<Answer> anslist) {
        this.anslist = anslist;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public void refresh() {
        addModuleCode = null;
        addPickYear = null;
        addPickSem = null;
        addErrorMsg = null;
        addButton = true;
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        username = (String) session.getAttribute("username");
        lecturerEntity = csbl.findLecturer(username);
        //System.out.println("Lecturer Name: " + username);
        this.setCurrentYearSem();
        if (lecturerEntity != null) {
            modules = crsbl.viewModules(lecturerEntity);
            polls = crsbl.viewPolls(lecturerEntity);
            currentModules = cmbl.getCurrentModulesFromLecturer(username, Integer.toString(currentYear), Integer.toString(currentSem));

        }
        createBarModel();
        System.out.println("ClassroomBean: reinititated");

        //modules = cmbl.getModulesFromLecturer(username);
    }

}
