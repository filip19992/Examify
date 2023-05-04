package pl.examify.Examify.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.examify.Examify.question.Question;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    @Column
    private String content;
    @Column(length = 1)
    private String isGoodAnswer;

    public Answer(Question question, String content, String isGoodAnswer) {
        this.question = question;
        this.content = content;
        this.isGoodAnswer = isGoodAnswer;
    }
}
