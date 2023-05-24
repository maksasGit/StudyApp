package com.example.client;

import com.example.client.ClientGUIReceiver;
import com.example.client.MainContainer;
import com.example.client.ServerThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ServerThread serverThread = new ServerThread("localhost", 5000);

        ClientGUIReceiver receiver = new ClientGUIReceiver();
        serverThread.setReceiver(receiver);
        serverThread.setDaemon(true);
        serverThread.start();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new LogInController(serverThread, receiver));
        Scene scene = new Scene(fxmlLoader.load(), 400, 250);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}


