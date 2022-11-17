package App.IntroPageAdmin;

import App.AdminProfile.AdminProfile;
import App.AllEmployee.AllEmployeeController;
import App.Client.AddClient.AddClientController;
import App.Client.AllClient.AllClientController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class IntroPageAdmin implements Initializable {

    public JFXButton allProjectsBtn;
    public JFXButton addNewProjectBtn;
    public JFXButton searchProjectBtn;
    public JFXButton addClient;
    public JFXButton allClientBtn;
    public Label clientCountLabel;
    public JFXButton logoutBtn;
    public JFXButton addEmployeeBtn;
    public JFXButton allEmployeeBtn;

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
    public TableView<Client> clientTableView;


    public TableColumn<Project,String> projectName;
    public TableColumn<Project,String> projectStartDate;
    public TableColumn<Project,String> projectEndDate;

    public TableColumn<Employee,String> employeeId;
    public TableColumn<Employee, String> employeeName;
    public TableColumn<Employee, String> employeeDesignation;

    public TableColumn<Client, String> clientId;
    public TableColumn<Client, String> clientName;
    public TableColumn<Client, String> contactPerson;


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
            String sql = "SELECT project_name, start_date, end_date FROM PROJECT_INFO WHERE end_date > NOW()";
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

    private void getClientTableData(){
        //Clear the all column data from table
        clientTableView.getItems().clear();
        clientCount = 0;

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT id, name, contact_person FROM CLIENT";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                clientCount++;
                int id = rs.getInt("id");
                String clientName = rs.getString("name");
                String contactPerson = rs.getString("contact_person");

                String clientId = String.valueOf(id);

                Client singleClient = new Client(clientId,clientName,contactPerson);
                clientTableView.getItems().add(singleClient);
            }

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        clientCountLabel.setText(String.valueOf(clientCount));
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

        employeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        employeeDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        employeeTableView.setEditable(false);

        clientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        contactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        clientTableView.setEditable(false);

        getLogoutIcon();
        
        getProjectTableData();

        getEmployeeTableData();
        
        getClientTableData();
    }

    public JFXButton profileBtn;

    public void ProfileBtnAction(ActionEvent event) throws SQLException {
        if(event.getSource() == profileBtn){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../AdminProfile/AdminProfile.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AdminProfile adminProfile = Loader.getController();
            adminProfile.setAdminId(getAdminId());
            adminProfile.setAdminInfo();
            adminProfile.setUserRole(getUserRole());

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
            projectSummaryController.setAdminId(getAdminId());
            projectSummaryController.initializeProjects(getUserRole());

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
            projectDetailController.getClientList();

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
        if(event.getSource() == allEmployeeBtn){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../AllEmployee/allemployee.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AllEmployeeController allEmpController = Loader.getController();
            allEmpController.setUserRole(getUserRole());
            allEmpController.setAdminId(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) allEmployeeBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void addEmployeeBtnAction(ActionEvent event) {
        if (event.getSource() == addEmployeeBtn) {
                FXMLLoader Loader = new FXMLLoader();

                Loader.setLocation(getClass().getResource("../AddEmployee/AddEmployee.fxml"));

                try {
                    Loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Parent p = Loader.getRoot();
//                stage = (Stage) btnAddEmployee.getScene().getWindow();
//                Scene scene = new Scene(p);
//                stage.setScene(scene);
//                stage.showAndWait();

                Parent p = Loader.getRoot();
                Stage stage = new Stage();
                Scene scene = new Scene(p);
                stage.setScene(scene);
                stage.showAndWait();
        }
    }
    
    public void addClientBtnAction(ActionEvent event) {
        if(event.getSource() == addClient){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../Client/AddClient/addclient.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AddClientController addClientController = Loader.getController();
            addClientController.setUserRole(getUserRole());
            addClientController.setAdminId(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) addClient.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void allClientBtnAction(ActionEvent event) {
        if(event.getSource() == allClientBtn){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../Client/AllClient/allclient.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AllClientController allClientController = Loader.getController();
            allClientController.setUserRole(getUserRole());
            allClientController.setAdminId(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) allClientBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void logoutBtnAction(ActionEvent event) {
        if(event.getSource() == logoutBtn){
            setAdminId(-1);
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
