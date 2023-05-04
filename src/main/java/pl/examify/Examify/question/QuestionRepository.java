package pl.examify.Examify.question;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.examify.Examify.question.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByExamId(Long examId);
}
