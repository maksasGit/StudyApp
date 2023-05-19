import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Storage {
    private List<Test> tests;

    public Storage() {
        connect();
        this.tests = new ArrayList<>(Arrays.asList(new Test("Tect 1"), new Test("Test2"), new Test("Test nomer 3"), new Test("Examination Test №4")));
    }

    public Connection connect() {
        Connection connection = null;
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



    public List<Test> getTests() {
        return tests;
    }

    public Test getTestbyID(String testID){
        for (Test test : tests){
            if (test.getTestID().equals(testID)){
                return test;
            }
        }
        System.out.println("No test found by TestID");
        return null;
    }
}
