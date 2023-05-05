package pl.examify.Examify.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.examify.Examify.answer.Answer;
import pl.examify.Examify.answer.AnswerRepository;
import pl.examify.Examify.exam.Exam;
import pl.examify.Examify.exam.ExamRepository;
import pl.examify.Examify.question.Question;
import pl.examify.Examify.question.QuestionRepository;
import pl.examify.Examify.user.User;
import pl.examify.Examify.user.UserRepository;
import pl.examify.Examify.user.roles.Privilege;
import pl.examify.Examify.user.roles.PrivilegeRepository;
import pl.examify.Examify.user.roles.Role;
import pl.examify.Examify.user.roles.RoleRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final PasswordEncoder passwordEncoder;

    public void run(ApplicationArguments args) {
        loadPrivilegesAndRoles();

        loadUsers();
    }

    private void loadUsers() {
        Role studentRole = roleRepository.findByName("ROLE_STUDENT");
        User student = new User();
        student.setFirstName("student");
        student.setLastName("student");
        student.setPassword(passwordEncoder.encode("student"));
        student.setEmail("student@student.com");
        student.setRoles(Arrays.asList(studentRole));
        student.setEnabled(true);
        userRepository.save(student);


        Role lecturerRole = roleRepository.findByName("ROLE_LECTURER");
        User lecturer = new User();
        lecturer.setFirstName("lecturer");
        lecturer.setLastName("lecturer");
        lecturer.setPassword(passwordEncoder.encode("lecturer"));
        lecturer.setEmail("lecturer@lecturer.com");
        lecturer.setRoles(Arrays.asList(lecturerRole));
        lecturer.setEnabled(true);
        userRepository.save(lecturer);

        Exam exam = examRepository.save(new Exam(List.of(student)));

        Question firstQuestion = questionRepository.save(new Question(1, "2 plus 2 to", exam));
        Question secoundQuestion = questionRepository.save(new Question(2, "5 minus 3 to", exam));
        Question thirdQuestion = questionRepository.save(new Question(3, "2 razy 5 to", exam));

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

    private void loadPrivilegesAndRoles() {
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_LECTURER", adminPrivileges);
        createRoleIfNotFound("ROLE_STUDENT", adminPrivileges);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
