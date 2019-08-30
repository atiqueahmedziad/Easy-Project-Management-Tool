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

    private String userRole;

    public String getUserRole() {
        return userRole;
    }

    @FXML
    private void Login(ActionEvent actionEvent) throws IOException {
        Connect connect = new Connect();
        Connection connection=connect.getConnection();

        if(!employeeToggle.isSelected() && !adminToggle.isSelected()){
            isConnected.setText("Select Admin or Employee from above");
            return;
        }

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM "+userRole+" WHERE username = '" + username.getText() + "' AND password = '" + password.getText() + "';";
            ResultSet resultSet = statement.executeQuery(sql);

            // Resultset contains all the username and password
            // if the username and password from database matches only then intropage loads
            if (resultSet.next()) {
                stage = (Stage) loginbutton.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("IntroPage/intropage.fxml"));
                Scene scene = new Scene(root);
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
