package mk.ukim.finki.web.datamigration.postgres.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudentSubjectEnrollment;
import mk.ukim.finki.web.datamigration.postgres.repository.PostgresStudentSubjectEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostgresStudentSubjectEnrollmentService {
    private final PostgresStudentSubjectEnrollmentRepository repository;

    public void save(PStudentSubjectEnrollment enrollment) {
        this.repository.save(enrollment);
        this.repository.flush();
    }

    public List<PStudentSubjectEnrollment> findAll(){
        return repository.findAll();
    }
}
