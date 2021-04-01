/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.SessionConfig;
import model.Patients;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class PatientsController implements Initializable {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNamefield;
    @FXML
    private TextField icNumberField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField residenceField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea warningNoteArea;
    @FXML
    private Button createPatientButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Patients> patientsTable;
    @FXML
    private TableColumn<Patients, String> firstNameCol;
    @FXML
    private TableColumn<Patients, String> lastNameCol;
    @FXML
    private TableColumn<Patients, String> icNumberCol;
    @FXML
    private TableColumn<Patients, String> addressCol;
    @FXML
    private TableColumn<Patients, String> residenceCol;
    @FXML
    private TableColumn<Patients, String> phoneNumberCol;
    @FXML
    private TableColumn<Patients, String> emailCol;
    @FXML
    private TableColumn<Patients, String> warningNoteCol;
    @FXML
    private Button updateButton;
    private ObservableList<Patients> patientList = FXCollections.observableArrayList();
    @FXML
    private Label charLabel;
    private Session session;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        icNumberCol.setCellValueFactory(new PropertyValueFactory<>("icNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        residenceCol.setCellValueFactory(new PropertyValueFactory<>("residence"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        warningNoteCol.setCellValueFactory(new PropertyValueFactory<>("warningNote"));

        patientsTable.setItems(patientList = Patients.loadPatients());

        if (patientsTable.getSelectionModel().isEmpty()) {
            updateButton.setDisable(true);
        }
    }

    /**
     * Creating a new patient and creating a text document with patient data. 
     * This document represents a medical chart of the patient.
     * 
     * @param event 
     */
    @FXML
    private void createPatient(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        boolean indicator = false;

        for (int i = 0; i < patientList.size(); i++) {
            if (icNumberField.getText().equals(patientList.get(i).getIcNumber())) {
                indicator = true;
                break;
            }
        }

        if (firstNameField.getText().equals("")) {
            a.setContentText("Unesite ime!");
            a.show();
        } else if (lastNamefield.getText().equals("")) {
            a.setContentText("Unesite prezime!");
            a.show();
        } else if (icNumberField.getText().toCharArray().length != 13) {
            a.setContentText("Matični broj mora imati 13 cifara!");
            a.show();
        } else if (addressField.getText().equals("")) {
            a.setContentText("Unesite  adresu!");
            a.show();
        } else if (residenceField.getText().equals("")) {
            a.setContentText("Unesite  naziv mesta!");
            a.show();
        } else if (phoneNumberField.getText().equals("")) {
            a.setContentText("Unesite  broj telefona!");
            a.show();
        } else if (warningNoteArea.getText().toCharArray().length > 1000) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Prekoračili ste maksimalni broj karaktera!\nMaksimalni unos je 1000 karaktera u polju za napomenu.");
            a.show();
        } else if (indicator) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Matični broj već postoji u bazi.\nIli je pacijent registrovan ili ste pogrešno uneli podatke!\nProverite unos pa pokušajte ponovo.");
            a.show();
        } else {
            try {
                Patients.createPatient(new Patients(null, firstNameField.getText(), lastNamefield.getText(), icNumberField.getText(), addressField.getText(),
                        residenceField.getText(), phoneNumberField.getText(), emailField.getText(), warningNoteArea.getText()));

                charLabel.setText("");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText(null);
            a.setContentText("Uspešno ste kreirali novog pacijenta.");
            a.show();

            patientsTable.setItems(patientList = Patients.loadPatients());

            //Creating document for new patient
            String filePath = "C:\\Kartoteka\\" + firstNameField.getText().trim() + " " + lastNamefield.getText().trim() + ", " + residenceField.getText().trim() + " -jmbg " + icNumberField.getText().trim() + ".txt";

            String textToAppend = firstNameField.getText().toUpperCase().trim() + " " + lastNamefield.getText().toUpperCase().trim() + "\nJMBG: " + icNumberField.getText().trim() + "\n\nNapomena: "
                    + warningNoteArea.getText().trim() + "\n\nPrebivalište: " + residenceField.getText().trim() + "\nAdresa: " + addressField.getText().trim() + "\n\n\nIntervencije:\n";

            File file = new File(filePath);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    Files.write(Paths.get(filePath), textToAppend.getBytes(), StandardOpenOption.APPEND);
                }

            } catch (IOException e) {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setHeaderText("Došlo je do greške prilikom kreiranje fajla!");
                a.setContentText("Pokušajte ponovo ili se pozovite podršku.");
                a.show();
            }
        }
    }

    /**
     * It clears all the data in the fields.
     * 
     * @param event 
     */
    @FXML
    private void clearData(ActionEvent event) {
        firstNameField.setText("");
        lastNamefield.setText("");
        icNumberField.setText("");
        addressField.setText("");
        residenceField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        warningNoteArea.setText("");
        patientsTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        createPatientButton.setDisable(false);
        searchField.setText("");
    }

    /**
     * Updating patients data.
     * 
     * @param event 
     */
    @FXML
    private void updatePatients(ActionEvent event) {

        session = SessionConfig.createSession();
        Transaction tr = null;
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        try {
            tr = session.beginTransaction();
            Patients p = (Patients) session.load(Patients.class, patientsTable.getSelectionModel().getSelectedItem().getIdpatients());
            if (firstNameField.getText().equals("")) {
                a.setContentText("Unesite ime!");
                a.show();
                clearData(event);

            } else if (lastNamefield.getText().equals("")) {
                a.setContentText("Unesite prezime!");
                a.show();

            } else if (icNumberField.getText().toCharArray().length != 13) {
                a.setContentText("Matični broj mora imati 13 cifara!");
                a.show();
                clearData(event);

            } else if (addressField.getText().equals("")) {
                a.setContentText("Unesite  adresu!");
                a.show();
            } else if (residenceField.getText().equals("")) {
                a.setContentText("Unesite  naziv mesta!");
                a.show();
                clearData(event);

            } else if (phoneNumberField.getText().equals("")) {
                a.setContentText("Unesite  broj telefona!");
                a.show();
                clearData(event);

            } else if (warningNoteArea.getText().toCharArray().length > 1000) {
                a.setContentText("Prekoračili ste maksimalni broj karaktera!\nMaksimalni unos je 1000 karaktera u polju za napomenu.");
                a.show();
                clearData(event);

            } else {
                p.setFirstName(firstNameField.getText());
                p.setLastName(lastNamefield.getText());
                p.setIcNumber(icNumberField.getText());
                p.setAddress(addressField.getText());
                p.setResidence(residenceField.getText());
                p.setEmail(emailField.getText());
                p.setPhoneNumber(phoneNumberField.getText());
                p.setWarningNote(warningNoteArea.getText());
                session.update(p);
                tr.commit();

                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Uspešno ste izmenili podatke o pacijentu!");
                a.show();
                clearData(event);
                charLabel.setText("");
            }
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
        } finally {
            session.close();
        }
        patientsTable.setItems(patientList = Patients.loadPatients());

    }

    /**
     * Filling the fields with data loaded from the table selection model.
     * Doing so, user can manipulate existing data.
     * 
     * @param event 
     */
    @FXML
    private void fillBoxes(MouseEvent event) {
        if (!patientsTable.getSelectionModel().isEmpty()) {
            firstNameField.setText(patientsTable.getSelectionModel().getSelectedItem().getFirstName());
            lastNamefield.setText(patientsTable.getSelectionModel().getSelectedItem().getLastName());
            icNumberField.setText(patientsTable.getSelectionModel().getSelectedItem().getIcNumber());
            addressField.setText(patientsTable.getSelectionModel().getSelectedItem().getAddress());
            residenceField.setText(patientsTable.getSelectionModel().getSelectedItem().getResidence());
            phoneNumberField.setText(patientsTable.getSelectionModel().getSelectedItem().getPhoneNumber());
            emailField.setText(patientsTable.getSelectionModel().getSelectedItem().getEmail());
            warningNoteArea.setText(patientsTable.getSelectionModel().getSelectedItem().getWarningNote());
            
            if (updateButton.isDisabled()) {
                updateButton.setDisable(false);
                createPatientButton.setDisable(true);
            }
        }
        

    }

    /**
     * Counting characters. Later used for limiting character entry.
     * 
     * @param event 
     */
    @FXML
    private void characterCount(KeyEvent event) {
        charLabel.setText(String.valueOf(warningNoteArea.getText().toCharArray().length));
    }

    /**
     * Searching for patients by firstName and/or lastName
     * 
     * @param event 
     */
    @FXML
    private void search(KeyEvent event) {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Patients> filteredData = new FilteredList<>(patientList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if ((patient.getFirstName().toLowerCase() + " " + patient.getLastName().toLowerCase()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return (patient.getLastName().toLowerCase() + " " + patient.getFirstName().toLowerCase()).contains(lowerCaseFilter);
                }
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Patients> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(patientsTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        patientsTable.setItems(sortedData);
    }
}
