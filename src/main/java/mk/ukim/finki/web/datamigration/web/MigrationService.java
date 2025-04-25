package mk.ukim.finki.web.datamigration.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.service.PostgresStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MigrationService {
    private final PostgresStudentService postgresStudentService;
    private final MStudentService sqlServerStudentService;

    @Transactional
    public MigrationResult migrateStudents(Long semesterId) {
        int migrated = 0, failed = 0;
        List<MStudent> students = this.sqlServerStudentService.findBySemesterId(semesterId);
        List<MStudent> failedStudents = new ArrayList<>();
        for (MStudent student : students) {
            try {
                PStudent destinationStudent = new PStudent();
                destinationStudent.setIndex(student.getIndex());
                destinationStudent.setEmail(student.getEmail());
                destinationStudent.setName(student.getName());
                destinationStudent.setSurname(student.getSurname());
                destinationStudent.setFathersName(student.getFathersName());
                destinationStudent.setProgramCode(student.getProgramCode());
                this.postgresStudentService.save(destinationStudent);
                migrated++;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.printf("Student %d failed%n", student.getIndex());
                failedStudents.add(student);
                failed++;
            }
        }
        String csvFileName = null;
        if (!failedStudents.isEmpty()) {
            csvFileName = "failed_students_" + UUID.randomUUID() + ".csv";
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/static/" + csvFileName))) {
                writer.println("Index,Email,Name,Surname,FathersName,ProgramCode");
                for (MStudent s : failedStudents) {
                    writer.printf("%s,%s,%s,%s,%s,%s%n",
                            s.getIndex(), s.getEmail(), s.getName(), s.getSurname(), s.getFathersName(), s.getProgramCode());
                }
            } catch (IOException e) {
                System.err.println("Could not write CSV: " + e.getMessage());
            }
        }

        System.out.println("========= MIGRATION FINISHED: successful: " + migrated + " failed: " + failed + " =========");

        return new MigrationResult(migrated, failed, csvFileName);
    }
}
