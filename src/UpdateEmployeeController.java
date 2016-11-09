
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import models.CivilStatus;
import models.EducationBackground;
import models.Employee;
import models.EmploymentStatus;
import models.LeaveCredits;
import models.Child;
import models.DailyAttendance;

public class UpdateEmployeeController implements Initializable {
    
    private static UpdateEmployeeController instance;

    // employee class 
    private Employee employee;

    @FXML
    TextField biometricsId;
    @FXML
    TextField lastName;
    @FXML
    TextField firstName;
    @FXML
    TextField middleName;
    @FXML
    TextField sss;
    @FXML
    TextField tin;
    @FXML
    TextField pagibig;
    @FXML
    TextField philhealth;
    @FXML
    TextField gender;
    @FXML
    DatePicker birthday;
    @FXML
    TextField sick_leave;
    @FXML
    TextField vacation_leave;
    @FXML
    TextField birthday_leave;
    @FXML
    TextField maternity_leave;
    @FXML
    TextField parental_leave;
    @FXML
    TextField special_leave;
    @FXML
    TextField vawc_leave;
    @FXML
    TextField paternity_leave;
    @FXML
    TextField primary;
    @FXML
    TextField primary_address;
    @FXML
    TextField year_graduated_primary;
    @FXML
    TextField secondary;
    @FXML
    TextField secondary_address;
    @FXML
    TextField year_graduated_secondary;
    @FXML
    TextField tertiary;
    @FXML
    TextField tertiary_address;
    @FXML
    TextField year_graduated_tertiary;
    @FXML
    TextArea other_education;
    @FXML
    public TextField permanentaddress;
    @FXML
    public TextField currentaddress;
    @FXML
    public TextField mobilenumber;
    @FXML
    public TextField emailaddress;
    @FXML
    public TableView<EmploymentStatus> employment_table;
    @FXML
    public TableColumn<EmploymentStatus, Date> contractStartColumn;
    @FXML
    public TableColumn<EmploymentStatus, Date> contractEndColumn;
    @FXML
    public TableColumn<EmploymentStatus, String> departmentColumn;
    @FXML
    public TableColumn<EmploymentStatus, String> employmentStatusColumn;
    @FXML
    public TableColumn<EmploymentStatus, String> positionColumn;
    @FXML
    public TableColumn<EmploymentStatus, Boolean> activeColumn;
    public ObservableList<EmploymentStatus> employmentRecordData = FXCollections.observableArrayList();
    
    @FXML public TableView<Child> dependentsTable;
    @FXML
    public TableColumn<Child, String> dependentColumnOne;
    @FXML
    public TableColumn<Child, Date> dependentColumnTwo;
    ObservableList<Child> dependents = FXCollections.observableArrayList();
    
    @FXML public ComboBox<String> civilStatus;
    @FXML public TextField spouseName;
    @FXML public Button save;
    
    Stage stage;
    private Stage addDependent_stage;
    private Stage addEmployment_stage;
    private Stage editEmployment_stage;
    
    @FXML public TextField imagepath;
    String image_path;
    private File file;
    
     public void setPhoto () throws Exception {
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFileChooser fileOpener = new JFileChooser();
        int choice = fileOpener.showSaveDialog(fileOpener);
        if(choice == JFileChooser.APPROVE_OPTION){
            file = fileOpener.getSelectedFile();
            imagepath.setText(file.getAbsolutePath());
            System.out.println("this is the extenson of the image: "
                    +file.getName().substring(file.getName().length()-4, file.getName().length()));
        }
    }
    
    public void resetAll () {
        spouseName.clear();
        dependents.clear();
        employmentRecordData.clear();
        primary.clear();
        primary_address.clear();
        year_graduated_primary.clear();
        secondary.clear();
        secondary_address.clear();
        year_graduated_secondary.clear();
        tertiary.clear();
        tertiary_address.clear();
        year_graduated_tertiary.clear();
        sick_leave.clear();
        vacation_leave.clear();
        birthday_leave.clear();
        maternity_leave.clear();
        parental_leave.clear();
        special_leave.clear();
        vawc_leave.clear();
        biometricsId.clear();
        lastName.clear();
        firstName.clear();
        middleName.clear();
        sss.clear();
        tin.clear();
        pagibig.clear();
        philhealth.clear();
        gender.clear();
        birthday.setValue(null);
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
        dependentColumnOne.setCellValueFactory(new PropertyValueFactory<Child, String>("name"));
        dependentColumnTwo.setCellValueFactory(new PropertyValueFactory<Child, Date>("birthday"));
        employment_table.setItems(employmentRecordData);
        dependentsTable.setItems(dependents);
        
        civilStatus.getItems().clear();
        civilStatus.getItems().addAll("Single", "Married", "Widowed", "It's Complicated");
    }
    
