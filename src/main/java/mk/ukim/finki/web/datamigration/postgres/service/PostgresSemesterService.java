package mk.ukim.finki.web.datamigration.postgres.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PSemester;
import mk.ukim.finki.web.datamigration.postgres.repository.PostgresSemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostgresSemesterService {
    private final PostgresSemesterRepository repository;

    public List<PSemester> findAll() {
        return repository.findAll();
    }

    public void save(PSemester semester) {
        this.repository.save(semester);
        this.repository.flush();
    }
}
