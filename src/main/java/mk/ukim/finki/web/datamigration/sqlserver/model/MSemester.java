package mk.ukim.finki.web.datamigration.sqlserver.model;

import jakarta.persistence.*;

import java.util.Set;

import java.time.LocalDate;

@Entity
@Table(name = "semesters")
public class MSemester {

    @Id
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "semester_type_id")
    private Integer semesterTypeId;

    @Column(name = "faculty_id")
    private Integer facultyId;

    @Column(name = "description")
    private String description;

    @Column(name = "ch_start_date")
    private LocalDate chStartDate;

    @Column(name = "ch_end_date")
    private LocalDate chEndDate;

    @ManyToMany(mappedBy = "semester")
    private Set<MStudent> students;
}
