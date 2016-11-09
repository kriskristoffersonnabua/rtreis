package models;

import com.sun.java.browser.plugin2.liveconnect.v1.Bridge;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.scene.control.ComboBox;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import models.ClockingSchedule;

@Embeddable
public class DailyAttendance {

    /************************************
    ***************DATA MODEL************
    *************************************/
    //to check if it is night differential
    public long date;
    public long clockInDate;
    public long clockOutDate;
    public boolean isNightDiff;
    //to check if it is holiday
    public boolean isRegularHoliday;
    public boolean isSpecialHoliday;
    //to check if to show in attendance view, if true store to history 
    public boolean isHistory;
    //clock in data hours & minutes
    public long timeIn;
    public long brk;
    public long resume;
    public long timeOut;
    // work data
    public long workinghours;
    public long lateIn;
    public long underTime;
    
    public long overtime;
    
    // schedule 
    public int schedule_id;
    public String schedule;
    
    public String dayType;
    public String remarks;
    
    public String getDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
        return dateFormatter.format(new Date(date));
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getClockInDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
        return dateFormatter.format(new Date(clockInDate));
    }

    public void setClockInDate(long clockInDate) {
        this.clockInDate = clockInDate;
    }

    public String getClockOutDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
        return dateFormatter.format(new Date(clockOutDate));
    }

    public void setClockOutDate(long clockOutDate) {
        this.clockOutDate = clockOutDate;
    }

    public boolean isIsNightDiff() {
        return isNightDiff;
    }

    public void setIsNightDiff(boolean isNightDiff) {
        this.isNightDiff = isNightDiff;
    }

    public void setIsRegularHoliday(boolean isRegularHoliday) {
        this.isRegularHoliday = isRegularHoliday;
    }

    public void setIsSpecialHoliday(boolean isSpecialHoliday) {
        this.isSpecialHoliday = isSpecialHoliday;
    }

    

    public boolean isIsHistory() {
        return isHistory;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public String getTimeIn() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a");
        return timeIn == 0 ? "" : dateFormatter.format(new Date(timeIn));
    }

    public void setTimeIn(long timeIn) {
        this.timeIn = timeIn;
    }

    public String getBrk() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a");
        return brk == 0 ? "" : dateFormatter.format(new Date(brk));
    }

    public void setBrk(long brk) {
        this.brk = brk;
    }

    public String getResume() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a");
        return resume == 0 ? "" : dateFormatter.format(new Date(resume));
    }

    public void setResume(long resume) {
        this.resume = resume;
    }

    public String getTimeOut() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a");
        return timeOut == 0 ? "" :dateFormatter.format(new Date(timeOut));
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public String getWorkinghours() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm");
        return workinghours == 0 ? "" :dateFormatter.format(new Date(workinghours));
    }

    public void setWorkinghours(long workinghours) {
        this.workinghours = workinghours;
    }

    public String getLateIn() {
        int h = 0;
        int m = (int) Math.floor(lateIn/60000);
        if(m>=60){
           h += m/60;
           m -= (m/60)*60;
        }
        
        String hours = h+"";
        String minutes = m+"";
        return lateIn == 0 ? "" :hours+"."+minutes;
    }

    public void setLateIn(long lateIn) {
        this.lateIn = lateIn;
    }

    public String getUnderTime() {
        int h = 0;
        int m = (int) Math.floor(underTime/60000);
        if(m>=60){
            h += m/60;
            m -= (m/60)*60;
        }
        String hours = h+"";
        String minutes = m+"";
        return underTime == 0 ? "" :hours+"."+minutes;
    }

    public void setUnderTime(long underTime) {
        this.underTime = underTime;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
        this.schedule = schedule+"";
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSchedule() {
        String sched = schedule_id+"";
        
        return sched;
    }

    public String getOvertime() {
        int h = 0;
        int m = (int) Math.floor(overtime/60000);
        if(m>=60){
           h += m/60;
           m -= (m/60)*60;
        }
        
        String hours = h+"";
        String minutes = m+"";
        return overtime == 0 ? "" :hours+":"+minutes;
    }

    public void setOvertime(long overtime) {
        this.overtime = overtime;
    }

    
}
