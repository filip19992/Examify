package pl.examify.Examify.exam;

import org.springframework.stereotype.Service;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerDTO;
import pl.examify.Examify.answer.AnswerRepository;
import pl.examify.Examify.question.Question;
import pl.examify.Examify.question.QuestionAnswersDTO;
import pl.examify.Examify.question.QuestionRepository;
import pl.examify.Examify.question.QuestionWithAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ExamAttemptRepository examAttemptRepository;

    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, ExamAttemptRepository examAttemptRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.examAttemptRepository = examAttemptRepository;
    }

    public List<QuestionAnswersDTO> findExamByUserId(Long userId) {
        return examRepository.findAll().stream()
                .filter(exam -> exam.getUsers().stream().anyMatch(user -> user.getId().equals(userId)))
                .findFirst()
                .map(exam -> {
                    List<Question> questions = questionRepository.findAllByExamId(exam.getId());
                    return questions.stream()
                            .map(question -> new QuestionAnswersDTO(question, getAnswersByQuestionId(question)))
                            .collect(Collectors.toList());
                })
                .orElse(List.of());
    }

    public int attemptExam(List<QuestionWithAnswer> questionWithAnswers, String name) {
        int maxPoints = questionWithAnswers.size();
        int gatheredPoints = 0;
        for(QuestionWithAnswer question : questionWithAnswers) {
            Answer answer = answerRepository.findById(question.getAnswerId()).get();
            if(answer.getIsGoodAnswer().equals("Y"))
                gatheredPoints++;
        }

        var result = gatheredPoints/maxPoints;

        examAttemptRepository.save(new ExamAttempt(name));

        return result;
    }

    private List<AnswerDTO> getAnswersByQuestionId(Question c) {
        List<Answer> answersByQuestionId = answerRepository.findAnswersByQuestionId(c.getId());
        List<AnswerDTO> answerDTOS = new ArrayList<>();

        answersByQuestionId.forEach(answer -> answerDTOS.add(new AnswerDTO(answer.getId(), answer.getContent())));

        return answerDTOS;
    }
}
