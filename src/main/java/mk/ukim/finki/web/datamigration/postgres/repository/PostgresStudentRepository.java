package mk.ukim.finki.web.datamigration.postgres.repository;

import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresStudentRepository extends JpaRepository<PStudent, String> {
}
