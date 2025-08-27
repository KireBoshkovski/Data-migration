package mk.ukim.finki.web.datamigration.postgres.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class PStudent {

    @Id
    @Column(name = "[index]", nullable = false)
    private Long index;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "fathers_name", nullable = false)
    private String fathersName;

    @Column(name = "code", nullable = false)
    private String programCode;

    @Column(name = "start_year", nullable = false)
    private Integer startYear;

    @ManyToMany
    @JoinTable(
            name = "student_semester",
            joinColumns = @JoinColumn(name = "student_index"),
            inverseJoinColumns = @JoinColumn(name = "semester_id")
    )
    private List<PSemester> semester;
}
