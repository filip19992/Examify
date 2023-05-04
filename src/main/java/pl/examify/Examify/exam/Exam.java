package pl.examify.Examify.exam;

import lombok.AllArgsConstructor;
import pl.examify.Examify.question.Question;
import pl.examify.Examify.user.User;
import pl.examify.Examify.user.roles.Role;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "exams")
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany
    @JoinTable(
            name = "exam_users",
            joinColumns = @JoinColumn(
                    name = "exam_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<User> users;

    public Exam() {
    }

    public long getId() {
        return id;
    }

    public Collection<User> getUsers() {
        return users;
    }
}
