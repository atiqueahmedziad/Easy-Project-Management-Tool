package App.ProjectDetail;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import App.chart.DateAxis;
import App.chart.GanttChartController;
import App.chart.GanttChartController.ExtraData;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
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
import javafx.scene.input.KeyEvent;
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

    Stage stage;
    Parent root;

    //Error messages used when changing the end_date value
    final private String NO_START_DATE_ERROR = "Start date cannot be empty";
    final private String INVALID_END_DATE = "End date must be equal or greater than start date";

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

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();
            PreparedStatement ps;

            String sql = "insert into project_info(id,project_name, start_date, end_date, estimated_time) values (?,?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, getProjectID().getText());
            ps.setString(2, getProjectName().getText());
            ps.setDate(3, Date.valueOf(start_date.getValue()));
            ps.setDate(4, Date.valueOf(end_date.getValue()));
            ps.setString(5, calcDays(start_date,end_date));
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

    public void checkProjectID(JFXTextField id) throws Exception {
        
        Connect connect =new Connect();
        Connection connection=connect.getConnection();

        String sql ="SELECT project_id FROM project_task";
        Statement stmt = connection.createStatement();
        ResultSet resultSet=stmt.executeQuery(sql);

        while(resultSet.next()){
            String project_id = resultSet.getString("project_id");

            if(project_id.equals(id.getText())){
                isidexist.setText("ID already exists!");
                ProjectName.setDisable(true);
                start_date.setDisable(true);
                end_date.setDisable(true);
                break;
            }
            else {
                isidexist.setText("");
                ProjectName.setDisable(false);
                start_date.setDisable(false);
                end_date.setDisable(false);
            }
        }
        stmt.close();
        connection.close();
    }

    @FXML
    private void onreleaseID(KeyEvent keyEvent) {
        try {
            checkProjectID(ProjectID);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            AddtaskController display = Loader.getController();
            display.setProjectName(getProjectName());
            display.setProject_id_task(getProjectID());
            try {
                display.getList(display.getDependency());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.showAndWait();

        }
    }

    public void getTableData(){
        //Clear the all column data from database
        tableview.getItems().clear();

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT task_name, time, task_start_date, task_end_date, progress, color, dependency, assigned FROM project_task WHERE project_id = "+getProjectID().getText();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String task_name = rs.getString("task_name");
                String time = rs.getString("time");
                String task_start_date = rs.getString("task_start_date");
                String task_end_date = rs.getString("task_end_date");
                String progress = rs.getString("progress");
                String color = rs.getString("color");
                String dependency = rs.getString("dependency");
                String assigned = rs.getString("assigned");

                Task task = new Task(task_name, time, task_start_date, task_end_date, progress, color, dependency, assigned);
                tableview.getItems().add(task);
            }

            statement.close();
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

        Statement statement = connection.prepareStatement(sql);
        statement.executeUpdate(sql);

        statement.close();
        connection.close();
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
            Loader.setLocation(getClass().getResource("../IntroPage/intropage.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }

            Parent p = Loader.getRoot();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }


    public void ProjectDetail(ActionEvent event) {
        if(event.getSource() == btnProjectDetail) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("projectdetail.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }


            Parent p = Loader.getRoot();
            stage = (Stage) btnProjectDetail.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void SearchProjectAction(ActionEvent event) {
        if(event.getSource() == searchproject) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../SearchProject/searchproject.fxml"));

            try{
                Loader.load();
            } catch (Exception e){
                e.printStackTrace();
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
        ArrayList<java.util.Date> TaskDates = new ArrayList<java.util.Date>();
        ArrayList<String> TaskNames = new ArrayList<String>();
        ArrayList<String> TaskColors = new ArrayList<>();
        int count = 0;

        if(event.getSource() == GanttChartButton){

            try {
                Connect connect =new Connect();
                Connection connection=connect.getConnection();

                Statement statement = connection.createStatement();
                String sql = "SELECT task_name,task_start_date,task_end_date,color FROM project_task WHERE project_id = "+getProjectID().getText();
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    String TaskName = rs.getString("task_name");
                    java.util.Date StartDate =  rs.getDate("task_start_date");
                    java.util.Date EndDate = rs.getDate("task_end_date");
                    String TaskColor = rs.getString("color");

                    TaskColors.add(toRGBCode(Color.valueOf(TaskColor)));
                    TaskNames.add(TaskName);
                    TaskDates.add(StartDate);
                    TaskDates.add(EndDate);
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

            int j = 0;
            int k = 1;

            for (int i =0; i<count; i++) {
                double length = xAxis.getDisplayPositionDate(TaskDates.get(j), TaskDates.get(k));
                XYChart.Series series = new XYChart.Series();
                series.getData().add(new XYChart.Data(TaskDates.get(j), TaskNames.get(i), new ExtraData( length, TaskColors.get(i))));
                chart.getData().add(series);
                j+=2; k+=2;
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
}
