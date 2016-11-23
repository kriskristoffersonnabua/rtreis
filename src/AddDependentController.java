
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Child;

public class AddDependentController implements Initializable{
    
    private static AddDependentController instance;
    
    @FXML public TextField childName;
    @FXML public DatePicker childBirthday;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
    }
    
    public void addChildToTable () {
        Child child = new Child();
        child.setName(childName.getText());
        child.setBirthday(Date.valueOf(childBirthday.getValue()));
        UpdateEmployeeController.getInstance().dependents.add(child);
    }
    
    
    
}
