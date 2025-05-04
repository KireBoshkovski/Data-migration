package mk.ukim.finki.web.datamigration.sqlserver.service;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudentSubjectEnrollment;
import mk.ukim.finki.web.datamigration.sqlserver.repository.MStudentSubjectEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MStudentSubjectEnrollmentService {
    private final MStudentSubjectEnrollmentRepository repository;

    public List<MStudentSubjectEnrollment> findAll() {
        return this.repository.findAll();
    }

    public void save(MStudentSubjectEnrollment enrollment) {
        this.repository.save(enrollment);
        this.repository.flush();
    }

    public List<MStudentSubjectEnrollment> findAllByCourseCode(String courseCode) {
        return this.repository.findAllByCourseCode(courseCode);
    }
}
