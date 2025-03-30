package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.repository.PostgresStudentRepository;
import mk.ukim.finki.web.datamigration.sqlserver.repository.SqlServerStudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"", "/"})
@AllArgsConstructor
public class StudentController {
    private final SqlServerStudentRepository sqlServerStudentRepository;
    private final PostgresStudentRepository studentRepository;

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("msstudents", sqlServerStudentRepository.findAll());
        model.addAttribute("pstudents", studentRepository.findAll());

        return "index";
    }
}
