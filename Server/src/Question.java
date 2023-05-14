import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {
    private String questionText;


    public Question(){
        this.questionText =  "What is the capital of France?";
    }

    public Question(String questionText){
        this.questionText =  questionText;
    }

    public String getQuestionText() {
        return questionText;
    }



}
