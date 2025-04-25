package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MigrationResult {
    private int migrated;
    private int failed;
    private String filename;
}
