package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContainer {
    ServerThread serverThread;
    ClientGUIReceiver receiver;

    public MainContainer(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
    }
    @FXML
    private TextField inputField;

    @FXML
    private TextArea outputArea;
    @FXML
    private Button sendButton;

    @FXML
    private ListView testsList;

    @FXML
    private GridPane mainPane;


    private String selectedTest = null;

    @FXML
    protected void onSendButtonClick() {
        send();
    }

    @FXML
    public void onInputEnter(ActionEvent actionEvent){
        send();
    }

    private void send() {
        String text = inputField.getText();
        inputField.clear();
        outputArea.clear();
        outputArea.appendText(TestManager.nextQuestion());
        TestManager.setAnswer(text);
    }


    public void setTestsList(List<String> TestsInfo) {
        testsList.getItems().clear();
        TestsInfo.forEach(info -> {
            String[] parts = info.split("//");
            String testName = parts[0];
            String testID = parts[1];
            testsList.getItems().add(testName);
        });
        testsList.setOnMouseClicked(event -> {
            String selectedTest = (String) testsList.getSelectionModel().getSelectedItem();
            int selectedIndex = testsList.getSelectionModel().getSelectedIndex();
            String selectedID = TestsInfo.get(selectedIndex).split("//")[1];
            newItemSelected(selectedID);
        });
    }

    public void newItemSelected(String newItemID){
        if (!newItemID.equals(selectedTest)){
            selectedTest = newItemID;
            System.out.println("Selected item ID: " + selectedTest);
            outputArea.clear();
            serverThread.getTestInfo(selectedTest);
        }
    }


    public void showTestQuestion(String Question) {
        outputArea.appendText(Question);
        outputArea.setScrollTop(Double.MAX_VALUE);
    }

    public void addToTestList(String clientName) {
        testsList.getItems().add(clientName);
    }

    public void removeFromTestList(String clientName) {
        testsList.getItems().remove(clientName);
    }

}