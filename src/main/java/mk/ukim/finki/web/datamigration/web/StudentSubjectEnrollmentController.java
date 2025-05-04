package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentSubjectEnrollmentService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentSubjectEnrollmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/student-subject"})
@AllArgsConstructor
public class StudentSubjectEnrollmentController {
    private final MigrationService migrationService;
    private final PostgresStudentSubjectEnrollmentService postgresStudentSubjectEnrollmentService;
    private final MStudentSubjectEnrollmentService mStudentSubjectEnrollmentService;

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("studentSubjectEnrollments", mStudentSubjectEnrollmentService.findAll());
        model.addAttribute("postgresStudentSubjectEnrollments", postgresStudentSubjectEnrollmentService.findAll());
        return "student-subject-enrollments";
    }

    @PostMapping("/migrate-by-course")
    public String migrateByCourseCode(@RequestParam String courseCode) {
        System.out.println("Received courseCode: " + courseCode);
        migrationService.migrateByCourseCode(courseCode);
        return "redirect:/student-subject";
    }
}
