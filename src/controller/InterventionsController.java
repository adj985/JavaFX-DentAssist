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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Diagnosis;
import model.Interventions;
import model.Patients;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class InterventionsController implements Initializable {

    @FXML
    private ComboBox<Patients> selectPatientComboBox;
    @FXML
    private ComboBox<Diagnosis> selectDiagnosisComboBox;
    @FXML
    private ComboBox<Integer> selectToothComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField priceField;
    @FXML
    private CheckBox chargedCheckBox;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button updateButton;
    @FXML
    private TableView<Interventions> interventionTable;
    @FXML
    private TableColumn<Interventions, String> firstNameCol;
    @FXML
    private TableColumn<Interventions, String> lastNameCol;
    @FXML
    private TableColumn<Interventions, String> residenceCol;
    @FXML
    private TableColumn<Interventions, String> phoneNumberCol;
    @FXML
    private TableColumn<Interventions, String> diagnosisCol;
    @FXML
    private TableColumn<Interventions, Integer> toothCol;
    @FXML
    private TableColumn<Interventions, Double> priceCol;
    @FXML
    private TableColumn<Interventions, String> chargedCol;
    @FXML
    private TableColumn<Interventions, LocalDate> dateCol;
    @FXML
    private TableColumn<Interventions, String> descriptionCol;
    @FXML
    private Hyperlink debtsHyperLink;
    @FXML
    private Hyperlink interventionsHyperLink;

    ObservableList<Integer> teethList = FXCollections.observableArrayList(11, 12, 13, 14, 15, 16, 17, 18, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 61, 62, 63, 64, 65, 71, 72, 73, 74, 75, 81, 82, 83, 84, 85);
    ObservableList<Patients> patientsList;
    ObservableList<Diagnosis> diagnosisList;

    ObservableList<Interventions> interventionsList;
    @FXML
    private TextField searchField;

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
        residenceCol.setCellValueFactory(new PropertyValueFactory<>("residence"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        toothCol.setCellValueFactory(new PropertyValueFactory<>("tooth"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        chargedCol.setCellValueFactory(new PropertyValueFactory<>("charged"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateOfIntervention"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        selectToothComboBox.setItems(teethList);
        selectPatientComboBox.setItems(patientsList = Patients.loadPatients());
        selectDiagnosisComboBox.setItems(diagnosisList = Diagnosis.loadDiagnosis());

        if (interventionTable.getSelectionModel().isEmpty()) {
            updateButton.setDisable(true);
        }

        interventionTable.setItems(interventionsList = Interventions.loadInterventions());
        
    }

    /**
     * Loading data from a selected row in the table into a form.
     * 
     * @param event 
     */
    @FXML
    private void loadData(MouseEvent event) {
        if (!interventionTable.getSelectionModel().isEmpty()) {
            saveButton.setDisable(true);
            updateButton.setDisable(false);
            priceField.setEditable(false);

            boolean isTrue = interventionTable.getSelectionModel().getSelectedItem().getCharged().equals("DA");

            selectPatientComboBox.getSelectionModel().clearSelection();
            selectDiagnosisComboBox.getSelectionModel().clearSelection();
            selectToothComboBox.getSelectionModel().clearSelection();
            datePicker.setValue(LocalDate.parse(interventionTable.getSelectionModel().getSelectedItem().getDateOfIntervention(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            priceField.setText(String.valueOf(interventionTable.getSelectionModel().getSelectedItem().getPrice()));
            chargedCheckBox.setSelected(isTrue);
            descriptionArea.setText(interventionTable.getSelectionModel().getSelectedItem().getDescription());

        } else {
            saveButton.setDisable(false);
            updateButton.setDisable(true);
        }
    }

    /**
     * Saving a new intervention and appending intervention data in a text file for the desired patient. 
     * This method also checks for identical interventions in the database and thus preventing duplicate entries.
     * 
     * @param event 
     */
    @FXML
    private void saveIntervention(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        //Checking for empty fields and boxes
        if (selectPatientComboBox.getSelectionModel().isEmpty()) {
            a.setContentText("Izaberite pacijenta!");
            a.show();
        } else if (selectDiagnosisComboBox.getSelectionModel().isEmpty()) {
            a.setContentText("Izaberite dijagnozu!");
            a.show();
        } else if (selectToothComboBox.getSelectionModel().isEmpty()) {
            a.setContentText("Izaberite zub!");
            a.show();
        } else if (datePicker.getValue() == null) {
            a.setContentText("Izaberite datum!");
            a.show();
        } else if (priceField.getText().equals("")) {
            a.setContentText("Unesite cenu!");
            a.show();
        } else if (descriptionArea.getText().equals("")) {
            a.setContentText("Unesite opis intervencije!");
            a.show();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String charged;

            //Storing String value in a "charged" variable (later used in the database)
            if (chargedCheckBox.isSelected()) {
                charged = "DA";
            } else {
                charged = "NE";
            }
            Interventions interventions = null;

            //Checking if there is an identical entry in the database
            for (int i = 0; i < interventionTable.getItems().size(); i++) {

                interventions = new Interventions(selectDiagnosisComboBox.getSelectionModel().getSelectedItem(), selectPatientComboBox.getSelectionModel().getSelectedItem(),
                        selectToothComboBox.getValue(), descriptionArea.getText(), datePicker.getValue().format(formatter), Double.parseDouble(priceField.getText()), charged);

                if (interventionTable.getItems().get(i).getFirstName().equals(interventions.getPatients().getFirstName())
                        && interventionTable.getItems().get(i).getLastName().equals(interventions.getPatients().getLastName())
                        && interventionTable.getItems().get(i).getResidence().equals(interventions.getPatients().getResidence())
                        && interventionTable.getItems().get(i).getPhoneNumber().equals(interventions.getPatients().getPhoneNumber())
                        && interventionTable.getItems().get(i).getDiagnosis().toString().equals(interventions.getDiagnosis().toString())
                        && interventionTable.getItems().get(i).getTooth() == interventions.getTooth()
                        && interventionTable.getItems().get(i).getDateOfIntervention().equals(interventions.getDateOfIntervention())) {

                    a.setContentText("U bazi vec postoji identican unos!");
                    a.show();
                    return;
                }
            }

            Interventions.saveIntervention(interventions = new Interventions(selectDiagnosisComboBox.getSelectionModel().getSelectedItem(),
                    selectPatientComboBox.getSelectionModel().getSelectedItem(), selectToothComboBox.getValue(), descriptionArea.getText(),
                    datePicker.getValue().format(formatter), Double.parseDouble(priceField.getText()), charged));
            
            
            //Appending intervention data into a patient document
            String filePath = "D:\\Kartoteka\\" + selectPatientComboBox.getSelectionModel().getSelectedItem().getFirstName() + " " + 
                    selectPatientComboBox.getSelectionModel().getSelectedItem().getLastName() + ", " + selectPatientComboBox.getSelectionModel().getSelectedItem().getResidence() + " -jmbg " + 
                    selectPatientComboBox.getSelectionModel().getSelectedItem().getIcNumber() + ".txt";
            String textToAppend = "\n\n\n" + datePicker.getValue().format(formatter) + "\n\n" + selectToothComboBox.getValue() + "- " + selectDiagnosisComboBox.getSelectionModel().getSelectedItem() + "\n" + descriptionArea.getText();
            File file = new File(filePath);
            try{
                if (file.exists()) {
                    Files.write(Paths.get(filePath), textToAppend.getBytes(), StandardOpenOption.APPEND);
                } else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Došlo je do greške! Kontaktirajte podršku!");
                    a.show();
                }
                
            } catch(IOException e){
                a.setAlertType(Alert.AlertType.ERROR);
                a.setHeaderText("Došlo je do greške!");
                a.setContentText("Pokušajte ponovo ili se pozovite podršku.");
                a.show();
            }

            //Refreshing the list after new entry
            interventionTable.getItems().clear();
            interventionsList.clear();

            interventionTable.setItems(interventionsList = Interventions.loadInterventions());

            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Uspešno ste uneli intervenciju!");
            a.show();
        }
    }

    /**
     * Reset all the fields
     * 
     * @param event 
     */
    @FXML
    private void clearSelection(ActionEvent event) {
        selectDiagnosisComboBox.getSelectionModel().clearSelection();
        selectDiagnosisComboBox.getSelectionModel().clearSelection();
        selectToothComboBox.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        priceField.clear();
        priceField.setEditable(true);
        chargedCheckBox.setSelected(true);
        descriptionArea.clear();
        interventionTable.getSelectionModel().clearSelection();
        saveButton.setDisable(false);
        updateButton.setDisable(true);
        searchField.setText("");
    }

    /**
     * Updating intervention data
     * 
     * @param event 
     */
    @FXML
    private void updateIntervention(ActionEvent event) {
        String charged;
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

            Interventions.updateIntervention(interventionTable.getSelectionModel().getSelectedItem().getIdinterventions(), charged = chargedCheckBox.isSelected() ? "DA" : "NE", descriptionArea.getText());

            interventionsList.clear();
            interventionTable.getItems().clear();
            interventionTable.setItems(interventionsList = Interventions.loadInterventions());

            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Uspešno ste izmenili podatke!");
            a.show();
            clearSelection(event);
    }

    /**
     * Showing only patients with debt
     * 
     * @param event 
     */
    @FXML
    private void showDebts(ActionEvent event) {
        if (!interventionsList.isEmpty() && !interventionTable.getItems().isEmpty()) {
            interventionsList.clear();
            interventionTable.getItems().clear();
        }

        interventionTable.setItems(interventionsList = Interventions.showDebts());
    }

    /**
     * Showing all interventions
     * 
     * @param event 
     */
    @FXML
    private void showInterventions(ActionEvent event) {
        if (!interventionsList.isEmpty() && !interventionTable.getItems().isEmpty()) {
            interventionsList.clear();
            interventionTable.getItems().clear();
        }

        interventionTable.setItems(interventionsList = Interventions.loadInterventions());

    }

    /**
     * Searching for patients by firstName and/or lastName
     * 
     * @param event 
     */
    @FXML
    private void search(KeyEvent event) {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Interventions> filteredData = new FilteredList<>(interventionsList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate((Interventions patient) -> {
                // If filter text is empty, display all patients.
                
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                // Compare first name and last name of every patient with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                
                if ((patient.getFirstName().toLowerCase() + " " + patient.getLastName().toLowerCase()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return (patient.getLastName().toLowerCase()+ " " + patient.getFirstName().toLowerCase()).contains(lowerCaseFilter);
                }
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Interventions> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(interventionTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        interventionTable.setItems(sortedData);
    }

}