    public void save () {
        if(save.getText().equalsIgnoreCase("save")) AddEmployee();
        else SaveEditEmployee();
    }
    
    public void SaveEditEmployee () {
        Employee e = BridgeUnit.getInstance().getEmployee(employee.getBiometrics_id());
        
        e.setBiometrics_id(Integer.valueOf(biometricsId.getText()));
        e.setLast_name(lastName.getText());
        e.setFirst_name(firstName.getText());
        e.setMiddle_name(middleName.getText());
        e.setGender(gender.getText());
        e.setSss(sss.getText());
        e.setPagibig(pagibig.getText());
        e.setPhilhealth(philhealth.getText());
        e.setTin(tin.getText());
        if(birthday.getValue()!=null)
        e.setBirthday(Date.valueOf(birthday.getValue()));
        e.setEmailaddress(emailaddress.getText());
        e.setMobilenumber(mobilenumber.getText());
        e.setPermanentaddress(permanentaddress.getText());
        e.setCurrentaddress(currentaddress.getText());
        
        e.getEmployment_records().addAll(employmentRecordData);
        for (Iterator<EmploymentStatus> iterator = employmentRecordData.iterator(); iterator.hasNext();) {
            EmploymentStatus next = iterator.next();
            System.out.println("Records");
            System.out.println(next);
        }
        
        CivilStatus cs = new CivilStatus();
        cs.setCivil_status(civilStatus.getSelectionModel().getSelectedItem());
        cs.setSpouse_name(spouseName.getText());
        cs.getChildren().addAll(dependents);
        e.setCivilstatus(cs);

        //education background
        EducationBackground eb = new EducationBackground();
        eb.setPrimary(primary.getText());
        eb.setPrimary_address(primary_address.getText());
        eb.setYear_graduated_primary(year_graduated_primary.getText());
        eb.setSecondary(secondary.getText());
        eb.setSecondary_address(secondary_address.getText());
        eb.setYear_graduated_secondary(year_graduated_secondary.getText());
        eb.setTertiary(tertiary.getText());
        eb.setTertiary_address(tertiary_address.getText());
        eb.setYear_graduated_tertiary(year_graduated_tertiary.getText());
        eb.setOthers(other_education.getText());
        e.setEducation_background(eb);
        
        //leave credits
        //leave credits
        LeaveCredits leave_credits = new LeaveCredits();
        if(!sick_leave.getText().equalsIgnoreCase(""))
        leave_credits.setSick_leave(Float.valueOf(sick_leave.getText()));
        if(!vacation_leave.getText().equalsIgnoreCase(""))
        leave_credits.setVacation_leave(Float.valueOf(vacation_leave.getText()));
        if(!birthday_leave.getText().equalsIgnoreCase(""))
        leave_credits.setBirthday_leave(Integer.valueOf(birthday_leave.getText()));
        if(!maternity_leave.getText().equalsIgnoreCase(""))
        leave_credits.setMaternity_leave(Integer.valueOf(maternity_leave.getText()));
        if(!parental_leave.getText().equalsIgnoreCase(""))
        leave_credits.setParental_leave(Float.valueOf(parental_leave.getText()));
        if(!special_leave.getText().equalsIgnoreCase(""))
        leave_credits.setSpecial_leave(Float.valueOf(special_leave.getText()));
        if(!vawc_leave.getText().equalsIgnoreCase(""))
        leave_credits.setVawc_leave(Float.valueOf(vawc_leave.getText()));
        if(!paternity_leave.getText().equalsIgnoreCase(""))
        leave_credits.setPaternity_leave(Float.valueOf(paternity_leave.getText()));
        e.setLeave_credits(leave_credits);
        
        if(file!=null){
            try {
                byte[] image = new  byte[(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(image);

                employee.setImage_extension(file.getName().substring(file.getName().length()-4, file.getName().length()));

                 new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL").mkdir();
                 new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL/img").mkdir();
                 File outputfile = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL");

                if(outputfile.isDirectory() && outputfile.exists()){ 
                    outputfile = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL/img/"+employee.getLast_name()+""+employee.getFirst_name()+".png");
                }
                else{
                    outputfile = new File(System.getProperty("user.dir")+"/Documents/RTRHOSPITAL/img/"+employee.getLast_name()+""+employee.getFirst_name()+".png");
                    outputfile.createNewFile();
                }
            

                FileOutputStream fos = new FileOutputStream(outputfile);
                fos.write(image);                    
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        BridgeUnit.getInstance().getRunningSession().update(e);
        BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
        BridgeUnit.getInstance().closeSession();
        resetAll();
        save.setText("Save");
        MainController.getInstance().stage.close();
        MainController.getInstance().refreshListOfEmployees();
    }
    
    public void AddEmployee() {
        //employee
        employee = new Employee();
        employee.setBiometrics_id(Integer.valueOf(biometricsId.getText()));
        employee.setLast_name(lastName.getText());
        employee.setFirst_name(firstName.getText());
        employee.setMiddle_name(middleName.getText());
        employee.setGender(gender.getText());
        employee.setSss(sss.getText());
        employee.setPagibig(pagibig.getText());
        employee.setPhilhealth(philhealth.getText());
        employee.setTin(tin.getText());
        System.out.println(birthday.getValue());
        if(birthday.getValue()!=null)
        employee.setBirthday(Date.valueOf(birthday.getValue()));
        employee.setEmailaddress(emailaddress.getText());
        employee.setMobilenumber(mobilenumber.getText());
        employee.setPermanentaddress(permanentaddress.getText());
        employee.setCurrentaddress(currentaddress.getText());
        
        
        if(file!=null){
            try {
            byte[] image = new  byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(image);
            
            employee.setImage_extension(file.getName().substring(file.getName().length()-4, file.getName().length()));
            new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL").mkdir();
            new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL/img").mkdir();
            File outputfile = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL");

                if(outputfile.isDirectory() && outputfile.exists()){ 
                    outputfile = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL/img/"+employee.getLast_name()+""+employee.getFirst_name()+".png");
                }
                else{
                    outputfile = new File(System.getProperty("user.dir")+"/Documents/RTRHOSPITAL/img/"+employee.getLast_name()+""+employee.getFirst_name()+".png");
                    outputfile.createNewFile();
                }
            
            FileOutputStream fos = new FileOutputStream(outputfile);
            fos.write(image);                    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
        
        employee.getEmployment_records().addAll(employmentRecordData);
        
        CivilStatus cs = new CivilStatus();
        cs.setCivil_status(civilStatus.getSelectionModel().getSelectedItem());
        cs.setSpouse_name(spouseName.getText());
        cs.getChildren().addAll(dependents);
        employee.setCivilstatus(cs);

        //education background
        EducationBackground eb = new EducationBackground();
        eb.setPrimary(primary.getText());
        eb.setPrimary_address(primary_address.getText());
        eb.setYear_graduated_primary(year_graduated_primary.getText());
        eb.setSecondary(secondary.getText());
        eb.setSecondary_address(secondary_address.getText());
        eb.setYear_graduated_secondary(year_graduated_secondary.getText());
        eb.setTertiary(tertiary.getText());
        eb.setTertiary_address(tertiary_address.getText());
        eb.setYear_graduated_tertiary(year_graduated_tertiary.getText());
        eb.setOthers(other_education.getText());
        employee.setEducation_background(eb);
        
        //leave credits
        LeaveCredits leave_credits = new LeaveCredits();
        if(!sick_leave.getText().equalsIgnoreCase(""))
        leave_credits.setSick_leave(Float.valueOf(sick_leave.getText()));
        if(!vacation_leave.getText().equalsIgnoreCase(""))
        leave_credits.setVacation_leave(Float.valueOf(vacation_leave.getText()));
        if(!birthday_leave.getText().equalsIgnoreCase(""))
        leave_credits.setBirthday_leave(Integer.valueOf(birthday_leave.getText()));
        if(!maternity_leave.getText().equalsIgnoreCase(""))
        leave_credits.setMaternity_leave(Integer.valueOf(maternity_leave.getText()));
        if(!parental_leave.getText().equalsIgnoreCase(""))
        leave_credits.setParental_leave(Float.valueOf(parental_leave.getText()));
        if(!special_leave.getText().equalsIgnoreCase(""))
        leave_credits.setSpecial_leave(Float.valueOf(special_leave.getText()));
        if(!vawc_leave.getText().equalsIgnoreCase(""))
        leave_credits.setVawc_leave(Float.valueOf(vawc_leave.getText()));
        if(!paternity_leave.getText().equalsIgnoreCase(""))
        leave_credits.setPaternity_leave(Float.valueOf(paternity_leave.getText()));
        employee.setLeave_credits(leave_credits);

        BridgeUnit.getInstance().save(employee);
        resetAll();
        MainController.getInstance().stage.close();
        MainController.getInstance().refreshListOfEmployees();
        AttendanceViewController.getInstance().refreshListOfEmployees();
        Writer.getInstance().refreshList();
        GenerateAttendanceController.getInstance().refreshList();
    }
    
    synchronized public void AddEmployeeFromTextFile(int id,String name) {
        //employee
        employee = new Employee();
        employee.setBiometrics_id(id);
        employee.setLast_name(name);
        employee.setFirst_name(firstName.getText());
        employee.setMiddle_name(middleName.getText());
        employee.setGender(gender.getText());
        employee.setSss(sss.getText());
        employee.setPagibig(pagibig.getText());
        employee.setPhilhealth(philhealth.getText());
        employee.setTin(tin.getText());
        System.out.println(birthday.getValue());
        if(birthday.getValue()!=null)
        employee.setBirthday(Date.valueOf(birthday.getValue()));
        employee.setEmailaddress(emailaddress.getText());
        employee.setMobilenumber(mobilenumber.getText());
        employee.setPermanentaddress(permanentaddress.getText());
        employee.setCurrentaddress(currentaddress.getText());
        
        
        if(file!=null){
            try {
            byte[] image = new  byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(image);
            
            employee.setImage_extension(file.getName().substring(file.getName().length()-4, file.getName().length()));
            
            File outputfile = new File(System.getProperty("user.dir")+"/src/resources/credentials/img/");
                
            if(outputfile.isDirectory() && outputfile.exists()){ 
                outputfile = new File(System.getProperty("user.dir")+"/src/resources/credentials/img/"+employee.getLast_name()+""+employee.getFirst_name()+".png");
            }
            else{
                new File(System.getProperty("user.dir")+"/src/resources/credentials/img/").mkdir();
                outputfile = new File(System.getProperty("user.dir")+"/src/resources/credentials/img/"+employee.getLast_name()+""+employee.getFirst_name()+".png");
                outputfile.createNewFile();
            }
            
            FileOutputStream fos = new FileOutputStream(outputfile);
            fos.write(image);                    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
        
        employee.getEmployment_records().addAll(employmentRecordData);
        
        CivilStatus cs = new CivilStatus();
        cs.setCivil_status(civilStatus.getSelectionModel().getSelectedItem());
        cs.setSpouse_name(spouseName.getText());
        cs.getChildren().addAll(dependents);
        employee.setCivilstatus(cs);

        //education background
        EducationBackground eb = new EducationBackground();
        eb.setPrimary(primary.getText());
        eb.setPrimary_address(primary_address.getText());
        eb.setYear_graduated_primary(year_graduated_primary.getText());
        eb.setSecondary(secondary.getText());
        eb.setSecondary_address(secondary_address.getText());
        eb.setYear_graduated_secondary(year_graduated_secondary.getText());
        eb.setTertiary(tertiary.getText());
        eb.setTertiary_address(tertiary_address.getText());
        eb.setYear_graduated_tertiary(year_graduated_tertiary.getText());
        eb.setOthers(other_education.getText());
        employee.setEducation_background(eb);
        
        //leave credits
        LeaveCredits leave_credits = new LeaveCredits();
        if(!sick_leave.getText().equalsIgnoreCase(""))
        leave_credits.setSick_leave(Float.valueOf(sick_leave.getText()));
        if(!vacation_leave.getText().equalsIgnoreCase(""))
        leave_credits.setVacation_leave(Float.valueOf(vacation_leave.getText()));
        if(!birthday_leave.getText().equalsIgnoreCase(""))
        leave_credits.setBirthday_leave(Integer.valueOf(birthday_leave.getText()));
        if(!maternity_leave.getText().equalsIgnoreCase(""))
        leave_credits.setMaternity_leave(Integer.valueOf(maternity_leave.getText()));
        if(!parental_leave.getText().equalsIgnoreCase(""))
        leave_credits.setParental_leave(Float.valueOf(parental_leave.getText()));
        if(!special_leave.getText().equalsIgnoreCase(""))
        leave_credits.setSpecial_leave(Float.valueOf(special_leave.getText()));
        if(!vawc_leave.getText().equalsIgnoreCase(""))
        leave_credits.setVawc_leave(Float.valueOf(vawc_leave.getText()));
        if(!paternity_leave.getText().equalsIgnoreCase(""))
        leave_credits.setPaternity_leave(Float.valueOf(paternity_leave.getText()));
        employee.setLeave_credits(leave_credits);

        BridgeUnit.getInstance().save(employee);
    }
    
    public void FillEditEmployeeView(Employee emp) {
        //reset all fields
        resetAll();
        Employee e = BridgeUnit.getInstance().getEmployee(emp.getBiometrics_id());
        biometricsId.setText(String.valueOf(e.getBiometrics_id()));
        lastName.setText(e.getLast_name());
        firstName.setText(e.getFirst_name());
        middleName.setText(e.getMiddle_name());
        gender.setText(e.getGender());
        sss.setText(e.getSss());
        pagibig.setText(e.getPagibig());
        philhealth.setText(e.getPhilhealth());
        tin.setText(e.getTin());
        if(e.getBirthday()!=null)
        birthday.setValue(e.getBirthday().toLocalDate());
        emailaddress.setText(e.getEmailaddress());
        mobilenumber.setText(e.getMobilenumber());
        permanentaddress.setText(e.getPermanentaddress());
        currentaddress.setText(e.getCurrentaddress());
        
        employmentRecordData.clear();
        employmentRecordData.addAll(e.getEmployment_records());
        
        CivilStatus cs = e.getCivilstatus();
        civilStatus.getSelectionModel().select(cs.getCivil_status());
        spouseName.setText(cs.getSpouse_name());
        dependents.clear();
        dependents.addAll(cs.getChildren());

        //education background
        EducationBackground eb = e.getEducation_background();
        primary.setText(eb.getPrimary());
        primary_address.setText(eb.getPrimary_address());
        year_graduated_primary.setText(eb.getYear_graduated_primary());
        secondary.setText(eb.getSecondary());
        secondary_address.setText(eb.getSecondary_address());
        year_graduated_secondary.setText(eb.getYear_graduated_secondary());
        tertiary.setText(eb.getTertiary());
        tertiary_address.setText(eb.getTertiary_address());
        year_graduated_tertiary.setText(eb.getYear_graduated_tertiary());
        other_education.setText(eb.getOthers());
        
        //leave credits
        LeaveCredits lc = e.getLeave_credits();
        sick_leave.setText(String.valueOf(lc.getSick_leave()));
        vacation_leave.setText(String.valueOf(lc.getVacation_leave()));
        birthday_leave.setText(String.valueOf(lc.getBirthday_leave()));
        maternity_leave.setText(String.valueOf(lc.getMaternity_leave()));
        parental_leave.setText(String.valueOf(lc.getParental_leave()));
        special_leave.setText(String.valueOf(lc.getSpecial_leave()));
        vawc_leave.setText(String.valueOf(lc.getVawc_leave()));
        paternity_leave.setText(String.valueOf(lc.getPaternity_leave()));
        
        employee = new Employee();
        employee = e;
        save.setText("Save Edit");
        BridgeUnit.getInstance().closeSession();
    }
    
    public void showAddChildren () {
        addDependent_stage = new Stage();
        addDependent_stage.setTitle("Dependent");
        addDependent_stage.setScene(CoreController.getInstance().add_child);
        addDependent_stage.show();
    }
    
    public void showAddEmploymentGUI () {
        addEmployment_stage = new Stage();
        addEmployment_stage.setTitle("Add Employment Record");
        addEmployment_stage.setScene(CoreController.getInstance().add_employment);
        addEmployment_stage.show();
    }
    
    public void removeEmploymentRecord () {
        EmploymentStatus emp = employment_table.getSelectionModel().getSelectedItem();
        getEmploymentRecordData().remove(emp);
    }

    public ObservableList<EmploymentStatus> getEmploymentRecordData() {
        return employmentRecordData;
    }

    public void setEmploymentRecordData(ObservableList<EmploymentStatus> employmentRecordData) {
        this.employmentRecordData = employmentRecordData;
    }

    public static UpdateEmployeeController getInstance() {
        return instance;
    }

    public ObservableList<Child> getDependents() {
        return dependents;
    }

    public void setDependents(ObservableList<Child> dependents) {
        this.dependents = dependents;
    }

    
}
