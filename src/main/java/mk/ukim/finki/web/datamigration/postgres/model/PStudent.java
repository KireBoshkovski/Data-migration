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
@Table(name = "[Student]")
public class PStudent {

    @Id
    @Column(name = "[index]")
    private Long index;

    @Column(name = "email")
    private String email;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "fathers_name")
    private String fathersName;

    @Column(name = "code")
    private String programCode;

    @Column(name = "start_year")
    private Integer startYear;

    @ManyToMany
    @JoinTable(
            name = "student_semester",
            joinColumns = @JoinColumn(name = "student_index"),
            inverseJoinColumns = @JoinColumn(name = "semester_id")
    )
    private List<PSemester> semester;
}
