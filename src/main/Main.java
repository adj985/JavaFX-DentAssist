/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
24166458 51496506

 */
package main;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Coa
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        URL location = getClass().getClassLoader().getResource("view/Login.fxml");
        AnchorPane root = FXMLLoader.<AnchorPane>load(location);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(1);
        });
        
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
