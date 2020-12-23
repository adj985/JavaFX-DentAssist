/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Expenses;
import model.Interventions;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class FinancesController implements Initializable {

    @FXML
    private TableView<Interventions> debtsTable;
    @FXML
    private TableColumn<Interventions, String> firstNameCol;
    @FXML
    private TableColumn<Interventions, String> lastNameCol;
    @FXML
    private TableColumn<Interventions, String> residenceCol;
    @FXML
    private TableColumn<Interventions, String> phoneNumberCol;
    @FXML
    private TableColumn<Interventions, String> debtCol;
    @FXML
    private TableColumn<Interventions, String> debtDateCol;
    @FXML
    private TableView<Expenses> expensesTable;
    @FXML
    private TableColumn<Expenses, String> expenseDateCol;
    @FXML
    private TableColumn<Expenses, Double> expenseAmountCol;
    @FXML
    private TableColumn<Expenses, String> expensePurposeCol;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Button loadDataButton;
    @FXML
    private Label numberOfInterventionsLabel;
    @FXML
    private Label incomeLabel;
    @FXML
    private Label expensesLabel;
    @FXML
    private Label debtsLabel;

    ObservableList<Interventions> debtsList = FXCollections.observableArrayList();
    ObservableList<Interventions> interventionsList = FXCollections.observableArrayList();
    ObservableList<Expenses> expensesList = FXCollections.observableArrayList();

    private final ObservableList<Interventions> filteredDebtsList = FXCollections.observableArrayList();
    private final ObservableList<Expenses> filteredExpensesList = FXCollections.observableArrayList();
    @FXML
    private Button clearDataButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        numberOfInterventionsLabel.setText("0");
        incomeLabel.setText("0");

        debtsList = Interventions.showDebts();
        expensesList = Expenses.loadExpenses();
        interventionsList = Interventions.loadInterventions();

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        residenceCol.setCellValueFactory(new PropertyValueFactory<>("residence"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        debtCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        debtDateCol.setCellValueFactory(new PropertyValueFactory<>("dateOfIntervention"));

        expenseAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountSpent"));
        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        expensePurposeCol.setCellValueFactory(new PropertyValueFactory<>("expensePurpose"));

        debtsTable.setItems(debtsList);
        expensesTable.setItems(expensesList);

        //Counting totalDebts, and showing data in a debtsLabel
        double totalDebts = 0;

        for (int i = 0; i < debtsList.size(); i++) {
            totalDebts += debtsList.get(i).getPrice();
        }

        debtsLabel.setText(String.valueOf(totalDebts));

        //Counting totalExpenses, and showing in expensesLabel
        double totalExpenses = 0;

        for (int i = 0; i < expensesList.size(); i++) {
            totalExpenses += expensesList.get(i).getAmountSpent();
        }
        expensesLabel.setText(String.valueOf(totalExpenses));
    }

    /**
     * Displays data for required time period
     * @param event 
     */
    @FXML
    private void loadData(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        if (fromDate.getEditor().getText().equals("")) {
            a.setContentText("Izaberite početni datum!");
            a.show();
        } else if (toDate.getEditor().getText().equals("")) {
            a.setContentText("Izaberite završni datum!");
            a.show();
        } else if (toDate.getValue().isBefore(fromDate.getValue())) {
            a.setContentText("Završni datum ne može biti pre početnog datuma!");
            a.show();
        } else if (toDate.getValue().isAfter(LocalDate.now())) {
            toDate.setValue(LocalDate.now());
        } else {
            LocalDate date;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            double totalExpenses = 0;
            int totalInterventions = 0;
            double totalIncome = 0;
            double totalDebts = 0;

            //Counting totalInterventions and totalIncome using fromDate and toDate as parameters
            for (int i = 0; i < interventionsList.size(); i++) {
                date = LocalDate.parse(interventionsList.get(i).getDateOfIntervention(), formatter);
                if (!date.isBefore(fromDate.getValue()) && !date.isAfter(toDate.getValue())) {
                    totalInterventions++;
                    if (interventionsList.get(i).getCharged().equals("DA")) {
                        totalIncome += interventionsList.get(i).getPrice();
                    }
                }
            }

            numberOfInterventionsLabel.setText(String.valueOf(totalInterventions));
            incomeLabel.setText(String.valueOf(totalIncome));

            //Populating debtsTable and counting debts for specified date parameters
            for (int i = 0; i < debtsList.size(); i++) {
                date = LocalDate.parse(debtsList.get(i).getDateOfIntervention(), formatter);
                if (!date.isBefore(fromDate.getValue()) && !date.isAfter(toDate.getValue())) {
                    filteredDebtsList.add(debtsList.get(i));
                    totalDebts += debtsList.get(i).getPrice();
                }
            }
            debtsTable.getItems().clear();
            debtsTable.setItems(filteredDebtsList);
            debtsLabel.setText(String.valueOf(totalDebts));

            //Populating expensesTable and counting totalExpenses for specified date parameters 
            for (int i = 0; i < expensesList.size(); i++) {
                date = LocalDate.parse(expensesList.get(i).getExpenseDate(), formatter);
                if (!date.isBefore(fromDate.getValue()) && !date.isAfter(toDate.getValue())) {
                    filteredExpensesList.add(expensesList.get(i));
                    totalExpenses += expensesList.get(i).getAmountSpent();
                }
            }
            expensesTable.getItems().clear();
            expensesLabel.setText(String.valueOf(totalExpenses));
            expensesTable.setItems(filteredExpensesList);

            loadDataButton.setDisable(true);
            fromDate.setDisable(true);
            toDate.setDisable(true);
        }

    }

    /**
     * Resets all the fields
     * @param event 
     */
    @FXML
    private void clearData(ActionEvent event) {
        loadDataButton.setDisable(false);
        fromDate.setDisable(false);
        toDate.setDisable(false);
        
        fromDate.setValue(null);
        toDate.setValue(null);
        incomeLabel.setText("0");
        numberOfInterventionsLabel.setText("0");
        filteredDebtsList.clear();
        filteredExpensesList.clear();

        debtsTable.setItems(debtsList = Interventions.showDebts());
        expensesTable.setItems(expensesList = Expenses.loadExpenses());

        //Counting totalDebts
        double totalDebts = 0;

        for (int i = 0; i < debtsList.size(); i++) {
            totalDebts += debtsList.get(i).getPrice();
        }

        debtsLabel.setText(String.valueOf(totalDebts));

        //Counting totalExpenses
        double totalExpenses = 0;

        for (int i = 0; i < expensesList.size(); i++) {
            totalExpenses += expensesList.get(i).getAmountSpent();
        }
        expensesLabel.setText(String.valueOf(totalExpenses));
    }

}
