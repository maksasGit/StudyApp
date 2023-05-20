package com.example.client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class StudentMainController {

    ServerThread serverThread;
    ClientGUIReceiver receiver;

    public StudentMainController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setStudentMainController(this);
    }

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



    public void updateTreeView(String textTree) {
        TreeItem<String> root = new TreeItem<>("My Subjects");
        tree.setRoot(root);
        Tree newTree = Tree.fromSend(textTree);
        addItemsToTreeView(root, newTree.items, 1);
    }

    private void addItemsToTreeView(TreeItem<String> parentItem, List<StringID> items, int depth) {
        for (StringID item : items) {
            TreeItem<String> newItem = new TreeItem<>(item.name);
            String itemCode = depth + ":" + item.id;
            newItem.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(itemCode);
                }
            });
            parentItem.getChildren().add(newItem);
            addItemsToTreeView(newItem, item.items, depth + 1);
        }
    }


    public void initialize(){
        serverThread.send("TR");
    }

    public void closeWindow(){
    }
}
