package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.service.SqlServerStudentService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MigrationService {
    private final PostgresStudentService postgresStudentService;
    private final SqlServerStudentService sqlServerStudentService;

    public void migrateStudents() {

    }
}
