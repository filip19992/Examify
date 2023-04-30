package pl.examify.Examify.question;

import lombok.Getter;
import lombok.Setter;
import pl.examify.Examify.answer.AnswerDTO;

import java.util.List;

@Getter
@Setter
public class QuestionAnswersDTO {
    private Question question;
    private List<AnswerDTO> answers;

    public QuestionAnswersDTO(Question question, List<AnswerDTO> answers) {
        this.question = question;
        this.answers = answers;
    }
}
