package main.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;






// start void window
// init Test and start first test by TestController
// next Test by TestController
// LastTestPage by testController
// save result protected

public class Start extends Application {
    Test test = new Test();

    @Override
    public void start(Stage stage) throws Exception {
        Question question = new Question();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionWindow.fxml"));
        loader.setControllerFactory(controller -> new QuestionController(question));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void nextQuestionWindow(){
        try{
            Question question = new Question();
            FXMLLoader loader = new FXMLLoader(Start.class.getResource("QuestionWindow.fxml"));
            loader.setControllerFactory(controller -> new QuestionController(question));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void testControl(){

    }


    public static void main(String[] args) {
        launch(args);
    }
}

