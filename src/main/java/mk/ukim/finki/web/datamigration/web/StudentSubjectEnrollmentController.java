package mk.ukim.finki.web.datamigration.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentSubjectEnrollmentService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentSubjectEnrollmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public String migrateByCourseCode(@RequestParam String courseCode,Model model, HttpSession session) {
        System.out.println("Received courseCode: " + courseCode);
        MigrationResult result = migrationService.migrateByCourseCode(courseCode);
        model.addAttribute("migrated", result.getMigrated());
        model.addAttribute("failed", result.getFailed());
        session.setAttribute("csvContent", result.getCsv());
        return "redirect:/student-subject";
    }

    @GetMapping("/migrate/download-failed")
    public ResponseEntity<byte[]> downloadFailedSubjects(HttpSession session) {
        String content = (String) session.getAttribute("csvContent");
        byte[] bytes = content.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"failed_subjects.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
