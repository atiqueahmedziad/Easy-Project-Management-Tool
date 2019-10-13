package App.IntroPageEmployee;

import App.Connect;
import App.EmployeeProfile.EmployeeProfile;
import App.IntroPageAdmin.Employee;
import App.ProjectSummary.ProjectSummaryController;
import App.SearchProject.SearchProject;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import App.IntroPageAdmin.Project;

public class IntroPageEmployee implements Initializable {

    public JFXButton allProjectsBtn;
    public JFXButton searchProjectBtn;
    public JFXButton logoutBtn;

    private int empId;

    private String UserRole;

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public int getEmployeeId() {
        return empId;
    }

    public void setEmployeeId(int empId) {
        this.empId = empId;
    }

    public Label nameUser;
    public Label projectCountLabel;
    public TableView<Project> projectTableView;

    public TableColumn<Project,String> projectName;
    public TableColumn<Project,String> projectStartDate;
    public TableColumn<Project,String> projectEndDate;


    private int projectCount;

    private Stage stage;

    public void getProjectTableData(){
        //Clear the all column data from table
        projectTableView.getItems().clear();
        projectCount = 0;

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT distinct PROJECT_INFO.id, project_name, start_date, end_date, estimated_time FROM PROJECT_INFO, PROJECT_TASK WHERE PROJECT_INFO.id = PROJECT_TASK.id AND assigned="+ getEmployeeId();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                projectCount++;
                String projectName = rs.getString("project_name");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");

                Project singleProject = new Project(projectName,startDate,endDate);
                projectTableView.getItems().add(singleProject);
            }

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        projectCountLabel.setText("Currently you have " + String.valueOf(projectCount) + " projects");
    }
    
    public void getEmployeeName(int id){
        String employeeName = "USER";
        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT name FROM EMPLOYEE WHERE id="+id;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                employeeName = rs.getString("name");
            }
            nameUser.setText("Welcome  " + employeeName + " !");

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    
    public void getLogoutIcon(){
        Image imageDecline = new Image(getClass().getResourceAsStream("../icons/log-out.png"));
        ImageView cameraIconView = new ImageView(imageDecline);
        cameraIconView.setFitHeight(18);
        cameraIconView.setFitWidth(18);
        logoutBtn.setGraphic(cameraIconView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        projectStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        projectEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));
        projectTableView.setEditable(false);

        getLogoutIcon();
    }
    public JFXButton profileBtn;

    public void ProfileBtnAction(ActionEvent event) throws SQLException {
        if (event.getSource() == profileBtn) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../EmployeeProfile/employeeprofile.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            EmployeeProfile empProfile = Loader.getController();
            empProfile.setEmpId(getEmployeeId());
            empProfile.setEmpInfo();
            empProfile.setUserRole(getUserRole());

            Parent p = Loader.getRoot();
            stage = (Stage) profileBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void allProjectsBtnAction(ActionEvent event) {
        if(event.getSource() == allProjectsBtn){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../ProjectSummary/projectsummary.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProjectSummaryController projectSummaryController = Loader.getController();
            projectSummaryController.setUserRole(getUserRole());
            projectSummaryController.setEmployeeId(getEmployeeId());
            projectSummaryController.initializeProjects(getUserRole());

            Parent p = Loader.getRoot();
            stage = (Stage) allProjectsBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }


    public void searchProjectBtnAction(ActionEvent event) {
        if(event.getSource() == searchProjectBtn) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../SearchProject/searchproject.fxml"));

            try {
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            SearchProject searchProject = Loader.getController();
            searchProject.setUserRole(getUserRole());
            searchProject.setEmployeeId(getEmployeeId());
            searchProject.initializeSearchPage(getUserRole());

            Parent p = Loader.getRoot();
            stage = (Stage) searchProjectBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }


    public void logoutBtnAction(ActionEvent event) {
        if(event.getSource() == logoutBtn){
            setEmployeeId(-1);
            setUserRole("");

            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../Login/loginpage.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            stage = (Stage) logoutBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
}
