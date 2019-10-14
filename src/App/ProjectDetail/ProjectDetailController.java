package App.ProjectDetail;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import App.IntroPageAdmin.IntroPageAdmin;
import App.IntroPageEmployee.IntroPageEmployee;
import App.ProjectSummary.ProjectSummaryController;
import App.SearchProject.SearchProject;
import App.chart.DateAxis;
import App.chart.GanttChartController;
import App.chart.GanttChartController.ExtraData;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import App.AddTask.AddtaskController;
import App.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ProjectDetailController implements Initializable {

    public TableView<Task> tableview;
    public TableColumn<Task,String> TaskName;
    public TableColumn<Task,String> TaskTime;
    public TableColumn<Task,String> TaskStartDate;
    public TableColumn<Task,String> TaskEndDate;
    public TableColumn<Task,String> TaskProgress;
    public TableColumn<Task,String> TaskColor;
    public TableColumn<Task,String> TaskDependency;
    public TableColumn<Task,String> TaskAssinged;
    public JFXButton DeleteTaskButton;
    public Label isidexist;
    public JFXButton btnProjectDetail;
    public JFXButton searchproject;
    public JFXButton GanttChartButton;
    public JFXButton homeBackBtn;

    Stage stage;
    Parent root;

    //Error messages used when changing the end_date value
    final private String NO_START_DATE_ERROR = "Start date cannot be empty";
    final private String INVALID_END_DATE = "End date must be equal or greater than start date";

    private String userRole;
    private int adminId;
    private int employeeId;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }



    @FXML
    JFXTextField ProjectID;
    @FXML
    JFXTextField ProjectName;
    @FXML
    JFXTextField esti_time;
    @FXML
    JFXDatePicker start_date = new JFXDatePicker();
    LocalDate start_dateValue = start_date.getValue();
    @FXML
    JFXDatePicker end_date = new JFXDatePicker();
    LocalDate end_dateValue = end_date.getValue();
    @FXML
    Label invalid_date_label = new Label();
    @FXML
    private JFXButton btnAddTask;
    @FXML
    private JFXButton LoadTaskButton;
    @FXML
    private JFXButton allproject;

    public JFXTextField getProjectID() {
        return ProjectID;
    }

    public JFXTextField getProjectName() {
        return ProjectName;
    }

    public void setProjectID(String projectID) {
        ProjectID.setText(projectID);
    }

    public void setProjectName(String projectName) {
        ProjectName.setText(projectName);
    }

    public void setEsti_time(String esti_time) {
        this.esti_time.setText(esti_time);
    }

    public JFXDatePicker getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date.setValue(start_date);
    }

    public LocalDate getStart_dateValue() {
        return start_dateValue;
    }

    public void setStart_dateValue(LocalDate start_dateValue) {
        this.start_dateValue = start_dateValue;
    }

    public JFXDatePicker getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date.setValue(end_date);
    }

    public LocalDate getEnd_dateValue() {
        return end_dateValue;
    }

    public void setEnd_dateValue(LocalDate end_dateValue) {
        this.end_dateValue = end_dateValue;
    }

    /**
     * Validates if start and end date have a valid range, that happens when start_date is lower or equals to end_date
     * @return True if is valid, false if it isn't
     */
    private boolean hasValidDateRange() {
        if (start_date.getValue() == null || end_date.getValue() == null) {
            return false;
        }
        final Date startDate = Date.valueOf(start_date.getValue());
        final Date endDate = Date.valueOf(end_date.getValue());

        //If endDate is greater than startDate return true (is valid) else return false (is invalid)
        return endDate.compareTo(startDate) >= 0;
    }

    private void insertProjectInfo() {
        getEsti_time();
        String[] ClientArrayStr = projectClient.getValue().split("-");
        int projectClientId = Integer.parseInt(ClientArrayStr[0].trim());

        if(projectClientId == 0){
            invalid_date_label.setText("A valid employee must be selected.");
            return;
        } else {
            invalid_date_label.setText("");
        }

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();
            PreparedStatement ps;

            String sql = "insert into project_info(id,project_name, start_date, end_date, estimated_time,client_id) values (?,?,?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, getProjectID().getText());
            ps.setString(2, getProjectName().getText());
            ps.setDate(3, Date.valueOf(start_date.getValue()));
            ps.setDate(4, Date.valueOf(end_date.getValue()));
            ps.setString(5, calcDays(start_date,end_date));
            ps.setInt(6,projectClientId);
            ps.executeUpdate();

            ps.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        getProjectName().setDisable(true);
        getProjectID().setDisable(true);
        getStart_date().setEditable(false);
        getEnd_date().setEditable(false);
    }


    @FXML
    private String calcDays(JFXDatePicker start_date, JFXDatePicker end_date) {
        long intervalDays = (ChronoUnit.DAYS.between(start_date.getValue(), end_date.getValue()) + 1);
        return String.valueOf(intervalDays);
    }

    @FXML
    public JFXTextField getEsti_time() {
        esti_time.setText(" "+ calcDays(start_date,end_date) + " Days");
        return esti_time;
    }

    // When end date of the project is chosen, this function works
    @FXML
    private void showDays(ActionEvent event) {
        if (hasValidDateRange()) {
            invalid_date_label.setVisible(false);
            insertProjectInfo();
            return;
        } else if (start_date.getValue() == null ) {
            end_date.setValue(null);
            invalid_date_label.setText(NO_START_DATE_ERROR);
        } else {
            invalid_date_label.setText(INVALID_END_DATE);
        }
        invalid_date_label.setVisible(true);
    }

    @FXML
    private void startDateChanged(ActionEvent event) {
        if (end_date.getValue() != null) {
            showDays(null);
        } else if (invalid_date_label.isVisible()) {
            //This means we were showing the NO_START_DATE_ERROR label error
            invalid_date_label.setVisible(false);
        }
    }

    @FXML
    private void AddTask(ActionEvent event) {
        if (event.getSource() == btnAddTask) {

            FXMLLoader Loader = new FXMLLoader();

            //load up OTHER FXML document

            Loader.setLocation(getClass().getResource("../AddTask/addtask.fxml"));
            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            AddtaskController addtaskController = Loader.getController();
            addtaskController.setProjectName(getProjectName());
            addtaskController.setProject_id_task(getProjectID());
            addtaskController.getDependencyList();
            addtaskController.getEmployeeList();

            Parent p = Loader.getRoot();
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setTitle("Add New Task");
            stage.setScene(scene);
            stage.showAndWait();

        }
    }

    public void getTableData(){
        //Clear the all column data from table
        tableview.getItems().clear();

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String taskSql = "SELECT task_name, task_time, task_start_date, task_end_date, progress, color, dependency, EMPLOYEE.name as assignedName FROM PROJECT_TASK JOIN EMPLOYEE ON PROJECT_TASK.assigned = EMPLOYEE.id WHERE PROJECT_TASK.id="+getProjectID().getText();
            ResultSet taskResultSet = statement.executeQuery(taskSql);

            while (taskResultSet.next()) {
                String task_name = taskResultSet.getString("task_name");
                String time = taskResultSet.getString("task_time");
                String task_start_date = taskResultSet.getString("task_start_date");
                String task_end_date = taskResultSet.getString("task_end_date");
                String progress = taskResultSet.getString("progress");
                String color = taskResultSet.getString("color");
                String dependency = taskResultSet.getString("dependency");
                String assigned = taskResultSet.getString("assignedName");

                Task task = new Task(task_name, time, task_start_date, task_end_date, progress, color, dependency, assigned);
                tableview.getItems().add(task);
            }
            taskResultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LoadTaskHandle(ActionEvent event) {
        if(event.getSource() == LoadTaskButton) {
            getTableData();
        }
    }

    public void onEditProgress(TableColumn.CellEditEvent<Task,String> taskStringCellEditEvent) throws SQLException {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setTaskProgress(taskStringCellEditEvent.getNewValue());

        Connect connect =new Connect();
        Connection connection=connect.getConnection();

        String sql = "UPDATE project_task SET progress= '"+task.getTaskProgress()+"' WHERE task_name='"+task.getTaskName()+"'";

        Statement statement = connection.createStatement();

        statement.executeUpdate(sql);

        statement.close();
        connection.close();
    }

    //CLient Choicebox

    public String getClientNameText() {
        return ClientNameText.getText();
    }

    public void setClientNameText(String clientNameText) {
        this.ClientNameText.setText(clientNameText);
    }

    public JFXTextField ClientNameText;
    private ObservableList<String> clientList = FXCollections.observableArrayList();

    public void setProjectClient(String clientId) {
        this.projectClient.setVisible(false);
        String clientName = "";
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name FROM CLIENT WHERE id="+clientId);

            while (rs.next()) {
                clientName = rs.getString("name");
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        this.setClientNameText(clientName);
    }

    public ChoiceBox<String> projectClient = new ChoiceBox<String>();

    public void getClientList() {
        Connect connect = new Connect();
        Connection connection=connect.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id, name FROM CLIENT");

            while (rs.next()) {
                clientList.add(rs.getInt("id") + " - " + rs.getString("name"));
            }

            projectClient.setItems(clientList);

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        TaskName.setCellValueFactory(new PropertyValueFactory<>("TaskName"));
        TaskTime.setCellValueFactory(new PropertyValueFactory<>("TaskTime"));
        TaskStartDate.setCellValueFactory(new PropertyValueFactory<>("TaskStartDate"));
        TaskEndDate.setCellValueFactory(new PropertyValueFactory<>("TaskEndDate"));
        TaskProgress.setCellValueFactory(new PropertyValueFactory<>("TaskProgress"));
        TaskColor.setCellValueFactory(new PropertyValueFactory<>("TaskColor"));
        TaskDependency.setCellValueFactory(new PropertyValueFactory<>("TaskDependency"));
        TaskAssinged.setCellValueFactory(new PropertyValueFactory<>("TaskAssinged"));

        tableview.setEditable(true);
        TaskProgress.setCellFactory(TextFieldTableCell.forTableColumn());

        Image imageDecline = new Image(getClass().getResourceAsStream("../icons/home-icon.png"));
        ImageView cameraIconView = new ImageView(imageDecline);
        cameraIconView.setFitHeight(25);
        cameraIconView.setFitWidth(25);
        homeBackBtn.setGraphic(cameraIconView);

        ClientNameText.setDisable(true);

        int id = 0;

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT MAX(id) as id FROM PROJECT_INFO";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                id = (int) rs.getInt("id");
            }

            rs.close();
            statement.close();
            connection.close();

            //increment id by 1 to set as new id
            id += 1;
            setProjectID(String.valueOf(id));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void DeleteTaskHandle(ActionEvent event) throws SQLException {
        if(event.getSource() == DeleteTaskButton) {

            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Task selectedItem = tableview.getSelectionModel().getSelectedItem();
            tableview.getItems().remove(selectedItem);

            PreparedStatement st = connection.prepareStatement("DELETE FROM project_task WHERE task_name = '" + selectedItem.getTaskName() + "';");
            st.executeUpdate();

            st.close();
            connection.close();
        }
    }

    public void allproject(ActionEvent event) throws Exception {
        if (event.getSource() == allproject) {

            FXMLLoader Loader = new FXMLLoader();

            //load up OTHER FXML document

            stage = (Stage) allproject.getScene().getWindow();
            //load up OTHER FXML document
            Loader.setLocation(getClass().getResource("../ProjectSummary/projectsummary.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            ProjectSummaryController projectSummaryController = Loader.getController();
            projectSummaryController.setUserRole(getUserRole());
            //System.out.println(getUserRole());
            if(getUserRole().matches("ADMIN_AUTH")){
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

    public void SearchProjectAction(ActionEvent event) {
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

    public void showGanttChart(ActionEvent event) {
        ArrayList<java.util.Date> ProjectDates = new ArrayList<java.util.Date>();
        List<java.util.Date> startDates = new ArrayList<java.util.Date>();
        List<java.util.Date> endDates = new ArrayList<java.util.Date>();
        ArrayList<String> TaskNames = new ArrayList<String>();
        ArrayList<String> TaskColors = new ArrayList<>();
        int count = 0;

        if(event.getSource() == GanttChartButton){

            try {
                Connect connect =new Connect();
                Connection connection=connect.getConnection();

                Statement statement = connection.createStatement();
                String sql = "SELECT task_name,task_start_date,task_end_date,color FROM project_task WHERE id = "+getProjectID().getText();
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    String TaskName = rs.getString("task_name");
                    java.util.Date StartDate =  rs.getDate("task_start_date");
                    java.util.Date EndDate = rs.getDate("task_end_date");
                    String TaskColor = rs.getString("color");

                    TaskColors.add(toRGBCode(Color.valueOf(TaskColor)));
                    TaskNames.add(TaskName);
                    startDates.add(StartDate);
                    endDates.add(EndDate);
                    ++count;
                }
                rs.close();
                statement.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                Connect connect =new Connect();
                Connection connection=connect.getConnection();

                Statement statement = connection.createStatement();
                String sql = "SELECT start_date,end_date FROM project_info WHERE id="+getProjectID().getText();
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    java.util.Date StartDate =  rs.getDate("start_date");
                    java.util.Date EndDate = rs.getDate("end_date");
                    ProjectDates.add(StartDate);
                    ProjectDates.add(EndDate);
                }
                rs.close();
                statement.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            final DateAxis xAxis = new DateAxis();
            final CategoryAxis yAxis = new CategoryAxis();

            final GanttChartController<java.util.Date,String> chart = new GanttChartController<>(xAxis,yAxis);
            xAxis.setLabel("");
            xAxis.setTickLabelFill(Color.DARKRED);
            xAxis.setTickLabelGap(10);
            xAxis.setLowerBound(ProjectDates.get(0));
            xAxis.setUpperBound(ProjectDates.get(1));
            xAxis.averageTickGap();
            xAxis.setTickLength(15);
            xAxis.setMaxWidth(1000);
            xAxis.setMinWidth(1000);
            xAxis.setAutoRanging(false);
            xAxis.setTickLabelRotation(90);

            yAxis.setLabel("");
            yAxis.setTickLabelFill(Color.GREEN);
            yAxis.setTickLabelGap(10);
            yAxis.setCategories(FXCollections.observableList(TaskNames));

            chart.setTitle(getProjectName().getText());
            chart.setLegendVisible(false);
            chart.setBlockHeight(50);

            for (int i =0; i<count; i++) {
                double length = xAxis.getDisplayPositionDate(startDates.get(i), endDates.get(i));
                XYChart.Series series = new XYChart.Series();
                series.getData().add(new XYChart.Data(startDates.get(i), TaskNames.get(i), new ExtraData( length, TaskColors.get(i))));
                chart.getData().add(series);
            }

            chart.getStylesheets().add(getClass().getResource("../chart/GanttChart.css").toExternalForm());

            Scene scene  = new Scene(chart,1200,600);
            stage = (Stage) GanttChartButton.getScene().getWindow();
            stage.setX(scene.getX()+50);
            stage.setScene(scene);
            stage.show();
        }
    }
    private String toRGBCode(Color color) {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn) {
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

    public void userIsEmployee(){
            btnAddTask.setDisable(true);
            DeleteTaskButton.setDisable(true);
            btnProjectDetail.setDisable(true);
            tableview.setEditable(false);
    }
}
