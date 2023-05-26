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


    public void changeTeacher(){

    }

    public void changeStudent(){
        serverAskStudentList();
    }

    public void changeGroup(){

    }

    public void addSubject(){

    }

    public void removeSubject(){

    }

    public void addStudent(){
        serverAskStudentList();
    }

    public void removeStudent(){

    }

    public void serverAskStudentList(){
        serverThread.send("GLstu");
    }

    public void serverAskTeacherList(){
        serverThread.send("GLtea");
    }

    public void serverAskGroupList(){
        serverThread.send("GLgro");
    }

    public void serverAskSubjectList(){
        serverThread.send("GLsub");
    }

    public String chooseStudent(String textList){

        Platform.runLater(() -> {
            label.setText("Choose student");
            list.setVisible(true);
            String[] mainParts = textList.split("\\*\\*");
            for (String part : mainParts) {
                if (part.length() > 0) {
                    String student_login = part;
                    list.getItems().add(student_login);
                }
            }
        });

        list.setOnMouseClicked(event -> {
            String selectedItem = (String) list.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                System.out.println("Selected: " + selectedItem);
            }
        });
    }

    public String chooseSubject(String textList){
        label.setText("Choose subject");
        String[] mainParts = textList.split("\\*\\*");
        for (String part: mainParts){
            if (part.length() > 0){
                String subject_name = part.split("::")[0];
                String subject_id = part.split("::")[1];
                list.getItems().add(subject_name);
            }
        }
    }

    public void chooseTeacher(String textList){
        label.setText("Choose teacher");
        String[] mainParts = textList.split("\\*\\*");
        for (String part: mainParts){
            if (part.length() > 0){
                String teacher_name = part.split("::")[0];
                String teacher_id = part.split("::")[1];
                list.getItems().add(teacher_name);
            }
        }
    }

    public void chooseGroup(String textList){
        label.setText("Choose group");
        String[] mainParts = textList.split("\\*\\*");
        for (String part: mainParts){
            if (part.length() > 0){
                String group_name = part.split("::")[0];
                String group_id = part.split("::")[1];
                list.getItems().add(group_name);
            }
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
        list.setVisible(false);
    }
}
