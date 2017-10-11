package com.example.subjecthub;

import com.example.subjecthub.entity.Assessment;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.AssessmentRepository;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.SubjectCommentRepository;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialises the test database with test data.
 */
@Component
public class DbInitialiser implements ApplicationRunner {

    private UniversityRepository universityRepository;
    private FacultyRepository facultyRepository;
    private SubjectRepository subjectRepository;
    private SubjectHubUserRepository subjectHubUserRepository;
    private AssessmentRepository assessmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DbInitialiser(
        UniversityRepository universityRepository,
        FacultyRepository facultyRepository,
        SubjectRepository subjectRepository,
        SubjectHubUserRepository subjectHubUserRepository,
        AssessmentRepository assessmentRepository
    ) {
        this.universityRepository = universityRepository;
        this.facultyRepository = facultyRepository;
        this.subjectRepository = subjectRepository;
        this.subjectHubUserRepository = subjectHubUserRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /* First university */
        University university1 = new University("University of Testing", "UoT");
        university1 = universityRepository.save(university1);

        Faculty u1Faculty1 = new Faculty("Faculty of Testing", "FoT", university1);
        u1Faculty1 = facultyRepository.save(u1Faculty1);

        // Variable naming scheme is u{university}f{faculty}s{subject}
        Subject u1f1Subject1 = new Subject();
        u1f1Subject1.setName("Introduction to Testing");
        u1f1Subject1.setCode("1234");
        u1f1Subject1.setCreditPoints(10);
        u1f1Subject1.setDescription("Introduces students to basic testing methodoligies");
        u1f1Subject1.setMinRequirements("Test coverage >= 70%.");
        u1f1Subject1.setRating(8.3);
        u1f1Subject1.setNumRatings(27);
        u1f1Subject1.setUndergrad(true);
        u1f1Subject1.setPostgrad(true);
        u1f1Subject1.setAutumn(false);
        u1f1Subject1.setSpring(true);
        u1f1Subject1.setSummer(false);
        u1f1Subject1.setFaculty(u1Faculty1);
        u1f1Subject1 = subjectRepository.save(u1f1Subject1);

        Assessment u1f1s1assessment1 = new Assessment();
        u1f1s1assessment1.setSubject(u1f1Subject1);
        u1f1s1assessment1.setDescription("A test with questions.");
        u1f1s1assessment1.setName("Test 1");
        u1f1s1assessment1.setGroupWork(false);
        u1f1s1assessment1.setLength("60 minutes");
        u1f1s1assessment1.setType(Assessment.AssessmentType.TEST);
        u1f1s1assessment1.setWeighting(20);
        u1f1s1assessment1 = assessmentRepository.save(u1f1s1assessment1);

        Subject u1f1Subject2 = new Subject();
        u1f1Subject2.setName("Advanced Testing");
        u1f1Subject2.setCode("1235");
        u1f1Subject2.setCreditPoints(10);
        u1f1Subject2.setDescription("Introduces phd students to advanced testing methodoligies");
        u1f1Subject2.setMinRequirements("Test coverage >= 90%.");
        u1f1Subject2.setRating(6.0);
        u1f1Subject2.setNumRatings(3);
        u1f1Subject2.setUndergrad(false);
        u1f1Subject2.setPostgrad(true);
        u1f1Subject2.setAutumn(true);
        u1f1Subject2.setSpring(false);
        u1f1Subject2.setSummer(false);
        u1f1Subject2.setFaculty(u1Faculty1);
        u1f1Subject2 = subjectRepository.save(u1f1Subject2);


        /* Second university */
        University university2 = new University("College of Testing", "CoT");
        university2 = universityRepository.save(university2);

        Faculty u2Faculty1 = new Faculty("Business Faculty", "BF", university2);
        u2Faculty1 = facultyRepository.save(u2Faculty1);

        Subject u2f1Subject1 = new Subject();
        u2f1Subject1.setName("Accounting or something");
        u2f1Subject1.setCode("B1003");
        u2f1Subject1.setCreditPoints(4);
        u2f1Subject1.setDescription("Teaches students about accounting or something like that.");
        u2f1Subject1.setMinRequirements("Test coverage >= 70%.");
        u2f1Subject1.setRating(3.2);
        u2f1Subject1.setNumRatings(5);
        u2f1Subject1.setUndergrad(true);
        u2f1Subject1.setPostgrad(true);
        u2f1Subject1.setAutumn(true);
        u2f1Subject1.setSpring(true);
        u2f1Subject1.setSummer(false);
        u2f1Subject1.setFaculty(u2Faculty1);
        u2f1Subject1 = subjectRepository.save(u2f1Subject1);

        String hashedPassword = passwordEncoder.encode("testpassword");
        SubjectHubUser user = new SubjectHubUser("testuser", hashedPassword, "test@example.com");
        user = subjectHubUserRepository.save(user);

        String adminPassword = passwordEncoder.encode("adminpassword");
        SubjectHubUser admin = new SubjectHubUser("admin", adminPassword, "admin@example.com",
            true);
        admin = subjectHubUserRepository.save(admin);
    }
}
