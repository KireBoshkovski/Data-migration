package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.service.SqlServerStudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping({"", "/"})
@AllArgsConstructor
public class StudentController {
    private final SqlServerStudentService sqlServerStudentService;
    private final PostgresStudentService postgresStudentService;

    @GetMapping
    public String listAll(Model model) {
        List<MStudent> msstudents = this.sqlServerStudentService.findAll();
        List<PStudent> pstudents = this.postgresStudentService.findAll();

        model.addAttribute("msstudents", msstudents);
        model.addAttribute("pstudents", pstudents);

        return "index";
    }
}
