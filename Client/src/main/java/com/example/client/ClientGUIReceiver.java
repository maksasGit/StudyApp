package com.example.client;

import javafx.application.Platform;

import java.util.Arrays;
import java.util.List;

public class ClientGUIReceiver {

    StudentMainController studentController = null;

    TeacherMainController teacherController = null;

    AdminController adminController = null;

    LogInController logInController = null;

    public ClientGUIReceiver() {
    }

    public void setStudentMainController(StudentMainController controller) {
        this.studentController = controller;
    }

    public void setTeacherController(TeacherMainController controller) {this.teacherController = controller;}

    public void setLogInController(LogInController controller) {this.logInController = controller;}

    public void getTree(String textTree){
        if (studentController == null) {
            this.teacherController.updateTreeView(textTree);
        }
        if (teacherController == null){
            this.studentController.updateTreeView(textTree);
        }
    }

    public void getTestQuestions(String testQuestions){
        // add if From Server get TryResult
        this.studentController.getTest(testQuestions);
    }

    public void getTry(String textTry){
        this.teacherController.showStudentTry(textTry);
    }


    public void getAnswer(String answer){
        this.logInController.handleLoginAsk(answer);
    }

}
