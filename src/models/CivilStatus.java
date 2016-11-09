package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Embeddable
public class CivilStatus {
    private String civil_status;
    private String spouse_name;
    @ElementCollection
    @GenericGenerator(name="hilo-gen",strategy="hilo")
    @CollectionId (columns = {@Column(name="childId")}, generator = "hilo-gen", type = @Type(type="long"))
    private Collection<Child> children = new ArrayList<Child>();

    public CivilStatus () {
        civil_status="";
        spouse_name="";
    }
    
    public String getCivil_status() {
        return civil_status;
    }

    public void setCivil_status(String civil_status) {
        this.civil_status = civil_status;
    }

    public String getSpouse_name() {
        return spouse_name;
    }

    public void setSpouse_name(String spouse_name) {
        this.spouse_name = spouse_name;
    }

    public Collection<Child> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }
    
}
