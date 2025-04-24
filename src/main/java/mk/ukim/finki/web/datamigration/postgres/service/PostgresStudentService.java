package mk.ukim.finki.web.datamigration.postgres.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.repository.PostgresStudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class PostgresStudentService {
    private final PostgresStudentRepository repository;

    public List<PStudent> findAll() {
        return this.repository.findAll();
    }

    public PStudent save(PStudent student) {
        return this.repository.save(student);
    }
}
