package App.Client.AddClient;

import App.Client.AllClient.AllClientController;
import App.Connect;
import App.IntroPageAdmin.IntroPageAdmin;
import App.ProjectDetail.ProjectDetailController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {

    Stage stage;

    private String userRole;
    private int adminId;

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


    public JFXButton homeBackBtn;
    public JFXButton AddClientBtn;
    public JFXButton viewAllClient;
    public JFXButton addNewClientBtn;
    public JFXTextField clientIdField;
    public JFXTextField addressField;
    public JFXTextField phoneField;
    public JFXTextField clientNameField;
    public JFXTextField contactPersonField;
    public Label confirmationMsg;

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../../IntroPageAdmin/intropageadmin.fxml"));

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
    }

    public void AddClientBtnAction(ActionEvent event) {
    }

    public void viewAllClientAction(ActionEvent event) {
        if(event.getSource() == viewAllClient){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../AllClient/allclient.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AllClientController allClientController = Loader.getController();
            allClientController.setUserRole(getUserRole());
            allClientController.setAdminId(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) AddClientBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void addNewClientAction(ActionEvent event) {
        if(event.getSource() == addNewClientBtn){
            int clientId = Integer.parseInt(clientIdField.getText());
            String clientName = clientNameField.getText();
            String contactPerson = contactPersonField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            if(clientName.trim().isEmpty() || contactPerson.trim().isEmpty() || phone.trim().isEmpty() || address.trim().isEmpty()){
                confirmationMsg.setText("Please fillup the form correctly.");
                return;
            }

            Connect connect = new Connect();
            Connection connection = connect.getConnection();
            PreparedStatement ps = null;

            try {
                String sql = "INSERT INTO CLIENT(name, contact_person, phone, address) values (?,?,?,?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, clientName);
                ps.setString(2, contactPerson);
                ps.setString(3, phone);
                ps.setString(4, address);
                ps.executeUpdate();

                ps.close();

                confirmationMsg.setStyle("-fx-text-fill: #24bb71");
                confirmationMsg.setText("A client named "+ clientName + " has been added.");

            } catch (SQLException e) {
                e.printStackTrace();
                confirmationMsg.setText(e.getMessage());
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int id = 0;

        Image imageDecline = new Image(getClass().getResourceAsStream("../../icons/home-icon.png"));
        ImageView cameraIconView = new ImageView(imageDecline);
        cameraIconView.setFitHeight(25);
        cameraIconView.setFitWidth(25);
        homeBackBtn.setGraphic(cameraIconView);

        AddClientBtn.setStyle("-fx-background-color: #5d2664");

        try {
            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT MAX(id) as id FROM CLIENT";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                id = (int) rs.getInt("id");
            }

            statement.close();
            connection.close();

            //increment id by 1 to set as new id
            id += 1;
            clientIdField.setText(String.valueOf(id));
            clientIdField.setEditable(false);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
