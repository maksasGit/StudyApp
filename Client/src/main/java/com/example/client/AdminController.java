package com.example.client;

public class AdminController {

    ServerThread serverThread;
    ClientGUIReceiver receiver;

    public AdminController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setAdminController(this);
    }
}
