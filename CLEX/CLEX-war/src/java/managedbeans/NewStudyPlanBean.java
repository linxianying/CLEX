/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package managedbeans;

import entity.Course;
import entity.Grade;
import entity.Module;
import entity.Student;
import entity.StudyPlan;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UISelectItem;
import org.primefaces.behavior.ajax.AjaxBehavior;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import session.ClexSessionBeanLocal;
import session.CourseMgmtBeanLocal;
import session.StudyPlanSessionBeanLocal;

/**
 *
 * @author caoyu
 */
@ManagedBean(name = "newStudyPlanBean")
@SessionScoped
public class NewStudyPlanBean implements Serializable {
    
    public NewStudyPlanBean() {
    }
    
    @EJB
    private StudyPlanSessionBeanLocal spsbl;
    @EJB
    private ClexSessionBeanLocal csbl;
    @EJB
            CourseMgmtBeanLocal cmbl;
    
    FacesContext context;
    HttpSession session;
    
    private Student student;
    private String username;
    private double cap;
    private double expectedCap;
    private ArrayList<Grade> grades;
    private ArrayList<Module> takingModules;
    private ArrayList<StudyPlan> studyPlans;
    private int currentYear;
    private int currentSem;
    // the column index of current semester
    private int currentColumnIndex;
    private int matricYear;
    private int matricSem;
    
    private int newYear;
    private int newSem;
    
    private Dashboard dashboard;
    Application application;
    private DashboardModel model;
    
    //for add new item
    private String addItem;
    private String addModuleCode;
    private int addPickYear;
    private int addPickSem;
    private String addErrorMsg;
    private boolean addButton;
    private List<Course> courses;
    
    //for edit grade
    private double newGrade;
    
    //for rendering the info after the student select the module
    Course courseFront;
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        application = context.getApplication();
        session = (HttpSession) context.getExternalContext().getSession(true);
        student = (Student) session.getAttribute("user");
        username = student.getUsername();
        this.setYearSem();
        courses = spsbl.getAllCourses();
        
        cap = student.getCap();
        
        if (student.getGrades().size() > 0) {
            grades = spsbl.getAllGrades(student);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!# of taken Moduless: " + grades.size());
        }
        
        takingModules = spsbl.getCurrentModules(student);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!# of taking Moduless: " + takingModules.size());
        
        if (student.getStudyPlan() != null) {
            studyPlans = spsbl.getAllStudyPlans(student);
        }
        
        newGrade = -1;
        
