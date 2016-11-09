/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Kaycee
 */
@Embeddable
public class EmploymentStatus {
   private String department;
    private Date start_of_employment;
    private Date end_of_employment;
    private String position;
    private String status_of_employment;
    private boolean isActive;

    public EmploymentStatus () {
        department = "";
        position = "";
        status_of_employment = "";
    }
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getStart_of_employment() {
        return start_of_employment;
    }

    public void setStart_of_employment(Date start_of_employment) {
        this.start_of_employment = start_of_employment;
    }

    public Date getEnd_of_employment() {
        return end_of_employment;
    }

    public void setEnd_of_employment(Date end_of_employment) {
        this.end_of_employment = end_of_employment;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus_of_employment() {
        return status_of_employment;
    }

    public void setStatus_of_employment(String status_of_employment) {
        this.status_of_employment = status_of_employment;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
}
