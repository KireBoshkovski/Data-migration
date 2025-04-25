package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.sqlserver.service.MSemesterService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping({"", "/"})
@AllArgsConstructor
public class StudentController {
    private final MStudentService sqlServerStudentService;
    private final PostgresStudentService postgresStudentService;
    private final MSemesterService semesterService;
    private final MigrationService migrationService;

    @GetMapping
    public String listAll(Model model) {
        List<MStudent> msstudents = this.sqlServerStudentService.findAll();
        List<PStudent> pstudents = this.postgresStudentService.findAll();

        model.addAttribute("msstudents", msstudents);
        model.addAttribute("pstudents", pstudents);

        return "index";
    }

    @GetMapping("/migrate")
    public String showMigrateStudent(Model model) {
        model.addAttribute("semesters", this.semesterService.getAllSemesters());

        return "migrate-students";
    }

    @PostMapping("/migrate")
    public String migrateStudents(@RequestParam Long semesterId, Model model) {
        MigrationResult result = this.migrationService.migrateStudents(semesterId);

        model.addAttribute("migrated", result.getMigrated());
        model.addAttribute("failed", result.getFailed());
        model.addAttribute("file", result.getFilename());
        model.addAttribute("semesters", this.semesterService.getAllSemesters());

        return "migrate-students";
    }
}
