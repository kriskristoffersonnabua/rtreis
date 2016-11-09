
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import models.DailyAttendance;
import models.Employee;
import models.TableRowModel;


public class GenerateAttendanceController implements Initializable{

    @FXML public TableView<TableRowModel> tableList;
    @FXML public TableColumn<TableRowModel, CheckBox> comboboxColumn;
    @FXML public TableColumn<TableRowModel, String> employeeNameColumn;
    ObservableList<TableRowModel> tableData = FXCollections.observableArrayList();
    
    @FXML CheckBox allCheckBox;
    @FXML CheckBox specificCheckBox;
    
    @FXML DatePicker start;
    @FXML DatePicker end;
        
    private static GenerateAttendanceController instance;

    public static GenerateAttendanceController getInstance() {
        return instance;
    }
    private double progress = 0;
    private int size;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        comboboxColumn.setCellValueFactory(new PropertyValueFactory<TableRowModel, CheckBox>("checkbox"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<TableRowModel, String>("name"));
        tableList.setItems(tableData);
        
        refreshList();
    }
    
    public void refreshList () {
        List<Employee> list = BridgeUnit.getInstance().getAllEmployees();
        tableData.clear();
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next = iterator.next();
            TableRowModel data = new TableRowModel(next.getLast_name()+", "+next.getFirst_name());
            data.setId(next.getBiometrics_id());
            tableData.add(data);
        }
    }
    
    public void generateAttendance() {
        if(start.getValue()==null){
            JOptionPane.showMessageDialog(null, "Starting date must not be empty.");
            return;
        }
        if(end.getValue()==null){
            JOptionPane.showMessageDialog(null, "End date must not be empty.");
            return;
        }
        AttendanceViewController.getInstance().stage.close(); 
        AttendanceViewController.getInstance().progbar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        
       generatorWorker();
    }
    
    public void generatorWorker () {
        Service<Void> backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if(specificCheckBox.isSelected()){
                            for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
                                TableRowModel next = iterator.next();
                                if(next.getCheckbox().isSelected()){
                                    Employee employee = BridgeUnit.getInstance().getEmployee(next.getId());
                                    LocalDate startDate = start.getValue();
                                    LocalDate endDate = end.getValue();
                                    Calendar startingDate = new GregorianCalendar(startDate.getYear(),startDate.getMonthValue()-1,startDate.getDayOfMonth());
                                    Calendar endingDate = new GregorianCalendar(endDate.getYear(),endDate.getMonthValue()-1,endDate.getDayOfMonth());
                                    startingDate.setFirstDayOfWeek(Calendar.MONDAY);
                                    while(startingDate.compareTo(endingDate)!=1){
                                        DailyAttendance data = new DailyAttendance();
                                        data.setDate(startingDate.getTimeInMillis());
                                        if(startingDate.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                                            data.setDayType("restday");
                                        }
                                        else{
                                            data.setDayType("workday");
                                        }
                                        data.setSchedule_id(0);

                                        String clockin = data.getDate()+" 8:15";
                                        String clockout = data.getDate()+" 17:00";
                                        java.util.Date clockIn = new java.util.Date(clockin);
                                        java.util.Date clockOut = new java.util.Date(clockout);

                                        data.setClockInDate(clockIn.getTime());
                                        data.setClockOutDate(clockOut.getTime());
                                        data.setIsNightDiff(false);

                                        data.setIsHistory(false);
                                        
                                        // dont add if attendance record is existing
                                        boolean toAdd = true;
                                        Collection<DailyAttendance> records = employee.getAttendance_record();
                                        for (Iterator<DailyAttendance> iterator1 = records.iterator(); iterator1.hasNext();) {
                                            DailyAttendance next1 = iterator1.next();
                                            if(next1.date==data.date){
                                                toAdd = false;
                                                break;
                                            }
                                        }
                                        if(toAdd) 
                                        employee.getAttendance_record().add(data);
                                        startingDate.add(Calendar.DAY_OF_YEAR, 1);
                                    }
                                    BridgeUnit.getInstance().getRunningSession().update(employee);
                                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                                    BridgeUnit.getInstance().closeSession();
                                }
                            }
                        }
                        else if(allCheckBox.isSelected()){
                            for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
                                TableRowModel next = iterator.next();
                                Employee employee = BridgeUnit.getInstance().getEmployee(next.getId());
                                LocalDate startDate = start.getValue();
                                LocalDate endDate = end.getValue();
                                Calendar startingDate = new GregorianCalendar(startDate.getYear(),startDate.getMonthValue()-1,startDate.getDayOfMonth());
                                Calendar endingDate = new GregorianCalendar(endDate.getYear(),endDate.getMonthValue()-1,endDate.getDayOfMonth());
                                startingDate.setFirstDayOfWeek(Calendar.MONDAY);
                                while(startingDate.compareTo(endingDate)!=1){
                                    DailyAttendance data = new DailyAttendance();
                                    data.setDate(startingDate.getTimeInMillis());
                                    if(startingDate.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                                        data.setDayType("restday");
                                    }
                                    else{
                                        data.setDayType("workday");
                                    }
                                    data.setSchedule_id(0);

                                    String clockin = data.getDate()+" 8:15";
                                    String clockout = data.getDate()+" 17:00";
                                    java.util.Date clockIn = new java.util.Date(clockin);
                                    java.util.Date clockOut = new java.util.Date(clockout);

                                    data.setClockInDate(clockIn.getTime());
                                    data.setClockOutDate(clockOut.getTime());
                                    data.setIsNightDiff(false);

                                    data.setIsHistory(false);
                                    
                                    boolean toAdd = true;
                                    Collection<DailyAttendance> records = employee.getAttendance_record();
                                    for (Iterator<DailyAttendance> iterator1 = records.iterator(); iterator1.hasNext();) {
                                        DailyAttendance next1 = iterator1.next();
                                        if(next1.date==data.date){
                                            toAdd = false;
                                            break;
                                        }
                                    }
                                    if(toAdd) 
                                    employee.getAttendance_record().add(data);
                                    startingDate.add(Calendar.DAY_OF_YEAR, 1);
                                }
                                BridgeUnit.getInstance().getRunningSession().update(employee);
                                BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                                BridgeUnit.getInstance().closeSession();
                            }
                        }
                        return null;
                    }
                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AttendanceViewController.getInstance().progbar.setProgress(0);
                JOptionPane.showMessageDialog(null, "Generating attendance completed.");
            }
        });
        backgroundThread.restart();
    }
    
    public void allSelected () {
        if(allCheckBox.isSelected())specificCheckBox.setSelected(false);
        
        for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
            TableRowModel next = iterator.next();
            if(!next.getCheckbox().isSelected()) next.getCheckbox().setSelected(true);
        }
    }
    public void specificSelected () {
        if(specificCheckBox.isSelected())allCheckBox.setSelected(false);
        
        for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
            TableRowModel next = iterator.next();
            if(next.getCheckbox().isSelected()) next.getCheckbox().setSelected(false);
        }
    }
}
