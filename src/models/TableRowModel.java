package models;

import javafx.scene.control.CheckBox;

public class TableRowModel {
    CheckBox checkbox;
    String name;
    int id;

    public TableRowModel(String name) {
        this.checkbox = new CheckBox();
        this.name = name;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
