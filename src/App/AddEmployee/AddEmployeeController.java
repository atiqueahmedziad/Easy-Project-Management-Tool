package App.AddEmployee;


import App.Alertbox;
import App.Connect;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {

    Stage stage;
    Parent root;
    private int adminId;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    private String userRole;

    public JFXTextField getEmployee_name() {
        return employee_name;
    }

    public JFXTextField getEmployee_username() {
        return  employee_username;
    }

    public JFXPasswordField getEmployee_password() {
        return employee_password;
    }

    public JFXTextField getEmployee_email() {
        return employee_email;
    }

    public JFXTextField getEmployee_contact() {
        return employee_contact;
    }

    public JFXTextField getEmployee_designation() {
        return employee_designation;
    }

    public JFXTextField getEmployee_department() {
        return employee_department;
    }


    @FXML
    private Button AddEmployeeButton;
    @FXML
    private JFXTextField employee_username;
    @FXML
    private JFXPasswordField employee_password;
    @FXML
    private JFXTextField employee_name;
    @FXML
    private JFXTextField employee_email;
    @FXML
    private JFXTextField employee_contact;
    @FXML
    private JFXTextField employee_designation;
    @FXML
    private JFXTextField employee_department;
    @FXML
    JFXComboBox<String>boxEmpGender = new JFXComboBox<>();
    @FXML
    private JFXTextField employee_id;
    private int empid;

    public Label errorMsg;

    private void setEmployeeId() throws Exception{

        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        Statement statement = connection.createStatement();
        String sql1 = "SELECT MAX(id) as id FROM EMPLOYEE";
        ResultSet rs = statement.executeQuery(sql1);
        if(rs.next()) {
             empid = rs.getInt("id");
        }


        employee_id.setText(Integer.toString(empid+1));
    }

    @FXML
    private void AddEmployee(javafx.event.ActionEvent event) throws Exception {
        insertEmployee();
    }

    private void insertEmployee() {

        String employeeName = getEmployee_name().getText();
        String department = getEmployee_department().getText();
        String password = getEmployee_password().getText();
        String username = getEmployee_username().getText();

        if(employeeName.trim().isEmpty() || department.trim().isEmpty() || password.trim().isEmpty() || username.trim().isEmpty()){
            errorMsg.setText("Please fillup the form correctly.");
            return;
        }


        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        PreparedStatement ps = null;
        PreparedStatement psa = null;


        try {

            String sql = "insert into EMPLOYEE(name, email, contact, department, designation, gender) values (?,?,?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, getEmployee_name().getText());
            ps.setString(2, getEmployee_email().getText());
            ps.setString(3, getEmployee_contact().getText());
            ps.setString(4, getEmployee_department().getText());
            ps.setString(5, getEmployee_designation().getText());
            ps.setString(6, boxEmpGender.getValue());
            ps.executeUpdate();

            ps.close();

            String sql_auth = "insert into EMPLOYEE_AUTH(username, password) values (?,?)";
            psa = connection.prepareStatement(sql_auth);
            psa.setString(1, getEmployee_name().getText());
            psa.setString(2, getEmployee_password().getText());
            psa.executeUpdate();

            psa.close();

            Alertbox.display("Confirmation","Employee named "+getEmployee_name().getText()+" has been added.");

        } catch (Exception e) {
            e.printStackTrace();
            errorMsg.setText(e.getMessage());
        }

        Stage stage = (Stage) AddEmployeeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setEmployeeId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boxEmpGender.getItems().addAll("Male","Female");
    }
}
