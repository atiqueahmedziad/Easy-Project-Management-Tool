package App.AdminProfile;


import App.Connect;

import App.IntroPageAdmin.IntroPageAdmin;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminProfile implements Initializable {
    private int adminId;
    private String UserRole;

    public String getUserRole(){ return UserRole; }

    public void setUserRole(String userRole){ this.UserRole=userRole; }

    public int getAdminId(){ return adminId; }

    public void setAdminId(int adminid){ this.adminId=adminid; }

    public JFXButton homeBackBtn;
    public JFXButton editProfileBtn;
    public JFXButton saveBtn;
    public JFXTextField adminIdText;
    public JFXTextField adminNameText;
    public JFXTextField adminUsernameText;
    public JFXTextField adminPassText;
    public JFXTextField adminDesText;
    public JFXTextField adminEmailText;
    public JFXTextField adminContactText;

    public Label confirmationMsg;

    public void setAdminInfo() throws SQLException {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM ADMIN WHERE id="+adminId;
        String sql2 = "SELECT * FROM ADMIN_AUTH WHERE id="+adminId;

        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String desig = rs.getString("designation");

            String email = rs.getString("email");
            String contact = rs.getString("contact");

            adminNameText.setText(name);
            adminIdText.setText(String.valueOf(id));
            adminDesText.setText(desig);
            adminEmailText.setText(email);
            adminContactText.setText(contact);

        }

        ResultSet rs2 = statement.executeQuery(sql2);

        while(rs2.next()){
            String username = rs2.getString("username");
            String pass = rs2.getString("password");

            adminUsernameText.setText(username);
            adminPassText.setText(pass);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setVisible(false);
        saveBtn.setDisable(true);

        adminNameText.setEditable(false);
        adminPassText.setEditable(false);
        adminDesText.setEditable(false);
        adminContactText.setEditable(false);
        adminEmailText.setEditable(false);
    }

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../IntroPageAdmin/IntroPageAdmin.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            IntroPageAdmin introPageAdmin = Loader.getController();
            introPageAdmin.setAdminId(adminId);
            introPageAdmin.setUserRole(getUserRole());
            introPageAdmin.getAdminName(adminId);

            Parent p = Loader.getRoot();
            Stage stage = (Stage) homeBackBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }


    public void editProfileBtnAction(ActionEvent event) {
        if(event.getSource() == editProfileBtn){
            saveBtn.setDisable(false);
            saveBtn.setVisible(true);

            adminNameText.setEditable(true);
            adminPassText.setEditable(true);
            adminDesText.setEditable(true);
            adminEmailText.setEditable(true);
            adminContactText.setEditable(true);
        }
    }

    public void saveBtnAction(ActionEvent event) {
        if(event.getSource() == saveBtn){
            String adminName = adminNameText.getText();
            String adminPass = adminPassText.getText();
            String adminDes = adminDesText.getText();
            String adminEmail = adminEmailText.getText();
            String adminContact = adminContactText.getText();

            if(adminName.trim().isEmpty() || adminPass.trim().isEmpty() || adminDes.trim().isEmpty()){
                confirmationMsg.setText("Please fill up the form correctly.");
                return;
            }

            Connect connect = new Connect();
            Connection connection = connect.getConnection();

            try{
                String sql = "UPDATE ADMIN set name=?, designation=?, email =?, contact =? WHERE id=?";

                PreparedStatement preparedStmt = connection.prepareStatement(sql);
                preparedStmt.setString(1, adminName);
                preparedStmt.setString(2, adminDes);
                preparedStmt.setString(3, adminEmail);
                preparedStmt.setString(4, adminContact);
                preparedStmt.setInt(5,adminId);

                preparedStmt.executeUpdate();

                String sql2 = "UPDATE ADMIN_AUTH set password=? WHERE id=?";

                PreparedStatement ps_auth = connection.prepareStatement(sql2);
                ps_auth.setString(1,adminPass);
                ps_auth.setInt(2,adminId);

                ps_auth.executeUpdate();

                saveBtn.setDisable(true);
                saveBtn.setVisible(false);
                adminNameText.setEditable(false);
                adminPassText.setEditable(false);
                adminDesText.setEditable(false);
                adminEmailText.setEditable(false);
                adminContactText.setEditable(false);
                confirmationMsg.setStyle("-fx-text-fill: #24bb71");
                confirmationMsg.setText("Saved Successfully!");
            }catch (Exception e){
                e.printStackTrace();
                confirmationMsg.setText(e.getMessage());
            }
        }
    }
}
