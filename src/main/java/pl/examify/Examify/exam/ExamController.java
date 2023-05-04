package pl.examify.Examify.exam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.examify.Examify.question.QuestionAnswersDTO;
import pl.examify.Examify.question.QuestionWithAnswer;
import pl.examify.Examify.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ExamController {
    private final UserRepository userRepository;
    private final ExamService examService;

    public ExamController(UserRepository userRepository, ExamService examService) {
        this.userRepository = userRepository;
        this.examService = examService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionAnswersDTO>> getExam(HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();

        var userId = userRepository.findByEmail(name).getId();

        try {
            return new ResponseEntity<>(examService.findExamByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/exam")
    public ResponseEntity<Integer> attemptExam(@RequestBody ExamAttemptDTO examAttempt, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();


        List<QuestionWithAnswer> questionWithAnswers = examAttempt.getQuestionsWithAnswers();

        try {
            return new ResponseEntity<>(examService.attemptExam(questionWithAnswers, name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
