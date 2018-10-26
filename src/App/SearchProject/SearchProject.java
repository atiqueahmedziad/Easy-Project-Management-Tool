package App.SearchProject;

import App.Connect;
import App.ProjectDetail.ProjectDetailController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class SearchProject {

    Stage stage;

    public Label isprojectfound;
    public JFXTextField inputprojectid;
    public JFXButton searchprojectid;
    public JFXButton btnProjectDetail;
    public JFXButton allproject;

    public JFXTextField getInputprojectid() {
        return inputprojectid;
    }

    public void allproject(ActionEvent event) {
        if (event.getSource() == allproject) {

            FXMLLoader Loader = new FXMLLoader();

            stage = (Stage) allproject.getScene().getWindow();
            //load up OTHER FXML document
            Loader.setLocation(getClass().getResource("../IntroPage/intropage.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void ProjectDetail(ActionEvent event) {
        if (event.getSource() == btnProjectDetail) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) btnProjectDetail.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void SearchProjectid(ActionEvent event) throws Exception {
        if (event.getSource() == searchprojectid) {

            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM project_info WHERE id=" + getInputprojectid().getText();
            ResultSet rs = statement.executeQuery(sql);

            if(rs.next()) {
                isprojectfound.setText("");
                String id = rs.getString("id");
                String projectname = rs.getString("project_name");
                String startdate = rs.getString("start_date");
                String enddate = rs.getString("end_date");
                String estitime = rs.getString("estimated_time");

                FXMLLoader Loader = new FXMLLoader();

                Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));
                try {
                    Loader.load();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                statement.close();
                connection.close();

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
                stage = (Stage) searchprojectid.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.show();
            }
            else {
                isprojectfound.setText("Project ID has not found!");
            }
        }
    }
}
