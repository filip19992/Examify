package pl.examify.Examify.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerDTO {
    private String content;
    private String isGoodAnswer;

    public AnswerDTO(String content) {
        this.content = content;
    }
}
