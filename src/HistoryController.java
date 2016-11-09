/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javax.swing.JOptionPane;
import models.DailyAttendance;
import models.Employee;

/**
 *
 * @author kc
 */
public class HistoryController implements Initializable{
    private static HistoryController instance;

    boolean toPresent = false;
    
    public static HistoryController getInstance() {
        return instance;
    }
    
    
    
    @FXML DatePicker from;
    @FXML DatePicker to;
    
    Employee employee;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
    }

    public boolean isToPresent() {
        return toPresent;
    }

    public void setToPresent(boolean toPresent) {
        this.toPresent = toPresent;
    }
    
    public void commandHandler() {
        if(toPresent){
            undoToHistory();
        }
        else toHistory();
    }
    
    public void close () {
        AttendanceViewController.getInstance().stage.close();
    }
    
    
    public void toHistory () {
        if(from.getValue()==null){
            JOptionPane.showMessageDialog(null, "Start date should not be blank.");
            return;
        }
        if(to.getValue()==null){
            JOptionPane.showMessageDialog(null, "End date should not be blank.");
            return;
        }
        AttendanceViewController.getInstance().progbar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        toHistoryWorker();
        
        AttendanceViewController.getInstance().stage.close();
    }
    
    public void toHistoryWorker () {
        Service<Void> backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        LocalDate startDate = from.getValue();
                        LocalDate endDate = to.getValue();
                        List employees = BridgeUnit.getInstance().getAllEmployees();
                        for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
                            Calendar startingDate = new GregorianCalendar(startDate.getYear(),startDate.getMonthValue()-1,startDate.getDayOfMonth());
                            Calendar endingDate = new GregorianCalendar(endDate.getYear(),endDate.getMonthValue()-1,endDate.getDayOfMonth());

                            Employee next = (Employee) iterator.next();
                            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
                            employee = BridgeUnit.getInstance().getEmployee(next.getBiometrics_id());
                            Collection records = employee.getAttendance_record();

                            if(records!=null){
                                while(startingDate.compareTo(endingDate)!=1){
                                    for (Iterator iterator1 = records.iterator(); iterator1.hasNext();) {
                                        DailyAttendance next1 = (DailyAttendance) iterator1.next();
                                        if(startingDate.getTimeInMillis()==next1.date){
                                            next1.setIsHistory(true);
                                        }
                                    }
                                    startingDate.add(Calendar.DAY_OF_YEAR, 1);
                                }
                            }

                            BridgeUnit.getInstance().getRunningSession().update(employee);
                            BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                        }
                        return null;
                    }
                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AttendanceViewController.getInstance().progbar.setProgress(0);
                JOptionPane.showMessageDialog(null, "Sending to history records completed. :)");
            }
        });
        backgroundThread.restart();
    }
    
    public void undoToHistory () {
        if(from.getValue()==null){
            JOptionPane.showMessageDialog(null, "Start date should not be blank.");
            return;
        }
        if(to.getValue()==null){
            JOptionPane.showMessageDialog(null, "End date should not be blank.");
            return;
        }
        AttendanceViewController.getInstance().progbar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        undoHistoryWorker();
    }
    public void undoHistoryWorker () {
        Service<Void> backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        LocalDate startDate = from.getValue();
                        LocalDate endDate = to.getValue();
                        List employees = BridgeUnit.getInstance().getAllEmployees();
                        for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
                            Calendar startingDate = new GregorianCalendar(startDate.getYear(),startDate.getMonthValue()-1,startDate.getDayOfMonth());
                            Calendar endingDate = new GregorianCalendar(endDate.getYear(),endDate.getMonthValue()-1,endDate.getDayOfMonth());

                            Employee next = (Employee) iterator.next();
                            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
                            employee = BridgeUnit.getInstance().getEmployee(next.getBiometrics_id());
                            Collection records = employee.getAttendance_record();

                            if(records!=null){
                                while(startingDate.compareTo(endingDate)!=1){
                                    for (Iterator iterator1 = records.iterator(); iterator1.hasNext();) {
                                        DailyAttendance next1 = (DailyAttendance) iterator1.next();
                                        if(startingDate.getTimeInMillis()==next1.date){
                                            next1.setIsHistory(false);
                                        }
                                    }
                                    startingDate.add(Calendar.DAY_OF_YEAR, 1);
                                }
                            }

                            BridgeUnit.getInstance().getRunningSession().update(employee);
                            BridgeUnit.getInstance().getRunningSession().getTransaction().commit();
                        }
                        return null;
                    }
                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AttendanceViewController.getInstance().progbar.setProgress(0);
                from.valueProperty().set(null);
                to.valueProperty().set(null);
                JOptionPane.showMessageDialog(null, "Sending to present records completed. :)");
            }
        });
        backgroundThread.restart();
    }
    
}
