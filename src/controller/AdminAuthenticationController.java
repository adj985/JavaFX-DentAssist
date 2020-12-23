/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.javafx.stage.StageHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class AdminAuthenticationController implements Initializable {

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button okButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Opens form for creating new user, after administrator authentication 
     * @param event 
     */
    @FXML
    private void openNewUserForm(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);

        if (!passwordField.getText().equals("admin123")) {
            a.setContentText("Uneli ste netačnu šifru administratora!");
            a.show();
        } else {
            try {

                Stage primaryStage = new Stage();

                URL location = getClass().getClassLoader().getResource("view/NewUser.fxml");
                AnchorPane root = FXMLLoader.<AnchorPane>load(location);

                Scene scene = new Scene(root);
                primaryStage.setTitle("Dodavanje novog korisnika");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();

                //Opens Login Form, after closing New User Form
                primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent closeEvent) {
                        try {
                            primaryStage.close();
                            Stage stage = new Stage();

                            URL location = getClass().getClassLoader().getResource("view/Login.fxml");
                            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

                            Scene scene = new Scene(root);
                            stage.setTitle("Login");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(AdminAuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }

            StageHelper.getStages().get(StageHelper.getStages().size() - 2).close();

        }

    }

}
