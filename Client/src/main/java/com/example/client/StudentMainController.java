package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentMainController {
    @FXML
    private TreeView tree;

    @FXML
    private HBox mainWindow;
    @FXML
    private VBox mainOutput;
    @FXML
    private HBox mainMenu;
    @FXML
    private Button send;
    @FXML
    private Button back;
    @FXML
    private Label outputLabel;
    @FXML
    private TextField textArea;
    @FXML
    private TextArea outputArea;



    public void  showTreeView(){
        TreeItem<String> root = new TreeItem<>("My Course");
        TreeItem<String> subjectItem1 = new TreeItem<>("math");
        TreeItem<String> subjectItem2 = new TreeItem<>("engl");
        TreeItem<String> subjectItem3 = new TreeItem<>("histo");
        TreeItem<String> topicItem1 = new TreeItem<>("exam");
        TreeItem<String> topicItem2 = new TreeItem<>("lab");
        TreeItem<String> topicItem3 = new TreeItem<>("wyklad");
        subjectItem1.getChildren().addAll(topicItem1, topicItem2);
        subjectItem2.getChildren().addAll(topicItem1, topicItem2, topicItem3);
        subjectItem3.getChildren().addAll(topicItem1, topicItem3);
        root.getChildren().addAll(subjectItem1 , subjectItem2 , subjectItem3);
        tree.setRoot(root);
    }

    public void initialize(){
        showTreeView();
    }

    public void closeWindow(){
    }
}
