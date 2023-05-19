public class Answer {
    private String answer;
    public enum SolutionStatus {
        CORRECT,
        WRONG,
        PARTIAL,
        NOT_VERIFIED
    }

    private SolutionStatus solutionStatus = SolutionStatus.NOT_VERIFIED;

    public Answer(String answer, SolutionStatus solutionStatus) {
        this.answer = answer;
        this.solutionStatus = solutionStatus;
    }


}
