package main.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StudentMenuController {


    @FXML
    private AnchorPane mainStage;
    @FXML
    private ListView<Test> testListView;

    @FXML
    private Button startTestButton;

    private ObservableList<Test> tests;


    @FXML
    public void initialize() {
        // populate the list of tests
        tests = FXCollections.observableArrayList(new Test(), new Test(), new Test());
        testListView.setItems(tests);
        // allow the user to select multiple tests
        testListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void handleStartTestButtonAction(ActionEvent event) {
        // get the selected test
        String selectedTest = testListView.getSelectionModel().getSelectedItem().getTestLabel();
        if (selectedTest == null) {
            // if no test is selected, show an error message
            System.out.println("Please select a test.");
        } else {
            // start the selected test
            System.out.println("Starting " + selectedTest);
            TestController testController = new TestController();
            Stage stage = (Stage) mainStage.getScene().getWindow();
            stage.close();
        }
    }

}
