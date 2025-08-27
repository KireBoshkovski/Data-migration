package mk.ukim.finki.web.datamigration.sqlserver.repository;

import mk.ukim.finki.web.datamigration.sqlserver.model.MStudentSubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MStudentSubjectEnrollmentRepository extends JpaRepository<MStudentSubjectEnrollment, String> {
    public List<MStudentSubjectEnrollment> findAllByCourseCode(String courseCode);
}
