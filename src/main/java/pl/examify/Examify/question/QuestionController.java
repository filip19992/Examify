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
        Question question = questionService.addQuestion(questionAnswersDTO);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}
