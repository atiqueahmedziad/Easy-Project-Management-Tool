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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    private Accordion accordion;

    @FXML
    private void ProjectDetail(ActionEvent event) throws IOException {
        if (event.getSource() == btnProjectDetail) {
            //get reference to the button's stage
            stage = (Stage) btnProjectDetail.getScene().getWindow();
            //load up OTHER FXML document
            root = FXMLLoader.load(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Group g = new Group();

        try {
            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM project_info");

            while (rs.next()) {

                TitledPane titledpane = new TitledPane();
                titledpane.setText(rs.getString("project_name"));
                VBox content = new VBox();

                String id = rs.getString("id");
                String projectname = rs.getString("project_name");
                String startdate = rs.getString("start_date");
                String enddate = rs.getString("end_date");
                String estitime = rs.getString("estimated_time");

                content.getChildren().add(new Label("Project ID: " + id));
                content.getChildren().add(new Label("Project Name: " + projectname));
                content.getChildren().add(new Label("Project Start Date: " + startdate));
                content.getChildren().add(new Label("Project End Date: " + enddate));
                content.getChildren().add(new Label("Estimated Time: " + estitime + " Days"));
                JFXButton showporject = new JFXButton("Show Project Detail");
                content.getChildren().add(showporject);

                titledpane.setContent(content);
                accordion.getPanes().add(titledpane);

                showporject.setOnAction((event) -> {
                    //stage = (Stage) btnProjectDetail.getScene().getWindow();

                    FXMLLoader Loader = new FXMLLoader();

                    //load up OTHER FXML document

                    Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));
                    try{
                        Loader.load();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    ProjectDetailController display = Loader.getController();
                    display.setProjectID(id);
                    display.getProjectID().setDisable(true);
                    display.setProjectName(projectname);
                    display.getProjectName().setEditable(false);
                    display.setStart_date(LocalDate.parse(startdate));
                    display.getStart_date().setDisable(true);
                    display.setEnd_date(LocalDate.parse(enddate));
                    display.getEnd_date().setDisable(true);
                    display.setEsti_time(estitime);
                    display.getEsti_time().setEditable(false);
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

    public void allproject(ActionEvent event) {
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
}
