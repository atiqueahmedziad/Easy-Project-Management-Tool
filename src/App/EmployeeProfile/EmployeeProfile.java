package App.EmployeeProfile;

import App.Connect;

import App.IntroPageAdmin.IntroPageAdmin;
import App.IntroPageEmployee.IntroPageEmployee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ResourceBundle;


public class EmployeeProfile implements Initializable {
    private int empId;
    private String UserRole;

    public String getUserRole(){ return UserRole; }

    public void setUserRole(String userRole){ this.UserRole=userRole; }

    public int getEmpId(){ return empId; }

    public void setEmpId(int adminid){ this.empId =adminid; }

    public JFXButton homeBackBtn;
    public JFXButton editProfileBtn;
    public JFXButton saveBtn;
    public JFXTextField empIdText;
    public JFXTextField empNameText;
    public JFXTextField empUsernameText;
    public JFXTextField empPassText;
    public JFXTextField empDesText;
    public JFXTextField empEmailText;
    public JFXTextField empContactText;

    public Label confirmationMsg;

    public void setEmpInfo() throws SQLException {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM EMPLOYEE WHERE id="+ empId;
        String sql2 = "SELECT * FROM EMPLOYEE_AUTH WHERE id="+ empId;

        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String desig = rs.getString("designation");
            String email = rs.getString("email");
            String contact = rs.getString("contact");

            empNameText.setText(name);
            empIdText.setText(String.valueOf(id));
            empDesText.setText(desig);
            empEmailText.setText(email);
            empContactText.setText(contact);
        }

        ResultSet rs2 = statement.executeQuery(sql2);

        while(rs2.next()){
            String username = rs2.getString("username");
            String pass = rs2.getString("password");

            empUsernameText.setText(username);
            empPassText.setText(pass);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setVisible(false);
        saveBtn.setDisable(true);

        empNameText.setEditable(false);
        empPassText.setEditable(false);
        empDesText.setEditable(false);
        empContactText.setEditable(false);
        empEmailText.setEditable(false);
    }

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../IntroPageEmployee/IntroPageEmployee.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            IntroPageEmployee introPageEmployee = Loader.getController();
            introPageEmployee.setEmployeeId(empId);
            introPageEmployee.setUserRole(getUserRole());
            introPageEmployee.getEmployeeName(empId);
            introPageEmployee.getProjectTableData();

            Parent p = Loader.getRoot();
            Stage stage = (Stage) homeBackBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }


    public void editProfileBtnAction(ActionEvent event) {
        if(event.getSource() == editProfileBtn){
            saveBtn.setDisable(false);
            saveBtn.setVisible(true);

            empNameText.setEditable(true);
            empPassText.setEditable(true);
            empEmailText.setEditable(true);
            empContactText.setEditable(true);
        }
    }

    public void saveBtnAction(ActionEvent event) {
        if(event.getSource() == saveBtn){
            String empName = empNameText.getText();
            String empPass = empPassText.getText();
            String empDes = empDesText.getText();
            String empEmail = empEmailText.getText();
            String empContact = empContactText.getText();

            if(empName.trim().isEmpty() || empPass.trim().isEmpty() || empDes.trim().isEmpty()){
                confirmationMsg.setText("Please fill up the form correctly.");
                return;
            }

            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            try{
                String sql = "UPDATE EMPLOYEE set name=?, designation=?, email =?, contact =? WHERE id=?";

                PreparedStatement preparedStmt = connection.prepareStatement(sql);
                preparedStmt.setString(1, empName);
                preparedStmt.setString(2, empDes);
                preparedStmt.setString(3, empEmail);
                preparedStmt.setString(4, empContact);
                preparedStmt.setInt(5, empId);

                preparedStmt.executeUpdate();

                String sql2 = "UPDATE EMPLOYEE_AUTH set password=? WHERE id=?";

                PreparedStatement ps_auth = connection.prepareStatement(sql2);
                ps_auth.setString(1,empPass);
                ps_auth.setInt(2, empId);

                ps_auth.executeUpdate();

                saveBtn.setDisable(true);
                saveBtn.setVisible(false);
                empNameText.setEditable(false);
                empPassText.setEditable(false);
                empDesText.setEditable(false);
                empEmailText.setEditable(false);
                empContactText.setEditable(false);

                confirmationMsg.setStyle("-fx-text-fill: #24bb71");
                confirmationMsg.setText("Saved Successfully!");

            }catch (Exception e){
                e.printStackTrace();
                confirmationMsg.setText(e.getMessage());
            }
        }
    }
}
