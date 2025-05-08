package mk.ukim.finki.web.datamigration.postgres.repository;

import mk.ukim.finki.web.datamigration.postgres.model.PStudentSubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostgresStudentSubjectEnrollmentRepository extends JpaRepository<PStudentSubjectEnrollment, String> {
    Optional<PStudentSubjectEnrollment> findByCourseCodeAndStudent_Index(String courseCode, Long index);
}
