package pl.examify.Examify.question;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerDTO;
import pl.examify.Examify.answer.AnswerRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;

    public QuestionController(QuestionRepository questionRepository, QuestionService questionService, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.answerRepository = answerRepository;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionAnswersDTO>> getQuestions() {
        try {
            return new ResponseEntity<>(questionService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/question")
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionAnswersDTO questionAnswersDTO) {
        Question newQuestion = questionRepository
                .save(Question.builder()
                        .content(questionAnswersDTO.getQuestion().getContent())
                        .build());

        List<AnswerDTO> answers = questionAnswersDTO.getAnswers();

        answers.forEach(answer -> answerRepository.save(new Answer(newQuestion, answer.getContent(), answer.getIsGoodAnswer())));

        return new ResponseEntity<>(newQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<HttpStatus> deleteQuestionById(@PathVariable("id") long id) {
        try {
            Question question = getQuestionBy(id);

            if (question != null) {
                questionRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/questions")
    public ResponseEntity<HttpStatus> deleteAllQuestions() {
        try {
            questionRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Question getQuestionBy(long id) {
        Optional<Question> empObj = questionRepository.findById(id);

        return empObj.orElse(null);
    }
}
