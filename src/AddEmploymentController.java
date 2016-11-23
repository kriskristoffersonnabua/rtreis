
import java.net.URL;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import models.Department;
import models.EmploymentStatus;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kaycee
 */
public class AddEmploymentController implements Initializable{
    
    private static AddEmploymentController instance;
    
    @FXML public ComboBox<String> department_combobox;
    @FXML public TextField position;
    @FXML public TextField employment_status;
    @FXML public DatePicker employment_start;
    @FXML public DatePicker employment_end;
    @FXML public CheckBox isActive;
    @FXML public Button updatebutton;
    
    public EmploymentStatus toUpdate;
    
    public void resetValues () {
        position.clear();
        employment_status.clear();
        employment_start.setValue(null);
        employment_end.setValue(null);
        isActive.setSelected(false);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       instance = this;
       populateComboBox();
    }
    
    public void fillEmploymentInformation() {
        
    }
    
    public void populateComboBox () {
        department_combobox.getItems().clear();
        List departments = BridgeUnit.getInstance().getAllDepartments();
        for (Iterator iterator = departments.iterator(); iterator.hasNext();) {
            Department next = (Department) iterator.next();
            department_combobox.getItems().add(next.getDepartment_name());
        }
    }

    public static AddEmploymentController getInstance() {
        return instance;
    }
    
    public void updateRecord () {
        if(AddEmploymentController.getInstance().updatebutton.getText().equalsIgnoreCase("save")){
            toUpdate.setDepartment(department_combobox.getSelectionModel().getSelectedItem().toString());
            toUpdate.setIsActive(isActive.isSelected());
            toUpdate.setStart_of_employment(Date.valueOf(employment_start.getValue()));
            toUpdate.setEnd_of_employment(Date.valueOf(employment_start.getValue()));
            toUpdate.setPosition(position.getText());
            toUpdate.setIsActive(isActive.isSelected());
            toUpdate.setStatus_of_employment(employment_status.getText());
            updatebutton.setText("Add");
        }
        else addEmploymentRecordToTable();
    }
    
    public void add () {
        if(updatebutton.getText().equalsIgnoreCase("save")){
           saveEmploymentRecord();
           updatebutton.setText("Add");
        }
        else if (updatebutton.getText().equalsIgnoreCase("add")){
            addEmploymentRecordToTable();
            updatebutton.setText("Add");
        }
    }
    
    public void saveEmploymentRecord () {
        UpdateEmployeeController.getInstance().getEmploymentRecordData().remove(toUpdate);
        toUpdate.setDepartment(department_combobox.getSelectionModel().getSelectedItem());
        toUpdate.setPosition(position.getText());
        if(employment_start.getValue()!=null)
            toUpdate.setStart_of_employment(Date.valueOf(employment_start.getValue()));
        if(employment_end.getValue()!=null)
            toUpdate.setEnd_of_employment(Date.valueOf(employment_end.getValue()));
        toUpdate.setIsActive(isActive.isSelected());
        UpdateEmployeeController.getInstance().getEmploymentRecordData().add(toUpdate);
        UpdateEmployeeController.getInstance().addEmployment_stage.close();
        resetValues();
    }
    
    public void addEmploymentRecordToTable () {
        if(employment_start.getValue()==null){
            JOptionPane.showMessageDialog(null, "Employment start date must not be empty.");
            return;
        }
        
        EmploymentStatus es = new EmploymentStatus();
        es.setDepartment(department_combobox.getValue());
        if(employment_start.getValue()!=null)
        es.setStart_of_employment(Date.valueOf(employment_start.getValue()));
        if(employment_end.getValue()!=null)
        es.setEnd_of_employment(Date.valueOf(employment_end.getValue()));
        es.setPosition(position.getText());
        es.setIsActive(isActive.isSelected());
        es.setStatus_of_employment(employment_status.getText());
        UpdateEmployeeController.getInstance().getEmploymentRecordData().add(es);
        UpdateEmployeeController.getInstance().addEmployment_stage.close();
        resetValues();
    }
    
    
}
