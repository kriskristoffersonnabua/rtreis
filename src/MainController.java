import com.sun.scenario.effect.Brightpass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import models.Child;
import models.CivilStatus;
import models.DailyAttendance;
import models.Department;
import models.EducationBackground;
import models.Employee;
import models.EmploymentStatus;
import models.LeaveCredits;
import sun.security.jca.GetInstance;

public class MainController implements Initializable{
    
    // stage for new windows
    public static Stage stage;
    private static MainController instance;
    
    @FXML public Button add_employee;
    
    // OVERVIEW TABLE
    @FXML public TableView<Employee> overviewTable;
    @FXML public TableColumn<Employee, Integer> biometiricIdColumn;
    @FXML public TableColumn<Employee, String> empName;
    @FXML public TableColumn<Employee, String> givenName;
    ObservableList<Employee> overviewTableData = FXCollections.observableArrayList();
    
    // INFO OVERVIEW
    @FXML public ImageView employeeimage;
    @FXML public Label nameofemployee;
    @FXML public Label departmentAssignment;
    @FXML public Label employmentStatus;
    @FXML public TextField position;
    @FXML public TextField birthday;
    @FXML public TextField gender;
    @FXML public TextField sss;
    @FXML public TextField tin;
    @FXML public TextField pagibig;
    @FXML public TextField philhealth;
    @FXML public TextArea permanentAddress;
    @FXML public TextArea currentAddress;
    @FXML public TextArea emailAddress;
    @FXML public TextArea mobileNumber;
    @FXML public TextField primary;
    @FXML public TextField primaryaddress;
    @FXML public TextField primarygraduated;
    @FXML public TextField secondary;
    @FXML public TextField secondaryaddress;
    @FXML public TextField secondarygraduated;
    @FXML public TextField tertiary;
    @FXML public TextField tertiaryaddress;
    @FXML public TextField tertiarygraduated;
    @FXML public TextArea others;
    @FXML public TextField sickleave;
    @FXML public TextField vacationleave;
    @FXML public TextField birthdayleave;
    @FXML public TextField maternityleave;
    @FXML public TextField parentalleave;
    @FXML public TextField specialleave;
    @FXML public TextField vawcleave;
    @FXML public TextField paternityleave;
    @FXML public TextField civilstatus;
    @FXML public TextField spousename;
    @FXML public ProgressBar mainbar;
    @FXML public TableView<Child> childtable;
    @FXML public TableColumn<Child, String> childname;
    @FXML public TableColumn<Child, Date> childbday;
    ObservableList<Child> childTableDate = FXCollections.observableArrayList();
    
    //scheduletable
    @FXML public TableView<DailyAttendance> schedTable;
    @FXML public TableColumn<DailyAttendance, String> date;
    @FXML public TableColumn<DailyAttendance, String> scheduleId;
    @FXML public TableColumn<DailyAttendance, String> timein;
    @FXML public TableColumn<DailyAttendance, String> timeout;
    ObservableList<DailyAttendance> recordTableDate = FXCollections.observableArrayList();

    @FXML public TextField search;
    private Service<Void> backgroundThread;
    private File fileToRead;
    private FileInputStream fis;
    
    public void foo () {
        int choice = JOptionPane.showConfirmDialog(null, "You are about to delete this employee. Are you sure?");
        if(choice==JOptionPane.OK_OPTION){
            deleteEmployee();
        }
    }
    
    public void openExchangeDutyPanel () {
        stage = new Stage();
        stage.setScene(CoreController.getInstance().exchangeduty);
        stage.show();
    }
    
    public void deleteEmployee () {
        Employee e = BridgeUnit.getInstance().getEmployee(overviewTable.getSelectionModel().getSelectedItem().getBiometrics_id());
        Employee emp = BridgeUnit.getInstance().getEmployee(e.getBiometrics_id());
        BridgeUnit.getInstance().getRunningSession().delete(emp);
        BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
        BridgeUnit.getInstance().closeSession();
        refreshListOfEmployees();
        AttendanceViewController.getInstance().refreshListOfEmployees();
        GenerateAttendanceController.getInstance().refreshList();
        Writer.getInstance().refreshList();
    }
    
