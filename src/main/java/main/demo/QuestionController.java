package main.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionController {
    @FXML
    private Label questionLabel;

    @FXML
    private VBox answerBox;

    @FXML
    private VBox mainStage;
    private String questionText;
    private List<String> answerOptions;

    public QuestionController(Question question) {
        this.questionText = question.getQuestionText();
        this.answerOptions = question.getAnswerOptions();
    }

    @FXML
    public void initialize() {
        questionLabel.setText(questionText);

        for (String answerOption : answerOptions) {
            Button answerButton = new Button(answerOption);
            answerButton.setId(answerOption);
            answerButton.setOnAction(event -> handleAnswerButton(answerOption));
            answerBox.getChildren().add(answerButton);
        }
    }

    private List<String> selectedAnswers = new ArrayList<>();

    private void handleAnswerButton(String answerOption) {
        Button answerButton = (Button) answerBox.lookup("#" + answerOption);
        if (answerButton.isDisable()) {               // change disable on change color
            answerButton.setDisable(false);
            selectedAnswers.remove(answerOption);
        } else {
            answerButton.setDisable(true);             // change disable on change color
            selectedAnswers.add(answerOption);
        }
    }

    @FXML
    public void handleCommitButtonClick() {
        try {
            Question question = new Question();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionWindow.fxml"));
            loader.setControllerFactory(controller -> new QuestionController(question));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) mainStage.getScene().getWindow();
        stage.close();
    }
}
