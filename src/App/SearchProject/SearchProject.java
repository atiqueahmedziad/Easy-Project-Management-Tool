package App.SearchProject;

import App.Connect;
import App.IntroPage.IntropageController;
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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }

    private String userRole;
    private int EmployeeId;

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

            IntropageController intropageController = Loader.getController();
            intropageController.setUserRole(getUserRole());
            if(getUserRole().matches("EMPLOYEE_AUTH")) {
                intropageController.setEmployeeId(getEmployeeId());
            }
            intropageController.initilizePorjects(getUserRole());

            Parent p = Loader.getRoot();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void ProjectDetail(ActionEvent event) {
        if(event.getSource() == btnProjectDetail) {
            if(getUserRole().matches("ADMIN_AUTH")) {
                FXMLLoader Loader = new FXMLLoader();

                Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

                try {
                    Loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ProjectDetailController projectDetailController = Loader.getController();
                projectDetailController.setUserRole(getUserRole());
                if (getUserRole().matches("EMPLOYEE_AUTH")) {
                    projectDetailController.setEmployeeId(getEmployeeId());
                }

                Parent p = Loader.getRoot();
                stage = (Stage) allproject.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Employee profile needed");
            }
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

    public void initializeSearchProject(){
        if(getUserRole().matches("EMPLOYEE_AUTH")) {
            btnProjectDetail.setText("Profile");
        }
    }
}
