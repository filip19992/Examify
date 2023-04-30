package pl.examify.Examify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerRepository;
import pl.examify.Examify.question.Question;
import pl.examify.Examify.question.QuestionRepository;

@Component
public class DataLoader implements ApplicationRunner {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public DataLoader(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public void run(ApplicationArguments args) {
        Question firstQuestion = questionRepository.save(new Question(1, "2 plus 2 to"));
        Question secoundQuestion = questionRepository.save(new Question(2, "5 minus 3 to"));
        Question thirdQuestion = questionRepository.save(new Question(3, "2 razy 5 to"));

        answerRepository.save(new Answer(1, firstQuestion, "cztery", "Y"));
        answerRepository.save(new Answer(2, firstQuestion, "jeden", "N"));
        answerRepository.save(new Answer(3, firstQuestion, "zero", "N"));
        answerRepository.save(new Answer(4, firstQuestion, "trzy", "N"));

        answerRepository.save(new Answer(5, secoundQuestion, "cztery", "N"));
        answerRepository.save(new Answer(6, secoundQuestion, "jeden", "N"));
        answerRepository.save(new Answer(7, secoundQuestion, "dwa", "Y"));
        answerRepository.save(new Answer(8, secoundQuestion, "trzy", "N"));

        answerRepository.save(new Answer(9, thirdQuestion, "cztery", "N"));
        answerRepository.save(new Answer(10, thirdQuestion, "dziesięć", "Y"));
        answerRepository.save(new Answer(11, thirdQuestion, "zero", "N"));
        answerRepository.save(new Answer(12, thirdQuestion, "trzy", "N"));
    }
}
