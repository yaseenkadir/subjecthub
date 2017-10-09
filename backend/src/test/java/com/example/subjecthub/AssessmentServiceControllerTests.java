package com.example.subjecthub;

import com.example.subjecthub.controller.AssessmentServiceController;
import com.example.subjecthub.entity.Assessment;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.exception.ExceptionAdvice;
import com.example.subjecthub.repository.AssessmentRepository;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.testutils.UrlUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.example.subjecthub.testutils.UrlUtils.buildAssessmentApiUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Data needed for tests is initialised in {@link DbInitialiser#run}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // this is used so that database is rolled back between tests.
public class AssessmentServiceControllerTests {

    @Autowired
    private AssessmentServiceController controller;

    @Autowired
    AssessmentRepository assessmentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    UniversityRepository universityRepository;

    private MockMvc mockMvc;

    private University university;
    private Subject subject;

    //Initialise university & subject object
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(new ExceptionAdvice())
            .build();

        this.university = universityRepository.findAll().get(0);
        this.subject = subjectRepository.findByFaculty_University_Id(this.university.getId()).get(0);
    }

    //Add a assessment to subject 1 and check the number of assessments afterwards & check the name of newly added assessment exists
    @Test
    public void testAddAssessment() throws Exception {
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/1/assessments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();

        createAssessment("TestAssignment",  this.subject);

        // Checks that new assessment appears in list
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/1/assessments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("TestAssignment")))
            .andReturn();
    }

    //Check if we have assessments in subject 2 (0 expected as no assessments were created under this subject)
    @Test
    public void testOtherSubject() throws Exception {
        // Tests that data for subject 2 exists.
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/2/assessments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }

    //Create a new assessment and get this assessment by its auto generated assessment id & check the assessment name
    @Test
    public void testGetSingleAssessment() throws Exception {
        Assessment a = createAssessment("TestSingleAssessment", this.subject);

        String assessmentUri = "/api/universities/university/"+ this.university.getId() + "/subjects/subject/" + this.subject.getId() + "/assessments/assessment/" + a.getId();
        mockMvc.perform(get(assessmentUri))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("TestSingleAssessment")))
            .andReturn();
    }

    //Create 2 assessments and add to the subject & get all the assessments matching the assessment name criteria
    @Test
    public void testGetAssessmentsFuzzySearch() throws Exception {
        // Tests basic fuzzy matching, used as an example of behaviour. Tests should change if
        // similarity ratios in FuzzyUtils change.
        String a1Name = "engineering and information technology";
        String a2Name = "engnerring nd niformatin echnlooogy";
        createAssessment(a1Name, this.subject);
        createAssessment(a2Name, this.subject);

        String[] names = {
            a1Name,
            a2Name,
            "EnGiNeErInG aNd InFoRmAtIoN TeChNoLoGy",
            "engineerin adn info rmation tecchnolgy",

            // because partial matching is used, these also match
            "information technology",
            "engineering"
        };

        for(String name : names) {
            mockMvc.perform(get("/api/universities/university/ "+ this.university.getId() + "/subjects/subject/" + this.subject.getId() + "/assessments?name=" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(a1Name)))
                .andExpect(jsonPath("$[1].name", is(a2Name)))
                .andReturn();
        }

        // Check that a non existent subject doesn't match.
        mockMvc.perform(get("/api/universities/university/ "+ this.university.getId() + "/subjects/subject/" + this.subject.getId() + "/assessments?name=samsepiol"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }

    @Test
    public void testGetAssessment404() throws Exception {
        mockMvc.perform(get(buildAssessmentApiUrl(1L, 1L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Assessment not found.")));
    }

    @Test
    public void testGetAssessmentUniversityNotFound404() throws Exception {
        mockMvc.perform(get(buildAssessmentApiUrl(10000L, 1L, 1L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("University not found.")));
    }

    @Test
    public void testGetAssessmentSubjectNotFound404() throws Exception {
        mockMvc.perform(get(buildAssessmentApiUrl(1L, 10000L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Subject not found.")));
    }

/**
     * Util test method that handles extraneous params for creating subject objects.
     */

    private Assessment createAssessment(String name, Subject subject) {
        Assessment assessment = new Assessment();
        assessment.setDescription("TEST - Assessment Description");
        assessment.setGroupWork(true);
        assessment.setLength("1500");
        assessment.setName(name);
        assessment.setSubject(subject);
        assessment.setType(Assessment.AssessmentType.FINAL);
        assessment.setWeighting(50);
        return assessmentRepository.save(assessment);
    }
}
