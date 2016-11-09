package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "ClockingSchedule.getAll", query = "from ClockingSchedule")
public class ClockingSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int clock_id;
    private String clocking_definition;
    private boolean isNightDifferential;
    private String timein;
    private String timeout;

    public int getClock_id() {
        return clock_id;
    }

    public void setClock_id(int clock_id) {
        this.clock_id = clock_id;
    }

    public String getClocking_definition() {
        return clocking_definition;
    }

    public void setClocking_definition(String clocking_definition) {
        this.clocking_definition = clocking_definition;
    }

    public boolean isIsNightDifferential() {
        return isNightDifferential;
    }

    public void setIsNightDifferential(boolean isNightDifferential) {
        this.isNightDifferential = isNightDifferential;
    }

    public String getTimein() {
        return timein;
    }

    public void setTimein(String timein) {
        this.timein = timein;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
    
}
