package com.example.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class StudentMainController {

    ServerThread serverThread;
    ClientGUIReceiver receiver;

    Try currentTry = new Try();


    public StudentMainController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setStudentMainController(this);
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
                        String[] parts = t1.getValue().split(":");
                        if (parts.length == 3) {
                            if (parts[1].equals("3")) {
                                outputLabel.setText(parts[0]);
                                currentTry.testId = Integer.parseInt(parts[2]);
                                serverThread.send("GT" + parts[2]);
                            }
                        }
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

    public void getTest(String testQuestions){
        String[] questionsFromText = testQuestions.split("##");
        for (String questionFromText : questionsFromText){
            String[] questionPart = questionFromText.split("\\*\\*");
            currentTry.addQuestion(new Question(questionPart[0] , Integer.parseInt(questionPart[1])));
        }
        showNextQuestion();
    }

    public void showNextQuestion(){
        outputArea.clear();
        String questionText = currentTry.nextQuestion();
        if (questionText.equals("Finish")){
            // send Try (test_id + (answer, question_num))
            String testID = String.valueOf(currentTry.testId);
            String answerQuestionNum = "";
            for (Question question : currentTry.questions){
                String answer = question.answer + "::" + question.question_num;
                answerQuestionNum = answerQuestionNum.concat(answer+"**");
            }
            serverThread.send("TT" + testID + "**" + answerQuestionNum);
        }
        outputArea.appendText(questionText);

    }

    public void setAnswer(){
        currentTry.setAnswer(textArea.getText());
        textArea.clear();
        showNextQuestion();
    }

    public void selectedTreeItem(){
        TreeItem<String> item = (TreeItem<String>) tree.getSelectionModel().getSelectedItem();
        System.out.println(item.getValue());
    }


    public void initialize(){
        serverThread.send("STTT_");
    }

    public void closeWindow(){
    }
}
