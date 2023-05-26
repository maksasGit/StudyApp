package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        ServerThread serverThread = new ServerThread("localhost", 5000);
        serverThread.setReceiver(new ClientGUIReceiver());
        serverThread.setDaemon(true);
        serverThread.start();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new LogInController(serverThread, serverThread.getReceiver()));

        primaryStage.setScene(new Scene(fxmlLoader.load(), 300, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
