/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Users;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class ChangePasswordController implements Initializable {

    @FXML
    private Button changePasswordButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
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
        // TODO
    }

    /**
     * The method used to change an old password
     * @param event 
     */
    @FXML
    private void changePassword(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        boolean indicator = false;
        int userId = 0;

        //Checking for empty fields
        if (userNameField.getText().equals("") || oldPasswordField.getText().equals("") || newPasswordField.getText().equals("")) {
            a.setContentText("Popunite sva polja!");
            a.show();
        } else {
            usersList = Users.loadUsers();

            //UserName and old password validation and getting userId
            for (int i = 0; i < usersList.size(); i++) {
                if (userNameField.getText().equals(usersList.get(i).getUserName()) && oldPasswordField.getText().equals(usersList.get(i).getPassword())) {
                    indicator = true;
                    userId = usersList.get(i).getIduser();
                    break;
                }
            }

            //If indicator is false it pops alert, else password is updating on userId
            if (!indicator) {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setHeaderText("Greška pri validaciji!");
                a.setContentText("Korisničko ime ili stara šifra nisu ispravni!\nProverite unos i pokušajte ponovo.");
                a.show();
            } else {
                if (userId > 0) {
                    Users.updatePassword(userId, newPasswordField.getText());
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Uspešno ste promenili šifru!");
                    a.show();
                } else {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Došlo je do greške! Probajte ponovo ili kontaktirajte podršku!");
                    a.show();
                }
            }
        }
    }

    /**
     * Clearing the fields
     * @param event 
     */
    @FXML
    private void clear(ActionEvent event) {
        userNameField.clear();
        oldPasswordField.clear();
        newPasswordField.clear();
    }

}
