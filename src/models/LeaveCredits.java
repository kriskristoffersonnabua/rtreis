package models;

import javax.persistence.Embeddable;

@Embeddable
public class LeaveCredits {
    private float sick_leave;
    private float vacation_leave;
    private int birthday_leave;
    private int maternity_leave;
    private float parental_leave;
    private float special_leave;
    private float vawc_leave;
    private float paternity_leave;

    public LeaveCredits () {
        sick_leave = 0;
        vacation_leave = 0;
        birthday_leave = 0;
        maternity_leave = 0;
        paternity_leave = 0;
        special_leave = 0;
        vawc_leave = 0;
        parental_leave = 0;
    }
    
    public float getSick_leave() {
        return sick_leave;
    }

    public void setSick_leave(float sick_leave) {
        this.sick_leave = sick_leave;
    }

    public float getVacation_leave() {
        return vacation_leave;
    }

    public void setVacation_leave(float vacation_leave) {
        this.vacation_leave = vacation_leave;
    }

    public int getBirthday_leave() {
        return birthday_leave;
    }

    public void setBirthday_leave(int birthday_leave) {
        this.birthday_leave = birthday_leave;
    }

    public int getMaternity_leave() {
        return maternity_leave;
    }

    public void setMaternity_leave(int maternity_leave) {
        this.maternity_leave = maternity_leave;
    }

    public float getParental_leave() {
        return parental_leave;
    }

    public void setParental_leave(float parental_leave) {
        this.parental_leave = parental_leave;
    }

    public float getSpecial_leave() {
        return special_leave;
    }

    public void setSpecial_leave(float special_leave) {
        this.special_leave = special_leave;
    }

    public float getVawc_leave() {
        return vawc_leave;
    }

    public void setVawc_leave(float vawc_leave) {
        this.vawc_leave = vawc_leave;
    }

    public float getPaternity_leave() {
        return paternity_leave;
    }

    public void setPaternity_leave(float paternity_leave) {
        this.paternity_leave = paternity_leave;
    }

}