        dashboard = (Dashboard) application.createComponent(context, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
        dashboard.setId("spDashboard");
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
        DashboardColumn column4 = new DefaultDashboardColumn();
        DashboardColumn column5 = new DefaultDashboardColumn();
        DashboardColumn column6 = new DefaultDashboardColumn();
        DashboardColumn column7 = new DefaultDashboardColumn();
        DashboardColumn column8 = new DefaultDashboardColumn();
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
        model.addColumn(column4);
        model.addColumn(column5);
        model.addColumn(column6);
        model.addColumn(column7);
        model.addColumn(column8);
        
        dashboard.setModel(model);
        
        int yearSem = 0;
        column1.addWidget("y11");
        column2.addWidget("y12");
        column3.addWidget("y21");
        column4.addWidget("y22");
        column5.addWidget("y31");
        column6.addWidget("y32");
        column7.addWidget("y41");
        column8.addWidget("y42");
        
        final UISelectItem item0 = new UISelectItem();
        item0.setItemLabel("select");
        item0.setItemValue("select");
        final UISelectItem item1 = new UISelectItem();
        item1.setItemLabel("A+");
        item1.setItemValue("A+");
        final UISelectItem item2 = new UISelectItem();
        item2.setItemLabel("A");
        item2.setItemValue("A");
        final UISelectItem item3 = new UISelectItem();
        item3.setItemLabel("A-");
        item3.setItemValue("A-");
        final UISelectItem item4 = new UISelectItem();
        item4.setItemLabel("B+");
        item4.setItemValue("B+");
        final UISelectItem item5 = new UISelectItem();
        item5.setItemLabel("B");
        item5.setItemValue("B");
        final UISelectItem item6 = new UISelectItem();
        item6.setItemLabel("B-");
        item6.setItemValue("B-");
        final UISelectItem item7 = new UISelectItem();
        item7.setItemLabel("C+");
        item7.setItemValue("C+");
        final UISelectItem item8 = new UISelectItem();
        item8.setItemLabel("C");
        item8.setItemValue("C");
        final UISelectItem item9 = new UISelectItem();
        item9.setItemLabel("D+");
        item9.setItemValue("D+");
        final UISelectItem item10 = new UISelectItem();
        item10.setItemLabel("D");
        item10.setItemValue("D");
        final UISelectItem item11 = new UISelectItem();
        item11.setItemLabel("F");
        item11.setItemValue("F");
        final UISelectItem item12 = new UISelectItem();
        item12.setItemLabel("S");
        item12.setItemValue("S");
        final UISelectItem item13 = new UISelectItem();
        item13.setItemLabel("U");
        item13.setItemValue("U");
            
        for (Grade g : grades) {
            Panel panel = (Panel) application.createComponent(context, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
            panel.setId("g" + g.getId());
            panel.setHeader(g.getModule().getCourse().getModuleCode());
            panel.setClosable(true);
            panel.setToggleable(false);
            panel.setStyle("width:138px");
            dashboard.getChildren().add(panel);
            
            yearSem = (Integer.parseInt(g.getModule().getTakenYear())-matricYear)*2+Integer.parseInt(g.getModule().getTakenSem());
            //column1.addWidget(panel.getId());
            switch(yearSem){
                case 1:
                    column1.addWidget(panel.getId());
                    break;
                case 2:
                    column2.addWidget(panel.getId());
                    break;
                case 3:
                    column3.addWidget(panel.getId());
                    break;
                case 4:
                    column4.addWidget(panel.getId());
                    break;
                case 5:
                    column5.addWidget(panel.getId());
                    break;
                case 6:
                    column6.addWidget(panel.getId());
                    break;
                case 7:
                    column7.addWidget(panel.getId());
                    break;
                case 8:
                    column8.addWidget(panel.getId());
                    break;
                default:
                    column8.addWidget(panel.getId());
                    break;
            }
            HtmlOutputText text = new HtmlOutputText();
            //text.setId("gt"+g.getId());
            text.setValue("Taken Module\n");
            panel.getChildren().add(text);
            HtmlOutputText t1 = new HtmlOutputText();
            t1.setValue("Grade " + g.getModuleGrade());
            panel.getChildren().add(t1);
        }
        
        for (Module m : takingModules) {
            Panel panel = (Panel) application.createComponent(context, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
            panel.setId("m" + m.getId());
            panel.setHeader(m.getCourse().getModuleCode());
            panel.setClosable(true);
            panel.setToggleable(false);
            panel.setStyle("width:138px");
            dashboard.getChildren().add(panel);
            
            yearSem = (currentYear-matricYear)*2+currentSem;
            //column1.addWidget(panel.getId());
            switch(yearSem){
                case 1:
                    column1.addWidget(panel.getId());
                    break;
                case 2:
                    column2.addWidget(panel.getId());
                    break;
                case 3:
                    column3.addWidget(panel.getId());
                    break;
                case 4:
                    column4.addWidget(panel.getId());
                    break;
                case 5:
                    column5.addWidget(panel.getId());
                    break;
                case 6:
                    column6.addWidget(panel.getId());
                    break;
                case 7:
                    column7.addWidget(panel.getId());
                    break;
                case 8:
                    column8.addWidget(panel.getId());
                    break;
                default:
                    column8.addWidget(panel.getId());
                    break;
            }
            HtmlOutputText text = new HtmlOutputText();
            text.setValue("Taking Module\n");
            panel.getChildren().add(text);
        }
        
        for (StudyPlan sp : studyPlans) {
            Panel panel = (Panel) application.createComponent(context, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
            panel.setId("sp" + sp.getId());
            panel.setHeader(sp.getCourse().getModuleCode());
            panel.setClosable(true);
            panel.setToggleable(false);
            panel.setStyle("width:138px");
            dashboard.getChildren().add(panel);
            
            yearSem = (Integer.parseInt(sp.getPickYear())-matricYear)*2+Integer.parseInt(sp.getPickSem());
            //column1.addWidget(panel.getId());
            switch(yearSem){
                case 1:
                    column1.addWidget(panel.getId());
                    break;
                case 2:
                    column2.addWidget(panel.getId());
                    break;
                case 3:
                    column3.addWidget(panel.getId());
                    break;
                case 4:
                    column4.addWidget(panel.getId());
                    break;
                case 5:
                    column5.addWidget(panel.getId());
                    break;
                case 6:
                    column6.addWidget(panel.getId());
                    break;
                case 7:
                    column7.addWidget(panel.getId());
                    break;
                case 8:
                    column8.addWidget(panel.getId());
                    break;
                default:
                    column8.addWidget(panel.getId());
                    break;
            }
            HtmlOutputText text = new HtmlOutputText();
            text.setId("spt"+sp.getId());
            text.setValue("Study Plan\n");
            panel.getChildren().add(text);
//            System.out.println("-------check count"+column1.getWidgetCount());
        }
        
        System.out.println("newStudyPlanBean finish ");
    }
    
    public void setYearSem(){
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
        matricYear = Integer.parseInt(student.getMatricYear());
        //for test purpose
        //matricYear = 2015;
        currentColumnIndex = (currentYear-matricYear)*2+currentSem - 1;
    }
    
    public void onTabChange(TabChangeEvent event){
        if (event.getTab().getId().equals("overviewTab")) {
            this.init();
            System.out.println("NewStudyPlanBean:onTabChange: studyPlans size " + studyPlans.size());
        }
    }
    
    public void handleReorder(DashboardReorderEvent event) {
        System.out.println("handel reorder");
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        //message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Reordered: " + event.getWidgetId() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());
        addMessage(message);
        String moduleCode;
        if (event.getSenderColumnIndex() != null) {
            String id = event.getWidgetId();
            this.setNewYearSem(event.getColumnIndex());
            Long updateId;
            if (id.startsWith("g")) {
                updateId = Long.parseLong(id.substring(1));
                this.reorderGrade(updateId);
            }
            else if (id.startsWith("m")) {
                //cannot reorder taking module must be this sem
                message.setDetail ("You are dragging the module currently taking to other semester!");
                addMessage(message);
            }
            else if (id.startsWith("sp")) {
                //setting a study plan for this or previous semester
                if (event.getColumnIndex() <= this.currentColumnIndex) {
                    message.setDetail ("You are dragging the Study Plan for previous semester!");
                    addMessage(message);
                }
                else {
                    id = id.substring(2);
                    updateId = Long.parseLong(id);
                    this.reorderStudyPlan(updateId);
                }
            }
        }
        init();
    }
    
    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");
        
        addMessage(message);
    }
    
    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());
        
        addMessage(message);
    }
    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void setNewYearSem(int sendeeColumnIndex) {
        newSem = sendeeColumnIndex % 2 + 1;
        if (newSem == 1)
            newYear = sendeeColumnIndex/2 + matricYear;
        else if (newSem == 2)
            newYear = (sendeeColumnIndex-1)/2 + matricYear;
        System.out.println("newSPbean: setNewYearSem: change to year " + newYear + ",sem " + newSem);
    }
    
