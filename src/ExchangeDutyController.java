
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.ComboBoxTableCell;
import javax.swing.JOptionPane;
import models.ClockingSchedule;
import models.DailyAttendance;
import models.Employee;

public class ExchangeDutyController implements Initializable{
    private List Employees;
    private List Schedule;
    @FXML public ComboBox<String> combobox;
    @FXML public ComboBox<String> combobox2;
    @FXML public ComboBox<String> schedule;
    @FXML public ComboBox<String> schedule2;
    @FXML public DatePicker originalschedule;
    @FXML public DatePicker newschedule;
    @FXML public CheckBox offduty;
    @FXML public CheckBox ofduty;
    @FXML public Button okay;
    @FXML public Label label;
    @FXML public Label label2;
    @FXML public Label employee2label;
    @FXML public Label schedule2label;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateBoxes();
    }
    
    public void populateBoxes() {
        Employees = BridgeUnit.getInstance().getAllEmployees();
        Schedule = BridgeUnit.getInstance().getAllClockingSchedule();
        for (Iterator iterator = Employees.iterator(); iterator.hasNext();) {
            Employee next = (Employee) iterator.next();
            combobox.getItems().add(next.getBiometrics_id()+"-"+next.getFirst_name()+" "+next.getLast_name());
            combobox2.getItems().add(next.getBiometrics_id()+"-"+next.getFirst_name()+" "+next.getLast_name());
        }
        for (Iterator iterator = Schedule.iterator(); iterator.hasNext();) {
            ClockingSchedule next = (ClockingSchedule) iterator.next();
            schedule.getItems().add(next.getClock_id()+"-"+next.getClocking_definition());
            schedule2.getItems().add(next.getClock_id()+"-"+next.getClocking_definition());
        }
    }
    
    public void offduty () {
        offduty.setSelected(true);
        ofduty.setSelected(false);
        
        originalschedule.visibleProperty().set(true);
        newschedule.visibleProperty().set(true);
        label.visibleProperty().set(true);
        label2.visibleProperty().set(true);
        schedule.disableProperty().set(true);
        schedule2.disableProperty().set(true);
        
        employee2label.visibleProperty().set(false);
        combobox2.visibleProperty().set(false);
        schedule2.visibleProperty().set(false);
        schedule2label.visibleProperty().set(false);
    }
    
    public void ofduty () {
        offduty.setSelected(false);
        ofduty.setSelected(true);
        
        originalschedule.visibleProperty().set(true);
        newschedule.visibleProperty().set(false);
        label.visibleProperty().set(true);
        label2.visibleProperty().set(false);
        schedule.disableProperty().set(false);
        schedule2.disableProperty().set(false);
        
        employee2label.visibleProperty().set(true);
        combobox2.visibleProperty().set(true);
        schedule2.visibleProperty().set(true);
        schedule2label.visibleProperty().set(true);
    }
    
    public void update () {
        if(ofduty.selectedProperty().get()){
            LocalDate exchangedate = originalschedule.getValue();
            Calendar ed = new GregorianCalendar(exchangedate.getYear(),exchangedate.getMonthValue()-1,exchangedate.getDayOfMonth());
            long date = ed.getTimeInMillis();
            
            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
            int id = Integer.valueOf(combobox2.getSelectionModel().getSelectedItem().split("-")[0]);
            Employee employee = BridgeUnit.getInstance().getEmployee(id);
            List attendance = (List) employee.getAttendance_record();
            for (Iterator iterator = attendance.iterator(); iterator.hasNext();) {
                DailyAttendance next = (DailyAttendance) iterator.next();
                if (date == next.date) {
                    next.setSchedule_id(Integer.valueOf(schedule.getValue().split("-")[0]));
                    AttendanceViewController.getInstance().updateOtherSettings(next);
                    break;
                }
            }
            
            BridgeUnit.getInstance().getRunningSession().update(employee);
            BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
            
            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
            id = Integer.valueOf(combobox.getSelectionModel().getSelectedItem().split("-")[0]);
            employee = BridgeUnit.getInstance().getEmployee(id);
            attendance = (List) employee.getAttendance_record();
            for (Iterator iterator = attendance.iterator(); iterator.hasNext();) {
                DailyAttendance next = (DailyAttendance) iterator.next();
                if (date == next.date) {
                    next.setSchedule_id(Integer.valueOf(schedule2.getValue().split("-")[0]));
                     AttendanceViewController.getInstance().updateOtherSettings(next);
                    break;
                }
            }
            BridgeUnit.getInstance().getRunningSession().update(employee);
            BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
            
            MainController.stage.close();
            JOptionPane.showMessageDialog(null, "Exchanging of duty successfull. :)");
        }
        else {
            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
            int id = Integer.valueOf(combobox.getSelectionModel().getSelectedItem().split("-")[0]);
            Employee employee = BridgeUnit.getInstance().getEmployee(id);
             LocalDate offdate = newschedule.getValue();
             Calendar offd = new GregorianCalendar(offdate.getYear(),offdate.getMonthValue()-1,offdate.getDayOfMonth());
             long newoff = offd.getTimeInMillis();
             LocalDate oldoffdate = originalschedule.getValue();
             Calendar oldoffd = new GregorianCalendar(oldoffdate.getYear(),oldoffdate.getMonthValue()-1,oldoffdate.getDayOfMonth());
             long oldoff = oldoffd.getTimeInMillis();
             List attendance = (List) employee.getAttendance_record();
             
            int schedule_id = 0;
            for (Iterator iterator = attendance.iterator(); iterator.hasNext();) {
                 DailyAttendance next = (DailyAttendance) iterator.next();
                if(newoff == next.date) {
                    schedule_id = next.getSchedule_id();
                    next.setDayType("day-off");
                    next.setSchedule_id(0);
                    break;
                }
            }
            for (Iterator iterator = attendance.iterator(); iterator.hasNext();) {
                 DailyAttendance next = (DailyAttendance) iterator.next();
                if(oldoff == next.date) {
                    next.setSchedule_id(schedule_id);
                    next.setDayType("workday");
                    AttendanceViewController.getInstance().updateOtherSettings(next);
                    System.out.println("---secondforloop");
                    break;
                }
            }
            
            BridgeUnit.getInstance().getRunningSession().update(employee);
            BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
            MainController.stage.close();
            JOptionPane.showMessageDialog(null, "Exchanging off duty successfull. :)");
        }
    }
}
