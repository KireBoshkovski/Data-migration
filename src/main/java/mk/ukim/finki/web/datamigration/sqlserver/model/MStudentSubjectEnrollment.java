package mk.ukim.finki.web.datamigration.sqlserver.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_subject_enrollment")
@Data
public class MStudentSubjectEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_index", referencedColumnName = "[index]")
    private MStudent student;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "num_enrollments")
    private Integer numEnrollments;
}
