package App.AllEmployee;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleStringProperty empId;
    private SimpleStringProperty empName;
    private SimpleStringProperty empDesignation;
    private SimpleStringProperty empPhone;
    private SimpleStringProperty empEmail;
    private SimpleStringProperty empDept;

    public Employee(String empId, String empName, String empDept, String empDesignation, String empPhone, String empEmail) {
        this.empId = new SimpleStringProperty(empId);
        this.empName = new SimpleStringProperty(empName);
        this.empDesignation = new SimpleStringProperty(empDesignation);
        this.empPhone = new SimpleStringProperty(empPhone);
        this.empEmail = new SimpleStringProperty(empEmail);
        this.empDept = new SimpleStringProperty(empDept);
    }

    public String getEmpId() {
        return empId.get();
    }

    public void setEmpId(String id) {
        this.empId = new SimpleStringProperty(id);
    }

    public String getEmpName() {
        return empName.get();
    }

    public void setEmpName(String empName) {
        this.empName = new SimpleStringProperty(empName);
    }

    public String getEmpDesignation() {
        return empDesignation.get();
    }

    public void setEmpDesignation(String empDesignation) {
        this.empDesignation = new SimpleStringProperty(empDesignation);
    }

    public String getEmpPhone() {
        return empPhone.get();
    }

    public void setEmpPhone(String phone) {
        this.empPhone = new SimpleStringProperty(phone);
    }

    public String getEmpEmail() {
        return empEmail.get();
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = new SimpleStringProperty(empEmail);
    }

    public String getEmpDept(){ return empDept.get(); }

    public void setEmpDept(String dept){ this.empDept = new SimpleStringProperty(dept); }
}
