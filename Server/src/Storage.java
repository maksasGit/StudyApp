import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private Connection connection;

    public Storage() {
        connect();
    }

    public Connection connect() {
        connection = null;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish a connection to the database
            String url = "jdbc:sqlite:C:/Users/imaks/OneDrive/Desktop/StudyApp/Server/DataBase/DB.sqlite";
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
        return connection;
    }

    public String getTree() {
        Tree tree = new Tree();
        try {
            // Retrieve subjects
            List<CustomString> subjects = getSubjects();
            tree.items.addAll(subjects);

            // Retrieve topics for each subject
            for (CustomString subject : subjects) {
                List<CustomString> topics = getTopicsForSubject(subject.id);
                subject.items.addAll(topics);

                // Retrieve tests for each topic
                for (CustomString topic : topics) {
                    List<CustomString> tests = getTestsForTopic(topic.id);
                    topic.items.addAll(tests);

                    for (CustomString test: tests){
                        List<CustomString> trys = getTrysFroTest(test.id);
                        test.items.addAll(trys);
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve tree data from the database: " + e.getMessage());
        }

        return tree.toSend();
    }

    private List<CustomString> getSubjects() throws SQLException {
        List<CustomString> subjects = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT subject_id, subject_name FROM Subject");
        while (resultSet.next()) {
            int id = resultSet.getInt("subject_id");
            String name = resultSet.getString("subject_name");
            subjects.add(new CustomString(name, id));
        }
        resultSet.close();
        statement.close();
        return subjects;
    }

    private List<CustomString> getTopicsForSubject(int subjectId) throws SQLException {
        List<CustomString> topics = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT Topic.topic_id, Topic.topic_name " +
                "FROM Topic " +
                "INNER JOIN SubjectTopic ON Topic.topic_id = SubjectTopic.topic_id " +
                "WHERE SubjectTopic.subject_id = ?");
        statement.setInt(1, subjectId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("topic_id");
            String name = resultSet.getString("topic_name");
            topics.add(new CustomString(name, id));
        }
        resultSet.close();
        statement.close();
        return topics;
    }

    private List<CustomString> getTestsForTopic(int topicId) throws SQLException {
        List<CustomString> tests = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT Test.test_id, Test.test_name " +
                "FROM Test " +
                "INNER JOIN TopicTest ON Test.test_id = TopicTest.test_id " +
                "WHERE TopicTest.topic_id = ?");
        statement.setInt(1, topicId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("test_id");
            String name = resultSet.getString("test_name");
            tests.add(new CustomString(name, id));
        }
        resultSet.close();
        statement.close();
        return tests;
    }

    private List<CustomString> getTrysFroTest(int testId) throws SQLException{
        List<CustomString> trys = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT Try.try_id, User.login AS User_name " +
                "FROM Try " +
                "JOIN User ON Try.user_id = User.user_id WHERE Try.test_id = ?");
        statement.setInt(1, testId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("try_id");
            String name = resultSet.getString("User_name");
            trys.add(new CustomString(name, id));
        }
        resultSet.close();
        statement.close();
        return trys;

    }


    public String getTestByID(String testID) {
        StringBuilder testString = new StringBuilder();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT question, question_num FROM TestQuestion WHERE test_id = ?");
            statement.setString(1, testID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String question = resultSet.getString("question");
                int questionNum = resultSet.getInt("question_num");
                testString.append(question).append("**").append(questionNum).append("##");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve test questions from the database: " + e.getMessage());
        }

        return testString.toString();
    }


    public String checkLogin(String username, String password) {
        String answer = "NEOK";
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT User.user_id, User_type.user_type " +
                    "FROM User " +
                    "JOIN User_type ON User.user_type_id = User_type.user_type_id " +
                    "WHERE User.login = ? AND User.password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String userType = resultSet.getString("user_type");
                answer = "OK:" + userType;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve test information from the database: " + e.getMessage());
        }
        return answer;
    }



    public String getTryById(String tryId) {
        StringBuilder getTry = new StringBuilder();

        // Retrieve test information
        String testInfo = getTestInfo(tryId);
        getTry.append(testInfo).append("::");

        // Retrieve user information
        String userInfo = getUserInfo(tryId);
        getTry.append(userInfo).append("::");


        // Get questions for it test
        String questions = getTestByID(testInfo.split("::")[0]);
        String textQuestions = "";
        String[] questionParts = questions.split("##");
        for (String questionPart : questionParts){
            String[] parts = questionPart.split("\\*\\*");
            textQuestions = textQuestions.concat(parts[0] + "::");
        }
        getTry.append(textQuestions);

        // Retrieve answers
        String answers = getAnswers(tryId);
        getTry.append(answers);

        return getTry.toString();
    }

    private String getTestInfo(String tryId) {
        StringBuilder testInfo = new StringBuilder();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT Test.test_id, Test.test_name " +
                    "FROM Try " +
                    "JOIN Test ON Try.test_id = Test.test_id " +
                    "WHERE Try.try_id = ?");
            statement.setString(1, tryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int testId = resultSet.getInt("test_id");
                String testName = resultSet.getString("test_name");
                testInfo.append(testId)
                        .append("::")
                        .append(testName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve test information from the database: " + e.getMessage());
        }

        return testInfo.toString();
    }

    private String getUserInfo(String tryId) {
        StringBuilder userInfo = new StringBuilder();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT User.login " +
                    "FROM Try " +
                    "JOIN User ON Try.user_id = User.user_id " +
                    "WHERE Try.try_id = ?");
            statement.setString(1, tryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userLogin = resultSet.getString("login");
                userInfo.append(userLogin);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve user information from the database: " + e.getMessage());
        }

        return userInfo.toString();
    }

    private String getAnswers(String tryId) {
        StringBuilder answers = new StringBuilder();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT answer_num, answer " +
                    "FROM TryAnswer " +
                    "WHERE try_id = ? " +
                    "ORDER BY answer_num");
            statement.setString(1, tryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int answerNum = resultSet.getInt("answer_num");
                String answer = resultSet.getString("answer");
                answers.append(answer).append("::");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve answers from the database: " + e.getMessage());
        }

        return answers.toString();
    }



}
