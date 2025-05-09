package mk.ukim.finki.web.datamigration.postgres.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PSemester;
import mk.ukim.finki.web.datamigration.postgres.repository.PSemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PSemesterService {
    private final PSemesterRepository repository;

    public List<PSemester> findAll() {
        return repository.findAll();
    }

    public void save(PSemester semester) {
        this.repository.save(semester);
        this.repository.flush();
    }
}
