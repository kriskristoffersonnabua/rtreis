/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javax.swing.JOptionPane;
import models.DailyAttendance;
import models.Employee;

/**
 *
 * @author kc
 */
public class LeaveController implements Initializable {

    @FXML ComboBox<String> type;
    @FXML DatePicker from;
    @FXML DatePicker to;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.getItems().addAll("Sick Leave","Vacation Leave","Birthday Leave","Maternity Leave","Parental Leave","Special Leave","VAWC Leave","Paternity Leave");
    }
    
    public void arrangeleave () {
        Employee e = BridgeUnit.getInstance().getEmployee(MainController.getInstance().overviewTable.getSelectionModel().getSelectedItem().getBiometrics_id());
        LocalDate startDate = from.getValue();
        LocalDate endDate = to.getValue();
        Calendar startingDate = new GregorianCalendar(startDate.getYear(),startDate.getMonthValue()-1,startDate.getDayOfMonth());
        Calendar endingDate = new GregorianCalendar(endDate.getYear(),endDate.getMonthValue()-1,endDate.getDayOfMonth());
        while(startingDate.compareTo(endingDate)!=1){
            Collection<DailyAttendance> record = e.getAttendance_record();
            for (Iterator<DailyAttendance> iterator = record.iterator(); iterator.hasNext();) {
                DailyAttendance next = iterator.next();
                if(next.date == startingDate.getTimeInMillis()){
                    next.setDayType(type.getSelectionModel().getSelectedItem().toString());
                    if(type.getSelectionModel().getSelectedItem().contains("Sick")){
                        double sl = e.getLeave_credits().getSick_leave();
                        if(sl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough sick leave credits.");
                        }
                        e.getLeave_credits().setSick_leave((float) (sl-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("Vacation")){
                        double vl = e.getLeave_credits().getVacation_leave();
                        if(vl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough vacation leave credits.");
                        }
                         e.getLeave_credits().setVacation_leave((float) (vl-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("Birthday")){
                        double bl = e.getLeave_credits().getBirthday_leave();
                        if(bl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough birthday leave credits.");
                        }
                         e.getLeave_credits().setBirthday_leave((int) (bl-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("Maternity")){
                        double ml = e.getLeave_credits().getMaternity_leave();
                        if(ml==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough maternity leave credits.");
                        }
                         e.getLeave_credits().setMaternity_leave((int) (ml-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("Parental")){
                        double pl = e.getLeave_credits().getParental_leave();
                        if(pl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough parental leave credits.");
                        }
                         e.getLeave_credits().setParental_leave((float) (pl-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("Special")){
                        double pl = e.getLeave_credits().getSpecial_leave();
                        if(pl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough special leave credits.");
                        }
                         e.getLeave_credits().setSpecial_leave((float) (pl-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("VAW")){
                        double pl = e.getLeave_credits().getVawc_leave();
                        if(pl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough vawc leave credits.");
                        }
                         e.getLeave_credits().setVawc_leave((float) (pl-1));
                        continue;
                    }
                    else if(type.getSelectionModel().getSelectedItem().contains("Paternity")){
                        double pl = e.getLeave_credits().getPaternity_leave();
                        if(pl==0){
                            JOptionPane.showMessageDialog(null, "Don't have enough paternity leave credits.");
                        }
                         e.getLeave_credits().setPaternity_leave((float) (pl-1));
                        continue;
                    }
                }
            }
            startingDate.add(Calendar.DAY_OF_YEAR, 1);
        }
        BridgeUnit.getInstance().getRunningSession().update(e);
        BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
        BridgeUnit.getInstance().closeSession();
    }
}