    public void reorderGrade(Long id) {
        Module m = spsbl.findGrade(id).getModule();
        spsbl.updateGradeYearSem(id, m.getCourse().getModuleCode(), newYear, newSem);
    }
    
//    public void reorderModule() {
//        
//    }
    
    public void reorderStudyPlan(Long id) {
        spsbl.updateStudyPlanYearSem(id, newYear, newSem);
    }
    
    public void check(){
//        System.out.println("into check");
//        FacesMessage message = new FacesMessage();
//        message.setSeverity(FacesMessage.SEVERITY_INFO);
//        message.setDetail ("Check!");
//        addMessage(message);
    }
    
    public void addNewItem() {
        System.out.println("start add");
        if (validattion()) {
            if (addItem.equals("Taken module")) {
                this.addTakenModule();
            }
            else if (addItem.equals("Taking module")) {
                this.addTakingModule();
            }
            else if (addItem.equals("Study Plan")) {
                this.addStudyPlan();
            }
        }
    }
    
    public boolean validattion() {
        boolean validate = true;
        FacesMessage fmsg = new FacesMessage();
        if (addItem.equals("Taken module")) {
            if (!(addPickYear < currentYear || (addPickYear == currentYear && addPickSem < currentSem))) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fail to add", "You cannot add a module taken before to a semester in future");
            context.addMessage(null, fmsg);
            validate = false;
            }
        }
        else if (addItem.equals("Taking module")) {
            if (addPickYear != currentYear ||  addPickSem != currentSem) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fail to add", "You cannot add a currently taking module to other semester");
            context.addMessage(null, fmsg);
            validate = false;
            }
        }
        else if (addItem.equals("Study Plan")) {
            if (!(addPickYear > currentYear || (addPickYear == currentYear && addPickSem > currentSem))) {
                fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fail to add", "You cannot add a study plan to a semester before");
            context.addMessage(null, fmsg);
            validate = false;
            }
        }
        if (addModuleCode.equals("select")) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No module selected", "Please select a module");
            context.addMessage(null, fmsg);
            validate = false;
        }
        else if (spsbl.checkStudyPlan(username, addModuleCode)) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The module " + addModuleCode + " already in your study plan", "Please change to another module");
            context.addMessage(null, fmsg);
            validate = false;
        } 
        //this course already in takenCourses list
        else if (spsbl.checkStudentModule(username, addModuleCode)) {
            fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You have already taken " + addModuleCode, "Please change to another module");
            context.addMessage(null, fmsg);
            validate = false;
        } 
        else {
            fmsg = null;
        }
        return validate;
    }
    
    public void addStudyPlan() {
        context = FacesContext.getCurrentInstance();
        spsbl.createStudyPlan(Integer.toString(addPickYear), Integer.toString(addPickSem), addModuleCode, csbl.findStudent(username));
        init();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Successful", "You have added module " + addModuleCode);
        context.addMessage(null, fmsg);
    }
    
    public void addTakenModule() {
        context = FacesContext.getCurrentInstance();
        spsbl.addTakenModule(Integer.toString(addPickYear), Integer.toString(addPickSem), addModuleCode, csbl.findStudent(username));
        init();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Successful", "You have added module " + addModuleCode);
        context.addMessage(null, fmsg);
    }
    
    public void addTakingModule() {
        context = FacesContext.getCurrentInstance();
        spsbl.addTakingModule(Integer.toString(addPickYear), Integer.toString(addPickSem), addModuleCode, csbl.findStudent(username));
        init();
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Successful", "You have added module " + addModuleCode);
        context.addMessage(null, fmsg);
    }
    
    public void editGrade() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Edit Grade");
    }
    
    public StudyPlanSessionBeanLocal getSpsbl() {
        return spsbl;
    }
    
    public void setSpsbl(StudyPlanSessionBeanLocal spsbl) {
        this.spsbl = spsbl;
    }
    
    public ClexSessionBeanLocal getCsbl() {
        return csbl;
    }
    
    public void setCsbl(ClexSessionBeanLocal csbl) {
        this.csbl = csbl;
    }
    
    public CourseMgmtBeanLocal getCmbl() {
        return cmbl;
    }
    
    public void setCmbl(CourseMgmtBeanLocal cmbl) {
        this.cmbl = cmbl;
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
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public double getCap() {
        return cap;
    }
    
    public void setCap(double cap) {
        this.cap = cap;
    }
    
    public double getExpectedCap() {
        return expectedCap;
    }
    
    public void setExpectedCap(double expectedCap) {
        this.expectedCap = expectedCap;
    }
    
    public ArrayList<Grade> getGrades() {
        return grades;
    }
    
    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }
    
    public ArrayList<Module> getTakingModules() {
        return takingModules;
    }
    
    public void setTakingModules(ArrayList<Module> takingModules) {
        this.takingModules = takingModules;
    }
    
    public ArrayList<StudyPlan> getStudyPlans() {
        return studyPlans;
    }
    
    public void setStudyPlans(ArrayList<StudyPlan> studyPlans) {
        this.studyPlans = studyPlans;
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
    
    public int getCurrentColumnIndex() {
        return currentColumnIndex;
    }
    
    public void setCurrentColumnIndex(int currentColumnIndex) {
        this.currentColumnIndex = currentColumnIndex;
    }
    
    public int getMatricYear() {
        return matricYear;
    }
    
    public void setMatricYear(int matricYear) {
        this.matricYear = matricYear;
    }
    
    public int getMatricSem() {
        return matricSem;
    }
    
    public void setMatricSem(int matricSem) {
        this.matricSem = matricSem;
    }
    
    public int getNewYear() {
        return newYear;
    }
    
    public void setNewYear(int newYear) {
        this.newYear = newYear;
    }
    
    public int getNewSem() {
        return newSem;
    }
    
    public void setNewSem(int newSem) {
        this.newSem = newSem;
    }
    
    public Dashboard getDashboard() {
        return dashboard;
    }
    
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    public Application getApplication() {
        return application;
    }
    
    public void setApplication(Application application) {
        this.application = application;
    }
    
    public DashboardModel getModel() {
        return model;
    }
    
    public void setModel(DashboardModel model) {
        this.model = model;
    }
    
    public String getAddItem() {
        return addItem;
    }
    
    public void setAddItem(String addItem) {
        this.addItem = addItem;
    }
    
    public String getAddModuleCode() {
        return addModuleCode;
    }
    
    public void setAddModuleCode(String addModuleCode) {
        this.addModuleCode = addModuleCode;
    }

    public int getAddPickYear() {
        return addPickYear;
    }

    public void setAddPickYear(int addPickYear) {
        this.addPickYear = addPickYear;
    }

    public int getAddPickSem() {
        return addPickSem;
    }

    public void setAddPickSem(int addPickSem) {
        this.addPickSem = addPickSem;
    }
    
    
    public String getAddErrorMsg() {
        return addErrorMsg;
    }
    
    public void setAddErrorMsg(String addErrorMsg) {
        this.addErrorMsg = addErrorMsg;
    }
    
    public boolean isAddButton() {
        return addButton;
    }
    
    public void setAddButton(boolean addButton) {
        this.addButton = addButton;
    }
    
    public List<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Course getCourseFront() {
        return courseFront;
    }

    public void setCourseFront(Course courseFront) {
        this.courseFront = courseFront;
    }

    public double getNewGrade() {
        return newGrade;
    }

    public void setNewGrade(double newGrade) {
        this.newGrade = newGrade;
    }
    
    
    
    
}


