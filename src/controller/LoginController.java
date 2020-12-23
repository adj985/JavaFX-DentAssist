/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Users;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class LoginController implements Initializable {

    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button clearButton;
    @FXML
    private Hyperlink newUserLink;
    @FXML
    private Hyperlink changePasswordLink;
    @FXML
    private Label statusLabel;

    private ObservableList<Users> usersList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Logging in and opening the main window if the username and password are correct.
     * @param event 
     */
    @FXML
    private void login(ActionEvent event) {
        statusLabel.setText("");
        boolean indicator = false;

        if (userNameField.getText().equals("") || passwordField.getText().equals("")) {
            statusLabel.setText("Popunite sva polja!");
            return;
        } else {
            usersList = Users.loadUsers();
            for (int i = 0; i < usersList.size(); i++) {
                if (userNameField.getText().equals(usersList.get(i).getUserName()) && passwordField.getText().equals(usersList.get(i).getPassword())) {
                    indicator = true;
                    break;
                }
            }
        }

        if (!indicator) {
            statusLabel.setText("Nepostojeće korisničko ime ili lozinka!\nProverite unos i pokušajte ponovo!");
        } else {
            try {
               
                Stage primaryStage = new Stage();

                URL location = getClass().getClassLoader().getResource("view/MainWindow.fxml");
                AnchorPane root = FXMLLoader.<AnchorPane>load(location);

                Scene scene = new Scene(root);
                primaryStage.setTitle("Dentassist");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();

                primaryStage.setOnCloseRequest((closeEvent) -> {
                    System.exit(1);
                });

            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Erases all the fields.
     * @param event 
     */
    @FXML
    private void clear(ActionEvent event) {
        userNameField.clear();
        passwordField.clear();
        statusLabel.setText("");
    }

    /**
     * Opens administrator authentication.
     * @param event 
     */
    @FXML
    private void openNewUserForm(ActionEvent event) {
        
        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/AdminAuthentication.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Authentication");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Opens a form for updating an old password.
     * @param event 
     */
    @FXML
    private void openChangePasswordForm(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/ChangePassword.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Promena lozinke");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
