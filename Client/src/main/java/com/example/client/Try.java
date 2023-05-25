package com.example.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Try {
    private int testId;
    private int currentQuestion = 0;
    private List<Question> questions = new ArrayList<>();

    public Try() {
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question newQuestion){
        this.questions.add(newQuestion);
    }

    public String nextQuestion(){
        if (currentQuestion >= questions.size()) {
            return "Finish";
        }
        else return questions.get(currentQuestion).getQuestion();
    }

    public void setAnswer(String answer){
        if (currentQuestion < questions.size()) {
            questions.get(currentQuestion).setAnswer(answer);
            currentQuestion++;
        }
        // else show that the test is over and wait for the result
    }

    public void shuffleQuestions(){
        Collections.shuffle(questions);
    }

    public void clear() {
        questions.clear();
        currentQuestion=0;
    }
}

class Question{
    private String question;
    private int questionNum;
    private String answer;

    public Question(String question, int questionNum) {
        this.question = question;
        this.questionNum = questionNum;
    }

    public String getQuestion() {
        return question;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }
}
