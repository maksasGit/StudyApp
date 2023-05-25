package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherMainController {

    ServerThread serverThread;
    ClientGUIReceiver receiver;

    public TeacherMainController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setTeacherController(this);
    }

    @FXML
    private TreeView tree;
    @FXML
    private Button send;
    @FXML
    private Label outputLabel;
    @FXML
    private TextField textArea;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button confirm;
    private CustomTreeItem<String> selectedItem;
    private CustomTreeItem<String> previousSelectedItem;




    public void updateTreeView(String textTree) {
        CustomTreeItem<String> root = new CustomTreeItem<>("My Subjects", "Else" , "0");
        tree.setRoot(root);
        Tree newTree = Tree.fromSend(textTree);
        addItemsToTreeView(root, newTree.items, 1);
//####################################################################################
        ContextMenu contextMenuSubjectTopic = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add new");
        addMenuItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add new item");
            dialog.setHeaderText("Enter the name of the new item:");
            dialog.showAndWait().ifPresent(name -> {
                CustomTreeItem<String> newItem = new CustomTreeItem<>(name);
                selectedItem.getChildren().add(newItem);
            });
        });

        MenuItem updateMenuItem = new MenuItem("Update");
        updateMenuItem.setOnAction(event -> {
            if (selectedItem != null && selectedItem != root) {
                // for test show on output box, else show in dialog box
                if (!selectedItem.getType().equals("Test")) {
                    // Prompt the user to enter the new name for the selected item
                    TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
                    dialog.setTitle("Update item");
                    dialog.setHeaderText("Enter the new name for the item:");
                    dialog.showAndWait().ifPresent(name -> selectedItem.setValue(name));
                } else {
                    outputArea.clear();
                    textArea.clear();
                    confirm.setVisible(true);
                    outputLabel.setText(selectedItem.getValue());
                    outputArea.setVisible(true);
                    send.setVisible(true);
                    textArea.setVisible(true);
                }
            }
        });

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            if (selectedItem != null && selectedItem != root) {
                selectedItem.getParent().getChildren().remove(selectedItem);
            }
        });

        contextMenuSubjectTopic.getItems().addAll(addMenuItem, updateMenuItem, deleteMenuItem);

        //##################################################################

        tree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                previousSelectedItem = selectedItem;
                selectedItem = (CustomTreeItem<String>) tree.getSelectionModel().getSelectedItem();
                System.out.println(selectedItem.getValue() + " " + selectedItem.getType() + " " + selectedItem.getAdditionalValue());


                // For each item correct Context Menu Test(updata, delete) , Try() , Else(Add,Update,Delete)
                if (selectedItem.getType().equals("Test")) {
                    System.out.println("Test");
                    contextMenuSubjectTopic.getItems().clear();
                    contextMenuSubjectTopic.getItems().addAll(updateMenuItem , deleteMenuItem);
                } else if (selectedItem.getType().equals("Try")) {
                    contextMenuSubjectTopic.getItems().clear();
                } else {
                    contextMenuSubjectTopic.getItems().clear();
                    contextMenuSubjectTopic.getItems().addAll(addMenuItem , updateMenuItem , deleteMenuItem);
                }


                // if new item selected then show , else hide
                if (selectedItem != null && previousSelectedItem != selectedItem) {
                    contextMenuSubjectTopic.show(tree, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenuSubjectTopic.hide();
                }
            }
            if (event.getClickCount() == 2) {
                CustomTreeItem<String> selectedItem = (CustomTreeItem<String>) tree.getSelectionModel().getSelectedItem();
                System.out.println("Selected item: " + selectedItem.getValue());
                if (selectedItem.getType().equals("Try")){
                        serverThread.send("STRY_"+selectedItem.getAdditionalValue());
                }
            }
        });
    }

    private void addItemsToTreeView(CustomTreeItem<String> parentItem, List<StringID> items, int depth) {
        Map<Integer, String> types = new HashMap<>();
        types.put(1,"Subject");
        types.put(2,"Topic");
        types.put(3,"Test");
        types.put(4,"Try");
        for (StringID item : items) {
            String itemCode = String.valueOf(item.id);
            String itemType = types.get(depth);
            CustomTreeItem<String> newItem = new CustomTreeItem<>(item.name , itemType , itemCode);
            parentItem.getChildren().add(newItem);
            addItemsToTreeView(newItem, item.items, depth + 1);
        }
    }



    public void initialize(){
        serverThread.send("STTT_");
        outputArea.setVisible(false);
        textArea.setVisible(false);
        send.setVisible(false);
        confirm.setVisible(false);
    }



    //#####################################################################
    //#########################STUDENTS__TRY###############################


    public void showStudentTry(String textTry) {
        outputArea.setVisible(true);
        textArea.setVisible(true);
        textArea.clear();
        send.setVisible(true);
        confirm.setVisible(false);
        String[] parts = textTry.split("::");
        Platform.runLater(() -> {
            outputLabel.setText(parts[2] + " - " + parts[1]);
            outputArea.clear();
            for (int i = 3; i < ((parts.length - 3) / 2)  + 3; i++) {
                outputArea.appendText(parts[i] + " \nStudent answer: " + parts[i + (parts.length - 3) / 2] + "\n\n\n");
            }
        });
    }



    public void setTryResult(){
        if (!textArea.getText().isEmpty()) {
            outputArea.setVisible(false);
            textArea.setVisible(false);
            outputLabel.setText("Choose something");
            serverThread.send("STRYR" + textArea.getText());
            textArea.clear();
        }

    }

    //#####################################################################
    //#########################TEST_EDITOR#################################

    public void showTest(){

    }

    public void deleteTest(){
    }

    public void updateTest(){
    }

    public void createTest(){
    }

    //#####################################################################
    //#########################SUBJECT_EDITOR##############################

    public void updateSubject(){
    }

    public void deleteSubject(){
    }

    public void addSubject(){
    }

    //#####################################################################
    //#########################TOPIC_EDITOR##############################

    public void updateTopic(){
    }

    public void deleteTopic(){
    }

    public void addTopic(){
    }

}

class CustomTreeItem<String> extends TreeItem<String> {
    private String additionalValue;
    private String type;

    public CustomTreeItem(String value) {
        super(value);
    }

    public CustomTreeItem(String value, String type,  String additionalValue){
        super(value);
        this.type = type;
        this.additionalValue = additionalValue;
    }

    public String getAdditionalValue() {
        return additionalValue;
    }

    public String getType() {
        return type;
    }

}

