package mk.ukim.finki.web.datamigration.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PSemester;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresSemesterService;
import mk.ukim.finki.web.datamigration.sqlserver.model.MSemester;
import mk.ukim.finki.web.datamigration.sqlserver.service.MSemesterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"/semester"})
@AllArgsConstructor
public class SemesterController {
    private final MSemesterService sqlServerSemesterService;
    private final PostgresSemesterService postgresServerSemesterService;
    private final MSemesterService mSemesterService;
    private final MigrationService migrationService;

    @GetMapping()
    public String listAll(Model model) {
        List<MSemester> mssemesters = this.sqlServerSemesterService.getAllSemesters();
        List<PSemester> psemesters = this.postgresServerSemesterService.findAll();

        model.addAttribute("mssemesters", mssemesters);
        model.addAttribute("psemesters", psemesters);

        return "semesters";
    }

    @GetMapping("/migrate-semesters")
    public String showMigrateSemesters(Model model) {
        List<MSemester> mssemesters = mSemesterService.getAllSemesters();
        List<PSemester> psemesters = postgresServerSemesterService.findAll();

        model.addAttribute("mssemesters", mssemesters);
        model.addAttribute("psemesters", psemesters);
        return "migrate-semesters";
    }

    @PostMapping("/migrate-semesters")
    public String migrateSemesters(@RequestParam Long semesterId, RedirectAttributes redirectAttributes, HttpSession session) {
        MigrationResult result = migrationService.migrateSemesters(semesterId);

        redirectAttributes.addFlashAttribute("migrated", result.getMigrated());
        redirectAttributes.addFlashAttribute("failed", result.getFailed());
        session.setAttribute("csvContent", result.getCsv());

        return "redirect:/semester/migrate-semesters";
    }


    @GetMapping("/migrate-semesters/download-failed")
    public ResponseEntity<byte[]> downloadFailedSemesters(HttpSession session) {
        String content = (String) session.getAttribute("csvContent");
        byte[] bytes = content.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"failed_semesters.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
