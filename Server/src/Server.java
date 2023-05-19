import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Server {
    private ServerSocket serverSocket;
    private Storage storage = new Storage();
    private List<ClientThread> clients = new ArrayList<>();

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listen(){
        System.out.println("Server Start");
        while(true){
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                ClientThread thread = new ClientThread(clientSocket, this);
                clients.add(thread);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void removeClient(ClientThread client) {
        clients.remove(client);
    }

    private Optional<ClientThread> getClient(String clientName) {
        return clients.stream()
                .filter(client -> clientName.equals(client.getClientName()))
                .findFirst();
    }

    public void sendTestsList(ClientThread sender){
        String testsList = "";
        for (Test currTest : storage.getTests()){
            testsList = testsList.concat(currTest.getTestLabel()+"//"+ currTest.getTestID() +"::" );
        }
        sender.send("TL" + testsList);
    }


    public void getTestInfo(ClientThread sender, String testID){
        Test test = storage.getTestbyID(testID);
        String testQuestions = "";
        for (Question question : test.shuffleQuestions()){
            testQuestions = testQuestions.concat(question.getQuestionText()+"::");
        }
        sender.send("TQ"+testQuestions);
    }

}
