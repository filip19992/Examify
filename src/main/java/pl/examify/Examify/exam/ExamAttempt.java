package pl.examify.Examify.exam;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "exam_result")
@NoArgsConstructor
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "student_name")
    private String studentUsername;

    @Column(name = "result")
    private double result;

    public ExamAttempt(String studentUsername, double result) {
        this.studentUsername = studentUsername;
        this.result = result;
    }
}
