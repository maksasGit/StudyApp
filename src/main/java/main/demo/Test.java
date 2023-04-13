package main.demo;

import java.util.*;

public class Test {


    // add basic info about Test and Test result's

    private String testLabel;
    private List<Question> questions;
    private List<Integer> testResult;


    public Test(String testLabel, List<Question> questions) {
        this.testLabel = testLabel;
        this.questions = questions;
    }

    public Test() {
        this.testLabel = "Тестовый тест";
        this.questions = new ArrayList<>(Arrays.asList(new Question(), new Question(), new Question(), new Question()));
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
