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
import java.util.Optional;
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
                .filter(exam -> isExamForGivenUserId(userId, exam))
                .findFirst()
                .map(this::getQuestionAnswersDTOS)
                .orElse(List.of());
    }

    private List<QuestionAnswersDTO> getQuestionAnswersDTOS(Exam exam) {
        var questions = questionRepository.findAllByExamId(exam.getId());
        return questions.stream()
                .map(this::mapQuestionToQuestionDTO)
                .collect(Collectors.toList());
    }

    public long attemptExam(List<QuestionWithAnswer> questionWithAnswers, String name) {
        long maxPoints = questionWithAnswers.size();
        long gatheredPoints = getPointsForCorrectAnswers(questionWithAnswers);

        var result = (double) gatheredPoints / maxPoints;

        examAttemptRepository.save(new ExamAttempt(name, result));

        return Math.round(result * 100);
    }

    public void createExam(ExamDTO examDTO) {
        var students = getUsersByEmails(examDTO);

        var savedExam = examRepository.save(new Exam(students));

        examDTO.getQuestions().forEach(question -> saveQuestionsAndAnswers(savedExam, question));
    }

    private void saveQuestionsAndAnswers(Exam savedExam, QuestionToCreateExamDTO question) {
        var savedQuestion = questionRepository.save(new Question(question.getQuestionContent(), savedExam));

        question.getAnswerToCreateExamList().forEach(answer -> saveAnswersForQuestion(savedQuestion, answer));
    }

    private void saveAnswersForQuestion(Question savedQuestion, AnswerToCreateExamDTO answer) {
        answerRepository.save(new Answer(savedQuestion, answer.getContent(), answer.getIsGoodAnswer()));
    }

    private List<User> getUsersByEmails(ExamDTO examDTO) {
        return examDTO.getStudentUsernames().stream()
                .map(userRepository::findByEmail)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private QuestionAnswersDTO mapQuestionToQuestionDTO(Question question) {
        return new QuestionAnswersDTO(new QuestionDTO(question.getId(), question.getContent()), getAnswersByQuestionId(question));
    }

    private long getPointsForCorrectAnswers(List<QuestionWithAnswer> questionWithAnswers) {
        return questionWithAnswers.stream()
                .mapToLong(q -> answerRepository.findById(q.getAnswerId())
                        .map(answer -> answer.getIsGoodAnswer().equals("Y") ? 1L : 0L)
                        .orElse(0L))
                .sum();
    }


    private List<AnswerDTO> getAnswersByQuestionId(Question c) {
        var answersByQuestionId = answerRepository.findAnswersByQuestionId(c.getId());
        var answerDTOS = new ArrayList<AnswerDTO>();

        answersByQuestionId.forEach(answer -> answerDTOS.add(new AnswerDTO(answer.getId(), answer.getContent())));

        return answerDTOS;
    }

    private boolean isExamForGivenUserId(Long userId, Exam exam) {
        return exam.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
    }
}
