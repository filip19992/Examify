package pl.examify.Examify.exam;

import org.springframework.stereotype.Service;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerDTO;
import pl.examify.Examify.answer.AnswerRepository;
import pl.examify.Examify.question.Question;
import pl.examify.Examify.question.QuestionAnswersDTO;
import pl.examify.Examify.question.QuestionRepository;
import pl.examify.Examify.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public List<QuestionAnswersDTO> findExamByUserId(Long userId) {
        var list = new ArrayList<QuestionAnswersDTO>();
        long examId = 0;

        for(Exam exam : examRepository.findAll()) {
            for(User user : exam.getUsers()) {
                if (user.getId().equals(userId)) {
                    examId = exam.getId();
                    break;
                }
            }
        }

        if(examId != 0) {
            List<Question> questions = questionRepository.findAllByExamId(examId);

            return questions
                    .stream()
                    .map(c -> new QuestionAnswersDTO(c, getAnswersByQuestionId(c)))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    private List<AnswerDTO> getAnswersByQuestionId(Question c) {
        List<Answer> answersByQuestionId = answerRepository.findAnswersByQuestionId(c.getId());
        List<AnswerDTO> answerDTOS = new ArrayList<>();

        answersByQuestionId.forEach(answer -> answerDTOS.add(new AnswerDTO(answer.getContent())));

        return answerDTOS;
    }
}
