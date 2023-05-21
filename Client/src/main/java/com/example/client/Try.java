package com.example.client;

import java.util.ArrayList;
import java.util.List;

public class Try {
    public int testId;

    public int currentQuestion = 0;
    public List<Question> questions = new ArrayList<>();

    public Try() {
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }


    public void addQuestion(Question newQuestion){
        this.questions.add(newQuestion);
    }

    public String nextQuestion(){
        if (currentQuestion> questions.size()-1) {
            return "Finish";
        }
        return questions.get(currentQuestion).question;
    }


    public void setAnswer(String answer){
        questions.get(currentQuestion).answer = answer;
        currentQuestion++;
    }

    // shuffle quesions

}

class Question{
    public String question;
    public int question_num;

    public String answer;

    public Question(String question, int question_num) {
        this.question = question;
        this.question_num = question_num;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }
}
