package App;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;

    public class Controller implements Initializable {
        Stage stage;
        Parent root;

        @FXML
        private Button loginbutton;
        @FXML
        private JFXTextField username;
        @FXML
        public JFXPasswordField password;

        public Label isConnected;

        @FXML
        private void Login(ActionEvent actionEvent) throws IOException {

            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            try {
                Statement statement=connection.createStatement();
                String sql="SELECT * FROM user WHERE username = '"+username.getText()+"' AND pass = '"+password.getText()+"';";
                ResultSet resultSet=statement.executeQuery(sql);

                if (resultSet.next()){

                    //isConnected.setText("Connected");
                    stage = (Stage) loginbutton.getScene().getWindow();
                    
                    root = FXMLLoader.load(getClass().getResource("IntroPage/intropage.fxml"));

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }else {
                    isConnected.setText("Username / password is wrong!");
                }

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void initialize(URL url, ResourceBundle rb) {

        }

    }
