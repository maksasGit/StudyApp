package com.example.client;

import javafx.application.Platform;

import java.util.Arrays;
import java.util.List;

public class ClientGUIReceiver {

    MainContainer mainController = null;

    public void setMainController(MainContainer mainController) {
        this.mainController = mainController;
    }


    public void receiveTestList(String[] strings) {
        mainController.setTestsList(Arrays.stream(strings).toList());
    }

    public void receiveTestQuestions(String[] strings){
        TestManager.resetQuestions(List.of(strings));
        mainController.showTestQuestion(TestManager.nextQuestion());
    }


}
