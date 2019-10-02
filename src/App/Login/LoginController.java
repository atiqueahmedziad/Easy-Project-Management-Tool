package App.Login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import App.Connect;
import App.IntroPageEmployee.IntroPageEmployee;
import App.ProjectSummary.ProjectSummaryController;
import App.IntroPageAdmin.IntroPageAdmin;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    Stage stage;
    Parent root;

    @FXML
    private Button loginbutton;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label isConnected;

    public JFXRadioButton adminToggle;
    public JFXRadioButton employeeToggle;

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }

    private int EmployeeId;

    public String userRole;

    public String getUserRole() {
        return userRole;
    }

    @FXML
    private void Login(ActionEvent actionEvent) throws IOException {

        if(!employeeToggle.isSelected() && !adminToggle.isSelected()){
            isConnected.setText("Select Admin or Employee from above");
            return;
        }

        FXMLLoader Loader = new FXMLLoader();

        if(userRole.matches("ADMIN_AUTH")){
            try {
                Connect connect = new Connect();
                Connection connection=connect.getConnection();
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM ADMIN_AUTH WHERE username = '" + username.getText() + "' AND password = '" + password.getText() + "'";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    //load up OTHER FXML document
                    Loader.setLocation(getClass().getResource("../IntroPageAdmin/intropageadmin.fxml"));
                    int adminid = resultSet.getInt("id");
                    try{
                        Loader.load();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    IntroPageAdmin introPageAdmin = Loader.getController();
                    introPageAdmin.setAdminId(adminid);
                    introPageAdmin.setUserRole(getUserRole());
                    introPageAdmin.getAdminName(adminid);

                    Parent p = Loader.getRoot();
                    stage = (Stage) adminToggle.getScene().getWindow();
                    Scene scene = new Scene(p);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    // If username & password doesn't match
                    isConnected.setText("Username / password is wrong!");
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            try {
                Connect connect = new Connect();
                Connection connection = connect.getConnection();
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM EMPLOYEE_AUTH WHERE username = '" + username.getText() + "' AND password = '" + password.getText() + "'";
                ResultSet resultSet = statement.executeQuery(sql);
                // Resultset contains all the username and password
                // if the username and password from database matches only then intropage loads
                if (resultSet.next()) {
                    //load up OTHER FXML document
                    int empid = resultSet.getInt("id");
                    Loader.setLocation(getClass().getResource("../IntroPageEmployee/intropageemployee.fxml"));
                    try{
                        Loader.load();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    IntroPageEmployee introPageEmp = Loader.getController();
                    introPageEmp.setEmployeeId(empid);
                    introPageEmp.setUserRole(getUserRole());
                    introPageEmp.getEmployeeName(empid);
                    introPageEmp.getProjectTableData();

                    Parent p = Loader.getRoot();
                    stage = (Stage) employeeToggle.getScene().getWindow();
                    Scene scene = new Scene(p);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    // If username & password doesn't match
                    isConnected.setText("Username / password is wrong!");
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void adminToggleOnAction(ActionEvent event) {
        if(adminToggle.isSelected()){
            employeeToggle.setSelected(false);
            userRole = "ADMIN_AUTH";
        }
    }

    public void employeeToggleOnAction(ActionEvent event) {
        if(employeeToggle.isSelected()){
            adminToggle.setSelected(false);
            userRole = "EMPLOYEE_AUTH";
        }
    }
}
