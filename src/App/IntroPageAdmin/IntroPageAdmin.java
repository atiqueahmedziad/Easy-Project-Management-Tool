package App.IntroPageAdmin;

import App.Connect;
import App.ProjectDetail.ProjectDetailController;
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
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class IntroPageAdmin implements Initializable {

    public JFXButton allProjectsBtn;
    public JFXButton addNewProjectBtn;
    public JFXButton searchProjectBtn;
    private int adminId;

    private String UserRole;

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public Label nameUser;
    public Label projectCountLabel;
    public Label employeeCountLabel;
    public TableView<Project> projectTableView;
    public TableView<Employee> employeeTableView;


    public TableColumn<Project,String> projectName;
    public TableColumn<Project,String> projectStartDate;
    public TableColumn<Project,String> projectEndDate;

    public TableColumn<Employee,String> employeeId;
    public TableColumn<Employee, String> employeeName;
    public TableColumn<Employee, String> employeeDesignation;


    private int projectCount, employeeCount, clientCount;

    private Stage stage;

    private void getProjectTableData(){
        //Clear the all column data from table
        projectTableView.getItems().clear();
        projectCount = 0;

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT project_name, start_date, end_date FROM PROJECT_INFO WHERE end_date > getdate()";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                projectCount++;
                String projectName = rs.getString("project_name");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");

                //Task task = new Task(task_name, time, task_start_date, task_end_date, progress, color, dependency, assigned);
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
    
    public void getAdminName(int id){
        String adminName = "ADMIN";
        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT name FROM ADMIN WHERE id="+id;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                adminName = rs.getString("name");
            }
            nameUser.setText("Welcome  " + adminName + " !");

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    

    private void getEmployeeTableData(){
        //Clear the all column data from table
        employeeTableView.getItems().clear();
        employeeCount = 0;

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT id, name, designation FROM EMPLOYEE";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                employeeCount++;
                int emId = rs.getInt("id");
                String employeeName = rs.getString("name");
                String employeeDesignation = rs.getString("designation");

                String employeeId = String.valueOf(emId);
                //Task task = new Task(task_name, time, task_start_date, task_end_date, progress, color, dependency, assigned);
                //Project singleProject = new Project(projectName,startDate,endDate);
                Employee singleEmployee = new Employee(employeeId,employeeName,employeeDesignation);
                employeeTableView.getItems().add(singleEmployee);
            }

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        employeeCountLabel.setText(String.valueOf(employeeCount));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        projectStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
        projectEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));
        projectTableView.setEditable(false);

        employeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        employeeDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        employeeTableView.setEditable(false);

        getProjectTableData();

        getEmployeeTableData();
    }

    public void ProfileBtnAction(ActionEvent event) {
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
            projectSummaryController.setAdminId(getAdminId());
            projectSummaryController.initilizePorjects(getUserRole());

            Parent p = Loader.getRoot();
            stage = (Stage) allProjectsBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void addNewProjectBtnAction(ActionEvent event) {
        if(event.getSource() == addNewProjectBtn) {

            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../ProjectDetail/projectdetail.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProjectDetailController projectDetailController = Loader.getController();
            projectDetailController.setUserRole(getUserRole());
            projectDetailController.setAdminId(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) addNewProjectBtn.getScene().getWindow();
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
            searchProject.setAdminId(getAdminId());
            Parent p = Loader.getRoot();
            stage = (Stage) searchProjectBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void allEmployeeBtnAction(ActionEvent event) {
    }

    public void addEmployeeBtnAction(ActionEvent event) {
    }

    public void allCientBtnAction(ActionEvent event) {
    }

    public void addClientBtnAction(ActionEvent event) {
    }

    public void pastProjectBtnAction(ActionEvent event) {
    }

    public void addAdminBtnAction(ActionEvent event) {
    }
}
