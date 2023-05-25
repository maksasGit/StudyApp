package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private final ServerThread serverThread;
    private final ClientGUIReceiver receiver;

    public LogInController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setLogInController(this);
    }

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        serverThread.send("LOGIN" + username + ":" + password);
        usernameField.clear();
        passwordField.clear();
    }

    public void handleLoginAsk(String answer) {
        String[] answerParts = answer.split(":");
        if (answerParts[0].equals("OK")) {
            String fxmlFile;
            switch (answerParts[1]) {
                case "student":
                    fxmlFile = "StudentMain.fxml";
                    break;
                case "teacher":
                    fxmlFile = "TeacherMain.fxml";
                    break;
                case "admin":
                    fxmlFile = "Admin.fxml";
                    break;
                default:
                    return;
            }

            String userType = answerParts[1];
            String username = usernameField.getText();
            Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                fxmlLoader.setControllerFactory(controllerClass -> switch (userType) {
                    case "student" -> new StudentMainController(serverThread, receiver);
                    case "teacher" -> new TeacherMainController(serverThread, receiver);
                    case "admin" -> new AdminController(serverThread, receiver);
                    default -> null;
                });

                try {
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 800, 600));
                    stage.setTitle("Client - " + username);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load " + fxmlFile + ": " + e.getMessage(), e);
                }
            });
            Platform.runLater(() -> {
                Stage mainStage = (Stage) root.getScene().getWindow();
                mainStage.close();
            });
        }

    }

}
