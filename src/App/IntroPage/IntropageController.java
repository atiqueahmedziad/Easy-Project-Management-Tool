package App.IntroPage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import App.Connect;
import App.ProjectDetail.ProjectDetailController;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntropageController implements Initializable {
    Stage stage;
    Parent root;

    @FXML
    private JFXButton btnProjectDetail;
    @FXML
    private JFXButton allproject;
    @FXML
    private JFXButton searchproject;
    @FXML
    private Accordion accordion;

    // When user clicks on Add new Project button
    @FXML
    private void ProjectDetail(ActionEvent event) throws IOException {
        if(event.getSource() == btnProjectDetail) {
            stage = (Stage) btnProjectDetail.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("../ProjectDetail/projectdetail.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    // When "All project" button is clicked
    @FXML
    private void allproject(ActionEvent event) {
        if(event.getSource() == allproject) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("intropage.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) allproject.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    // When "Search project" button is clicked
    @FXML
    private void SearchProjectAction(ActionEvent event) {
        if(event.getSource() == searchproject) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../SearchProject/searchproject.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) searchproject.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            Statement statement = connection.createStatement();

            // * = id, project_name, start_date, end_date, estimated_time
            // All above information are taken from project_info table
            ResultSet rs = statement.executeQuery("SELECT * FROM project_info");

            while (rs.next()) {

                // Gerarating TitledPane taking project information from database
                TitledPane titledpane = new TitledPane();
                titledpane.setText(rs.getString("project_name"));

                // VBox is used to place content inside TitledPane
                VBox content = new VBox();

                // Variables to store information from Database
                String id = rs.getString("id");
                String projectname = rs.getString("project_name");
                String startdate = rs.getString("start_date");
                String enddate = rs.getString("end_date");
                String estitime = rs.getString("estimated_time");

                // Save the information in VBox for TitledPane to display
                content.getChildren().add(new Label("Project ID: " + id));
                content.getChildren().add(new Label("Project Name: " + projectname));
                content.getChildren().add(new Label("Project Start Date: " + startdate));
                content.getChildren().add(new Label("Project End Date: " + enddate));
                content.getChildren().add(new Label("Estimated Time: " + estitime + " Days"));
                JFXButton showporject = new JFXButton("Show Project Detail");
                //JFXButton deleteproject  = new JFXButton("Delete this project");
                content.getChildren().add(showporject);

                titledpane.setContent(content);

                // accordion is an object of Accordion Class
                accordion.getPanes().add(titledpane);

                showporject.setOnAction((event) -> {
                    FXMLLoader Loader = new FXMLLoader();
                    Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

                    try{
                        Loader.load();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    // A ProjectDetailController object is created
                    // to send information of the project detail page
                    ProjectDetailController display = Loader.getController();

                    display.setProjectID(id);
                    display.getProjectID().setDisable(true);
                    display.setProjectName(projectname);
                    display.getProjectName().setEditable(false);

                    // startdate is a string
                    display.setStart_date(LocalDate.parse(startdate));
                    display.getStart_date().setDisable(true);

                    // enddate is a string
                    display.setEnd_date(LocalDate.parse(enddate));

                    // Note: for LocalDate setEditable() method doesn't work in Jfonix.
                    // Ref: https://github.com/jfoenixadmin/JFoenix/issues/708
                    display.getEnd_date().setDisable(true);
                    display.setEsti_time(estitime);
                    display.getEsti_time().setEditable(false);

                    // Calling getTableData() method to fill the table data from database following the above information
                    display.getTableData();

                    Parent p = Loader.getRoot();
                    stage = (Stage) showporject.getScene().getWindow();
                    Scene scene = new Scene(p);
                    stage.setScene(scene);
                    stage.show();
                });
            }
            rs.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
