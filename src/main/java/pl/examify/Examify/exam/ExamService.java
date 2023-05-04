package pl.examify.Examify.exam;

import org.springframework.stereotype.Service;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerDTO;
import pl.examify.Examify.answer.AnswerRepository;
import pl.examify.Examify.question.*;
import pl.examify.Examify.user.User;
import pl.examify.Examify.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final UserRepository userRepository;

    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, ExamAttemptRepository examAttemptRepository, UserRepository userRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.userRepository = userRepository;
    }

    public List<QuestionAnswersDTO> findExamByUserId(Long userId) {
        return examRepository.findAll().stream()
                .filter(exam -> exam.getUsers().stream().anyMatch(user -> user.getId().equals(userId)))
                .findFirst()
                .map(exam -> {
                    List<Question> questions = questionRepository.findAllByExamId(exam.getId());
                    return questions.stream()
                            .map(question -> new QuestionAnswersDTO(new QuestionDTO(question.getId(), question.getContent()), getAnswersByQuestionId(question)))
                            .collect(Collectors.toList());
                })
                .orElse(List.of());
    }

    public long attemptExam(List<QuestionWithAnswer> questionWithAnswers, String name) {
        long maxPoints = questionWithAnswers.size();
        long gatheredPoints = questionWithAnswers.stream()
                .mapToLong(q -> answerRepository.findById(q.getAnswerId())
                        .map(answer -> answer.getIsGoodAnswer().equals("Y") ? 1L : 0L)
                        .orElse(0L))
                .sum();

        var result = (double) gatheredPoints / maxPoints;

        examAttemptRepository.save(new ExamAttempt(name, result));

        return Math.round(result * 100);
    }



    private List<AnswerDTO> getAnswersByQuestionId(Question c) {
        var answersByQuestionId = answerRepository.findAnswersByQuestionId(c.getId());
        var answerDTOS = new ArrayList<AnswerDTO>();

        answersByQuestionId.forEach(answer -> answerDTOS.add(new AnswerDTO(answer.getId(), answer.getContent())));

        return answerDTOS;
    }

    public Long createExam(ExamDTO examDTO) {
        var students = new ArrayList<User>();
        var questions = new ArrayList<Question>();

        for(var username : examDTO.getStudentUsernames()) {
            var student = userRepository.findByEmail(username);
            students.add(student);
        }

        var savedExam = examRepository.save(new Exam(students));

        for(var question : examDTO.getQuestions()) {
            Question savedQuestion = questionRepository.save(new Question(question.getQuestionContent(), savedExam));
            for(var answer : question.getAnswerToCreateExamList()){
                answerRepository.save(new Answer(savedQuestion, answer.getContent(), answer.getIsGoodAnswer()));
            }
        }
        return 0L;
    }
}
