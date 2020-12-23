/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.javafx.stage.StageHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Users;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class NewUserController implements Initializable {

    @FXML
    private Button newUserButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;

    private ObservableList<Users> usersList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StageHelper.getStages().get(StageHelper.getStages().size() - 2).close();

    }

    /**
     * It creates a new user.
     * @param event 
     */
    @FXML
    private void createUser(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        boolean indicator = false;

        //Checking for empty fields
        if (userNameField.getText().equals("") || passwordField.getText().equals("") || repeatPasswordField.getText().equals("")) {
            a.setContentText("Popunite sva polja!");
            a.show();
            return;
        } 
        //Checking if passwords are match in both fields
        if (!passwordField.getText().equals(repeatPasswordField.getText())) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Šifre se ne podudaraju!\nUnesite identične šifre u oba polja!");
            a.show();
            return;
        } else {
            usersList = Users.loadUsers();
            //Checking if desired username allready exists in the database. If yes, indicator is set to true. 
            for (int i = 0; i < usersList.size(); i++) {
                if (userNameField.getText().equals(usersList.get(i).getUserName())) {
                    indicator = true;
                    break;
                }
            }
        }
        //If the indicator is true the desired username already exists in the database so the alert pops with a message. If the indicator is false a new user is created.
        if (indicator) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Korisnik već postoji!\nIzaberite drugo korisničko ime.");
            a.show();
            usersList.clear();
        } else {
            Users.createUser(new Users(null, userNameField.getText(), passwordField.getText()));
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Uspešno ste registrovali korisnika");
            a.show();
        }
    }

    /**
     * It clears all the fields.
     * @param event 
     */
    @FXML
    private void clear(ActionEvent event) {
        userNameField.clear();
        passwordField.clear();
        repeatPasswordField.clear();
    }

}
