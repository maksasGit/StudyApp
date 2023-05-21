package com.example.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

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
        TreeItem<String> root = new TreeItem<>("My Subjects" );
        tree.setRoot(root);
        Tree newTree = Tree.fromSend(textTree);
        addItemsToTreeView(root, newTree.items, 1);

        tree.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> stringTreeItem, TreeItem<String> t1) {
                        System.out.println("Selected item: " + t1.getValue());
                    }
                });
    }

    private void addItemsToTreeView(TreeItem<String> parentItem, List<StringID> items, int depth) {
        for (StringID item : items) {
            TreeItem<String> newItem = new TreeItem<>(item.name+":"+depth+":"+item.id);
            String itemCode = depth + ":" + item.id;
            parentItem.getChildren().add(newItem);
            addItemsToTreeView(newItem, item.items, depth + 1);
        }
    }


    public void initialize(){
        serverThread.send("TR");
    }



    //#####################################################################
    //#########################STUDENTS__TRY###############################
    public void showStudentTryList(){
        
    }

    public void showStudentTry(){

    }
    public void setTryResult(){
    }

    //#####################################################################
    //#########################TEST_EDITOR#################################
    public void showTestsList(){
    }

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
