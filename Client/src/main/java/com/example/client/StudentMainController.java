package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMainController {

    private ServerThread serverThread;
    private ClientGUIReceiver receiver;

    private Try currentTry = new Try();

    public StudentMainController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setStudentMainController(this);
    }

    @FXML
    private TreeView<String> subjectTreeView;

    @FXML
    private Label outputLabel;

    @FXML
    private TextField answerTextField;

    @FXML
    private TextArea outputArea;
    @FXML
    private Button sendButton;

    private CustomTreeItem<String> selectedItem;

    public void updateTreeView(String textTree) {
        CustomTreeItem<String> root = new CustomTreeItem<>("My Subjects", "Else", "0");
        subjectTreeView.setRoot(root);
        Tree newTree = Tree.fromSend(textTree);
        addItemsToTreeView(root, newTree.items, 1);

        subjectTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                selectedItem = (CustomTreeItem<String>) subjectTreeView.getSelectionModel().getSelectedItem();
                System.out.println(selectedItem.getValue() + " " + selectedItem.getType() + " " + selectedItem.getAdditionalValue());
                if (selectedItem.getType().equals("Test")) {
                    outputLabel.setText(selectedItem.getValue());
                    currentTry.setTestId(Integer.parseInt(selectedItem.getAdditionalValue()));
                    serverThread.send("GTEST" + selectedItem.getAdditionalValue());
                }
            }
        });
    }

    private void addItemsToTreeView(CustomTreeItem<String> parentItem, List<StringID> items, int depth) {
        Map<Integer, String> types = new HashMap<>();
        types.put(1, "Subject");
        types.put(2, "Topic");
        types.put(3, "Test");
        types.put(4, "Try");
        for (StringID item : items) {
            String itemCode = String.valueOf(item.id);
            String itemType = types.get(depth);
            CustomTreeItem<String> newItem = new CustomTreeItem<>(item.name, itemType, itemCode);
            parentItem.getChildren().add(newItem);
            addItemsToTreeView(newItem, item.items, depth + 1);
        }
    }

    public void getTest(String testQuestions) {
        sendButton.setDisable(false);
        String[] questionsFromText = testQuestions.split("##");
        for (String questionFromText : questionsFromText) {
            String[] questionPart = questionFromText.split("\\*\\*");
            currentTry.addQuestion(new Question(questionPart[0], Integer.parseInt(questionPart[1])));
        }
        currentTry.shuffleQuestions();
        showNextQuestion();
    }

    public void showNextQuestion() {
        outputArea.clear();
        String questionText = currentTry.nextQuestion();
        if (questionText.equals("Finish")) {
            String testID = String.valueOf(currentTry.getTestId());
            StringBuilder answerQuestionNum = new StringBuilder();
            for (Question question : currentTry.getQuestions()) {
                String answer = question.getAnswer() + "::" + question.getQuestionNum();
                answerQuestionNum.append(answer).append("**");
            }
            sendButton.setDisable(true);
            serverThread.send("GTRY_" + testID + "**" + answerQuestionNum);
        }
        outputArea.appendText(questionText);
    }

    public void setAnswer() {
        currentTry.setAnswer(answerTextField.getText());
        answerTextField.clear();
        showNextQuestion();
    }

    public void initialize() {
        serverThread.send("STT__");
    }

    public void getResultNotTest(String result) {
        sendButton.setDisable(true);
        outputArea.clear();
        outputArea.appendText("Your Result is: " + result);
    }
}