    public void setEmployeeInfo () {
        Employee e = BridgeUnit.getInstance().getEmployee(overviewTable.getSelectionModel().getSelectedItem().getBiometrics_id());
        
        try{
            URL path = getClass().getClassLoader().getResource("resources/credentials/img/"+e.getLast_name()+""+e.getFirst_name()+".png");
            Image img;
            
            img = new Image(path.toString());
            if(img!=null){
                employeeimage.setImage(img);
            }
        }catch ( Exception exception){
            URL path = getClass().getClassLoader().getResource("resources/images/images.png");
            Image img;
            img = new Image(path.toString());
            if(img!=null){
                employeeimage.setImage(img);
            }
        }finally{
            
        }
        
        //info
        Collection<Child> children = e.getCivilstatus().getChildren();
        childTableDate.clear();
        for (Iterator<Child> iterator = children.iterator(); iterator.hasNext();) {
            Child next = iterator.next();
            childTableDate.add(next);
        }
        
        civilstatus.setText(e.getCivilstatus().getCivil_status());
        spousename.setText(e.getCivilstatus().getSpouse_name());
        
        sickleave.setText(String.valueOf(e.getLeave_credits().getSick_leave()));
        vacationleave.setText(String.valueOf(e.getLeave_credits().getVacation_leave()));
        birthdayleave.setText(String.valueOf(e.getLeave_credits().getBirthday_leave()));
        maternityleave.setText(String.valueOf(e.getLeave_credits().getMaternity_leave()));
        parentalleave.setText(String.valueOf(e.getLeave_credits().getParental_leave()));
        specialleave.setText(String.valueOf(e.getLeave_credits().getSpecial_leave()));
        vawcleave.setText(String.valueOf(e.getLeave_credits().getVawc_leave()));
        paternityleave.setText(String.valueOf(e.getLeave_credits().getPaternity_leave()));
        
        others.setText(e.getEducation_background().getOthers());
        tertiary.setText(e.getEducation_background().getTertiary());
        tertiaryaddress.setText(e.getEducation_background().getTertiary_address());
        tertiarygraduated.setText(e.getEducation_background().getYear_graduated_tertiary());
        secondary.setText(e.getEducation_background().getSecondary());
        secondaryaddress.setText(e.getEducation_background().getSecondary_address());
        secondarygraduated.setText(e.getEducation_background().getYear_graduated_secondary());
        primary.setText(e.getEducation_background().getPrimary());
        primaryaddress.setText(e.getEducation_background().getPrimary_address());
        primarygraduated.setText(e.getEducation_background().getYear_graduated_primary());
        mobileNumber.setText(e.getMobilenumber());
        emailAddress.setText(e.getEmailaddress());
        currentAddress.setText(e.getCurrentaddress());
        permanentAddress.setText(e.getPermanentaddress());
        philhealth.setText(e.getPhilhealth());
        pagibig.setText(e.getPagibig());
        tin.setText(e.getTin());
        sss.setText(e.getSss());
        gender.setText(e.getGender());
        SimpleDateFormat sf = new SimpleDateFormat("MMM/dd/yyyy");
        if(e.getBirthday()!=null)
        birthday.setText(sf.format(new java.util.Date(e.getBirthday().getTime())));
        //name
        nameofemployee.setText(e.getLast_name()+", "+e.getFirst_name()+" "+e.getMiddle_name());
        //department assignment
        
        departmentAssignment.setText("No Department Assigned");
        employmentStatus.setText("No Employment Status");
        position.setText("");
        Collection<EmploymentStatus> er =  e.getEmployment_records();
        EmploymentHistory.getInstance().setItems((List) er);
        for (Iterator<EmploymentStatus> iterator = er.iterator(); iterator.hasNext();) {
            EmploymentStatus next = iterator.next();
            if(next.isIsActive()){
                departmentAssignment.setText(next.getDepartment());
                employmentStatus.setText("("+next.getStatus_of_employment()+")");
                position.setText(next.getPosition());
                break;
            }
            
        }
        
        Collection<DailyAttendance> records = e.getAttendance_record();
        recordTableDate.clear();
        for (Iterator<DailyAttendance> iterator = records.iterator(); iterator.hasNext();) {
            DailyAttendance next = iterator.next();
            if(!next.isHistory){
                recordTableDate.addAll(next);
            }
        }
        BridgeUnit.getInstance().closeSession();
    }
    
    public void showEmploymentHistory () {
        stage = new Stage();
        stage.setScene(CoreController.getInstance().employment_history);
        stage.show();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage = new Stage();
        instance = this;
        
        biometiricIdColumn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("biometrics_id"));
        empName.setCellValueFactory(new PropertyValueFactory<Employee, String>("last_name"));
        givenName.setCellValueFactory(new PropertyValueFactory<Employee, String>("first_name"));
        overviewTable.setItems(overviewTableData);
        
        childname.setCellValueFactory(new PropertyValueFactory<Child, String>("name"));
        childbday.setCellValueFactory(new PropertyValueFactory<Child, Date>("birthday"));
        childtable.setItems(childTableDate);
        
