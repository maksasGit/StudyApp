package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
