package pl.examify.Examify.exam;

import java.util.List;

public class ExamDTO {
    private List<QuestionToCreateExamDTO> questions;
    private List<String> studentUsernames;

    public List<String> getStudentUsernames() {
        return studentUsernames;
    }
}
