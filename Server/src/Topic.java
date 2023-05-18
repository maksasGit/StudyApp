import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String topicName;
    private List<Test> tests = new ArrayList<>();

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public List<Test> getTests() {
        return tests;
    }

    public String getTopicName() {
        return topicName;
    }

    public void addTest(Test newTest){
        tests.add(newTest);
    }
}
