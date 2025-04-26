package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MigrationService {
    private final PostgresStudentService postgresStudentService;
    private final MStudentService sqlServerStudentService;

    public MigrationResult migrateStudents(Long semesterId) {
        int migrated = 0, failed = 0;
        List<MStudent> students = this.sqlServerStudentService.findBySemesterId(semesterId);
        List<MStudent> failedStudents = new ArrayList<>();
        for (MStudent student : students) {
            System.out.println("Student: " + student.getName() + " is migrating");
            try {
                PStudent destinationStudent = new PStudent();
                destinationStudent.setIndex(student.getIndex());
                destinationStudent.setEmail(student.getEmail());
                destinationStudent.setName(student.getName());
                destinationStudent.setSurname(student.getSurname());
                destinationStudent.setFathersName(student.getFathersName());
                destinationStudent.setProgramCode(student.getProgramCode());
                destinationStudent.setStartYear(student.getStartYear());
                this.postgresStudentService.save(destinationStudent);
                System.out.println("Student: " + destinationStudent + " successfully migrated!");
                migrated++;
            } catch (DataIntegrityViolationException e) {
                failed++;
                failedStudents.add(student);
                System.err.println(e.getMessage());
                System.out.printf("Student %d failed%n", student.getIndex());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.printf("Student %d failed%n", student.getIndex());
                failedStudents.add(student);
                failed++;
            }
        }
        StringWriter csvWriter = getStringWriter(failedStudents);

        System.out.println("========= MIGRATION FINISHED: successful: " + migrated + " failed: " + failed + " =========");

        return new MigrationResult(migrated, failed, csvWriter.toString());
    }

    private static StringWriter getStringWriter(List<MStudent> failedStudents) {
        StringWriter csvWriter = new StringWriter();
        if (!failedStudents.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(csvWriter)) {
                writer.println("Index,Email,Name,Surname,FathersName,ProgramCode");
                for (MStudent s : failedStudents) {
                    writer.printf("%s,%s,%s,%s,%s,%s%n",
                            s.getIndex(), s.getEmail(), s.getName(), s.getSurname(), s.getFathersName(), s.getProgramCode());
                }
            }
        }
        return csvWriter;
    }
}
