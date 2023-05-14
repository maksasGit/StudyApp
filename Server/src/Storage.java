import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Storage {
    private List<Test> tests;

    public Storage() {
        this.tests = new ArrayList<>(Arrays.asList(new Test("Tect 1"), new Test("Test2"), new Test("Test nomer 3"), new Test("Examination Test №4")));
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
