package com.example.client;

import java.util.ArrayList;
import java.util.List;

public class TestManager {
    private static String testLabel;
    private static List<String> questions;

    private static List<String> answers = new ArrayList<>();
    private static int currQuestion = 0;

    public static String nextQuestion(){
        currQuestion++;
        if (currQuestion > questions.size()){
            if (currQuestion == questions.size()+1){

            }
            return "Finish";
        }
        return questions.get(currQuestion-1);
    }

    public static void resetQuestions(List<String> new_questions){
        questions = new_questions;
        currQuestion = 0;
    }

    public static void resetLabel(String new_Label){
        testLabel = new_Label;
    }

    public static List<String> getAnswers(){
        return answers;
    }

    public static void setAnswer(String answer){
        answers.add(answer);
    }

    public static void clearAnswers(){
        answers.clear();
    }

}