        date.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("date"));        
        scheduleId.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("schedule"));
        timein.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("timeIn"));
        timeout.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("timeOut"));
        schedTable.setItems(recordTableDate);
        
        refreshListOfEmployees();
    }
    
    public void refreshListOfEmployees() {
         List<Employee> l = BridgeUnit.getInstance().getAllEmployees();
         overviewTableData.clear();
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
            Employee next = (Employee) iterator.next();
            overviewTableData.add(next);
        }
    }
    
    public void ViewAddEmployee () {
        UpdateEmployeeController.getInstance().resetAll();
        stage.setScene(CoreController.getInstance().updateview);
        stage.show();
//        CoreController.getInstance().stage.setScene(CoreController.getInstance().updateview);
    }
    public void ViewEditEmployee () {
        Employee e = overviewTable.getSelectionModel().getSelectedItem();
        UpdateEmployeeController.getInstance().FillEditEmployeeView(e);
        stage.setScene(CoreController.getInstance().updateview);
        stage.show();
//        CoreController.getInstance().stage.setScene(CoreController.getInstance().updateview);
    }

    public void ViewDepartments () {
        stage.setScene(CoreController.getInstance().departmentview);
        stage.show();
    }

    public void ViewClockingSchedules () {
        stage.setScene(CoreController.getInstance().clocking_schedule);
        stage.show();
    }
    
    public void ViewAttendance () {
        AttendanceViewController.getInstance().setIsHistory(false);
        AttendanceViewController.getInstance().attendanceRecord.clear();
        AttendanceViewController.getInstance().toHistoryButton.setText("To History");
        stage.setScene(CoreController.getInstance().attendanceview);
        stage.show();
    }
    
    public void ViewAttendanceHistory () {
        AttendanceViewController.getInstance().setIsHistory(true);
        AttendanceViewController.getInstance().attendanceRecord.clear();
        AttendanceViewController.getInstance().toHistoryButton.setText("To Present");
        stage.setScene(CoreController.getInstance().attendanceview);
        stage.show();
    }
    
    public void viewArrangeLeave () {
        stage.setScene(CoreController.getInstance().leave);
        stage.show();
    }
    
    public static MainController getInstance() {
        return instance;
    }
     
    public void print() {
        stage.setScene(CoreController.getInstance().print);
        stage.show();
    }
    
    public void importEmployees () {
        JFileChooser filechooser = new JFileChooser ();
        int choice = filechooser.showSaveDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION){
            try {
                fileToRead = filechooser.getSelectedFile();
                fis = new FileInputStream(fileToRead);
                mainbar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                executeThreads();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public void executeThreads () {
        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try(BufferedReader br = new BufferedReader(new FileReader(fileToRead))) {
                            br.readLine();
                            br.readLine();
                            while(true){
                                String line = br.readLine();
                                String[] data = line.split(";");
                                System.out.println(data[0]);
                                System.out.println(data[1]);
                                int id = Integer.valueOf(data[0]);
                                Employee emp = BridgeUnit.getInstance().getEmployee(id);
                                if(emp==null){
                                    UpdateEmployeeController.getInstance().AddEmployeeFromTextFile(id, data[1]);
                                }
                            }
                        }
                        catch(Exception e){}
                        return null;
                    }
                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                refreshListOfEmployees();
                mainbar.setProgress(0);
                AttendanceViewController.getInstance().refreshListOfEmployees();
                Writer.getInstance().refreshList();
                GenerateAttendanceController.getInstance().refreshList();
            }
        });
        backgroundThread.restart();
    }
    
    public void search() {
        System.out.println(search.getText());
        if(search.getText().equalsIgnoreCase("")){
            refreshListOfEmployees();
        }
        else{
           Collection<Employee> list = new ArrayList<>();
           for (Iterator<Employee> iterator = overviewTableData.iterator(); iterator.hasNext();) {
                Employee next = iterator.next();
                
                if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
                next = BridgeUnit.getInstance().getEmployee(next.getBiometrics_id());
                if(
                     next.getLast_name().toLowerCase().contains(search.getText().trim()) ||
                     next.getGender().toLowerCase().contains(search.getText().trim()) ||
                     next.getFirst_name().toLowerCase().contains(search.getText().trim()) ||
                     next.getMiddle_name().toLowerCase().contains(search.getText().trim()) ||
                     next.getSss().toLowerCase().contains(search.getText().trim()) ||
                     next.getPhilhealth().toLowerCase().contains(search.getText().trim()) ||
                     next.getPagibig().toLowerCase().contains(search.getText().trim()) ||
                     next.getTin().toLowerCase().contains(search.getText().trim()) ||
                     next.getCurrentaddress().toLowerCase().contains(search.getText().trim()) ||
                     next.getEducation_background().getPrimary().toLowerCase().contains(search.getText().trim()) ||
                     next.getEducation_background().getYear_graduated_primary().toLowerCase().contains(search.getText().trim()) ||
                     next.getEducation_background().getSecondary().toLowerCase().contains(search.getText().trim()) ||
                     next.getEducation_background().getYear_graduated_secondary().toLowerCase().contains(search.getText().trim()) ||
                     next.getEducation_background().getTertiary().toLowerCase().contains(search.getText().trim()) ||
                     next.getEducation_background().getYear_graduated_tertiary().toLowerCase().contains(search.getText().trim())
                )
                {
                    list.add(next);
                    continue;
                }
                else{
                    Collection<EmploymentStatus> records = next.getEmployment_records();
                    for (Iterator<EmploymentStatus> iterator1 = records.iterator(); iterator1.hasNext();) {
                        EmploymentStatus next1 = iterator1.next();
                        if(
                                next1.getDepartment().toLowerCase().contains(search.getText()) ||
                                next1.getPosition().toLowerCase().contains(search.getText()) ||
                                next1.getStatus_of_employment().toLowerCase().contains(search.getText())
                        ){
                            list.add(next);
                            continue;
                        }
                    }
                }
           }
           overviewTableData.clear();
           for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
                Employee next = iterator.next();
                overviewTableData.add(next);
           }
        }
    }
}
