package App.ProjectDetail;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.SimpleDateFormat;

public class Task {

    private SimpleStringProperty TaskName;
    private SimpleStringProperty TaskTime;
    private SimpleStringProperty TaskStartDate;
    private SimpleStringProperty TaskEndDate;
    private SimpleStringProperty TaskProgress;
    private SimpleStringProperty TaskColor;
    private SimpleStringProperty TaskDependency;
    private SimpleStringProperty TaskAssinged;

    public Task(String taskName, String taskTime, String taskStartDate, String taskEndDate, String taskProgress, String taskColor, String taskDependency, String taskAssinged) {
        TaskName = new SimpleStringProperty(taskName);
        TaskTime = new SimpleStringProperty(taskTime);
        TaskStartDate = new SimpleStringProperty(taskStartDate);
        TaskEndDate = new SimpleStringProperty(taskEndDate);
        TaskProgress = new SimpleStringProperty(taskProgress);
        TaskColor = new SimpleStringProperty(taskColor);
        TaskDependency = new SimpleStringProperty(taskDependency);
        TaskAssinged = new SimpleStringProperty(taskAssinged);
    }

    public String getTaskName() {
        return TaskName.get();
    }

    public void setTaskName(String taskName) {
        TaskName = new SimpleStringProperty(taskName);
    }

    public String getTaskTime() {
        return TaskTime.get();
    }

    public void setTaskTime(String taskTime) {
        TaskTime = new SimpleStringProperty(taskTime);
    }

    public String getTaskStartDate() {
        return TaskStartDate.get();
    }

    public void setTaskStartDate(String taskStartDate) {
        TaskStartDate = new SimpleStringProperty(taskStartDate);
    }

    public String getTaskEndDate() {
        return TaskEndDate.get();
    }

    public void setTaskEndDate(String taskEndDate) {
        TaskEndDate = new SimpleStringProperty(taskEndDate);
    }

    public String getTaskProgress() {
        return TaskProgress.get();
    }

    public void setTaskProgress(String taskProgress) {
        this.TaskProgress = new SimpleStringProperty(taskProgress);
    }

    public String getTaskColor() {
        return TaskColor.get();
    }

    public void setTaskColor(String taskColor) {
        TaskColor = new SimpleStringProperty(taskColor);
    }

    public String getTaskDependency() {
        return TaskDependency.get();
    }

    public void setTaskDependency(String taskDependency) {
        TaskDependency = new SimpleStringProperty(taskDependency);
    }

    public String getTaskAssinged() {
        return TaskAssinged.get();
    }

    public void setTaskAssinged(String taskAssinged) {
        TaskAssinged = new SimpleStringProperty(taskAssinged);
    }
}
