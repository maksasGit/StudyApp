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

    public String getTreeForStudent(String user_login) {
        Tree tree = new Tree();
        try {
            // Retrieve subjects
            List<CustomString> subjects = getSubjectsForStudent(user_login);
            tree.items.addAll(subjects);

            // Retrieve topics for each subject
            for (CustomString subject : subjects) {
                List<CustomString> topics = getTopicsForSubject(subject.id);
                subject.items.addAll(topics);

                // Retrieve tests for each topic
                for (CustomString topic : topics) {
                    List<CustomString> tests = getTestsForTopic(topic.id);
                    topic.items.addAll(tests);

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

    private List<CustomString> getSubjectsForStudent(String user_login) throws SQLException {
        System.out.println("getSubjectsForStudent: " + user_login);
        List<CustomString> subjects = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT Subject.subject_id, Subject.subject_name " +
                        "FROM Subject " +
                        "JOIN SubjectGroup ON Subject.subject_id = SubjectGroup.subject_id " +
                        "JOIN User ON SubjectGroup.group_id = User.group_id " +
                        "WHERE User.login = ?"
        );
        statement.setString(1, user_login);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("subject_id");
            String name = resultSet.getString("subject_name");
            System.out.println("\t Subject_info: " + id + " " + name);
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


    public String getUserIdbyUserLogin(String userLogin){
        StringBuilder answer = new StringBuilder();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM User " +
                    "WHERE login = ? ");
            statement.setString(1, userLogin);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("user_id");
                answer.append(userID);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve answers from the database: " + e.getMessage());
        }

        return answer.toString();
    }


    public void saveTry(String testId, String userLogin, List<String> answers, List<String> answersNum) {
        String userId = getUserIdbyUserLogin(userLogin);
        try {
            // Insert the new Try record
            String insertTryQuery = "INSERT INTO Try (test_id, user_id) VALUES (?, ?)";
            try (PreparedStatement tryStatement = connection.prepareStatement(insertTryQuery)) {
                tryStatement.setString(1, testId);
                tryStatement.setString(2, userId);
                tryStatement.executeUpdate();
            }

            // Get the try_id of the newly inserted Try
            String selectTryIdQuery = "SELECT try_id FROM Try WHERE test_id = ? AND user_id = ?";
            int tryId;
            try (PreparedStatement tryIdStatement = connection.prepareStatement(selectTryIdQuery)) {
                tryIdStatement.setString(1, testId);
                tryIdStatement.setString(2, userId);
                try (ResultSet tryIdResult = tryIdStatement.executeQuery()) {
                    if (tryIdResult.next()) {
                        tryId = tryIdResult.getInt("try_id");
                    } else {
                        // Handle error if the try_id was not retrieved
                        throw new SQLException("Failed to retrieve try_id");
                    }
                }
            }

            // Insert the answers into TryAnswer table
            String insertTryAnswerQuery = "INSERT INTO TryAnswer (try_id, answer, answer_num) VALUES (?, ?, ?)";
            try (PreparedStatement tryAnswerStatement = connection.prepareStatement(insertTryAnswerQuery)) {
                for (int i = 0; i < answers.size(); i++) {
                    tryAnswerStatement.setInt(1, tryId);
                    tryAnswerStatement.setString(2, answers.get(i));
                    tryAnswerStatement.setString(3, answersNum.get(i));
                    tryAnswerStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve answers from the database: " + e.getMessage());
        }
    }


    public int getResult(String testID, String userLogin) {
        String userId = getUserIdbyUserLogin(userLogin);
        try {
            String query = "SELECT result FROM Try WHERE test_id = ? AND user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, testID);
            statement.setString(2, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int result = resultSet.getInt("result");
                return result;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to check result from the database: " + e.getMessage());
        }
        return 0;
    }

    public boolean isUserHaveTry(String testID, String userLogin) {
        System.out.println("start isUserHaveTry");
        String userID = getUserIdbyUserLogin(userLogin);
        System.out.println("get userID: " + userID);
        try {
            String query = "SELECT COUNT(*) AS count FROM Try WHERE user_id = ? AND test_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            statement.setString(2, testID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return (count > 0);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to check if user has a try in the database: " + e.getMessage());
        }
        return false;
    }

    public void setTryResult(int tryId, int newResult) {
        try {
            String updateQuery = "UPDATE Try SET result = ? WHERE try_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, newResult);
            statement.setInt(2, tryId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Try result updated successfully.");
            } else {
                System.out.println("Failed to update try result. Try ID not found.");
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to update try result: " + e.getMessage());
        }
    }

    public void addNewTest(String testName, List<String> questions) {
        try {
            // Insert the new test into the 'Test' table
            String insertTestQuery = "INSERT INTO Test (test_name) VALUES (?)";
            PreparedStatement insertTestStatement = connection.prepareStatement(insertTestQuery, Statement.RETURN_GENERATED_KEYS);
            insertTestStatement.setString(1, testName);
            insertTestStatement.executeUpdate();

            // Get the generated test ID
            ResultSet generatedKeys = insertTestStatement.getGeneratedKeys();
            int testId = -1;
            if (generatedKeys.next()) {
                testId = generatedKeys.getInt(1);
            }

            // Insert questions into the 'TestQuestion' table
            String insertQuestionQuery = "INSERT INTO TestQuestion (test_id, question, question_num) VALUES (?, ?, ?)";
            PreparedStatement insertQuestionStatement = connection.prepareStatement(insertQuestionQuery);
            for (int i = 0; i < questions.size(); i++) {
                insertQuestionStatement.setInt(1, testId);
                insertQuestionStatement.setString(2, questions.get(i));
                insertQuestionStatement.setInt(3, i );
                insertQuestionStatement.executeUpdate();
            }

            System.out.println("New test added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add a new test.");
        }
    }

    public void deleteTest(String testID) {
        try {
            // Delete records from the 'TryAnswer' table for the specified test ID
            String deleteTryAnswerQuery = "DELETE FROM TryAnswer WHERE try_id IN " +
                    "(SELECT try_id FROM Try WHERE test_id = ?)";
            PreparedStatement deleteTryAnswerStatement = connection.prepareStatement(deleteTryAnswerQuery);
            deleteTryAnswerStatement.setString(1, testID);
            deleteTryAnswerStatement.executeUpdate();

            // Delete records from the 'Try' table for the specified test ID
            String deleteTryQuery = "DELETE FROM Try WHERE test_id = ?";
            PreparedStatement deleteTryStatement = connection.prepareStatement(deleteTryQuery);
            deleteTryStatement.setString(1, testID);
            deleteTryStatement.executeUpdate();

            // Delete records from the 'TestQuestion' table for the specified test ID
            String deleteTestQuestionQuery = "DELETE FROM TestQuestion WHERE test_id = ?";
            PreparedStatement deleteTestQuestionStatement = connection.prepareStatement(deleteTestQuestionQuery);
            deleteTestQuestionStatement.setString(1, testID);
            deleteTestQuestionStatement.executeUpdate();

            // Delete the record from the 'Test' table for the specified test ID
            String deleteTestQuery = "DELETE FROM Test WHERE test_id = ?";
            PreparedStatement deleteTestStatement = connection.prepareStatement(deleteTestQuery);
            deleteTestStatement.setString(1, testID);
            deleteTestStatement.executeUpdate();

            System.out.println("Test and related records deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete test and related records.");
        }
    }

    public void deleteTopic(String topicId) {
        try {
            // Retrieve all tests associated with the specified topic
            List<String> testIds = getTestsByTopic(topicId);

            // Delete each test associated with the topic
            for (String testId : testIds) {
                deleteTest(testId);
            }

            // Delete the topic
            String deleteTopicQuery = "DELETE FROM Topic WHERE topic_id = ?";
            PreparedStatement deleteTopicStatement = connection.prepareStatement(deleteTopicQuery);
            deleteTopicStatement.setString(1, topicId);
            deleteTopicStatement.executeUpdate();

            System.out.println("Topic and associated tests deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete topic and associated tests.");
        }
    }

    public List<String> getTestsByTopic(String topicId) {
        List<String> testIds = new ArrayList<>();

        try {
            // Retrieve the test IDs associated with the specified topic
            String selectTestsQuery = "SELECT test_id FROM TopicTest WHERE topic_id = ?";
            PreparedStatement selectTestsStatement = connection.prepareStatement(selectTestsQuery);
            selectTestsStatement.setString(1, topicId);
            ResultSet resultSet = selectTestsStatement.executeQuery();

            // Add the test IDs to the list
            while (resultSet.next()) {
                String testId = resultSet.getString("test_id");
                testIds.add(testId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return testIds;
    }

    public void updateTopic(String topicId, String newTopicName) {
        try {
            // Update the topic name in the 'Topic' table
            String updateTopicQuery = "UPDATE Topic SET topic_name = ? WHERE topic_id = ?";
            PreparedStatement updateTopicStatement = connection.prepareStatement(updateTopicQuery);
            updateTopicStatement.setString(1, newTopicName);
            updateTopicStatement.setString(2, topicId);
            updateTopicStatement.executeUpdate();

            System.out.println("Topic updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update topic.");
        }
    }

    public void addSubject(String newSubject) {
        try {
            // Insert a new subject into the 'Subject' table
            String insertSubjectQuery = "INSERT INTO Subject (subject_name) VALUES (?)";
            PreparedStatement insertSubjectStatement = connection.prepareStatement(insertSubjectQuery);
            insertSubjectStatement.setString(1, newSubject);
            insertSubjectStatement.executeUpdate();

            System.out.println("Subject added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add subject.");
        }
    }

    public void updateSubject(int subjectId, String newSubjectName) {
        try {
            // Update the subject name in the 'Subject' table
            String updateSubjectQuery = "UPDATE Subject SET subject_name = ? WHERE subject_id = ?";
            PreparedStatement updateSubjectStatement = connection.prepareStatement(updateSubjectQuery);
            updateSubjectStatement.setString(1, newSubjectName);
            updateSubjectStatement.setInt(2, subjectId);
            updateSubjectStatement.executeUpdate();

            System.out.println("Subject updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update subject.");
        }
    }

    public void deleteSubject(String subjectId) {
        try {
            // Retrieve all topics associated with the subject
            List<String> topicIds = getTopicsBySubject(subjectId);

            // Delete records from the 'SubjectTopic' table for the specified subject
            String deleteSubjectTopicQuery = "DELETE FROM SubjectTopic WHERE subject_id = ?";
            PreparedStatement deleteSubjectTopicStatement = connection.prepareStatement(deleteSubjectTopicQuery);
            deleteSubjectTopicStatement.setString(1, subjectId);
            deleteSubjectTopicStatement.executeUpdate();

            // Delete records from the 'SubjectGroup' table for the specified subject
            String deleteSubjectGroupQuery = "DELETE FROM SubjectGroup WHERE subject_id = ?";
            PreparedStatement deleteSubjectGroupStatement = connection.prepareStatement(deleteSubjectGroupQuery);
            deleteSubjectGroupStatement.setString(1, subjectId);
            deleteSubjectGroupStatement.executeUpdate();

            // Delete the subject record from the 'Subject' table
            String deleteSubjectQuery = "DELETE FROM Subject WHERE subject_id = ?";
            PreparedStatement deleteSubjectStatement = connection.prepareStatement(deleteSubjectQuery);
            deleteSubjectStatement.setString(1, subjectId);
            deleteSubjectStatement.executeUpdate();

            // Delete all topics associated with the subject
            for (String topicId : topicIds) {
                deleteTopic(topicId);
            }

            System.out.println("Subject and associated records deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete subject and associated records.");
        }
    }

    public List<String> getTopicsBySubject(String subjectId) {
        List<String> topicIds = new ArrayList<>();

        try {
            // Retrieve the topic IDs associated with the specified subject
            String selectTopicsQuery = "SELECT topic_id FROM SubjectTopic WHERE subject_id = ?";
            PreparedStatement selectTopicsStatement = connection.prepareStatement(selectTopicsQuery);
            selectTopicsStatement.setString(1, subjectId);
            ResultSet resultSet = selectTopicsStatement.executeQuery();

            // Add the topic IDs to the list
            while (resultSet.next()) {
                String topicId = resultSet.getString("topic_id");
                topicIds.add(topicId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topicIds;
    }

}
