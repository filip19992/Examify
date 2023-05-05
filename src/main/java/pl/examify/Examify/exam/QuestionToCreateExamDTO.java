package pl.examify.Examify.exam;

import java.util.List;

public class QuestionToCreateExamDTO {
    private String questionContent;
    private List<AnswerToCreateExamDTO> answerToCreateExamList;

    public String getQuestionContent() {
        return questionContent;
    }

    public List<AnswerToCreateExamDTO> getAnswerToCreateExamList() {
        return answerToCreateExamList;
    }
}
