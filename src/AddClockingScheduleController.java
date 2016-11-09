import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.Child;
import models.ClockingSchedule;

public class AddClockingScheduleController implements Initializable{
    
    private static AddClockingScheduleController instance;
    
    ClockingSchedule sched;
    
    @FXML TextField clockdefinition;
    @FXML TextField timein;
    @FXML TextField timeout;
    @FXML CheckBox isNightDiff;
    @FXML Button saveButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
    }
    
    public void resetFields () {
        clockdefinition.clear();
        timein.clear();
        timeout.clear();
        isNightDiff.setSelected(false);
    }
    
    public void setInfo ( ClockingSchedule schedule ){
        clockdefinition.setText(schedule.getClocking_definition());
        timein.setText(schedule.getTimein());
        timeout.setText(schedule.getTimeout());
        isNightDiff.setSelected(schedule.isIsNightDifferential());
        saveButton.setText("save");
        sched = schedule;
    }
    
    public void addClockingSchedule () {
        ClockingSchedule schedule = new ClockingSchedule();
        schedule.setClocking_definition(clockdefinition.getText());
        schedule.setTimein(timein.getText());
        schedule.setTimeout(timeout.getText());
        
        if(isNightDiff.isSelected())schedule.setIsNightDifferential(true);
        else schedule.setIsNightDifferential(false);
        
        BridgeUnit.getInstance().save(schedule);
        ClockingScheduleController.getInstance().populateTable();
        AttendanceViewController.getInstance().refreshClockingSchedule();
    }
    
    public void saveEditedClockingSchedule () {
        ClockingSchedule schedule = BridgeUnit.getInstance().getClockingSchedule(sched.getClock_id());
        schedule.setClocking_definition(clockdefinition.getText());
        schedule.setTimein(timein.getText());
        schedule.setTimeout(timeout.getText());
        schedule.setIsNightDifferential(isNightDiff.isSelected());
        
        saveButton.setText("Add");
        
        BridgeUnit.getInstance().getRunningSession().update(schedule);
        BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
        BridgeUnit.getInstance().closeSession();
        resetFields();
        ClockingScheduleController.getInstance().schedule.clear();
        ClockingScheduleController.getInstance().populateTable();
        ClockingScheduleController.getInstance().stage.close();
    }
    
    public void addOrSaveEdit () {
        if(saveButton.getText().equalsIgnoreCase("add")){
            addClockingSchedule();
        }
        else{
            saveEditedClockingSchedule();
        }
    }

    public static AddClockingScheduleController getInstance() {
        return instance;
    }
}
