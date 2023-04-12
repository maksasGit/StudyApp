package main.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Arrays;

public class Start extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }
}

