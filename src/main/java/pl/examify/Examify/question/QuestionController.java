package pl.examify.Examify.question;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QuestionController {

    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getEmployees() {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") long id) {
        try {
            Question empObj = getQuestionBy(id);

            if (empObj != null) {
                return new ResponseEntity<>(empObj, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/question")
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionDTO question) {
        Question newQuestion = questionRepository
                .save(Question.builder()
                        .content(question.getContent())
                        .build());
        return new ResponseEntity<>(newQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<HttpStatus> deleteQuestionById(@PathVariable("id") long id) {
        try {
            //check if employee exist in database
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
