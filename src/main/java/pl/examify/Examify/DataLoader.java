package pl.examify.Examify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final QuestionRepository questionRepository;

    @Autowired
    public DataLoader(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void run(ApplicationArguments args) {
        questionRepository.save(new Question(1,"pytanie1"));
        questionRepository.save(new Question(2,"pytanie2"));
        questionRepository.save(new Question(3,"pytanie3"));
    }
}
