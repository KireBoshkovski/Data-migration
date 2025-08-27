package mk.ukim.finki.web.datamigration.postgres.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
        name = "student_subject_enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_index", "course_code"})
)
@Data
public class PStudentSubjectEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_index", referencedColumnName = "[index]")
    private PStudent student;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "num_enrollments")
    private Integer numEnrollments;
}
