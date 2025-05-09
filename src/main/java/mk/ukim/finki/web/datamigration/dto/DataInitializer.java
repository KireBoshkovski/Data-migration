package mk.ukim.finki.web.datamigration.dto;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.sqlserver.model.MSemester;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudentSubjectEnrollment;
import mk.ukim.finki.web.datamigration.sqlserver.repository.MSemesterRepository;
import mk.ukim.finki.web.datamigration.sqlserver.repository.MStudentRepository;
import mk.ukim.finki.web.datamigration.sqlserver.repository.MStudentSubjectEnrollmentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer {
    private final MStudentRepository studentRepository;
    private final MSemesterRepository semesterRepository;
    private final MStudentSubjectEnrollmentRepository enrollmentRepository;

    @PostConstruct
    public void seedData() {
        List<MSemester> semesters = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            MSemester semester = new MSemester(
                    (long) i,
                    LocalDate.of(2020 + (i / 2), (i % 2 == 0) ? 3 : 10, 1),
                    LocalDate.of(2020 + (i / 2), (i % 2 == 0) ? 7 : 2, 28),
                    i % 2 == 0 ? 2 : 1,
                    1,
                    "Semester " + i,
                    LocalDate.of(2020 + (i / 2), (i % 2 == 0) ? 3 : 10, 1),
                    LocalDate.of(2020 + (i / 2), (i % 2 == 0) ? 7 : 2, 28),
                    new HashSet<>()
            );
            semesters.add(semester);
        }

        semesterRepository.saveAll(semesters);

        // 2. Create 10 students and link them with all 8 semesters
        List<MStudent> students = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Long index = 200000L + i;

            MStudent student = new MStudent(
                    index,
                    "student" + i + "@example.com",
                    "Surname" + i,
                    "Name" + i,
                    "Father" + i,
                    "INF",
                    2020,
                    new ArrayList<>(semesters)
            );

            students.add(student);
        }

        studentRepository.saveAll(students);
        semesterRepository.saveAll(semesters);

        List<MStudentSubjectEnrollment> enrollments = new ArrayList<>();
        for (MStudent student : students) {
            for (int j = 1; j <= 3; j++) {
                MStudentSubjectEnrollment enrollment = new MStudentSubjectEnrollment();
                enrollment.setStudent(student);
                enrollment.setCourseCode("COURSE" + j);
                enrollment.setNumEnrollments((j % 3) + 1);
                enrollments.add(enrollment);
            }
        }

        enrollmentRepository.saveAll(enrollments);
    }
}
