package main.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {


    @FXML
    private AnchorPane mainStage;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Please enter both username and password.");
        } else if (username.equals("admin") && password.equals("password")) {
            errorMessageLabel.setText("Login successful!");
            FXMLLoader loader = new FXMLLoader(Start.class.getResource("StudentMenu.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage = (Stage) mainStage.getScene().getWindow();
            stage.close();


        } else {
            errorMessageLabel.setText("Invalid username or password.");
        }
    }

}

