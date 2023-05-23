import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    public Socket getSocket() {
        return socket;
    }

    private Socket socket;
    private PrintWriter writer;
    private Server server;
    private String clientName = null;

    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run(){
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while ((message = reader.readLine()) != null){
                String prefix = message.substring(0,5);
                String postfix = message.substring(5);
                System.out.println("Server received: " +message);
                switch(prefix) {
                    case "LOGIN" -> login(postfix);
                    case "STTT_" -> server.sendSubjectTopicTestTree(this);
                    case "GTEST" -> server.sendTest(this,postfix);
                    case "GTRY_" -> server.getTry(this,postfix);
                    case "STRY_" -> server.sendTry(this,postfix);
                    case "STRYR" -> server.getResult(this,postfix);
                }
            }
            server.removeClient(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        System.out.println("Server: " + message);
        writer.println(message);
    }

    public String getClientName() {
        return clientName;
    }

    public void login(String name) {
        clientName = name;
        // server.online(this);
        //server.sendTestsList(this);
    }


}

