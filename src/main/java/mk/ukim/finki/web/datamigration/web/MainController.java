package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", ""})
@AllArgsConstructor
public class MainController {

    @GetMapping
    public String listAll() {
        return "index";
    }
}
