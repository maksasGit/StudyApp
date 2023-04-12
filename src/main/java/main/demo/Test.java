package main.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    // basic info

    private String testLabel;
    private List<Question> questions;
    private List<Integer> testResult;


    public Test(String testLabel, List<Question> questions,  List<Integer> testResult) {
        this.testLabel = testLabel;
        this.questions = questions;
        this.testResult = testResult;
    }

    public String getTestLabel() {
        return testLabel;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Integer> getTestResult() {
        return testResult;
    }


    public List<Question> shuffleQuestions() {
        List<Question> shuffledList = new ArrayList<>(questions);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }


}
