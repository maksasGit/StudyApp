package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
        dialog.setTitle("Create Teacher");
        dialog.setHeaderText("Enter Teacher Details");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Create Teacher");
        loginDialog.setHeaderText("Enter Teacher Login");
        loginDialog.setContentText("Login:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Create Teacher");
            passwordDialog.setHeaderText("Enter Teacher Password");
            passwordDialog.setContentText("Password:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the teacher using the login and password
                // Your implementation here
                System.out.println("Created teacher: " + login + " " + password);

                showSuccessDialog("Teacher created successfully.");
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
        dialog.setTitle("Create Student");
        dialog.setHeaderText("Enter Student Details");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Create Student");
        loginDialog.setHeaderText("Enter Student Login");
        loginDialog.setContentText("Login:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Create Student");
            passwordDialog.setHeaderText("Enter Student Password");
            passwordDialog.setContentText("Password:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the user using the login and password
                // Your implementation here
                System.out.println("Created Student: " + login + " " + password);

                showSuccessDialog("Student created successfully.");
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
        dialog.setTitle("Create Group");
        dialog.setHeaderText("Enter Group Details");

        TextInputDialog groupNameDialog = new TextInputDialog();
        groupNameDialog.setTitle("Create Group");
        groupNameDialog.setHeaderText("Enter Group Name");
        groupNameDialog.setContentText("Group Name:");
        Optional<String> groupNameResult = groupNameDialog.showAndWait();

        if (groupNameResult.isPresent()) {
            String groupName = groupNameResult.get();

            // Create the group using the group name
            // Your implementation here
            System.out.println("Created group: " + groupName);
            showSuccessDialog("Group created successfully.");
            serverThread.send("Agrou" + groupName);
        } else {
            // User canceled entering the group name
            System.out.println("Group creation canceled.");
        }
    }


    @FXML
    public void addStudentToGroup() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Create Student");
        dialog.setHeaderText("Enter Student Details");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Create Student");
        loginDialog.setHeaderText("Enter Student Login");
        loginDialog.setContentText("Login:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Create Student");
            passwordDialog.setHeaderText("Enter Group name");
            passwordDialog.setContentText("Group name:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the user using the login and password
                // Your implementation here
                System.out.println("Created Student to group: " + login + " " + password);

                showSuccessDialog("Student created successfully.");
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
        dialog.setTitle("Create Student");
        dialog.setHeaderText("Enter Student Details");

        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Create Student");
        loginDialog.setHeaderText("Enter Student Login");
        loginDialog.setContentText("Subject Name:");
        Optional<String> loginResult = loginDialog.showAndWait();

        if (loginResult.isPresent()) {
            String login = loginResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Create Student");
            passwordDialog.setHeaderText("Enter Student Password");
            passwordDialog.setContentText("Group Name:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // Create the user using the login and password
                // Your implementation here
                System.out.println("Connect subject - group: " + login + " " + password);

                showSuccessDialog("Connect subject - group successfully.");
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
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(getStage());
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) menuBar.getScene().getWindow();
    }


    public void listClear(){
        label.setText("");
        list.getItems().clear();
    }

    public void initialize(){
    }
}
