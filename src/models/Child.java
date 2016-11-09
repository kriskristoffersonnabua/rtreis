package models;

import java.sql.Date;
import javax.persistence.Embeddable;

@Embeddable
public class Child {

    public String name;
    public Date birthday;
    
    public Child () {
        name = "";
    }
    
    public Child(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}
