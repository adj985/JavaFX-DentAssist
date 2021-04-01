/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.javafx.stage.StageHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Scheduling;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class MainWindowController implements Initializable {

    @FXML
    private Button schedulingButton;
    @FXML
    private Button patientsButton;
    @FXML
    private Button interventionsButton;
    @FXML
    private Button dataUpdateButton;
    @FXML
    private Button expensesButton;
    @FXML
    private Button diagnosisButton;
    @FXML
    private Button financeButton;
    @FXML
    private TableView<Scheduling> scheduleTable;
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
    private Hyperlink scheduleLink;
    @FXML
    private Hyperlink refreshLink;

    private ObservableList<Scheduling> scheduleList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        StageHelper.getStages().get(0).close();

        timeCol.setCellValueFactory(new PropertyValueFactory<>("localScheduledTime"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        residenceCol.setCellValueFactory(new PropertyValueFactory<>("residence"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        scheduleTable.setItems(scheduleList = Scheduling.loadDailySchedule());
        if (scheduleTable.getItems().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Čisto da znate...");
            a.setHeaderText(null);
            a.setContentText("Danas nema zakazanih termina. Prijatan Vam dan!");
            a.showAndWait();
        }

        //Creating folder for patients docs

            File file = new File("C:\\Kartoteka");
            if (!file.exists()) {
                file.mkdir();
            }
    }

    /**
     * Opens the window for creating new appointments.
     * @param event 
     */
    @FXML
    private void openScheduling(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/Scheduling.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Zakazivanje");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            schedulingButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                schedulingButton.setDisable(false);
                scheduleTable.setItems(scheduleList = Scheduling.loadDailySchedule());
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens form for creating and manipulating patients data.
     * @param event 
     */
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
            patientsButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                patientsButton.setDisable(false);
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens form for creating and interventions patients data.
     * @param event 
     */
    @FXML
    private void openInterventions(ActionEvent event) {

        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/Interventions.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Intervencije");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            interventionsButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                interventionsButton.setDisable(false);
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Opens window for the expenses managing.
     * @param event 
     */
    @FXML
    private void openExpenses(ActionEvent event) {

        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/Expenses.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Zakazivanje");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            expensesButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                expensesButton.setDisable(false);
                scheduleTable.setItems(scheduleList = Scheduling.loadDailySchedule());
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Opens a window for creating diagnosis.
     * @param event 
     */
    @FXML
    private void openDiagnosis(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/Diagnosis.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Dijagnoze");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            diagnosisButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                diagnosisButton.setDisable(false);
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens a window for a financial overview.
     * @param event 
     */
    @FXML
    private void openFinances(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/Finances.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Finansije");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            financeButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                financeButton.setDisable(false);

            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens window with all the appointments and allows users to change the date and time of an appointment and also canceling one. 
     * @param event 
     */
    @FXML
    private void openSchedule(ActionEvent event) {

        try {
            Stage primaryStage = new Stage();

            URL location = getClass().getClassLoader().getResource("view/ScheduleOverview.fxml");
            AnchorPane root = FXMLLoader.<AnchorPane>load(location);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Pregled zakazanih termina");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            schedulingButton.setDisable(true);

            primaryStage.setOnCloseRequest((closeEvent) -> {
                primaryStage.close();
                schedulingButton.setDisable(false);
                scheduleTable.setItems(scheduleList = Scheduling.loadDailySchedule());
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
