/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Scheduling;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class ScheduleOverviewController implements Initializable {

    @FXML
    private TableView<Scheduling> scheduleTable;
    @FXML
    private TableColumn<Scheduling, LocalDate> dateCol;
    @FXML
    private TableColumn<Scheduling, LocalTime> timeCol;
    @FXML
    private TableColumn<Scheduling, String> firstNameCol;
    @FXML
    private TableColumn<Scheduling, String> lastNameCol;
    @FXML
    private TableColumn<Scheduling, String> residenceCol;
    @FXML
    private TableColumn<Scheduling, String> phoneNumberCol;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button deleteButton;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label residenceLabel;
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Spinner<Integer> minutesSpinner;
    @FXML
    private Button updateButton;
    @FXML
    private TextField idField;

    ObservableList<Scheduling> scheduleList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(07, 22));
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15));

        timeCol.setCellValueFactory(new PropertyValueFactory<>("localScheduledTime"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("localScheduledDate"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        residenceCol.setCellValueFactory(new PropertyValueFactory<>("residence"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        scheduleTable.setItems(scheduleList = Scheduling.loadScheduleOverview());

    }

    
    /**
     * Filling labels from the table selection model. Used to update data.
     * 
     * @param event 
     */
    @FXML
    private void fillLabels(MouseEvent event) {
        if (scheduleTable.getSelectionModel().getSelectedItem() != null) {
            firstNameLabel.setText(scheduleTable.getSelectionModel().getSelectedItem().getFirstName());
            lastNameLabel.setText(scheduleTable.getSelectionModel().getSelectedItem().getLastName());
            residenceLabel.setText(scheduleTable.getSelectionModel().getSelectedItem().getResidence());
            datePicker.setValue(scheduleTable.getSelectionModel().getSelectedItem().getLocalScheduledDate());
        }

    }

    /**
     * Canceling the appointment from the schedule.
     * 
     * @param event 
     */
    @FXML
    private void deleteFromSchedule(ActionEvent event){

        Alert a;
        if (scheduleTable.getSelectionModel().getSelectedIndex() < 0) {
            a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Morate prvo odabrati termin u tabeli koji biste hteli da otkažete.");
            a.show();
        } else {
            a = new Alert(Alert.AlertType.WARNING, "Jeste li sigurni da želite da otkažete izabrani termin?", ButtonType.OK, ButtonType.CANCEL);
            a.setHeaderText(null);
            Optional<ButtonType> result = a.showAndWait();

            if (result.get() == ButtonType.OK) {
                Scheduling s = Scheduling.findScheduling(scheduleTable.getSelectionModel().getSelectedItem().getIdscheduling());
                Scheduling.removeScheduling(s);
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Uspešno ste otkazali termin.");
                a.show();
                scheduleTable.setItems(scheduleList = Scheduling.loadScheduleOverview());
                clearLabels();
            }
        }
    }

    /**
     * Updating data in the schedule.
     * 
     * @param event 
     */
    @FXML
    private void updateSchedule(ActionEvent event) {
        Alert a;
        if (scheduleTable.getSelectionModel().getSelectedIndex() < 0) {
            a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Morate prvo odabrati termin u tabeli koju biste hteli da izmenite.");
            a.show();
        } else {

            for (int i = 0; i < scheduleList.size(); i++) {

                if (scheduleList.get(i).getLocalScheduledDate().equals(datePicker.getValue()) && scheduleList.get(i).getLocalScheduledTime().equals(LocalTime.of(hoursSpinner.getValue(), minutesSpinner.getValue()))) {
                    a = new Alert(Alert.AlertType.WARNING);
                    a.setHeaderText(null);
                    a.setContentText("Termin je zauzet! Izaberite drugi termin.");
                    a.show();
                    return;
                }
            }
            
            a = new Alert(Alert.AlertType.CONFIRMATION, "Jeste li sigurni da želite da izmenite izabrani termin?", ButtonType.OK, ButtonType.CANCEL);
            a.setHeaderText(null);
            a.setTitle(null);
            Optional<ButtonType> result = a.showAndWait();
            

            if (result.get() == ButtonType.OK) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate newDate = datePicker.getValue();
                String newTime = hoursSpinner.getValue() + ":" + minutesSpinner.getValue();
                System.out.println(newDate.format(formatter));
                System.out.println(newTime);
                Scheduling.updateSchedule(scheduleTable.getSelectionModel().getSelectedItem().getIdscheduling(), newDate.format(formatter), newTime);

                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle(null);
                a.setContentText("Termin je promenjen.");
                a.show();
                scheduleTable.setItems(scheduleList = Scheduling.loadScheduleOverview());
            }
        }
    }
    
    /**
     * Reseting labels.
     */
    private void clearLabels(){
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        residenceLabel.setText("");
        datePicker.setValue(null);
    }
    
    

}
