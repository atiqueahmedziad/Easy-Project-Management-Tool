package App.IntroPageAdmin;

import javafx.beans.property.SimpleStringProperty;

public class Employee {

    private SimpleStringProperty id;
    private SimpleStringProperty employeeName;
    private SimpleStringProperty designation;

    public Employee(String id, String employeeName, String designation) {
        this.id = new SimpleStringProperty(id);
        this.employeeName = new SimpleStringProperty(employeeName);
        this.designation = new SimpleStringProperty(designation);
    }

    public String getEmployeeId() {
        return id.get();
    }

    public void setEmployeeId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = new SimpleStringProperty(employeeName);
    }

    public String getDesignation() {
        return designation.get();
    }

    public void setDesignation(String designation) {
        this.designation = new SimpleStringProperty(designation);
    }
}
