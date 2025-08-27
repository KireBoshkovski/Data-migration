package mk.ukim.finki.web.datamigration.sqlserver.repository;

import mk.ukim.finki.web.datamigration.sqlserver.model.MSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MSemesterRepository extends JpaRepository<MSemester, Long> {
}
