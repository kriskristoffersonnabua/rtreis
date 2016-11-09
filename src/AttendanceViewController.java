
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import models.ClockingSchedule;
import models.DailyAttendance;
import models.Employee;
import models.TableRowModel;

public class AttendanceViewController  implements Initializable {
    
    Stage stage;
    private static AttendanceViewController instance;
    @FXML public TableColumn<Employee, Integer> id;
    @FXML public TableColumn<Employee, String> lastName;
    @FXML public TableColumn<Employee, String> givenName;
    @FXML public TableView<Employee> listTable;
    ObservableList<Employee> listData = FXCollections.observableArrayList();
    
    @FXML public TableView<DailyAttendance> attendanceTable;
    @FXML public TableColumn<DailyAttendance, String> date;
    @FXML public TableColumn<DailyAttendance, String> scheduleId;
    @FXML public TableColumn<DailyAttendance, String> dayType;
    @FXML public TableColumn<DailyAttendance, String> timeIn;
    @FXML public TableColumn<DailyAttendance, String> brk;
    @FXML public TableColumn<DailyAttendance, String> resume;
    @FXML public TableColumn<DailyAttendance, String> timeOut;
    @FXML public TableColumn<DailyAttendance, Integer> lateIn;
    @FXML public TableColumn<DailyAttendance, Integer> shortMinutes;
    @FXML public TableColumn<DailyAttendance, String> remarks;
    @FXML public TableColumn<DailyAttendance, String> overtime;
    ObservableList<DailyAttendance> attendanceRecord = FXCollections.observableArrayList();
    
    @FXML Button editButton;
    @FXML Button toHistoryButton;
    
    List<ClockingSchedule> schedule_list;
    Employee employee;
    int currentIdTargeted;
    ObservableList<String> comboBoxData;
    
    boolean isHistory =false;
    @FXML public ProgressBar progbar;
    private File fileToRead;
    private int idEmployee;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage = new Stage();
        instance = this;
        
        comboBoxData = FXCollections.observableArrayList();
        
        schedule_list = BridgeUnit.getInstance().getAllClockingSchedule();
        
