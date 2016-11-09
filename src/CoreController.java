import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CoreController extends Application {

    public Scene 
                mainview, 
                updateview, 
                attendanceview,
                departmentview,
                add_employment,
                add_child,
                clocking_schedule,
                add_clocking_schedule,
                history,
                print,
                employment_history,
                generate_attendance,
                exchangeduty,
                leave;
    public Stage stage;
    public static CoreController instance;

    public CoreController() {
        //empty constructor
    }

    public static CoreController getInstance() {
        return instance;
    }

    @Override
    public void stop () {
        System.out.println("Closing Application...");
        BridgeUnit.getInstance().close();
        Platform.exit();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        Parent root2 = FXMLLoader.load(getClass().getResource("fxml/update_employee.fxml"));
        Parent root3 = FXMLLoader.load(getClass().getResource("fxml/department.fxml"));
        Parent root4 = FXMLLoader.load(getClass().getResource("fxml/add_employment.fxml"));
        Parent root5 = FXMLLoader.load(getClass().getResource("fxml/add_child.fxml"));
        Parent root6 = FXMLLoader.load(getClass().getResource("fxml/clockingschedule.fxml"));
        Parent root7 = FXMLLoader.load(getClass().getResource("fxml/addclockingschedule.fxml"));
        Parent root8 = FXMLLoader.load(getClass().getResource("fxml/attendance.fxml"));
        Parent root9 = FXMLLoader.load(getClass().getResource("fxml/generate_attendance.fxml"));
        Parent root10 = FXMLLoader.load(getClass().getResource("fxml/history.fxml"));
        Parent root11 = FXMLLoader.load(getClass().getResource("fxml/timecard.fxml"));
        Parent root12 = FXMLLoader.load(getClass().getResource("fxml/employmenthistory.fxml"));
        Parent root13 = FXMLLoader.load(getClass().getResource("fxml/leave.fxml"));
        Parent root14 = FXMLLoader.load(getClass().getResource("fxml/exchangeduty.fxml"));
        
        mainview = new Scene(root);
        updateview = new Scene(root2);
        departmentview = new Scene(root3);
        add_employment = new Scene(root4);
        add_child = new Scene(root5);
        clocking_schedule = new Scene(root6);
        add_clocking_schedule = new Scene(root7);
        attendanceview = new Scene(root8);
        generate_attendance = new Scene(root9);
        history = new Scene(root10);
        print = new Scene(root11);
        employment_history = new Scene(root12);
        leave = new Scene(root13);
        exchangeduty = new Scene(root14);
        
        instance = this;
        this.stage = stage;
       
        Image img = new Image(getClass().getResourceAsStream("resources/images/logos.png"));
        this.stage.getIcons().add(img);
       
        this.stage.titleProperty().setValue("RTR HOSPITAL INFORMATION SYSTEM");
        this.stage.setScene(mainview);
        this.stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
