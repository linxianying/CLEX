/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;


import entity.Lecturer;
import entity.Module;
import entity.Poll;
import entity.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import static org.primefaces.component.focus.Focus.PropertyKeys.context;
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
@Named(value = "classroomBean")
@RequestScoped

public class ClassroomBean {
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
    private ArrayList<Poll> polls ;
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
        lecturerEntity = (Lecturer) session.getAttribute("user");
        username = lecturerEntity.getUsername();
        System.out.println("Lecturer Name: " + username);
        if(lecturerEntity!=null){
            modules = crsbl.viewModules(lecturerEntity);
            polls = crsbl.viewPolls(lecturerEntity);
        }
        createBarModel();
        System.out.println("ClassroomBean: initial finished");

        //modules = cmbl.getModulesFromLecturer(username);
    }
    
    public void testViewPolls(){
        polls = crsbl.testViewPolls();
    }
    
    public void viewModules(){
        //lecturerEntity = csbl.findLecturer("lecturer");
        lecturerEntity = csbl.findLecturer(username);
        if(lecturerEntity!=null)
            modules = crsbl.viewModules(lecturerEntity);
    }
    
    public void generateReport(ActionEvent event){
        
        try{
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/CLEX-war/jasperreport/cherry.jpg");

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/pollsreport.jasper");                
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, clexDataSource.getConnection());
        }
        catch(Exception ex){
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
        }
        else {
            addErrorMsg = null;
            this.addButton = true;
        }
    }

    public void addPoll(){
        fmsg = new FacesMessage();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Poll p = crsbl.createPoll(addModuleCode, addPickYear, addPickSem, dateFormat.format(date), 
                addPollTopic, Double.parseDouble(addPollCorrectRate), addPollType, addPollContent);
        //System.out.println("addPollType:"+addPollType+"//////////////////////Topic:"+addPollTopic);
        if(p==null){
            System.out.println("ClassroomBean: Create Poll failed ");
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Poll creation failed. Please check the module details", "Unsuccessfuly");
            context.addMessage(null, fmsg);
        }else{
            System.out.println("ClassroomBean: New Poll id is "+p.getId());
            fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Poll '" + p.getId() + " is created.", "Successfuly");
            context.addMessage(null, fmsg);
        }
        
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
        
        barModel.setTitle("Poll AnalysisT By Topic");
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
 
        if(!polls.isEmpty()){
            ChartSeries p = new ChartSeries();
            p.setLabel("PollsByTopic");
            for(Poll pl : polls){
                p.set(""+pl.getTopic(), crsbl.getCorrectRateByTopic(polls, pl.getTopic()));
            }        

            model.addSeries(p);
         
            return model;
        }
        return null;
    }

    private BarChartModel initPollBarModelByType() {
        BarChartModel model = new BarChartModel();
 
        if(!polls.isEmpty()){
                 
            ChartSeries p1 = new ChartSeries();
            p1.setLabel("PollsByType");
            for(Poll pl : polls){
                p1.set(""+pl.getType(), crsbl.getCorrectRateByType(polls, pl.getType()));
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
    
    
}
