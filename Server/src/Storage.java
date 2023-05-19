import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private Connection connection;

    public Storage() {
        connect();
        System.out.println(getTree());
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
            List<StringID> subjects = getSubjects();
            tree.items.addAll(subjects);

            // Retrieve topics for each subject
            for (StringID subject : subjects) {
                List<StringID> topics = getTopicsForSubject(subject.id);
                subject.items.addAll(topics);

                // Retrieve tests for each topic
                for (StringID topic : topics) {
                    List<StringID> tests = getTestsForTopic(topic.id);
                    topic.items.addAll(tests);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve tree data from the database: " + e.getMessage());
        }

        return tree.toSend();
    }

    private List<StringID> getSubjects() throws SQLException {
        List<StringID> subjects = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT subject_id, subject_name FROM Subject");
        while (resultSet.next()) {
            int id = resultSet.getInt("subject_id");
            String name = resultSet.getString("subject_name");
            subjects.add(new StringID(name, id));
        }
        resultSet.close();
        statement.close();
        return subjects;
    }

    private List<StringID> getTopicsForSubject(int subjectId) throws SQLException {
        List<StringID> topics = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT Topic.topic_id, Topic.topic_name " +
                "FROM Topic " +
                "INNER JOIN SubjectTopic ON Topic.topic_id = SubjectTopic.topic_id " +
                "WHERE SubjectTopic.subject_id = ?");
        statement.setInt(1, subjectId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("topic_id");
            String name = resultSet.getString("topic_name");
            topics.add(new StringID(name, id));
        }
        resultSet.close();
        statement.close();
        return topics;
    }

    private List<StringID> getTestsForTopic(int topicId) throws SQLException {
        List<StringID> tests = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT Test.test_id, Test.test_name " +
                "FROM Test " +
                "INNER JOIN TopicTest ON Test.test_id = TopicTest.test_id " +
                "WHERE TopicTest.topic_id = ?");
        statement.setInt(1, topicId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("test_id");
            String name = resultSet.getString("test_name");
            tests.add(new StringID(name, id));
        }
        resultSet.close();
        statement.close();
        return tests;
    }

}
