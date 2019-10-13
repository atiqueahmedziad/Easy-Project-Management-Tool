package App.SearchProject;

import javafx.beans.property.SimpleStringProperty;

public class ProjectEach {
    private SimpleStringProperty id;
    private SimpleStringProperty projectName;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleStringProperty estTime;

    public ProjectEach(String id, String projectName, String startDate, String endDate, String estTime) {
        this.id = new SimpleStringProperty(id);
        this.projectName = new SimpleStringProperty(projectName);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.estTime = new SimpleStringProperty(estTime);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getProjectName() {
        return projectName.get();
    }

    public void setProjectName(String projectName) {
        this.projectName = new SimpleStringProperty(projectName);
    }

    public String getProjectStartDate() {
        return startDate.get();
    }

    public void setProjectStartDate(String startDate) {
        this.startDate = new SimpleStringProperty(startDate);
    }

    public String getProjectEndDate() {
        return endDate.get();
    }

    public void setProjectEndDate(String endDate) {
        this.endDate = new SimpleStringProperty(endDate);
    }

    public String getEstTime() {
        return estTime.get();
    }

    public void setEstTime(String estTime) {
        this.estTime = new SimpleStringProperty(estTime);
    }

}
