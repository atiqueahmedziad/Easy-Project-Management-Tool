package App;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import App.IntroPage.IntropageController;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
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

    public RadioButton adminToggle;
    public RadioButton employeeToggle;

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

//        if(userRole.matches("EMPLOYEE_AUTH")){
//            try {
//                Connect connect = new Connect();
//                Connection connection=connect.getConnection();
//                Statement statement = connection.createStatement();
//                String sql = "SELECT id FROM project_task WHERE id = "+getProjectID().getText();
//                ResultSet rs = statement.executeQuery(sql);
//
//                while (rs.next()) {
//                    String TaskName = rs.getString("task_name");
//                    java.util.Date StartDate =  rs.getDate("task_start_date");
//                    java.util.Date EndDate = rs.getDate("task_end_date");
//                    String TaskColor = rs.getString("color");
//
//                    TaskColors.add(toRGBCode(Color.valueOf(TaskColor)));
//                    TaskNames.add(TaskName);
//                    startDates.add(StartDate);
//                    endDates.add(EndDate);
//                    ++count;
//                }
//                statement.close();
//                connection.close();
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        try {
            Connect connect = new Connect();
            Connection connection=connect.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM "+userRole+" WHERE username = '" + username.getText() + "' AND password = '" + password.getText() + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            // Resultset contains all the username and password
            // if the username and password from database matches only then intropage loads
            if (resultSet.next()) {
                //load up OTHER FXML document
                int employeeid = resultSet.getInt("id");
                Loader.setLocation(getClass().getResource("IntroPage/intropage.fxml"));
                try{
                    Loader.load();
                } catch (Exception e){
                    e.printStackTrace();
                }

                IntropageController intropageController = Loader.getController();
                intropageController.setUserRole(getUserRole());
                if(userRole.matches("EMPLOYEE_AUTH")){
                    intropageController.setEmployeeId(employeeid);
                }

                intropageController.initilizePorjects(getUserRole());

                Parent p = Loader.getRoot();
                stage = (Stage) adminToggle.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
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
