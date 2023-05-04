package pl.examify.Examify.exam;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "student_exam")
@NoArgsConstructor
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "student_name")
    private String studentUsername;

    public ExamAttempt(String studentUsername) {
        this.studentUsername = studentUsername;
    }
}
