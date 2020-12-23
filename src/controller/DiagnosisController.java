/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Diagnosis;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class DiagnosisController implements Initializable {

    @FXML
    private TableView<Diagnosis> diagnosisTable;
    @FXML
    private TableColumn<Diagnosis, Integer> idDiagnosisCol;
    @FXML
    private TableColumn<Diagnosis, String> diagnosisNameCol;
    @FXML
    private TextField diagnosisNameField;
    @FXML
    private Button addDiagnosisButton;
    @FXML
    private Button updateDiagnosisButton;

    ObservableList<Diagnosis> diagnosisList;
    @FXML
    private Button clearButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        idDiagnosisCol.setCellValueFactory(new PropertyValueFactory<>("iddiagnosis"));
        diagnosisNameCol.setCellValueFactory(new PropertyValueFactory<>("diagnosisName"));
        updateDiagnosisButton.setDisable(true);

        //Loading diagnosis in a table
        try {
            diagnosisTable.setItems(diagnosisList = Diagnosis.loadDiagnosis());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    /**
     * Creating a new diagnosis
     */
    @FXML
    private void newDiagnosis(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        if (diagnosisNameField.getText().equals("")) {
            a.setContentText("Unesite naziv diajgnoze!");
            a.show();
        } else {
            Diagnosis.newDiagnosis(new Diagnosis(null, diagnosisNameField.getText()));
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Uspešno ste dodali dijagnozu.");
            a.show();
            diagnosisNameField.clear();
        }

        try {
            diagnosisTable.setItems(diagnosisList = Diagnosis.loadDiagnosis());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    /**
     * Updates diagnosis data after selecting in the table
     * @param event 
     */
    @FXML
    private void updateDiagnosis(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        if (diagnosisNameField.getText().equals("")) {
            a.setContentText("Unesite naziv dijagnoze");
            a.show();
        } else {
            Diagnosis.updateDiagnosis(diagnosisTable.getSelectionModel().getSelectedItem().getIddiagnosis(), diagnosisNameField.getText());
            diagnosisList.clear();
            diagnosisTable.setItems(null);
            diagnosisNameField.clear();
            try {
                diagnosisTable.setItems(diagnosisList = Diagnosis.loadDiagnosis());
                updateDiagnosisButton.setDisable(true);
                addDiagnosisButton.setDisable(false);
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Uspešno ste ažurirali podatke o diajgnozi.");
            a.show();
        }

    }

    /**
     * Enters selected value from table into the textBox for possible changes
     * @param event 
     */
    @FXML
    private void fillBox(MouseEvent event) {

        if (!diagnosisTable.getSelectionModel().isEmpty()) {
            diagnosisNameField.setText(diagnosisTable.getSelectionModel().getSelectedItem().getDiagnosisName());
            updateDiagnosisButton.setDisable(false);
            addDiagnosisButton.setDisable(true);
        }
    }

    /**
     * Clearing textBox and table selection
     */
    @FXML
    private void clearSelection(ActionEvent event) {
        diagnosisTable.getSelectionModel().clearSelection();
        diagnosisNameField.clear();
        addDiagnosisButton.setDisable(false);
        updateDiagnosisButton.setDisable(true);
    }

}
