package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
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
    @FXML
    private Button exit;
    @FXML
    private HBox mainWindow;
    private CustomTreeItem<String> selectedItem;
    private CustomTreeItem<String> previousSelectedItem;

    private String choosenTryId = null;


    public void updateTreeView(String textTree) {
        Platform.runLater(() -> {
            CustomTreeItem<String> root = new CustomTreeItem<>("Список дисциплин", "Else", "0");
            tree.setRoot(root);
            Tree newTree = Tree.fromSend(textTree);
            addItemsToTreeView(root, newTree.items, 1);

            // Context menu items
            ContextMenu contextMenuSubjectTopic = new ContextMenu();

            MenuItem addMenuItem = new MenuItem("Добавить");
            addMenuItem.setOnAction(event -> {
                if (selectedItem.getType().equals("Topic")) {
                    outputArea.clear();
                    textArea.clear();
                    confirm.setVisible(true);
                    outputLabel.setText("Введите название теста");
                    outputArea.setVisible(true);
                    send.setVisible(false);
                    textArea.setVisible(false);
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Добавление теста");
                    dialog.setHeaderText("Введите название нового объекта:");
                    dialog.showAndWait().ifPresent(name -> {
                        outputLabel.setText(name);
                    });
                } else {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Добавить новый объект");
                    dialog.setHeaderText("Введите название нового объекта:");
                    dialog.showAndWait().ifPresent(name -> {
                        CustomTreeItem<String> newItem = new CustomTreeItem<>(name);
                        if (selectedItem.getType().equals("Else")) {
                            serverThread.send("Nsubj" + newItem.getValue());
                            clearTree();
                        }
                        if (selectedItem.getType().equals("Subject")) {
                            serverThread.send("Ntopi" + selectedItem.getAdditionalValue() + "::" + newItem.getValue());
                            clearTree();
                        }
                    });
                }
            });

            MenuItem updateMenuItem = new MenuItem("Изменить");
            updateMenuItem.setOnAction(event -> {
                if (selectedItem != null && selectedItem != root) {
                    if (!selectedItem.getType().equals("Test")) {
                        TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
                        dialog.setTitle("Изменить объект");
                        dialog.setHeaderText("Введите новое название для " + selectedItem.getType() + ": ");
                        dialog.showAndWait().ifPresent(name -> selectedItem.setValue(name));
                        String newName = selectedItem.getValue();
                        if (selectedItem.getType().equals("Subject")) {
                            serverThread.send("Usubj" + selectedItem.getAdditionalValue() + "::" + newName);
                        }
                        if (selectedItem.getType().equals("Topic")) {
                            serverThread.send("Utopi" + selectedItem.getAdditionalValue() + "::" + newName);
                        }
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

            MenuItem deleteMenuItem = new MenuItem("Удалить");
            deleteMenuItem.setOnAction(event -> {
                if (selectedItem != null && selectedItem != root) {
                    selectedItem.getParent().getChildren().remove(selectedItem);
                    if (selectedItem.getType().equals("Subject")) {
                        serverThread.send("Dsubj" + selectedItem.getAdditionalValue());
                    }
                    if (selectedItem.getType().equals("Topic")) {
                        serverThread.send("Dtopi" + selectedItem.getAdditionalValue());
                    }
                    if (selectedItem.getType().equals("Test")) {
                        serverThread.send("Dtest" + selectedItem.getAdditionalValue());
                    }
                }
            });

            contextMenuSubjectTopic.getItems().addAll(addMenuItem, updateMenuItem, deleteMenuItem);

            tree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    selectedItem = (CustomTreeItem<String>) tree.getSelectionModel().getSelectedItem();
                    System.out.println(selectedItem.getValue() + " " + selectedItem.getType() + " " + selectedItem.getAdditionalValue());

                    if (selectedItem.getType().equals("Test")) {
                        System.out.println("Test");
                        contextMenuSubjectTopic.getItems().clear();
                        contextMenuSubjectTopic.getItems().addAll(deleteMenuItem);
                    } else if (selectedItem.getType().equals("Try")) {
                        contextMenuSubjectTopic.getItems().clear();
                    } else {
                        contextMenuSubjectTopic.getItems().clear();
                        contextMenuSubjectTopic.getItems().addAll(addMenuItem, updateMenuItem, deleteMenuItem);
                    }

                    if (selectedItem != null) {
                        contextMenuSubjectTopic.show(tree, event.getScreenX(), event.getScreenY());
                    } else {
                        contextMenuSubjectTopic.hide();
                    }
                }
                if (event.getClickCount() == 2) {
                    CustomTreeItem<String> selectedItem = (CustomTreeItem<String>) tree.getSelectionModel().getSelectedItem();
                    System.out.println("Selected item: " + selectedItem.getValue() + " " + selectedItem.getType() + " " + selectedItem.getAdditionalValue());
                    if (selectedItem.getType().equals("Try")) {
                        this.choosenTryId = selectedItem.getAdditionalValue();
                        serverThread.send("STRY_" + selectedItem.getAdditionalValue());
                    }
                }
            });
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

    public void clearTree(){
        TreeItem<String> rootItem = tree.getRoot();
        rootItem.getChildren().clear();
        tree.setRoot(null);
        serverThread.send("STTT_");
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
            setTextFieldPreviusResult(parts[3]);
            for (int i = 4; i < ((parts.length - 4) / 2)  + 4; i++) {
                outputArea.appendText(parts[i] + " \nОтвет студента: " + parts[i + (parts.length - 4) / 2] + "\n\n\n");
            }
        });
    }


    public void setTextFieldPreviusResult(String result){
        if (!result.isEmpty() && !result.equals("null"))  {
            textArea.setText(result);
        } else {
            textArea.clear();
        }
    }



    public void setTryResult(){
        if (!textArea.getText().isEmpty()) {
            outputArea.setVisible(false);
            textArea.setVisible(false);
            confirm.setVisible(false);
            send.setVisible(false);
            outputLabel.setText("Окно преподавателя");
            serverThread.send("STRYR" + choosenTryId + "::" + textArea.getText() );
            textArea.clear();
        }

    }

    //#####################################################################
    //#########################TEST_EDITOR#################################





    public void onConfirmAction(){
        String[] info = outputArea.getText().split("\n");
        String questionText = "";
        String answersText = "";
        for (String inf : info){
            String question = inf.split("\\[")[0];
            String answer = inf.split("\\[")[1];
            answer = answer.split("]")[0];
            questionText = questionText.concat("**" + question);
            answersText = answersText.concat("**" + answer);
        }
        serverThread.send("Ntest" + selectedItem.getAdditionalValue() + "**" + outputLabel.getText() + questionText + answersText);
        clearTree();
        outputArea.setVisible(false);
        textArea.setVisible(false);
        outputLabel.setText("Окно преподавателя");
        send.setVisible(false);
        confirm.setVisible(false);
    }

    private Stage getStage() {
        return (Stage) mainWindow.getScene().getWindow();
    }

    @FXML
    private void exitApplication(){
        getStage().close();
        receiver.setTeacherController(null);
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

