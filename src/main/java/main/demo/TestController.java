package main.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestController {
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
}
