package mk.ukim.finki.web.datamigration.sqlserver.repository;

import mk.ukim.finki.web.datamigration.sqlserver.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SqlServerStudentRepository extends JpaRepository<Student, String> {
}
