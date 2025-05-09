package mk.ukim.finki.web.datamigration.migration;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web.datamigration.dto.MigrationResult;
import mk.ukim.finki.web.datamigration.postgres.model.PSemester;
import mk.ukim.finki.web.datamigration.postgres.model.PStudent;
import mk.ukim.finki.web.datamigration.postgres.model.PStudentSubjectEnrollment;
import mk.ukim.finki.web.datamigration.postgres.service.PSemesterService;
import mk.ukim.finki.web.datamigration.postgres.service.PStudentService;
import mk.ukim.finki.web.datamigration.postgres.service.PStudentSubjectEnrollmentService;
import mk.ukim.finki.web.datamigration.sqlserver.model.MSemester;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudent;
import mk.ukim.finki.web.datamigration.sqlserver.model.MStudentSubjectEnrollment;
import mk.ukim.finki.web.datamigration.sqlserver.service.MSemesterService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentService;
import mk.ukim.finki.web.datamigration.sqlserver.service.MStudentSubjectEnrollmentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MigrationService {
    private final PStudentService pStudentService;
    private final MStudentService sqlServerStudentService;

    private final PSemesterService postgresSemesterService;
    private final MSemesterService sqlServerSemesterService;

    private final PStudentSubjectEnrollmentService pStudentSubjectEnrollmentService;
    private final MStudentSubjectEnrollmentService msStudentSubjectEnrollmentService;

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
                this.pStudentService.save(destinationStudent);
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

    public MigrationResult migrateSemesters(Long semesterId) {
        int migrated = 0, failed = 0;
        StringBuilder csvBuilder = new StringBuilder();
        List<MSemester> failedSemesters = new ArrayList<>();
        MSemester semester = this.sqlServerSemesterService.getSemesterById(semesterId);

        if (semester != null) {
            try {
                PSemester pSemester = new PSemester();
                pSemester.setId(semester.getId());
                pSemester.setDescription(semester.getDescription());
                pSemester.setStartDate(semester.getStartDate());
                pSemester.setEndDate(semester.getEndDate());
                pSemester.setChStartDate(semester.getChStartDate());
                pSemester.setChEndDate(semester.getChEndDate());
                pSemester.setFacultyId(semester.getFacultyId());
                pSemester.setSemesterTypeId(semester.getSemesterTypeId());
                this.postgresSemesterService.save(pSemester);
                migrated++;
                csvBuilder.append(pSemester.getId())
                        .append(", ")
                        .append(pSemester.getDescription())
                        .append(", ")
                        .append(pSemester.getStartDate())
                        .append(", ")
                        .append(pSemester.getEndDate())
                        .append("\n");

            } catch (DataIntegrityViolationException e) {
                failed++;
                failedSemesters.add(semester);
            } catch (Exception e) {
                failed++;
                failedSemesters.add(semester);
            }
        } else {
            failed++;
        }

        StringWriter csvWriter = getSemesterCsvWriter(failedSemesters);
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

    private static StringWriter getSemesterCsvWriter(List<MSemester> failedSemesters) {
        StringWriter csvWriter = new StringWriter();
        if (!failedSemesters.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(csvWriter)) {
                writer.println("Id,Description,StartDate,EndDate");
                for (MSemester s : failedSemesters) {
                    writer.printf("%d,%s,%s,%s%n",
                            s.getId(), s.getDescription(), s.getStartDate(), s.getEndDate());
                }
            }
        }
        return csvWriter;
    }

    public MigrationResult migrateByCourseCode(String courseCode) {
        int migrated = 0, failed = 0;
        List<MStudentSubjectEnrollment> failedEnrollments = new ArrayList<>();
        StringBuilder csvBuilder = new StringBuilder();
        List<MStudentSubjectEnrollment> enrollments = msStudentSubjectEnrollmentService.findAllByCourseCode(courseCode);
        System.out.println("Found " + enrollments.size() + " enrollments for course code: " + courseCode);

        for (MStudentSubjectEnrollment mEnrollment : enrollments) {
            try {
                if (mEnrollment.getStudent() == null) {
                    System.out.println("Enrollment has null student: " + mEnrollment.getId());
                    failed++;
                    failedEnrollments.add(mEnrollment);
                    continue;
                }

                Long studentIndex = mEnrollment.getStudent().getIndex();
                System.out.println("Processing enrollment for student index: " + studentIndex);

                Optional<PStudent> pStudentOpt = pStudentService.findByIndex(studentIndex);

                if (pStudentOpt.isEmpty()) {
                    System.out.println("Student not found in Postgres: " + studentIndex);
                    failed++;
                    failedEnrollments.add(mEnrollment);
                    continue;
                }

                PStudentSubjectEnrollment pEnrollment = new PStudentSubjectEnrollment();
                pEnrollment.setStudent(pStudentOpt.get());
                pEnrollment.setCourseCode(mEnrollment.getCourseCode());
                pEnrollment.setNumEnrollments(mEnrollment.getNumEnrollments());

                pStudentSubjectEnrollmentService.save(pEnrollment);
                System.out.println("Migrated enrollment for student: " + studentIndex + ", course: " + mEnrollment.getCourseCode());
                migrated++;
                csvBuilder.append(pEnrollment.getId())
                        .append(", ")
                        .append(pEnrollment.getStudent().getIndex())
                        .append(", ")
                        .append(pEnrollment.getCourseCode())
                        .append(", ")
                        .append(pEnrollment.getNumEnrollments() != null ? pEnrollment.getNumEnrollments() : 0)
                        .append("\n");

            } catch (Exception e) {
                System.out.println("Exception while migrating enrollment ID: " + mEnrollment.getId() + " => " + e.getMessage());
                failed++;
                failedEnrollments.add(mEnrollment);
            }
        }

        StringWriter csvWriter = getEnrollmentCsvWriter(failedEnrollments);
        return new MigrationResult(migrated, failed, csvWriter.toString());
    }

    private static StringWriter getEnrollmentCsvWriter(List<MStudentSubjectEnrollment> failedEnrollments) {
        StringWriter csvWriter = new StringWriter();
        if (!failedEnrollments.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(csvWriter)) {
                writer.println("StudentIndex,CourseCode,NumEnrollments");
                for (MStudentSubjectEnrollment e : failedEnrollments) {
                    writer.printf("%d,%s,%d%n",
                            e.getStudent() != null ? e.getStudent().getIndex() : null,
                            e.getCourseCode(),
                            e.getNumEnrollments() != null ? e.getNumEnrollments() : 0);
                }
            }
        }
        return csvWriter;
    }
}