        id.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("biometrics_id"));
        lastName.setCellValueFactory(new PropertyValueFactory<Employee, String>("last_name"));
        givenName.setCellValueFactory(new PropertyValueFactory<Employee, String>("first_name"));
        listTable.setItems(listData);
        
        date.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("date"));        
        scheduleId.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("schedule"));
        dayType.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("dayType"));
        timeIn.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("timeIn"));
        brk.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("brk"));
        resume.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("resume"));
        timeOut.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("timeOut"));
        lateIn.setCellValueFactory(new PropertyValueFactory<DailyAttendance, Integer>("lateIn"));
        shortMinutes.setCellValueFactory(new PropertyValueFactory<DailyAttendance, Integer>("underTime"));
        overtime.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("overtime"));
        remarks.setCellValueFactory(new PropertyValueFactory<DailyAttendance, String>("remarks"));
        attendanceTable.setItems(attendanceRecord);
        
        overtime.setCellFactory(TextFieldTableCell.<DailyAttendance>forTableColumn());
        overtime.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    if(t.getNewValue()==""||t.getNewValue().equalsIgnoreCase("")){
                        dataToUpdate.setOvertime(0);
                    }
                    else{
                        long mill = 0;
                        String overtime = t.getNewValue().toString();
                        String[] data = overtime.split(":");
                        int hours = Integer.valueOf(data[0]);
                        int minutes = Integer.valueOf(data[1]);
                        
                        mill += (minutes * 60) * 1000;
                        mill += ( (hours * 60) * 60 ) * 1000;
                        dataToUpdate.setOvertime(mill);
                    }
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    getRecords();
                }
            }
        );
        remarks.setCellFactory(TextFieldTableCell.<DailyAttendance>forTableColumn());
        remarks.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    if(t.getNewValue().trim()!=""&&!t.getNewValue().trim().equals(""))
                    dataToUpdate.setRemarks(t.getNewValue());
                    else dataToUpdate.setRemarks("");
                    
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    
                    getRecords();
                }
            }
        );
        
        timeIn.setCellFactory(TextFieldTableCell.<DailyAttendance>forTableColumn());
        timeIn.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    System.out.println(t.getNewValue()+"sadfasdf");
                    String timein = dataToUpdate.getDate()+" "+t.getNewValue().toString();
                    Date timeIn = new Date(timein);
                    if(t.getNewValue().trim()!=""&&!t.getNewValue().trim().equals(""))
                    dataToUpdate.setTimeIn(timeIn.getTime());
                    else dataToUpdate.setTimeIn(0);
                    
                    updateOtherSettings(dataToUpdate);
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    
                    getRecords();
                }
            }
        );
        timeOut.setCellFactory(TextFieldTableCell.<DailyAttendance>forTableColumn());
        timeOut.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    String timeout = null;
                    Date timeOut = null;
                    
                    
                    
                    if(!dataToUpdate.isIsNightDiff()){
                        timeout = dataToUpdate.getDate()+" "+t.getNewValue().toString();
                        timeOut = new Date(timeout);
                        dataToUpdate.setTimeOut(timeOut.getTime());
                    }
                    else{
                        if(t.getNewValue().contains("am") || t.getNewValue().contains("AM")){
                            timeout = dataToUpdate.getDate()+" "+t.getNewValue().toString();
                            timeOut = new Date(timeout);
                            
                            dataToUpdate.setTimeOut(timeOut.getTime()+86400000);
                        }
                        else {
                            timeout = dataToUpdate.getDate()+" "+t.getNewValue().toString();
                            timeOut = new Date(timeout);
                            
                            dataToUpdate.setTimeOut(timeOut.getTime());
                        }
                    }
                    
                    if(t.getNewValue().trim()==""||t.getNewValue().trim().equals("")){
                        dataToUpdate.setTimeOut(0);
                    }
                    
                    
                    updateOtherSettings(dataToUpdate);
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    getRecords();
                }
            }
        );
        
        brk.setCellFactory(TextFieldTableCell.<DailyAttendance>forTableColumn());
        brk.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    
                    String brk = dataToUpdate.getDate()+" "+t.getNewValue().toString();
                    Date Brk = new Date(brk);
                    if(t.getNewValue().trim()!=""&&!t.getNewValue().trim().equals(""))
                    dataToUpdate.setBrk(Brk.getTime());
                    else dataToUpdate.setBrk(0);
                    
                    updateOtherSettings(dataToUpdate);
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    getRecords();
                }
            }
        );
        
        resume.setCellFactory(TextFieldTableCell.<DailyAttendance>forTableColumn());
        resume.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    
                    String resume = dataToUpdate.getDate()+" "+t.getNewValue().toString();
                    Date Resume = new Date(resume);
                    if(t.getNewValue().trim()!=""&&!t.getNewValue().trim().equals(""))
                    dataToUpdate.setResume(Resume.getTime());
                    else dataToUpdate.setResume(0);
                    
                    updateOtherSettings(dataToUpdate);
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    getRecords();
                }
            }
        );
        
        refreshClockingSchedule();
        
        scheduleId.setCellFactory(ComboBoxTableCell.<DailyAttendance, String>forTableColumn(comboBoxData));
        scheduleId.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    
                    int id = Integer.valueOf(t.getNewValue().split(":")[0].trim());
                    
                    dataToUpdate.setSchedule_id(id);
                    
                    ClockingSchedule schedule = null;
                    
                    for (Iterator<ClockingSchedule> iterator = schedule_list.iterator(); iterator.hasNext();) {
                        ClockingSchedule next = iterator.next();
                        if(next.getClock_id() == id){ 
                            schedule = next;
                            break;
                        }
                    }
                    
                    if(schedule == null) {
                        System.err.print("Error, Schedule was not found!");
                        return;
                    }
                    
                    System.out.println(schedule.getTimein());
                    System.out.println(schedule.getTimeout());
                    
                    String clockin = dataToUpdate.getDate()+" "+schedule.getTimein();
                    String clockout = dataToUpdate.getDate()+" "+schedule.getTimeout();
                    Date clockIn = new Date(clockin);
                    Date clockOut = new Date(clockout);
                    
                    dataToUpdate.setIsNightDiff(schedule.isIsNightDifferential());
                    
                    dataToUpdate.setClockInDate(clockIn.getTime());
                    if(schedule.isIsNightDifferential())
                    dataToUpdate.setClockOutDate(clockOut.getTime()+86400000);
                    else dataToUpdate.setClockOutDate(clockOut.getTime());
                    
                    System.out.println("Clock in: "+new Date(dataToUpdate.clockInDate));
                    System.out.println("Clock out: "+new Date(dataToUpdate.clockOutDate));
                    
                    updateOtherSettings(dataToUpdate);
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    getRecords();
                }
            }
        );
        
        ObservableList<String> daytype= FXCollections.observableArrayList();
        daytype.addAll("workday","restday","S-holiday","R-holiday","leave","day-off");
        dayType.setCellFactory(ComboBoxTableCell.<DailyAttendance, String>forTableColumn(daytype));
        dayType.setOnEditCommit(
            new EventHandler<CellEditEvent<DailyAttendance, String>>() {
                @Override
                public void handle(CellEditEvent<DailyAttendance, String> t) {
                    DailyAttendance dataToUpdate = attendanceTable.getSelectionModel().getSelectedItem();
                    
                    dataToUpdate.setDayType(t.getNewValue());
                    if(t.getNewValue().equalsIgnoreCase("s-holiday")){
                        dataToUpdate.setIsSpecialHoliday(true);
                        dataToUpdate.setIsRegularHoliday(false);
                    }
                    else if(t.getNewValue().equalsIgnoreCase("r-holiday")){
                        dataToUpdate.setIsRegularHoliday(true);
                        dataToUpdate.setIsSpecialHoliday(false);
                    }
                    else{
                        dataToUpdate.setIsRegularHoliday(false);
                        dataToUpdate.setIsSpecialHoliday(false);
                    }
                    
                    System.out.println(dataToUpdate.isRegularHoliday);
                    System.out.println(dataToUpdate.isSpecialHoliday);
                    
                    BridgeUnit.getInstance().getRunningSession().update(employee);
                    BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                    getRecords();
                }
            }
        );
        
        refreshListOfEmployees();
    }
    
    public void refreshClockingSchedule () {
        comboBoxData.clear();
        Collection<ClockingSchedule> listahan = BridgeUnit.getInstance().getAllClockingSchedule();
        for (Iterator<ClockingSchedule> iterator = listahan.iterator(); iterator.hasNext();) {
            ClockingSchedule next = iterator.next();
            comboBoxData.add(next.getClock_id()+" : "+next.getClocking_definition());
        }
    }
    
    public static AttendanceViewController getInstance() {
        return instance;
    }

    public void refreshListOfEmployees() {
        List<Employee> l = BridgeUnit.getInstance().getAllEmployees();
        listData.clear();
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
            Employee next = (Employee) iterator.next();
            listData.add(next);
        }
    }    
    
    public void clearObservableAttendanceList () {
           attendanceRecord.clear();
    }
    
    public void getRecords() {
        idEmployee = listTable.getSelectionModel().getSelectedItem().getBiometrics_id();
        if(isHistory){
            getHistoryAttendanceRecord();
        }
        else getAttendanceRecord();
    }
    
    public void deleteEntry(KeyEvent event){
        if (event.getCode() == KeyCode.DELETE) {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?");
            
            if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.NO_OPTION)
            return;
            
            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
            idEmployee = listTable.getSelectionModel().getSelectedItem().getBiometrics_id();
            employee = BridgeUnit.getInstance().getEmployee(idEmployee);

            Collection<DailyAttendance> records = employee.getAttendance_record();
            DailyAttendance record = attendanceTable.getSelectionModel().getSelectedItem();
            
            for(DailyAttendance r : records){
                System.out.print("1");
            }
            for(DailyAttendance r : records){
                System.out.println(r);
                System.out.println(record);
                if(r.date == record.date){
                    System.out.println("removing");
                    records.remove(r);
                    break;
                }
            }
            
            
            System.out.println("");
            for(DailyAttendance r : records){
                System.out.print("1");
            }
            
            employee.setAttendance_record(records);
            
            BridgeUnit.getInstance().getRunningSession().update(employee);
            BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
            getRecords();
        }
    }
    
    synchronized public void getAttendanceRecord () {
        if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
        Employee e = BridgeUnit.getInstance().getEmployee(idEmployee);
        currentIdTargeted = listTable.getSelectionModel().getSelectedItem().getBiometrics_id();
        employee = e;
        clearObservableAttendanceList();
        Collection<DailyAttendance> records = e.getAttendance_record();
        for (Iterator<DailyAttendance> iterator = records.iterator(); iterator.hasNext();) {
            DailyAttendance next = iterator.next();
            if(!next.isIsHistory()){
                attendanceRecord.add(next);
            }
        }
        System.out.println(BridgeUnit.getInstance().getRunningSession().isOpen());
    }
    
    synchronized public void getHistoryAttendanceRecord () {
        if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
        Employee e = BridgeUnit.getInstance().getEmployee(idEmployee);
        currentIdTargeted = listTable.getSelectionModel().getSelectedItem().getBiometrics_id();
        employee = e;
        clearObservableAttendanceList();
        Collection<DailyAttendance> records = e.getAttendance_record();
        for (Iterator<DailyAttendance> iterator = records.iterator(); iterator.hasNext();) {
            DailyAttendance next = iterator.next();
            if(next.isIsHistory()){
                attendanceRecord.add(next);
            }
        }
        System.out.println(BridgeUnit.getInstance().getRunningSession().isOpen());
    }
    
    synchronized public void getAttendanceRecordHistory () {
        if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
        Employee e = BridgeUnit.getInstance().getEmployee(idEmployee);
        employee = e;
        clearObservableAttendanceList();
        Collection<DailyAttendance> records = e.getAttendance_record();
        for (Iterator<DailyAttendance> iterator = records.iterator(); iterator.hasNext();) {
            DailyAttendance next = iterator.next();
            if(next.isIsHistory()){
                attendanceRecord.add(next);
            }
        }
        System.out.println(BridgeUnit.getInstance().getRunningSession().isOpen());
    }
    
    public void editButtonAction () {
        if(editButton.getText().contains("Edit")){
            editButton.setText("Save");
            attendanceTable.setEditable(true);
        }
        else if(editButton.getText().contains("Save")){
            editButton.setText("Edit");
            attendanceTable.setEditable(false);
        }
    }
    
    public void viewAttendanceView () {
        stage = new Stage();
        stage.setScene(CoreController.getInstance().generate_attendance);
        stage.show();
    }
    
    public void viewToHistory () {
        stage = new Stage();
        if(isHistory){
            HistoryController.getInstance().setToPresent(true);
        }
        else HistoryController.getInstance().setToPresent(false);
        stage.setScene(CoreController.getInstance().history);
        stage.show();
    }
    
    public void importAttendance () {
        JFileChooser filechooser = new JFileChooser ();
        int choice = filechooser.showSaveDialog(null);
             
        if(choice == JFileChooser.APPROVE_OPTION){
            fileToRead = filechooser.getSelectedFile();
            progbar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            importerAttendanceWorker();
        }
    }
    
    public void importerAttendanceWorker () {
        Service<Void> backgroundThread;
        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        FileInputStream fis;
                        try {
                            fis = new FileInputStream(fileToRead);
                            Scanner scanner = new Scanner(fis);
                            scanner.next();
                            while(scanner.hasNext()){
                                String[] data = scanner.next().split(";");
                                System.out.println(data[0]);
                                System.out.println(data[1]);
                                System.out.println(data[2]);
                                if(data[0].trim().equalsIgnoreCase("") || data[1].trim().equalsIgnoreCase("") 
                                        || data[2].trim().equalsIgnoreCase("")) continue;
                                insertAttendance(data[1],data[2], Integer.valueOf(data[0]));
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progbar.setProgress(0);
                JOptionPane.showMessageDialog(null, "Importing attendance completed. :)");
            }
        });
        backgroundThread.restart();
    }
    
    synchronized public void insertAttendance (String date, String time, int biometrics_id) {
        Date newClockDate = new Date(date);
        Date yesterdayDate = new Date(newClockDate.getTime()-86400000);
        Date attendanceRecord = new Date(date+" "+time);
        
        if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
        employee = BridgeUnit.getInstance().getEmployee(biometrics_id);
        
        if(employee==null) return;
        
        DailyAttendance day = null;
        DailyAttendance yesterday = null;
        
        Collection<DailyAttendance> records = employee.getAttendance_record();
        for (Iterator<DailyAttendance> iterator = records.iterator(); iterator.hasNext();) {
            DailyAttendance next = iterator.next();
            if(next.date == yesterdayDate.getTime()){
                yesterday = next;
            }
            if(next.date == newClockDate.getTime()){
                day = next;
                break;
            }
        }
        
        if(day==null){
            return;
        }
        
        System.out.println(day.isNightDiff+": day is night diff");
        
        if(yesterday!=null){
            if (yesterday.isNightDiff) {
                if(yesterday.timeOut==0){
                    yesterday.setTimeOut(attendanceRecord.getTime());
                }
                else if( attendanceRecord.getTime() < yesterday.timeOut ){
                    if(day.isNightDiff){
                        insertNightDiff(day, yesterday.timeOut);
                    }
                    else insertNormal(day, yesterday.timeOut);
                    yesterday.setTimeOut(attendanceRecord.getTime());
                }
                else{
                    if(day.isNightDiff){
                        insertNightDiff(day, attendanceRecord.getTime());
                    }
                    else insertNormal(day, attendanceRecord.getTime());
                }
            }
            else{
                if(day.isNightDiff){
                    insertNightDiff(day, attendanceRecord.getTime());
                }
                else insertNormal(day, attendanceRecord.getTime());
            }
        }
        else {
            if(day.isNightDiff){
                insertNightDiff(day, attendanceRecord.getTime());
            }
            else insertNormal(day, attendanceRecord.getTime());
        }
        
        if(yesterday!=null)
        updateOtherSettings(yesterday);
        
        updateOtherSettings(day);
        
        BridgeUnit.getInstance().getRunningSession().update(employee);
        BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
        
        getAttendanceRecord();
    }

    synchronized public void insertNightDiff(DailyAttendance day, long attendanceRecord) {
        if (day.timeIn==0){
            day.setTimeIn(attendanceRecord);
        }
        else{
            if (day.brk==0){
                if(attendanceRecord>day.timeIn){
                    day.setBrk(attendanceRecord);
                }
                else{
                    day.setBrk(day.timeIn);
                    day.setTimeIn(attendanceRecord);
                }
            }
            else{
                if (attendanceRecord<day.brk&&attendanceRecord>day.timeIn){
                    day.setResume(day.brk);
                    day.setBrk(attendanceRecord);
                }
                else{
                    if(attendanceRecord<day.timeIn){
                        day.setResume(day.brk);
                        day.setBrk(day.timeIn);
                        day.setTimeIn(attendanceRecord);
                    }
                    else{
                        day.setResume(attendanceRecord);
                    }
                }
            }
        }
    }
    
    synchronized public void insertNormal(DailyAttendance day, long attendanceRecord){
        long newRecord = attendanceRecord;
        if(day.timeIn==0){
            day.setTimeIn(newRecord);
        }
        else{
            if(day.timeOut==0){
                if(day.timeIn<newRecord){
                    day.setTimeOut(newRecord);
                }
                else{
                    day.setTimeOut(day.timeIn);
                    day.setTimeIn(newRecord);
                }
            }
            else{
                if(newRecord<day.timeOut&&newRecord>day.timeIn){
                    if(day.brk==0){
                        day.setBrk(newRecord);
                    }
                    else{
                        if(day.brk<newRecord){
                            day.setResume(newRecord);
                        }
                        else{
                            day.setResume(day.brk);
                            day.setBrk(newRecord);
                        }
                    }
                }
                else{
                    if(newRecord<day.timeIn){
                        day.setBrk(day.timeIn);
                        day.setTimeIn(newRecord);
                    }
                    else{
                        if(newRecord>day.timeOut){
                            day.setResume(day.timeOut);
                            day.setTimeOut(newRecord);
                        }
                    }
                }
            }
        }
    }
    
    /*
        Update the other settings of a DailyAttendance
    */
    synchronized public void updateOtherSettings (DailyAttendance record) {
        if(record.timeIn!=0){
            long latein = record.timeIn - record.clockInDate;
            if(latein>=0){
                record.setLateIn(latein);
            }
            else record.setLateIn(0);
            
        }
        if(record.timeOut!=0){
            long undertime = record.timeOut - record.clockOutDate;
            if(undertime<0){
                System.out.println(undertime*(-1));
                record.setUnderTime(undertime*(-1));
            }
            else{
                record.setUnderTime(0);
//                if(undertime>=0)
//                record.setOvertime(undertime);
            }
        }
        if(record.timeIn!=0&&record.timeOut!=0){
            long workinghours = record.timeOut - record.timeIn;
            record.setWorkinghours(workinghours);
        }
        if(record.timeIn==0||record.timeOut!=0){
            if(record.timeIn==0)record.setLateIn(0);
            if(record.timeOut==0){
                record.setUnderTime(0);
                record.setOvertime(0);
            }
        }
        
    }

    public boolean isIsHistory() {
        return isHistory;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }
   
    
}
