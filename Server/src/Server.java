import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Cleaner;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private ServerSocket serverSocket;
    private Storage storage = new Storage();
    private List<ClientThread> clients = new ArrayList<>();

    private Map<ClientThread, String> clientsName = new HashMap<>();

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


    //################################################################################
    //############################NEEED?##############################################

    public void removeClient(ClientThread client) {
        clients.remove(client);
    }


    private Optional<ClientThread> getClient(String clientName) {
        return clients.stream()
                .filter(client -> clientName.equals(client.getClientName()))
                .findFirst();
    }


    //################################################################################
    //############################COMMON##############################################



    // login

    public void login(ClientThread receiver, String info){
        System.out.println("get login" + info);
        String[] parts = info.split(":");
        String answer = storage.checkLogin(parts[0] , parts[1]);
        System.out.println("get answer" + answer);
        receiver.send("Lo" + answer);
        if (answer.split(":")[0].equals("OK")){
            clients.add(receiver);
            clientsName.put(receiver,parts[0]);
        }
    }


    //#################################################################################
    //############################STUDENT##############################################

    // send Tree
    public void sendSubjectTopicTest(ClientThread receiver) {
        String result = storage.getTreeForStudent(clientsName.get(receiver));
        receiver.send("TR" + result);
    }

    // getTry

        public void getTry(ClientThread sender, String stringTry){
             String[] mainParts  = stringTry.split("\\*\\*");
             String testId = mainParts[0];
             String userLogin = clientsName.get(sender);
             List<String> answers = new ArrayList<>();
             List<String> answersNum = new ArrayList<>();
             for (String mainPart : mainParts){
                 if (mainPart.split("::").length == 2) {
                     System.out.println(mainPart);
                     answers.add(mainPart.split("::")[0]);
                     answersNum.add(mainPart.split("::")[1]);
                 }
             }
             storage.saveTry(testId , userLogin, answers , answersNum);
             System.out.println("try saved");
        }

    // sendTest
        public void sendTest(ClientThread receiver, String testID) {
            System.out.println("Start check try");
            if (storage.isUserHaveTry(testID, clientsName.get(receiver))) {
                System.out.println("u have try");
                int result = storage.getResult(testID, clientsName.get(receiver));
                System.out.println("you have result: " + result);
                receiver.send("Sr" + result);
            } else {
                System.out.println("no trys");
                String testQuestions = storage.getTestByID(testID);
                receiver.send("ST" + testQuestions);
            }
        }


    //###############################################################################
    //############################TEACHER############################################


    // send Tree

    public void sendSubjectTopicTestTree(ClientThread receiver) {
        String result = storage.getTree();
        receiver.send("TR" + result);
    }

    // get newSubject
        public void getNewSubject(ClientThread sender, String newSubject){
            storage.addSubject(newSubject);
        }


    // addNewTopic

        public void getNewTopic(ClientThread sender, String addTopic) {
            String[] mainParts  = addTopic.split("::");
            String newTopic = mainParts[1];
            String subjectId = mainParts[0];
            storage.addTopic(subjectId , newTopic);
        }

    //getUpdateSubject

        public void getUpdateSubject(ClientThread sender, String updateSubject){
             int subjectId = Integer.parseInt(updateSubject.split("::")[0]);
             String newSubject = updateSubject.split("::")[1];
             storage.updateSubject(subjectId , newSubject);
        }

    //getDeleteSubject
        public void getDeleteSubject(ClientThread sender, String subjectId){
             storage.deleteSubject(subjectId);
        }


    //getUpdateTopic

        public void getUpdateTopic(ClientThread sender, String updateTopic){
            String topicId = updateTopic.split("::")[0];
            String newTopicName = updateTopic.split("::")[1];
            storage.updateTopic(topicId, newTopicName);
        }

    //getDeleteTopic

        public void getDeleteTopic(ClientThread sender, String topicId){
             storage.deleteTopic(topicId);
        }

        // getNewTest

        public void getNewTest(ClientThread sender, String newTestText){
            String[] mainParts = newTestText.split("\\*\\*");
            String testName = mainParts[1];
            String topicId = mainParts[0];
            List<String> questions = new ArrayList<>();
            for (int i = 2; i < mainParts.length; i++){
                questions.add(mainParts[i]);
            }
            storage.addNewTest(topicId , testName , questions);
        }

    // getDeleteTest

        public void getDeleteTest(ClientThread sender, String testID){
            storage.deleteTest(testID);
        }


    // sendTry
    public void sendTry(ClientThread receiver, String tryId){
        String sendTry = storage.getTryById(tryId);
        receiver.send("SR"+sendTry);
    }


    // getResult
        public void getResult(ClientThread sender, String result){
             int tryId = Integer.parseInt(result.split("::")[0]);
             int newResult = Integer.parseInt(result.split("::")[1]);
             storage.setTryResult(tryId, newResult);
        }



    //###############################################################################
    //############################ADMIN##############################################



    // sendStudents
        public void sendStudents(ClientThread receiver){
            storage.getStudents();
            receiver.send("sl"+storage.getStudents());
        }



    // getGroup

        public void getGroup(ClientThread sender, String newGroup){
             storage.addGroup(newGroup);
        }



        public void addTeacher(ClientThread sender, String data){
            String login = data.split("::")[0];
            String password = data.split("::")[1];
            storage.addTeacher(login, password);
        }


    public void addStudent(ClientThread clientThread, String data) {
        String login = data.split("::")[0];
        String password = data.split("::")[1];
        storage.addStudent(login,password);
    }

    public void addStudentToGroup(ClientThread clientThread, String postfix) {
        String userLogin = postfix.split("::")[0];
        String groupName = postfix.split("::")[1];
        storage.addStudentToGroup(userLogin, groupName);
    }

    public void addSubjectToGroup(ClientThread clientThread, String postfix) {
        String subjecName = postfix.split("::")[0];
        String groupName = postfix.split("::")[1];
        storage.addSubjectGroup(subjecName , groupName);
    }
}
