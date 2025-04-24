package mk.ukim.finki.web.datamigration.sqlserver.repository;

import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlServerStudentRepository extends JpaRepository<MStudent, String> {
}
