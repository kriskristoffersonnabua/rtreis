
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.swing.JOptionPane;
import models.Department;

public class DepartmentController implements Initializable {
    
    private static DepartmentController instance;
    
    @FXML public TextField department_name_textfield;
    
    @FXML public TableView<Department> departmentListTable;
    @FXML public TableColumn<Department, String> departmentNameColumn;
    ObservableList<Department> dept;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        //initialize table with existing departments
        departmentListTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        populateTable();
    }
    
    public void populateTable(){
        //department table initialize columns and etc and get stored departments
        dept = FXCollections.observableArrayList(BridgeUnit.getInstance().getAllDepartments());
        for(Department depts:dept)System.out.println(depts.getDepartment_name());
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("department_name"));
        if(dept.size()>0)
        departmentListTable.setItems(dept);
    }
    
    public void addDepartment () {
        String name = JOptionPane.showInputDialog("Department");
        if(!name.equalsIgnoreCase("")&&!name.equalsIgnoreCase(null))
            addDepartment(name);
    }
    
    public void addDepartment ( String departmentName ) {
        Department department = new Department();
        department.setDepartment_name(departmentName);
        BridgeUnit.getInstance().save(department);
        populateTable();
        AddEmploymentController.getInstance().populateComboBox();
    }
    
    public void deleteDepartment () {
        int selection_id = departmentListTable.getSelectionModel().getSelectedItem().getDepartment_id();
        System.out.println(selection_id);
        Department d = new Department();
        d.setDepartment_id(selection_id);
        BridgeUnit.getInstance().delete(d);
        populateTable();
    }

    public static DepartmentController getInstance() {
        return instance;
    }
    
}
