import java.util.ArrayList;
import java.util.List;

public class Subject {
    private String subjectName;
    private List<Topic> topics = new ArrayList<>();

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void addTopic(Topic newTopic) {
        topics.add(newTopic);
    }
}

