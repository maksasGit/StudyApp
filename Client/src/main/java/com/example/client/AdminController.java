package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {

    private ServerThread serverThread;
    private ClientGUIReceiver receiver;

    public AdminController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setAdminController(this);
    }
    @FXML
    private MenuBar menuBar;

    @FXML
    private Label label;
    @FXML
    private ListView list;
    @FXML
    public void createTeacher() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Создать аккаунт преподавателя");
        dialog.setHeaderText("Ввод данных");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Создать аккаунт преподавателя");
        loginDialog.setHeaderText("Ввод логина");
        loginDialog.setContentText("Логин:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Создать аккаунт преподавателя");
            passwordDialog.setHeaderText("Ввод пароля");
            passwordDialog.setContentText("Пароль:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the teacher using the login and password
                // Your implementation here
                System.out.println("Created teacher: " + login + " " + password);

                showSuccessDialog("Аккаунт создан успешно!");
                serverThread.send("Ateac" + login + "::" + password);
            } else {
                // User canceled entering the password
                System.out.println("Teacher creation canceled.");
            }
        } else {
            // User canceled entering the login
            System.out.println("Teacher creation canceled.");
        }
    }

    @FXML
    public void createUser() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Создать аккаунт студента");
        dialog.setHeaderText("Ввод данных");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Создать аккаунт студента");
        loginDialog.setHeaderText("Ввод логина");
        loginDialog.setContentText("Логин:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Создать аккаунт студента");
            passwordDialog.setHeaderText("Ввод пароля");
            passwordDialog.setContentText("Пароль:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the user using the login and password
                // Your implementation here
                System.out.println("Created Student: " + login + " " + password);

                showSuccessDialog("Аккаунт создан успешно!");
                serverThread.send("Astud" + login + "::" + password);
            } else {
                // User canceled entering the password
                System.out.println("Student creation canceled.");
            }
        } else {
            // User canceled entering the login
            System.out.println("Student creation canceled.");
        }
    }

    @FXML
    public void createGroup() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Создать группу");
        dialog.setHeaderText("Ввод данных");

        TextInputDialog groupNameDialog = new TextInputDialog();
        groupNameDialog.setTitle("Создать группу");
        groupNameDialog.setHeaderText("Ввод номера группы");
        groupNameDialog.setContentText("Номер группы:");
        Optional<String> groupNameResult = groupNameDialog.showAndWait();

        if (groupNameResult.isPresent()) {
            String groupName = groupNameResult.get();

            // Create the group using the group name
            // Your implementation here
            System.out.println("Created group: " + groupName);
            showSuccessDialog("Группа создана успешно!");
            serverThread.send("Agrou" + groupName);
        } else {
            // User canceled entering the group name
            System.out.println("Group creation canceled.");
        }
    }


    @FXML
    public void addStudentToGroup() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Добавить студента к группе");
        dialog.setHeaderText("Ввод данных");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Выбор студента");
        loginDialog.setHeaderText("Ввод логина");
        loginDialog.setContentText("Логин:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Выбор группы");
            passwordDialog.setHeaderText("Ввод номера группы");
            passwordDialog.setContentText("Номер группы:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the user using the login and password
                // Your implementation here
                System.out.println("Created Student to group: " + login + " " + password);

                showSuccessDialog("Студент добавлен к группе!");
                serverThread.send("Asttg" + login + "::" + password);
            } else {
                // User canceled entering the password
                System.out.println("Student creation canceled.");
            }
        } else {
            // User canceled entering the login
            System.out.println("Student creation canceled.");
        }
    }


    @FXML
    public void addSubjectToGroup() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Добавить дисциплину группе");
        dialog.setHeaderText("Ввод данных");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Выбор дисциплины");
        loginDialog.setHeaderText("Ввод названия дисциплины");
        loginDialog.setContentText("Название дисциплины:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Выбор группы");
            passwordDialog.setHeaderText("Ввод номера группы");
            passwordDialog.setContentText("Номер группы:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the user using the login and password
                // Your implementation here
                System.out.println("Connect subject - group: " + login + " " + password);

                showSuccessDialog("Дисциплина успешно добавлена!");
                serverThread.send("Asutg" + login + "::" + password);
            } else {
                // User canceled entering the password
                System.out.println("Student creation canceled.");
            }
        } else {
            // User canceled entering the login
            System.out.println("Student creation canceled.");
        }
    }



    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешно");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(getStage());
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) menuBar.getScene().getWindow();
    }


    @FXML
    private void exitApplication(){
        getStage().close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new LogInController(serverThread, serverThread.getReceiver()));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 300, 300));
        stage.setTitle("Клиент");
        stage.show();
    }


    public void listClear(){
        label.setText("");
        list.getItems().clear();
    }

    public void initialize(){
    }
}
