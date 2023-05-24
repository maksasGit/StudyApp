package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField usernameField;

    @FXML
    private AnchorPane root;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button LogInButton;

    ServerThread serverThread;
    ClientGUIReceiver receiver;

    public LogInController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setLogInController(this);
    }

    private String username = null;

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        this.username = username;
        serverThread.send("LOGIN" + username + ":" + password);
        usernameField.clear();
        passwordField.clear();
    }

    public void handleLoginAsk(String answer){
        String[] answerParts = answer.split(":");
        if (answerParts[0].equals("OK")) {
            if (answerParts[1].equals("student")){
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(LogInController.class.getResource("StudentMain.fxml"));
                    fxmlLoader.setControllerFactory(controllerClass -> new StudentMainController(serverThread, receiver));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load StudentMain.fxml: " + e.getMessage(), e);
                    }

                    Stage studentMainStage = new Stage();
                    studentMainStage.setScene(new Scene(root, 800, 600));
                    studentMainStage.setTitle("Client - " + username);
                    studentMainStage.show();
                });

            }
            if (answerParts[1].equals("teacher")){
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(LogInController.class.getResource("TeacherMain.fxml"));
                    fxmlLoader.setControllerFactory(controllerClass -> new TeacherMainController(serverThread, receiver));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load TeacherMain.fxml: " + e.getMessage(), e);
                    }

                    Stage teacherMainStage = new Stage();
                    teacherMainStage.setScene(new Scene(root, 800, 600));
                    teacherMainStage.setTitle("Client - " + username);
                    teacherMainStage.show();
                });

            }
            if (answerParts[1].equals("admin")){
//                FXMLLoader fxmlLoader = new FXMLLoader(LogInController.class.getResource("StudentMain.fxml"));
//                fxmlLoader.setControllerFactory(controllerClass -> new AdminController(serverThread, receiver));
//                Parent root = null;
//                try {
//                    root = fxmlLoader.load();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//                Stage studentMainStage = new Stage();
//                studentMainStage.setScene(new Scene(root, 900, 800));
//                studentMainStage.setTitle("Client - " + username);
//                studentMainStage.show();
            }
        }
        Platform.runLater(() -> {
            Stage mainStage = (Stage) root.getScene().getWindow();
            mainStage.close();
        });

    }

}
