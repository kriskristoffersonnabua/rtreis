import com.sun.javafx.scene.control.skin.TableViewSkin;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.swing.JOptionPane;
import models.ClockingSchedule;
import models.Department;

public class ClockingScheduleController implements Initializable {
    
    private static ClockingScheduleController instance;
    Stage stage;
    
    @FXML public TableView<ClockingSchedule> clockingScheduleListTable;
    @FXML public TableColumn<ClockingSchedule, String> clockingScheduleDefinition;
    ObservableList<ClockingSchedule> schedule;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        stage = new Stage();
        //initialize table with existing departments
        clockingScheduleListTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        clockingScheduleListTable.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount()==2){
                        showEditClockingSchedule();
                    }
                }
            }
        });
        populateTable();
    }
    
    public void populateTable(){
        //department table initialize columns and etc and get stored departments
        schedule = FXCollections.observableArrayList(BridgeUnit.getInstance().getAllClockingSchedule());
        for(ClockingSchedule sched:schedule)System.out.println(sched.getClocking_definition());
        clockingScheduleDefinition.setCellValueFactory(new PropertyValueFactory<ClockingSchedule, String>("clocking_definition"));
        if(schedule.size()>0)
        clockingScheduleListTable.setItems(schedule);
    }

    public void addClockingSchedule () {
        stage.setScene(CoreController.getInstance().add_clocking_schedule);
        stage.show();
    }
    
    public void deleteClockingSchedule () {
        int selection_id = clockingScheduleListTable.getSelectionModel().getSelectedItem().getClock_id();
        ClockingSchedule sched = new ClockingSchedule();
        sched.setClock_id(selection_id);
        BridgeUnit.getInstance().delete(sched);
        populateTable();
        AttendanceViewController.getInstance().refreshClockingSchedule();
    }

    public void showEditClockingSchedule () {
       try{
            AddClockingScheduleController.getInstance().setInfo(clockingScheduleListTable.getSelectionModel().getSelectedItem());
            stage.setScene(CoreController.getInstance().add_clocking_schedule);
            stage.show();
       }
       catch(Exception e){
       }
    }
    
    public static ClockingScheduleController getInstance() {
        return instance;
    }
    
}
