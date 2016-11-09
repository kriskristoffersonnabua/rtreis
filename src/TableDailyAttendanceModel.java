

import com.sun.java.browser.plugin2.liveconnect.v1.Bridge;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.ComboBox;
import models.ClockingSchedule;

public class TableDailyAttendanceModel {
    
    int year;
    int month;
    
    //to check if it is night differential
    String date;
    int clockInDate;
    int clockOutDate;
    boolean isNightDiff;
    
    //to check if it is holiday
    boolean isHoliday;
    
    //to check if to show in attendance view, if true store to history 
    boolean isHistory;
    
    //clock in data hours & minutes
    String timeIn;
    String brk;
    String resume;
    String timeOut;
    
    // work data
    int workinghours;
    int lateIn;
    int underTime;
    
    //remarks
    String remarks;
    
    // schedule 
    ComboBox<String> schedule;
    ComboBox<String> dayType;
    
    //record ID
    int record_id;
    
    public TableDailyAttendanceModel () {
        dayType = new ComboBox<String>();
        schedule = new ComboBox<String>();
                
        List schedule_list = BridgeUnit.getInstance().getSchedules();
        for (Iterator iterator = schedule_list.iterator(); iterator.hasNext();) {
            ClockingSchedule next = (ClockingSchedule) iterator.next();
            System.out.println(next.getClocking_definition());
            schedule.getItems().add(next.getClock_id()+" - "+next.getClocking_definition());
        }
        
        dayType.getItems().addAll("restday","workday","holiday");
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getClockInDate() {
        return clockInDate;
    }

    public void setClockInDate(int clockInDate) {
        this.clockInDate = clockInDate;
    }

    public int getClockOutDate() {
        return clockOutDate;
    }

    public void setClockOutDate(int clockOutDate) {
        this.clockOutDate = clockOutDate;
    }

    public boolean isIsNightDiff() {
        return isNightDiff;
    }

    public void setIsNightDiff(boolean isNightDiff) {
        this.isNightDiff = isNightDiff;
    }

    public boolean isIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean isIsHistory() {
        return isHistory;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getBrk() {
        return brk;
    }

    public void setBrk(String brk) {
        this.brk = brk;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public int getWorkinghours() {
        return workinghours;
    }

    public void setWorkinghours(int workinghours) {
        this.workinghours = workinghours;
    }

    public int getLateIn() {
        return lateIn;
    }

    public void setLateIn(int lateIn) {
        this.lateIn = lateIn;
    }

    public int getUnderTime() {
        return underTime;
    }

    public void setUnderTime(int underTime) {
        this.underTime = underTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ComboBox<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(ComboBox<String> schedule) {
        this.schedule = schedule;
    }

    public ComboBox<String> getDayType() {
        return dayType;
    }

    public void setDayType(ComboBox<String> dayType) {
        this.dayType = dayType;
    }
    
    
}
