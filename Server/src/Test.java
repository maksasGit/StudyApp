import java.util.*;

public class Test {


    // add basic info about Test and Test result's

    private String testLabel;
    private List<Question> questions;
    private String testID;


    public Test(String testLabel, List<Question> questions) {
        this.testLabel = testLabel;
        this.questions = questions;
        this.testID = IDGenerate(6);
    }



    public Test() {
        this.testLabel = "Тестовый тест";
        this.questions = new ArrayList<>(Arrays.asList(new Question(), new Question(), new Question(), new Question()));
        this.testID = IDGenerate(6);
    }

    public Test(String testLabel) {
        this.testLabel = testLabel;
        this.questions = new ArrayList<>(Arrays.asList(new Question("abba"), new Question("labba"), new Question("dabba"), new Question("dooooooo")));
        this.testID = IDGenerate(6);
    }

    private String IDGenerate(int length){
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public String getTestLabel() {
        return testLabel;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTestID() {
        return testID;
    }

    public List<Question> shuffleQuestions() {
        List<Question> shuffledList = new ArrayList<>(questions);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }



}
