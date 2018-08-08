package App.AddTask;

import App.Connect;
import App.ProjectDetail.ProjectDetailController;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AddtaskController implements Initializable {


    public JFXTextField getTask_name() {
        return Task_name;
    }

    public JFXTextField getAssignedto() {
        return Assignedto;
    }

    public JFXDatePicker getTask_start_date() {
        return task_start_date;
    }

    public JFXDatePicker getTask_end_date() {
        return task_end_date;
    }

    public JFXColorPicker getTask_color() {
        return task_color;
    }


    public JFXTextField getTaskProjectID() {
        return TaskProjectID;
    }

    public void setProjectName(JFXTextField projectName) {
        TaskProjectName.setText(projectName.getText());
    }

    public void setProject_id_task(JFXTextField project_id_task) {
        getTaskProjectID().setText(project_id_task.getText());
    }

    @FXML
    private JFXTextField TaskProjectName;
    @FXML
    private JFXTextField TaskProjectID;
    @FXML
    private JFXTextField Task_name;
    @FXML
    private JFXTextField Assignedto;
    @FXML
    private JFXDatePicker task_start_date = new JFXDatePicker();
    @FXML
    private JFXDatePicker task_end_date = new JFXDatePicker();
    @FXML
    private JFXColorPicker task_color = new JFXColorPicker();

    public ObservableList<String> list = FXCollections.observableArrayList();

    public void setDependency(ChoiceBox<String> dependency) {
        this.dependency = dependency;
    }

    public ChoiceBox<String> getDependency() {
        return dependency;
    }

    @FXML
    ChoiceBox<String> dependency = new ChoiceBox<String>();

    int i=0;
    int numberRow;
    String[] dependencyItems;
    public String dependencyItem ="";
    int columnsNumber;

    public void getList(ChoiceBox<String> checkbox) throws SQLException {

        Connect connect =new Connect();
        Connection connection=connect.getConnection();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT task_name FROM project_task WHERE project_id="+getTaskProjectID().getText());

        while (rs.next()) {
            list.add(rs.getString("task_name"));
        }

        checkbox.setItems(list);

        rs.close();
        connection.close();
    }

    @FXML
    private JFXButton AddTaskButton;


    private String calcDays(JFXDatePicker start_date, JFXDatePicker end_date) throws IOException {
        long intervalDays = (ChronoUnit.DAYS.between(start_date.getValue(), end_date.getValue()) + 1);
        return String.valueOf(intervalDays);
    }

    @FXML
    private void AddTaskDetail(javafx.event.ActionEvent event) throws Exception {
        if (event.getSource() == AddTaskButton) {

            Connect connect = new Connect();
            Connection connection = connect.getConnection();
            PreparedStatement ps = null;

            try {
                String sql = "insert into project_task(project_id,task_name, time, task_start_date, task_end_date, progress, color, dependency, assigned) values (?,?,?,?,?,?,?,?,?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, getTaskProjectID().getText());
                ps.setString(2, getTask_name().getText());
                ps.setString(3, calcDays(task_start_date,task_end_date) + " Days");
                ps.setDate(4, Date.valueOf(task_start_date.getValue()));
                ps.setDate(5, Date.valueOf(task_end_date.getValue()));
                ps.setString(6, "0%");
                ps.setString(7, String.valueOf(getTask_color().getValue()));
                ps.setString(8, dependency.getValue());
                ps.setString(9, getAssignedto().getText());
                ps.executeUpdate();

                ps.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


            Stage stage = (Stage) AddTaskButton.getScene().getWindow();
            stage.close();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //dependency.getItems().addAll(dependencyItems);

    }

}
