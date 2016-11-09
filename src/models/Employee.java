package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity ( name="employee" ) 
public class Employee {
    
    public Employee () {
        last_name = "";
        first_name = "";
        middle_name = "";
        gender = "";
        sss = "";
        tin = "";
        pagibig = "";
        philhealth = "";
        permanentaddress = "";
        currentaddress = "";
        mobilenumber = "";
        emailaddress = "";
        image_extension = "";
    }
    
    @Id
    private int biometrics_id;
    private String last_name;
    private String first_name;
    private String middle_name;
    private String gender;
    private String sss;
    private String tin;
    private String pagibig;
    private String philhealth;
    private String permanentaddress;
    private String currentaddress;
    private String mobilenumber;
    private String emailaddress;
    private Date birthday;
    private String image_extension;
    
    @ElementCollection
    @GenericGenerator(name="hilo-gen",strategy="hilo")
    @CollectionId (columns = {@Column(name="employmentRecordId")}, generator = "hilo-gen", type = @Type(type="long"))
    private Collection<EmploymentStatus> employment_records = new ArrayList<EmploymentStatus>();
    
    private CivilStatus civilstatus;
    private EducationBackground education_background;
    private LeaveCredits leave_credits;
    
    @ElementCollection
    @JoinTable (name="attendance_pivot_table",joinColumns = @JoinColumn(name="employee_id"))
    @GenericGenerator(name="hilo-gen",strategy="hilo")
    @CollectionId (columns = {@Column(name="attendanceId")}, generator = "hilo-gen", type = @Type(type="long"))
    @OrderBy("date ASC")
    private Collection<DailyAttendance> attendance_record = new ArrayList<DailyAttendance>();
    
//    @ElementCollection
//    @GenericGenerator(name="hilo-gen",strategy="hilo")
//    @CollectionId (columns = {@Column(name="attendanceRecordId")}, generator = "hilo-gen", type = @Type(type="long"))
//    private Collection<DailyAttendance> attendance_record_history = new ArrayList<DailyAttendance>();

    public EducationBackground getEducation_background() {
        return education_background;
    }

   
    public void setEducation_background(EducationBackground education_background) {
        this.education_background = education_background;
    }

    public int getBiometrics_id() {
        return biometrics_id;
    }

    public void setBiometrics_id(int biometrics_id) {
        this.biometrics_id = biometrics_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage_extension() {
        return image_extension;
    }

    public void setImage_extension(String image_extension) {
        this.image_extension = image_extension;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSss() {
        return sss;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public LeaveCredits getLeave_credits() {
        return leave_credits;
    }

    public void setLeave_credits(LeaveCredits leave_credits) {
        this.leave_credits = leave_credits;
    }

    public String getPagibig() {
        return pagibig;
    }

    public void setPagibig(String pagibig) {
        this.pagibig = pagibig;
    }

    public String getPhilhealth() {
        return philhealth;
    }

    public void setPhilhealth(String philhealth) {
        this.philhealth = philhealth;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Collection<EmploymentStatus> getEmployment_records() {
        return employment_records;
    }

    public void setEmployment_records(Collection<EmploymentStatus> employment_records) {
        this.employment_records = employment_records;
    }

    public CivilStatus getCivilstatus() {
        return civilstatus;
    }

    public void setCivilstatus(CivilStatus civilstatus) {
        this.civilstatus = civilstatus;
    }

    public String getPermanentaddress() {
        return permanentaddress;
    }

    public void setPermanentaddress(String permanentaddress) {
        this.permanentaddress = permanentaddress;
    }

    public String getCurrentaddress() {
        return currentaddress;
    }

    public void setCurrentaddress(String currentaddress) {
        this.currentaddress = currentaddress;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public Collection<DailyAttendance> getAttendance_record() {
        return attendance_record;
    }

    public void setAttendance_record(Collection<DailyAttendance> attendance_record) {
        this.attendance_record = attendance_record;
    }

}
