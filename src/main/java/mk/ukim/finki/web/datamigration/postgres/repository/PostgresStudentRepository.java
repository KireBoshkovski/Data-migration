package mk.ukim.finki.web.datamigration.postgres.repository;

import mk.ukim.finki.web.datamigration.postgres.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresStudentRepository extends JpaRepository<Student, String> {
}
