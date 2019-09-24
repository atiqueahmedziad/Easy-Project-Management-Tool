package App.IntroPageAdmin;

import javafx.beans.property.SimpleStringProperty;

public class Project {
    private SimpleStringProperty projectName;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;

    public Project(String projectName, String startDate, String endDate) {
        this.projectName = new SimpleStringProperty(projectName);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
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

}
