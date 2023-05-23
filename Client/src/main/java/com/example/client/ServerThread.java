package com.example.client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private ClientGUIReceiver receiver = null;

    public void setReceiver(ClientGUIReceiver receiver) {
        this.receiver = receiver;
    }

    public ServerThread(String address, int port) {
        try {
            this.socket = new Socket(address, port);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void run() {
        try {
            InputStream input = this.socket.getInputStream();
            OutputStream output = this.socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            this.writer = new PrintWriter(output, true);

            String message;
            while((message = reader.readLine()) != null) {
                String prefix = message.substring(0, 2);
                String postfix = message.substring(2);
                System.out.println("Server received: " + message);
                String[] postfixArr;
                switch (prefix) {
                    case "TR" -> this.receiver.getTree(postfix);
                    case "ST" -> this.receiver.getTestQuestions(postfix);
                    case "SR" -> this.receiver.getTry(postfix);
                }
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        }

    }

    public void send(String message) {
        System.out.println("Send to server" + message);
        this.writer.println(message);
    }

    public void login(String name) {
        this.writer.println("LOGIN" + name);
    }

    public void getTestInfo(String testID){
        this.writer.println("TI" + testID);
    }
}
