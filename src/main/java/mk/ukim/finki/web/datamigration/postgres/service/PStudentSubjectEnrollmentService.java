package mk.ukim.finki.web.datamigration.postgres.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudentSubjectEnrollment;
import mk.ukim.finki.web.datamigration.postgres.repository.PStudentSubjectEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PStudentSubjectEnrollmentService {
    private final PStudentSubjectEnrollmentRepository repository;

    public void save(PStudentSubjectEnrollment enrollment) {
        this.repository.save(enrollment);
        this.repository.flush();
    }

    public List<PStudentSubjectEnrollment> findAll(){
        return repository.findAll();
    }

    public Optional<PStudentSubjectEnrollment> findByStudentAndCourseCode(Long index, String courseCode) {
        return repository.findByCourseCodeAndStudent_Index(courseCode, index);
    }
}
