package mk.ukim.finki.web.datamigration.postgres.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.repository.PostgresStudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PostgresStudentService {
    private final PostgresStudentRepository repository;

    public List<PStudent> findAll() {
        return this.repository.findAll();
    }

    public void save(PStudent student) {
        this.repository.save(student);
        this.repository.flush();
    }

    public Optional<PStudent> findByIndex(Long index) {
        return repository.findByIndex(index);
    }
}
