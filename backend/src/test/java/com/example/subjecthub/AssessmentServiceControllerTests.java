package com.example.subjecthub;

import com.example.subjecthub.controller.AssessmentServiceController;
import com.example.subjecthub.entity.Assessment;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.exception.ExceptionAdvice;
import com.example.subjecthub.repository.AssessmentRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.testutils.TestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.example.subjecthub.testutils.UrlUtils.buildAssessmentApiUrl;
import static com.example.subjecthub.testutils.UrlUtils.buildAssessmentsApiUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    public void testAddAssessment() throws Exception {
        //Add a assessment to subject 1 and check that it exists
        mockMvc.perform(get(buildAssessmentsApiUrl(1L, 1L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();

        createAssessment("TestAssignment",  this.subject);

        // Checks that new assessment appears in list
        mockMvc.perform(get(buildAssessmentsApiUrl(1L, 1L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[1].name", is("TestAssignment")))
            .andReturn();
    }

    @Test
    public void testOtherSubject() throws Exception {
        // Check that we don't have assessments in subject 2
        mockMvc.perform(get(buildAssessmentsApiUrl(1L, 2L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }

    @Test
    public void testGetSingleAssessment() throws Exception {
        Assessment a = createAssessment("TestSingleAssessment", subject);

        // Create a new assessment and check that it can be fetched
        mockMvc.perform(get(buildAssessmentApiUrl(1L, 1L, a.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("TestSingleAssessment")))
            .andReturn();
    }

    //Create 2 assessments and add to the subject & get all the assessments matching the assessment name criteria
    @Test
    public void testGetAssessmentsFuzzySearch() throws Exception {
        // TODO: Fuzzy matching should be a "/search" endpoint instead of on a list endpoint.
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
            mockMvc.perform(get(buildAssessmentsApiUrl(1L, 1L) + "?name=" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(a1Name)))
                .andExpect(jsonPath("$[1].name", is(a2Name)))
                .andReturn();
        }

        // Check that a non existent subject doesn't match.
        mockMvc.perform(get(buildAssessmentsApiUrl(1L, 1L) + "?name=samsepiol"))
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

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testDeleteAssessment() throws Exception {
        Assessment a = createAssessment("Delete Assessment", subject);
        String assessmentUrl = buildAssessmentApiUrl(university.getId(), subject.getId(), a.getId());
        mockMvc.perform(delete(assessmentUrl))
            .andExpect(status().isOk());

        Assert.assertNull(assessmentRepository.findOne(a.getId()));

        mockMvc.perform(get(assessmentUrl))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Assessment not found.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantDeleteAssessment() throws Exception {
        Assessment a = createAssessment("Delete Assessment", subject);
        String assessmentUrl = buildAssessmentApiUrl(university.getId(), subject.getId(), a.getId());
        mockMvc.perform(delete(assessmentUrl))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditAssessment() throws Exception {
        // Renames an assessment and checks that the name is in the assessments list.
        Assessment editedAssessment = assessmentRepository.findOne(1L);
        editedAssessment.setName("Edited Test");

        mockMvc
            .perform(put(buildAssessmentApiUrl(1L, 1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(editedAssessment)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Edited Test")))
            .andExpect(jsonPath("$.description", is("A test with questions.")));

        mockMvc.perform(get(buildAssessmentsApiUrl(1L, 1L)))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Edited Test")))
            .andExpect(jsonPath("$[0].description", is("A test with questions.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditAssessmentIdsDontMatch() throws Exception {
        Assessment assessment = new Assessment();
        assessment.setId(2L);

        mockMvc
            .perform(put(buildAssessmentApiUrl(1L, 1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(assessment)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Payload assessmentId and URL path assessmentId do not match.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantEditAssessment() throws Exception {
        Assessment assessment = new Assessment();

        mockMvc
            .perform(put(buildAssessmentApiUrl(1L, 1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(assessment)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateAssessment() throws Exception {
        Assessment assessment = new Assessment();
        assessment.setName("Created Assessment");
        assessment.setType(Assessment.AssessmentType.PROJECT);
        assessment.setGroupWork(true);
        assessment.setLength("5000 LoC");
        assessment.setDescription("A software project");
        assessment.setWeighting(50);

        MvcResult result = mockMvc
            .perform(post(buildAssessmentsApiUrl(1L, 1L) + "/assessment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(assessment)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Created Assessment")))
            .andExpect(jsonPath("$.type", is("PROJECT")))
            .andExpect(jsonPath("$.groupWork", is(true)))
            .andExpect(jsonPath("$.length", is("5000 LoC")))
            .andExpect(jsonPath("$.description", is("A software project")))
            .andExpect(jsonPath("$.weighting", is(50)))
            .andReturn();

        JsonNode jsonNode = TestUtils.fromString(result.getResponse().getContentAsString());
        Long newId = jsonNode.get("id").asLong();

        mockMvc.perform(get(buildAssessmentsApiUrl(1L, 1L)))
            .andExpect(jsonPath("$", hasSize(2)))
            // We know it exists from before, so we only check a couple of fields.
            .andExpect(jsonPath("$[1].id", is(newId.intValue())))
            .andExpect(jsonPath("$[1].name", is("Created Assessment")))
            .andExpect(jsonPath("$[1].description", is("A software project")))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateAssessmentFailsWithIdSupplied() throws Exception {
        Assessment assessment = new Assessment();
        assessment.setId(1L);

        mockMvc
            .perform(post(buildAssessmentsApiUrl(1L, 1L) + "/assessment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(assessment)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Cannot specify id for assessment creation.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantCreateAssessment() throws Exception {
        Assessment assessment = new Assessment();

        mockMvc
            .perform(post(buildAssessmentsApiUrl(1L, 1L) + "/assessment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(assessment)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
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
