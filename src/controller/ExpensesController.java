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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Expenses;

/**
 * FXML Controller class
 *
 * @author Coa
 */
public class ExpensesController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField expenseAmountField;
    @FXML
    private TextArea expensePurposeArea;
    @FXML
    private Button saveButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button clearButton;
    @FXML
    private TableView<Expenses> expenseTable;
    @FXML
    private TableColumn<Expenses, String> expenseDateCol;
    @FXML
    private TableColumn<Expenses, Double> expenseAmountCol;
    @FXML
    private TableColumn<Expenses, String> expensePurposeCol;

    ObservableList<Expenses> expensesList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        expenseAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountSpent"));
        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        expensePurposeCol.setCellValueFactory(new PropertyValueFactory<>("expensePurpose"));

        //Loading data in expenses table
        expenseTable.setItems(expensesList = Expenses.loadExpenses());
        updateButton.setDisable(true);
    }

    /**
     * Saving expense data
     * @param event 
     */
    @FXML
    private void saveExpense(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        try {
            //Checking for empty fields
            if (expenseAmountField.getText().equals("")) {
                a.setContentText("Unesite iznos!");
                a.show();
            } else if (datePicker.getValue() == null) {
                a.setContentText("Izaberite datum!");
                a.show();
            } else if (expensePurposeArea.getText().equals("")) {
                a.setContentText("Unesite svrhu rashoda!");
                a.show();
            } else {
                //Formatting date, saving the expense data, and clearing all the fields
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                Expenses.saveExpense(new Expenses(null, expensePurposeArea.getText(), Double.parseDouble(expenseAmountField.getText()), datePicker.getValue().format(formatter)));
                expenseTable.getItems().clear();
                expensesList.clear();
                expenseTable.setItems(expensesList = Expenses.loadExpenses());

                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Uspešno ste dodali rashod!");
                a.show();
            }
        } catch (NumberFormatException ex)/*The value must be a number, or else it pops alert*/ {
            a.setContentText("Polje za iznos sme sadržati samo cifre!\nUnesite validan iznos");
            a.show();
        }
    }

    @FXML
    private void updateExpense(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        try {
            if (expenseAmountField.getText().equals("")) {
                a.setContentText("Unesite iznos!");
                a.show();
            } else if (datePicker.getValue() == null) {
                a.setContentText("Izaberite datum!");
                a.show();
            } else if (expensePurposeArea.getText().equals("")) {
                a.setContentText("Unesite svrhu rashoda!");
                a.show();
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                Expenses.updateExpense(expenseTable.getSelectionModel().getSelectedItem().getIdexpenses(), Double.parseDouble(expenseAmountField.getText()), datePicker.getValue().format(formatter), expensePurposeArea.getText());
                expenseTable.getItems().clear();
                expensesList.clear();
                expenseTable.setItems(expensesList = Expenses.loadExpenses());

                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Uspešno ste izmenili podatke!");
                a.show();
            }
        } catch (NumberFormatException ex) {
            a.setContentText("Polje za iznos sme sadržati samo cifre!\nUnesite validan iznos");
            a.show();
        }
    }

    /**
     * Erasing all data in the fields
     * @param event 
     */
    @FXML
    private void clearFields(ActionEvent event) {
        expenseAmountField.setText("");
        datePicker.setValue(null);
        expensePurposeArea.setText("");
        expenseTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        saveButton.setDisable(false);
    }

    /**
     * Populating fields after selecting in the table
     * @param event 
     */
    @FXML
    private void loadData(MouseEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);

        if (expenseTable.getSelectionModel().isEmpty()) {
            a.setContentText("Izaberite u tabeli stavku koju biste da izmenite!");
            a.show();
        } else {
            expenseAmountField.setText(String.valueOf(expenseTable.getSelectionModel().getSelectedItem().getAmountSpent()));
            datePicker.setValue(LocalDate.parse(expenseTable.getSelectionModel().getSelectedItem().getExpenseDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            expensePurposeArea.setText(expenseTable.getSelectionModel().getSelectedItem().getExpensePurpose());
            saveButton.setDisable(true);
            updateButton.setDisable(false);
        }
    }

}
