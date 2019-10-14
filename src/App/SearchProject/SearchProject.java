package App.SearchProject;

import App.Connect;
import App.IntroPageAdmin.IntroPageAdmin;
import App.IntroPageEmployee.IntroPageEmployee;
import App.ProjectSummary.ProjectSummaryController;
import App.ProjectDetail.ProjectDetailController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SearchProject implements Initializable {

    public Tab searchByClient;
    public Tab searchByEmployee;
    public TableView<ProjectEach> EmProjectTable;

    public TableColumn<ProjectEach, String> projectId;
    public TableColumn<ProjectEach, String> projectName;
    public TableColumn<ProjectEach, String> projectStartDate;
    public TableColumn<ProjectEach, String> projectEndDate;
    public Label errorLabel;
    public TableView<ProjectEach> clientProjectTable;
    public TableColumn<ProjectEach, String> client_id;
    public TableColumn<ProjectEach, String> client_project_name;
    public TableColumn<ProjectEach, String> client_start_date;
    public TableColumn<ProjectEach, String> client_end_date;
    public Label errorLabelClientTab;
    public JFXButton showPDBtnClient;
    public JFXDatePicker startDate;
    public JFXDatePicker endDate;
    public TableView<ProjectEach> DateProjectTable;
    public TableColumn<ProjectEach, String> dateProjectId;
    public TableColumn<ProjectEach, String> dateprojectName;
    public TableColumn<ProjectEach, String> dateprojectStartDate;
    public TableColumn<ProjectEach, String> dateprojectEndDate;
    public Tab searchByDates;
    public Label errorLabelDate;


    @FXML
    JFXComboBox<String> clientDropdown = new JFXComboBox<>();

    @FXML
    JFXComboBox<String> employeeDropdown = new JFXComboBox<String>();

    private ObservableList<String> clientList = FXCollections.observableArrayList();

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
            if (getUserRole().matches("ADMIN_AUTH")) {
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
        if (event.getSource() == btnProjectDetail) {
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
        if (getInputprojectid().getText().trim().isEmpty() || !Pattern.matches("\\d+", getInputprojectid().getText())) {
            //we should display a message saying: Put a valid number
            return;
        }

        if (event.getSource() == searchprojectid) {

            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            String sql;

            Statement statement = connection.createStatement();
            if (userRole.matches("ADMIN_AUTH")) {
                sql = "SELECT * FROM project_info WHERE id=" + getInputprojectid().getText();
            } else {
                sql = "SELECT distinct PROJECT_INFO.id, project_name, start_date, end_date, estimated_time FROM PROJECT_INFO INNER JOIN PROJECT_TASK ON PROJECT_INFO.id = PROJECT_TASK.id AND assigned=" + getEmployeeId() + " AND PROJECT_INFO.id=" + getInputprojectid().getText();
            }

            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
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
                projectDetailController.setProjectClient(id);
                projectDetailController.getTableData();

                projectDetailController.setUserRole(getUserRole());

                if (getUserRole().matches("ADMIN_AUTH")) {
                    projectDetailController.setAdminId(getAdminId());
                } else {
                    projectDetailController.setEmployeeId(getEmployeeId());
                    projectDetailController.userIsEmployee();
                }

                Parent p = Loader.getRoot();
                stage = (Stage) searchprojectid.getScene().getWindow();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.show();
            } else {
                isprojectfound.setText("Project ID has not found!");
            }
        }
    }

    public void homeBackBtnAction(ActionEvent event) {
        if (event.getSource() == homeBackBtn) {
            FXMLLoader Loader = new FXMLLoader();
            if (getUserRole().matches("ADMIN_AUTH")) {
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
            } else {
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

    public void SearchProjectAction(ActionEvent event) {

    }

    public void initializeSearchPage(String userRole) {
        if (!userRole.matches("ADMIN_AUTH")) {
            allproject.setDisable(true);
            btnProjectDetail.setDisable(true);
            searchByClient.setDisable(true);
            searchByClient.setStyle("-fx-opacity: 0;");
            searchByEmployee.setDisable(true);
            searchByEmployee.setStyle("-fx-opacity: 0;");
            searchByDates.setDisable(true);
            searchByEmployee.setStyle("-fx-opacity: 0;");
        }
    }

    public void getEmployeeList() {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();

        ObservableList<String> employeeList = FXCollections.observableArrayList();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id, name FROM EMPLOYEE");

            while (rs.next()) {
                employeeList.add(rs.getInt("id") + " - " + rs.getString("name"));
            }

            employeeDropdown.setItems(employeeList);

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getClientList() {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();

        ObservableList<String> clientList = FXCollections.observableArrayList();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name FROM CLIENT");

            while (rs.next()) {
                clientList.add(rs.getString("name"));
            }

            clientDropdown.setItems(clientList);

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        projectId.setCellValueFactory(new PropertyValueFactory<>("id"));
        projectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        projectStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        projectEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));

        client_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        client_project_name.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        client_start_date.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        client_end_date.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));

        dateProjectId.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateprojectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        dateprojectStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        dateprojectEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));

        Image imageDecline = new Image(getClass().getResourceAsStream("../icons/home-icon.png"));
        ImageView cameraIconView = new ImageView(imageDecline);
        cameraIconView.setFitHeight(25);
        cameraIconView.setFitWidth(25);
        homeBackBtn.setGraphic(cameraIconView);
    }

    public void selectTabSEN(Event event) {
        if (searchByEmployee.isSelected()) {
            getEmployeeList();
            System.out.println("tab selected");
            EmProjectTable.getItems().clear();
        }
        employeeDropdown.valueProperty().set(null);
    }

    public void showProjectDetailAction(ActionEvent event) {
        ProjectEach project = EmProjectTable.getSelectionModel().getSelectedItem();
        if (project != null) {

            FXMLLoader Loader = new FXMLLoader();

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

            projectDetailController.setProjectID(project.getId());
            projectDetailController.getProjectID().setDisable(true);
            projectDetailController.setProjectName(project.getProjectName());
            projectDetailController.getProjectName().setEditable(false);

            // startdate is a string
            projectDetailController.setStart_date(LocalDate.parse(project.getProjectStartDate()));
            projectDetailController.getStart_date().setDisable(true);

            // enddate is a string
            projectDetailController.setEnd_date(LocalDate.parse(project.getProjectEndDate()));

            // Note: for LocalDate setEditable() method doesn't work in Jfonix.
            // Ref: https://github.com/jfoenixadmin/JFoenix/issues/708
            projectDetailController.getEnd_date().setDisable(true);
            projectDetailController.setEsti_time(project.getEstTime());
            projectDetailController.getEsti_time().setEditable(false);
            projectDetailController.setProjectClient(project.getClientId());

            // Calling getTableData() method to fill the table data from database following the above information
            projectDetailController.getTableData();

            projectDetailController.setUserRole(getUserRole());
            if (getUserRole().matches("EMPLOYEE_AUTH")) {
                projectDetailController.setEmployeeId(getEmployeeId());
                projectDetailController.userIsEmployee();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) searchprojectid.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        } else {
            errorLabel.setText("Nothing is selected from table");
        }
    }

    public void selectEmAction(ActionEvent event) {
        if (employeeDropdown.getValue() != null) {
            EmProjectTable.getItems().clear();

            System.out.println("aokk");

            String[] EmpArrayStr = employeeDropdown.getValue().split("-");
            int employeeId = Integer.parseInt(EmpArrayStr[0].trim());
            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT distinct PROJECT_INFO.id, project_name, start_date, end_date, estimated_time, client_id FROM PROJECT_INFO INNER JOIN PROJECT_TASK ON PROJECT_INFO.id = PROJECT_TASK.id AND assigned=" + employeeId);

                while (rs.next()) {
                    String id = String.valueOf(rs.getInt("id"));
                    String projectName = rs.getString("project_name");
                    String startDate = rs.getString("start_date");
                    String endDate = rs.getString("end_date");
                    String estTime = rs.getString("estimated_time");
                    String clientId = String.valueOf(rs.getInt("client_id"));

                    ProjectEach projectEach = new ProjectEach(id, projectName, startDate, endDate, estTime, clientId);
                    EmProjectTable.getItems().add(projectEach);
                }

                rs.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void selectClientAction(ActionEvent event) {
        if (clientDropdown.getValue() != null) {
            clientProjectTable.getItems().clear();
//            System.out.println("clientaokk");
            String clientName = clientDropdown.getValue().trim();
            //System.out.println(employeeId);
            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT distinct PROJECT_INFO.id, project_name, start_date, end_date, estimated_time, client_id FROM PROJECT_INFO WHERE client_id=(SELECT id FROM CLIENT WHERE name='" + clientName + "')");

                while (rs.next()) {
                    //System.out.println("asdsd");
                    String id = String.valueOf(rs.getInt("id"));
                    String projectName = rs.getString("project_name");
                    String startDate = rs.getString("start_date");
                    String endDate = rs.getString("end_date");
                    String estTime = rs.getString("estimated_time");
                    String clientId = String.valueOf(rs.getInt("client_id"));

                    ProjectEach projectEach = new ProjectEach(id, projectName, startDate, endDate, estTime, clientId);

                    clientProjectTable.getItems().add(projectEach);
                }

                rs.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void selectTabClient(Event event) {
        if (searchByClient.isSelected()) {
            getClientList();
//            System.out.println("tab selected");
            clientProjectTable.getItems().clear();
        }
        clientDropdown.valueProperty().set(null);

    }

    public void showPDBtnClientAction(ActionEvent event) {
        ProjectEach project = clientProjectTable.getSelectionModel().getSelectedItem();
        if (project != null) {

            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProjectDetailController projectDetailController = Loader.getController();

            projectDetailController.setAdminId(getAdminId());
            projectDetailController.setUserRole(getUserRole());

            projectDetailController.setProjectID(project.getId());
            projectDetailController.getProjectID().setDisable(true);
            projectDetailController.setProjectName(project.getProjectName());
            projectDetailController.getProjectName().setEditable(false);

            projectDetailController.setStart_date(LocalDate.parse(project.getProjectStartDate()));
            projectDetailController.getStart_date().setDisable(true);

            projectDetailController.setEnd_date(LocalDate.parse(project.getProjectEndDate()));

            // Note: for LocalDate setEditable() method doesn't work in Jfonix.
            // Ref: https://github.com/jfoenixadmin/JFoenix/issues/708
            projectDetailController.getEnd_date().setDisable(true);
            projectDetailController.setEsti_time(project.getEstTime());
            projectDetailController.getEsti_time().setEditable(false);
            projectDetailController.setProjectClient(project.getClientId());

            projectDetailController.getTableData();

            projectDetailController.setUserRole(getUserRole());
            if (getUserRole().matches("EMPLOYEE_AUTH")) {
                projectDetailController.setEmployeeId(getEmployeeId());
                projectDetailController.userIsEmployee();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) searchprojectid.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        } else {
            errorLabelClientTab.setText("Nothing is selected from table.");
        }
    }

    public void selectTabDate(Event event) {
        if (searchByDates.isSelected()) {
            DateProjectTable.getItems().clear();
            startDate.setValue(null);
            endDate.setValue(null);
        }
    }

    public void showPDFromDate(ActionEvent event) {
        ProjectEach project = DateProjectTable.getSelectionModel().getSelectedItem();
        if (project != null) {

            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProjectDetailController projectDetailController = Loader.getController();

            projectDetailController.setAdminId(getAdminId());
            projectDetailController.setUserRole(getUserRole());

            projectDetailController.setProjectID(project.getId());
            projectDetailController.getProjectID().setDisable(true);
            projectDetailController.setProjectName(project.getProjectName());
            projectDetailController.getProjectName().setEditable(false);

            projectDetailController.setStart_date(LocalDate.parse(project.getProjectStartDate()));
            projectDetailController.getStart_date().setDisable(true);

            projectDetailController.setEnd_date(LocalDate.parse(project.getProjectEndDate()));

            // Note: for LocalDate setEditable() method doesn't work in Jfonix.
            // Ref: https://github.com/jfoenixadmin/JFoenix/issues/708
            projectDetailController.getEnd_date().setDisable(true);
            projectDetailController.setEsti_time(project.getEstTime());
            projectDetailController.getEsti_time().setEditable(false);
            projectDetailController.setProjectClient(project.getClientId());

            projectDetailController.getTableData();

            projectDetailController.setUserRole(getUserRole());
            if (getUserRole().matches("EMPLOYEE_AUTH")) {
                projectDetailController.setEmployeeId(getEmployeeId());
                projectDetailController.userIsEmployee();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) searchprojectid.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        } else {
            errorLabelClientTab.setText("Nothing is selected from table.");
        }
    }

    public void findProjectWithDates(ActionEvent event) {
        if(startDate.getValue() == null){
            errorLabelDate.setText("Start Date is not selected.");
            endDate.setValue(null);
            return;
        }

        errorLabelDate.setText("");

        DateProjectTable.getItems().clear();

        String getStartDate = startDate.getValue().toString();
        String getEndDate = endDate.getValue().toString();

        Connect connect = new Connect();
        Connection connection = connect.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM PROJECT_INFO WHERE end_date BETWEEN '"+ getStartDate +"' AND '"+ getEndDate +"'");

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String projectName = rs.getString("project_name");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String estTime = rs.getString("estimated_time");
                String clientId = String.valueOf(rs.getInt("client_id"));

                ProjectEach projectEach = new ProjectEach(id, projectName, startDate, endDate, estTime, clientId);
                DateProjectTable.getItems().add(projectEach);
            }

            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
