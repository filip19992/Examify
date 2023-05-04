package pl.examify.Examify.exam;

import pl.examify.Examify.question.QuestionWithAnswer;

import java.util.List;

public class ExamAttemptDTO {
    private List<QuestionWithAnswer> questionWithAnswers;

    public List<QuestionWithAnswer> getQuestionsWithAnswers() {
        return questionWithAnswers;
    }
}
