package main.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {
    private String questionText;
    private List<String> answerOptions;
    private List<String> correctAnswers;


    public Question(String questionText, List<String> answerOptions, List<String> correctAnswers) {
        this.questionText = questionText;
        this.answerOptions = answerOptions;
        this.correctAnswers = correctAnswers;
    }

    public Question(){
        this.questionText =  "What is the capital of France?";
        this.answerOptions = Arrays.asList("London", "Paris", "Berlin", "Madrid");
        this.correctAnswers = Arrays.asList("Paris");
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public int isCorrectAnswers(List<String> selectedAnswers) {
        int result = 0;
        for (String selected : selectedAnswers) {
            for (String correct : correctAnswers){
                if (selected.equals(correct)){
                    result++;
                }
            }
        }
        return result;
    }

    public List<String> shuffleAnswers() {
        List<String> shuffledList = new ArrayList<>(answerOptions);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

}
