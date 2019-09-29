package App.SearchProject;

import App.Connect;
import App.IntroPageAdmin.IntroPageAdmin;
import App.IntroPageEmployee.IntroPageEmployee;
import App.ProjectSummary.ProjectSummaryController;
import App.ProjectDetail.ProjectDetailController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SearchProject implements Initializable {

    Stage stage;

    private String userRole;
    private int EmployeeId;
    private int adminId;

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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public Label isprojectfound;
    public JFXTextField inputprojectid;
    public JFXButton searchprojectid;
    public JFXButton btnProjectDetail;
    public JFXButton allproject;
    public JFXButton homeBackBtn;

    public JFXTextField getInputprojectid() {
        return inputprojectid;
    }

    public void allproject(ActionEvent event) {
        if (event.getSource() == allproject) {

            FXMLLoader Loader = new FXMLLoader();

            stage = (Stage) allproject.getScene().getWindow();
            //load up OTHER FXML document
            Loader.setLocation(getClass().getResource("../ProjectSummary/projectsummary.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProjectSummaryController projectSummaryController = Loader.getController();
            projectSummaryController.setUserRole(getUserRole());
            if(getUserRole().matches("ADMIN_AUTH")) {
                projectSummaryController.setAdminId(getAdminId());
            } else {
                projectSummaryController.setEmployeeId(getEmployeeId());
            }
            projectSummaryController.initializeProjects(getUserRole());

            Parent p = Loader.getRoot();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void ProjectDetail(ActionEvent event) {
        if(event.getSource() == btnProjectDetail) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProjectDetailController projectDetailController = Loader.getController();
            projectDetailController.setUserRole(getUserRole());
            if (getUserRole().matches("ADMIN_AUTH")) {
                projectDetailController.setAdminId(getAdminId());
            } else {
                projectDetailController.setEmployeeId(getEmployeeId());
            }

            Parent p = Loader.getRoot();
            stage = (Stage) allproject.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void SearchProjectid(ActionEvent event) throws Exception {
        if(getInputprojectid().getText().trim().isEmpty() || !Pattern.matches("\\d+",getInputprojectid().getText())){
            //we should display a message saying: Put a valid number
            return;
        }

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

                ProjectDetailController projectDetailController = Loader.getController();
                projectDetailController.setProjectID(id);
                projectDetailController.getProjectID().setDisable(true);
                projectDetailController.setProjectName(projectname);
                projectDetailController.getProjectName().setEditable(false);
                projectDetailController.setStart_date(LocalDate.parse(startdate));
                projectDetailController.getStart_date().setDisable(true);
                projectDetailController.setEnd_date(LocalDate.parse(enddate));
                projectDetailController.getEnd_date().setDisable(true);
                projectDetailController.setEsti_time(estitime);
                projectDetailController.getEsti_time().setEditable(false);
                projectDetailController.getTableData();

                projectDetailController.setUserRole(getUserRole());

                if(getUserRole().matches("ADMIN_AUTH")){
                    projectDetailController.setAdminId(getAdminId());
                } else {
                    projectDetailController.setEmployeeId(getEmployeeId());
                }

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

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn) {
            FXMLLoader Loader = new FXMLLoader();
            if (getUserRole() == "ADMIN_AUTH") {
                Loader.setLocation(getClass().getResource("../IntroPageAdmin/intropageadmin.fxml"));

                try {
                    Loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                IntroPageAdmin introPageAdmin = Loader.getController();
                introPageAdmin.setAdminId(getAdminId());
                introPageAdmin.setUserRole(getUserRole());
                introPageAdmin.getAdminName(getAdminId());

                Parent p = Loader.getRoot();
                stage = (Stage) homeBackBtn.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.show();
            }

            else{
                Loader.setLocation(getClass().getResource("../IntroPageEmployee/intropageemployee.fxml"));

                try {
                    Loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                IntroPageEmployee introPageEmp = Loader.getController();
                introPageEmp.setEmployeeId(getEmployeeId());
                introPageEmp.setUserRole(getUserRole());
                introPageEmp.getEmployeeName(getEmployeeId());

                Parent p = Loader.getRoot();
                stage = (Stage) homeBackBtn.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            }
        }

    }

    public void SearchProjectAction(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imageDecline = new Image(getClass().getResourceAsStream("../icons/home-icon.png"));
        ImageView cameraIconView = new ImageView(imageDecline);
        cameraIconView.setFitHeight(25);
        cameraIconView.setFitWidth(25);
        homeBackBtn.setGraphic(cameraIconView);
    }
}
