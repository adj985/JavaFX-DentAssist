/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Patients;
import model.Scheduling;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class SchedulingController implements Initializable {

    @FXML
    private Button scheduleButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField residenceField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Spinner<Integer> minutesSpinner;
    @FXML
    private ComboBox<Patients> selectPatientCombo;

    Patients p;

    ObservableList<Patients> patientsList;
    ArrayList<Scheduling> arrayList;
    ObservableList<Scheduling> scheduleList;
    @FXML
    private Hyperlink addPatients;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(07, 22));
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15));

        try {
            patientsList = Patients.loadPatients();
            selectPatientCombo.getItems().addAll(patientsList);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    /**
     * Saving a new appointment in the database.
     * 
     * @param event 
     */
    @FXML
    private void schedule(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        scheduleList = Scheduling.loadSchedule();

        boolean isFree = true;

        //Checking if the patient is selected.
        try {
            if (selectPatientCombo.getSelectionModel().isSelected(-1)) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Izaberite pacijenta!");
                a.setHeaderText(null);
                a.show();
                return;
            }

            //Checking if the desired time of an appointment is not taken already.
            for (int i = 0; i < scheduleList.size(); i++) {
                if (scheduleList.get(i).getScheduledDate().equals(date.format(formatter)) && scheduleList.get(i).getScheduledTime().equals(hoursSpinner.getValue() + ":" + minutesSpinner.getValue())) {
                    isFree = false;
                    break;
                }
            }
            
            if (isFree == false) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Termin je zauzet!\nIzaberite drugi termin.");
                a.setHeaderText(null);
                a.show();
            } else if (date.isBefore(LocalDate.now())) {

                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Traženi datum je već prošao!\nIzaberite drugi datum");
                a.setHeaderText(null);
                a.show();

            } else {
                p = Patients.findPatient(selectPatientCombo.getSelectionModel().getSelectedItem().getIdpatients());

                Scheduling.schedule(new Scheduling(date.format(formatter), hoursSpinner.getValue() + ":" + minutesSpinner.getValue(), p));

                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Uspešno ste izvršili zakazivanje!");
                a.setHeaderText(null);
                a.show();
            }

        } catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Izaberite datum!");
            a.setHeaderText(null);
            a.show();
        }

    }

    /**
     * Clearing the fields.
     * 
     * @param event 
     */
    @FXML
    private void clear(ActionEvent event) {
        selectPatientCombo.getSelectionModel().clearSelection();
        firstNameField.clear();
        lastNameField.clear();
        residenceField.clear();
        phoneNumberField.clear();
        datePicker.setValue(null);

    }

    /**
     * Loading data in the fields from the table selection model. Used for data update.
     * 
     * @param event 
     */
    @FXML
    private void dataLoad(ActionEvent event) {
        try {
            p = Patients.findPatient(selectPatientCombo.getSelectionModel().getSelectedItem().getIdpatients());
            firstNameField.setText(p.getFirstName());
            lastNameField.setText(p.getLastName());
            residenceField.setText(p.getResidence());
            phoneNumberField.setText(p.getPhoneNumber());

        } catch (Exception e) {
            e.getMessage();
        }

    }

    @FXML
    private void openPatients(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/Patients.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Pacijenti");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            addPatients.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                addPatients.setDisable(false);
                patientsList.clear();
                selectPatientCombo.getItems().clear();

                try {
                    patientsList = Patients.loadPatients();

                    for (int i = 0; i < patientsList.size(); i++) {
                        selectPatientCombo.getItems().add(i, patientsList.get(i));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

//                primaryStage.close();
            });

        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
