package pl.examify.Examify.question;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.examify.Examify.question.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
