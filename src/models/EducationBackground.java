package models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class EducationBackground {
    
    @Column ( name="elementary" )
    private String primary;
    private String primary_address;
    private String year_graduated_primary;
    private String secondary;
    private String secondary_address;
    private String year_graduated_secondary;
    private String tertiary;
    private String tertiary_address;
    private String year_graduated_tertiary;
    private String others;

    public EducationBackground () {
        primary = "";
        primary_address = "";
        year_graduated_primary = "";
        secondary = "";
        secondary_address = "";
        year_graduated_secondary = "";
        tertiary = "";
        tertiary_address = "";
        year_graduated_tertiary = "";
        others = "";
    }
    
    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getPrimary_address() {
        return primary_address;
    }

    public void setPrimary_address(String primary_address) {
        this.primary_address = primary_address;
    }

    public String getYear_graduated_primary() {
        return year_graduated_primary;
    }

    public void setYear_graduated_primary(String year_graduated_primary) {
        this.year_graduated_primary = year_graduated_primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getSecondary_address() {
        return secondary_address;
    }

    public void setSecondary_address(String secondary_address) {
        this.secondary_address = secondary_address;
    }

    public String getYear_graduated_secondary() {
        return year_graduated_secondary;
    }

    public void setYear_graduated_secondary(String year_graduated_secondary) {
        this.year_graduated_secondary = year_graduated_secondary;
    }

    public String getTertiary() {
        return tertiary;
    }

    public void setTertiary(String tertiary) {
        this.tertiary = tertiary;
    }

    public String getTertiary_address() {
        return tertiary_address;
    }

    public void setTertiary_address(String tertiary_address) {
        this.tertiary_address = tertiary_address;
    }

    public String getYear_graduated_tertiary() {
        return year_graduated_tertiary;
    }

    public void setYear_graduated_tertiary(String year_graduated_tertiary) {
        this.year_graduated_tertiary = year_graduated_tertiary;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
    
 }
