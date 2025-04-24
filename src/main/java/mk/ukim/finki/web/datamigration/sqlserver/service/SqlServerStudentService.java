package mk.ukim.finki.web.datamigration.sqlserver.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.sqlserver.repository.SqlServerStudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class SqlServerStudentService {
    private final SqlServerStudentRepository repository;

    public List<MStudent> findAll() {
        return this.repository.findAll();
    }

    public MStudent save(MStudent student) {
        return this.repository.save(student);
    }
}
