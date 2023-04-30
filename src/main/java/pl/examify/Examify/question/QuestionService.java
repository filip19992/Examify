package pl.examify.Examify.question;

import org.springframework.stereotype.Service;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerDTO;
import pl.examify.Examify.answer.AnswerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public List<QuestionAnswersDTO> findAll() {
        List<Question> questions = questionRepository.findAll();

        return questions
                .stream()
                .map(c -> new QuestionAnswersDTO(c, getAnswersByQuestionId(c)))
                .collect(Collectors.toList());
    }

    public Question addQuestion(QuestionAnswersDTO questionAnswersDTO) {
        Question newQuestion = questionRepository
                .save(Question.builder()
                        .content(questionAnswersDTO.getQuestion().getContent())
                        .build());

        List<AnswerDTO> answers = questionAnswersDTO.getAnswers();

        answers.forEach(answer -> answerRepository.save(new Answer(newQuestion, answer.getContent(), answer.getIsGoodAnswer())));

        return newQuestion;
    }

    private List<AnswerDTO> getAnswersByQuestionId(Question c) {
        List<Answer> answersByQuestionId = answerRepository.findAnswersByQuestionId(c.getId());
        List<AnswerDTO> answerDTOS = new ArrayList<>();

        answersByQuestionId.forEach(answer -> answerDTOS.add(new AnswerDTO(answer.getContent())));

        return answerDTOS;
    }
}
