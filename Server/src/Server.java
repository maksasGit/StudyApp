import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Cleaner;
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

    // send Tree

    public void sendSubjectTopicTestTree(ClientThread receiver) {
        // get all subject -> topic -> test and send it to make as tree
        receiver.send("TR");
    }


    //#################################################################################
    //############################STUDENT##############################################


    // getTry

        public void getTry(ClientThread sender, String stringTry){
            //Try newTry = Try(stringTry)
            // storage.saveTry(newTry)
        }



    // sendTest
//        public void sendTest(ClientThread receiver, String testID){
//            Test test = storage.getTestbyID(testID);
//            String testQuestions = "";
//            for (Question question : test.shuffleQuestions()){
//                testQuestions = testQuestions.concat(question.getQuestionText()+"::");
//            }
//            receiver.send("TQ"+testQuestions);
//        }


    //###############################################################################
    //############################TEACHER############################################


    // getSubject

        public void getSubject(ClientThread sender, String newSubject){
            // storage.addSubject(newSubject)
        }

    //getUpdateSubject

        public void getUpdateSubject(ClientThread sender, String updateSubject){
            // int subjectId = updateSubject
            // Subject updateSubject = Subject(updateSubject)
            // storage.updateSubject(subjectId , updateSubject)
        }

    //getDeleteSubject
        public void getDeleteSubject(ClientThread sender, String deleteSubject){
            // storage.deleteSubject(subjectId)
        }

    // getTopic
        public void getTopic(ClientThread sender, String newTopic){
            // storage.addTopic(newTopic)
        }

    //getUpdateTopic

        public void getUpdateTopic(ClientThread sender, String updateTopic){
            // storage.updateTopic(topicId, updateTopic)
        }

    //getDeleteTopic

        public void getDeleteTopiv(ClientThread sender, String deleteTopic){
            // storage.deleteTopic(topicId)
        }

    // getTest

        public void getTest(ClientThread sender, String newTest){
            // storage.addTest(newTest)
        }

    // getUpdateTest

        public void getUpdateTest(ClientThread sender, String updateTest){
            // storage.updateTest(testId, updateTest)
        }

    // getDeleteTest

        public void getDeleteTest(ClientThread sender, String deleteTest){
            // storage.deleteTest(testID)
        }

    // sendTry
        public void sendTry(ClientThread receiver){
            // getTry as big TXT : que - ans
            receiver.send("TR");
        }

    // sendTryList
        public void sendTryList(ClientThread receiver){
            // getallTry and just user_name and try_id
            receiver.send("TL");
        }

    // getResult
        public void getResult(ClientThread sender, String result){
            // int tryId = result
            // int newResult = result
            // storage.setTryResult(tryID, newResult)
        }

    // getUpdateResult
        public void getUpdateResult(ClientThread sender, String updateResult){
            // int tryId = result
            // int updateResult = result
            // storage.updateTryResult(tryID,updateResult)
        }

    // getDeleteResult

        public void getDeleteResult(ClientThread sender, String deleteTest){
            // int tryId = deleteTest
            //storage.deletetryResult(tryId)
        }

    //###############################################################################
    //############################ADMIN##############################################

    // sendGroups
        public void sendGropus(ClientThread receiver){
            // storage.getGroups()
            receiver.send("GR");
        }

    // sendStudents
        public void sendStudents(ClientThread receiver){
            // storage.getStudents()
            receiver.send("ST");
        }

    // getAddInGroup

        public void getAddInGroup(ClientThread sender, String addInGroup){
            //storage.addInGroup(userId, groupId)
        }

    // getRemoveFromGroup

        public void getRemoveFromGroup(ClientThread sender, String removeFromGroup){
            // storage.removeFromGroup(userId , groupId)
        }


    // getGroup

        public void getGroup(ClientThread sender, String newGruop){
            // storage.addGroup(newGroup)
        }

    // getUpdateGroup

        public void getUpdateGroup(ClientThread sender, String update){
            // storage.updateGroup(groupId,update)
        }

    // getDeleteGroup
        public void getDeleteGroup(ClientThread sender, String delete){
            //storage.deleteGroup(groupId)
        }


}
