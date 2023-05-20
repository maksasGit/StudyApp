package com.example.client;

import javafx.application.Platform;

import java.util.Arrays;
import java.util.List;

public class ClientGUIReceiver {

    StudentMainController controller = null;

    public ClientGUIReceiver() {
    }

    public void setStudentMainController(StudentMainController controller) {
        this.controller = controller;
    }


    public void getTree(String textTree){
        this.controller.updateTreeView(textTree);
    }


}
