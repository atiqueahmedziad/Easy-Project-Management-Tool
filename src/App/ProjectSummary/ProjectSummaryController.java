package App.ProjectSummary;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import App.Connect;
import App.IntroPageAdmin.IntroPageAdmin;
import App.IntroPageEmployee.IntroPageEmployee;
import App.ProjectDetail.ProjectDetailController;
import App.SearchProject.SearchProject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProjectSummaryController implements Initializable {
    Stage stage;

    @FXML
    private JFXButton btnProjectDetail;
    @FXML
    private JFXButton allproject;
    @FXML
    private JFXButton searchproject;
    @FXML
    private Accordion accordion;
    @FXML
    private JFXButton homeBackBtn;

    private String userRole;
    private int employeeId;
    private int adminId;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    // When user clicks on Add new Project button
    @FXML
    private void ProjectDetail(ActionEvent event) throws IOException {
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
            projectDetailController.getClientList();

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

    // When "All project" button is clicked
    @FXML
    private void allproject(ActionEvent event) {
        if(event.getSource() == allproject) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("projectsummary.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
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

            try {
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            SearchProject searchProject = Loader.getController();
            searchProject.setUserRole(getUserRole());
            searchProject.initializeSearchPage(getUserRole());

            if(getUserRole().matches("ADMIN_AUTH")){
                searchProject.setAdminId(getAdminId());
            } else {
                searchProject.setEmployeeId(getEmployeeId());
            }

            Parent p = Loader.getRoot();
            stage = (Stage) searchproject.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void initializeProjects(String userRole){
        FXMLLoader Loader = new FXMLLoader();
        String sql;

        if(userRole.matches("ADMIN_AUTH")) {
            sql  = "SELECT * FROM PROJECT_INFO,CLIENT WHERE CLIENT.id=client_id";
        }
        else {
            sql = "SELECT distinct PROJECT_INFO.id, project_name, start_date, end_date, estimated_time,client_id,CLIENT.name FROM PROJECT_INFO, PROJECT_TASK,CLIENT WHERE PROJECT_INFO.id = PROJECT_TASK.id AND CLIENT.id = client_id AND assigned="+ getEmployeeId();

            btnProjectDetail.setDisable(true);
        }


        try {
            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

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
                String clientName = rs.getString("name");

                // Save the information in VBox for TitledPane to display
                content.getChildren().add(new Label("Project ID: " + id));
                content.getChildren().add(new Label("Project Client: " + clientName));
                content.getChildren().add(new Label("Project Name: " + projectname));
                content.getChildren().add(new Label("Project Start Date: " + startdate));
                content.getChildren().add(new Label("Project End Date: " + enddate));
                content.getChildren().add(new Label("Estimated Time: " + estitime + " Days"));
                JFXButton showporject = new JFXButton("Show Project Detail");
                showporject.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #4a3f99, #42649d);");
                //JFXButton deleteproject  = new JFXButton("Delete this project");
                content.getChildren().add(showporject);

                titledpane.setContent(content);

                // accordion is an object of Accordion Class
                accordion.getPanes().add(titledpane);

                showporject.setOnAction((event) -> {

                    Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

                    try {
                        Loader.load();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // A ProjectDetailController object is created
                    // to send information of the project detail page
                    ProjectDetailController projectDetailController = Loader.getController();

                    projectDetailController.setAdminId(getAdminId());
                    projectDetailController.setUserRole(getUserRole());

                    projectDetailController.setProjectID(id);
                    projectDetailController.getProjectID().setDisable(true);
                    projectDetailController.setProjectName(projectname);
                    projectDetailController.getProjectName().setEditable(false);

                    // startdate is a string
                    projectDetailController.setStart_date(LocalDate.parse(startdate));
                    projectDetailController.getStart_date().setDisable(true);

                    // enddate is a string
                    projectDetailController.setEnd_date(LocalDate.parse(enddate));

                    // Note: for LocalDate setEditable() method doesn't work in Jfonix.
                    // Ref: https://github.com/jfoenixadmin/JFoenix/issues/708
                    projectDetailController.getEnd_date().setDisable(true);
                    projectDetailController.setEsti_time(estitime);
                    projectDetailController.getEsti_time().setEditable(false);

                    // Calling getTableData() method to fill the table data from database following the above information
                    projectDetailController.getTableData();
                    projectDetailController.getClientList();

//                    ObservableList<String> clientList = FXCollections.observableArrayList();
//                    clientList.add(clientName);
//                    projectDetailController.projectClient.setValue(clientName);
                    projectDetailController.ClientNameText.setDisable(false);
                    projectDetailController.ClientNameText.setText(clientName);
                    projectDetailController.projectClient.setVisible(false);

                    projectDetailController.setUserRole(getUserRole());

                    if(getUserRole().matches("EMPLOYEE_AUTH")) {
                        projectDetailController.setEmployeeId(getEmployeeId());
                        projectDetailController.userIsEmployee();
                    }

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

    @FXML
    private JFXButton btnAddEmployee;

    //when Add Employee button is clicked
    @FXML
    private void AddEmployeeAction(ActionEvent event) {
        if (event.getSource() == btnAddEmployee) {
            if (getUserRole().matches("ADMIN_AUTH")) {
                FXMLLoader Loader = new FXMLLoader();

                Loader.setLocation(getClass().getResource("../AddEmployee/AddEmployee.fxml"));

                try {
                    Loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Parent p = Loader.getRoot();
                Stage stage = new Stage();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imageDecline = new Image(getClass().getResourceAsStream("../icons/home-icon.png"));
        ImageView cameraIconView = new ImageView(imageDecline);
        cameraIconView.setFitHeight(25);
        cameraIconView.setFitWidth(25);
        homeBackBtn.setGraphic(cameraIconView);
    }

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn) {
            FXMLLoader Loader = new FXMLLoader();

            if(getUserRole().matches("ADMIN_AUTH")){
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
                introPageEmp.getProjectTableData();

                Parent p = Loader.getRoot();
                stage = (Stage) homeBackBtn.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            }
        }

    }
}
