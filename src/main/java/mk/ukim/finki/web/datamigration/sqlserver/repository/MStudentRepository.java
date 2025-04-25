package mk.ukim.finki.web.datamigration.sqlserver.repository;

import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MStudentRepository extends JpaRepository<MStudent, String> {
    List<MStudent> findAllBySemesterId(Long semesterId);
}
