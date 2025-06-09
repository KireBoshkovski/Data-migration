package mk.ukim.finki.web.datamigration.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.dto.MigrationResult;
import mk.ukim.finki.web.datamigration.migration.MigrationService;
import mk.ukim.finki.web.datamigration.postgres.service.PStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MSemesterService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentService;
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
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {
    private final MStudentService sqlServerStudentService;
    private final PStudentService pStudentService;
    private final MSemesterService mSemesterService;
    private final MigrationService migrationService;

    @GetMapping()
    public String showMigrateStudent(Model model) {
        model.addAttribute("semesters", this.mSemesterService.getAllSemesters());
        model.addAttribute("mstudents", this.sqlServerStudentService.findAll());
        model.addAttribute("pstudents", this.pStudentService.findAll());
        return "migrate-students";
    }

    @PostMapping()
    public String migrateStudents(@RequestParam Long semesterId, Model model, HttpSession session) {
        MigrationResult result = this.migrationService.migrateStudents(semesterId);

        model.addAttribute("migrated", result.getMigrated());
        model.addAttribute("failed", result.getFailed());
        model.addAttribute("mstudents", this.sqlServerStudentService.findAll());
        model.addAttribute("pstudents", this.pStudentService.findAll());
        session.setAttribute("csvContent", result.getCsv());
        model.addAttribute("semesters", this.mSemesterService.getAllSemesters());
        return "migrate-students";
    }

    @GetMapping("/download-failed")
    public ResponseEntity<byte[]> downloadFailedStudents(HttpSession session) {
        String content = (String) session.getAttribute("csvContent");
        byte[] bytes = content.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"failed_students.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
