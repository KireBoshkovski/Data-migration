package mk.ukim.finki.web.datamigration.sqlserver.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.sqlserver.model.MSemester;
import mk.ukim.finki.web.datamigration.sqlserver.repository.MSemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MSemesterService {
    private final MSemesterRepository repository;

    public List<MSemester> getAllSemesters() {
        return this.repository.findAll();
    }

    public MSemester getSemesterById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
