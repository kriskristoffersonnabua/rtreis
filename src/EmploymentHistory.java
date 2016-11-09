/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.EmploymentStatus;

/**
 *
 * @author kc
 */
public class EmploymentHistory implements Initializable{
    private static EmploymentHistory instance;
    
    public static EmploymentHistory getInstance() {
        return instance;
    }
    
    @FXML TableView<EmploymentStatus> employment_table;
    ObservableList<EmploymentStatus> employmenthistory= FXCollections.observableArrayList();
    private @FXML TableColumn<EmploymentStatus, Date> contractStartColumn;
    private @FXML TableColumn<EmploymentStatus, Date> contractEndColumn;
    @FXML
    public TableColumn<EmploymentStatus, String> departmentColumn;
    @FXML
    public TableColumn<EmploymentStatus, String> employmentStatusColumn;
    @FXML
    public TableColumn<EmploymentStatus, String> positionColumn;
    @FXML
    public TableColumn<EmploymentStatus, Boolean> activeColumn;
    
    public void setItems(List records){
         employmenthistory.clear();
         employmenthistory.addAll(records);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        contractStartColumn.setCellValueFactory(new PropertyValueFactory<EmploymentStatus, Date>("start_of_employment"));
        contractEndColumn.setCellValueFactory(new PropertyValueFactory<EmploymentStatus, Date>("end_of_employment"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<EmploymentStatus, String>("department"));
        employmentStatusColumn.setCellValueFactory(new PropertyValueFactory<EmploymentStatus, String>("status_of_employment"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<EmploymentStatus, String>("position"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<EmploymentStatus, Boolean>("isActive"));
        employment_table.setItems(employmenthistory);
    }
    
}
