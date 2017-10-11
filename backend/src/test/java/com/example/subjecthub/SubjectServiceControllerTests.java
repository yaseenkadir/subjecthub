package com.example.subjecthub;

import com.example.subjecthub.controller.SubjectServiceController;

import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.Tag;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.exception.ExceptionAdvice;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.TagRepository;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.testutils.EntityUtils;
import com.example.subjecthub.testutils.TestUtils;


import com.example.subjecthub.entity.*;
import com.example.subjecthub.repository.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.subjecthub.testutils.UrlUtils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * Data needed for tests is initialised in {@link DbInitialiser#run}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // this is used so that database is rolled back between tests.
public class SubjectServiceControllerTests {

    @Autowired
    private SubjectServiceController controller;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    SubjectCommentRepository subjectCommentRepository;

    @Autowired
    SubjectHubUserRepository subjectHubUserRepository;

    @Autowired
    EntityUtils entityUtils;

    private MockMvc mockMvc;

    private University testUniversity;
    private Faculty testFaculty;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(new ExceptionAdvice())
            .build();

        this.testUniversity = universityRepository.findOne(1L);
        this.testFaculty = facultyRepository.findByUniversityId(testUniversity.getId()).get(0);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddNewTagToSubjectThatExists() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(
                post(buildSubjectApiUrl(1L, 1L) + "/addTag")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"BAD_AUTHORITY"})
    public void testAddNewTagWithoutAuthority() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(
                post(buildSubjectApiUrl(1L, 1L) + "/addTag")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddNewTagToSubjectThatDoesNotExist() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 10000L) + "/addTag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddExistingTagToSubjectThatExists() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 1L) + "/addTag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(get(buildSubjectApiUrl(1L, 1L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tags[0].name", is("Test Tag")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddExistingTagToSubjectThatDoesNotExist() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 1L) +"/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 10000L) +"/addTag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddExistingTagToSubjectThatAlreadyHasTag() throws Exception {
        Tag tag = new Tag("Test Tag");

        String subjectUrl = buildSubjectApiUrl(1L, 1L);
        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(subjectUrl + "/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post(subjectUrl + "/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message", is("Tag already exists for subject.")));
    }

    @Test
    public void testAddSubject() throws Exception {
        String subjectsUrl = buildSubjectsApiUrl(1L);
        mockMvc.perform(get(subjectsUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        createSubject("Test", "abcxyz");

        // Checks that new subject appears in list
        mockMvc.perform(get(subjectsUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Test")))
            .andReturn();
    }

    @Test
    public void testOtherUni() throws Exception {
        // Tests that data for university2 exists.
        mockMvc.perform(get(buildSubjectsApiUrl(2L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Accounting or something")))
            .andExpect(jsonPath("$[0].code", is("B1003")))
            .andReturn();
    }

    @Test
    public void testGetSingleSubject() throws Exception {
        // Creates a subject and checks that it can be fetched.
        Subject s = createSubject("test", "ABCDEF");

        mockMvc.perform(get(buildSubjectApiUrl(1L, s.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("test")))
            .andExpect(jsonPath("$.code", is("ABCDEF")))
            .andReturn();
    }

    @Test
    public void testGetSubjectsFuzzySearch() throws Exception {
        // Tests basic fuzzy matching, used as an example of behaviour. Tests should change if
        // similarity ratios in FuzzyUtils change.
        String s1Name = "engineering and information technology";
        String s2Name = "engnerring nd niformatin echnlooogy";
        createSubject(s1Name, "123456");
        createSubject(s2Name, "123457");

        String[] names = {
            s1Name,
            s2Name,
            "EnGiNeErInG aNd InFoRmAtIoN TeChNoLoGy",
            "engineerin adn info rmation tecchnolgy",

            // because partial matching is used, these also match
            "information technology",
            "engineering"
        };

        String subjectsUrl = buildSubjectsApiUrl(1L);
        for(String name : names) {
            mockMvc.perform(get(subjectsUrl + "?name=" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(s1Name)))
                .andExpect(jsonPath("$[1].name", is(s2Name)))
                .andReturn();
        }

        // Check that a non existent subject doesn't match.
        mockMvc.perform(get(subjectsUrl + "?name=samsepiol"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testAdminCanDeleteSubject() throws Exception {
        Subject s = createSubject("Subject To Delete", "S2D");
        String subjectsUrl = buildSubjectsApiUrl(testUniversity.getId());

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Subject To Delete")))
            .andReturn();

        mockMvc
            .perform(delete(buildSubjectApiUrl(testUniversity.getId(), s.getId())))
            .andExpect(status().isOk());

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser
    public void testStudentCantDeleteSubject() throws Exception {
        String subjectsUrl = buildSubjectsApiUrl(testUniversity.getId());
        Subject s = createSubject("Subject To Delete", "S2D");

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Subject To Delete")))
            .andReturn();

        mockMvc
            .perform(delete(subjectsUrl + "/subject/" + s.getId()))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    public void testGetSubject404() throws Exception {
        mockMvc.perform(get(buildSubjectApiUrl(1L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Subject not found.")))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditSubject() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);

        Subject subject = subjectRepository.findOne(1L);
        subject.setName("Renamed Subject");
        subject.setFaculty(faculty);
        subject.getAssessments().clear();

        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            // Subject should be renamed but other fields should remain the same.
            .andExpect(jsonPath("$.name", is("Renamed Subject")))
            .andExpect(jsonPath("$.code", is("1234")));

        mockMvc.perform(get(buildSubjectsApiUrl(1L)))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Renamed Subject")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditSubjectIdsDontMatch() throws Exception {
        Subject subject = subjectRepository.findOne(1L);
        subject.setId(2L);

        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Payload subjectId and URL path subjectId do not match.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantEditSubject() throws Exception {
        Subject subject = new Subject();

        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testValidateSubjectPayloadFacultyIdRequired() throws Exception {
        Subject subject = subjectRepository.findOne(1L);
        subject.setFaculty(null);

        // Test without faculty entity
        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Must include a nested faculty in the request with an associated facultyId.")));

        Faculty f = new Faculty("Faculty 1", "faculty", universityRepository.findOne(1L));
        f.setId(null);
        subject.setFaculty(f);

        // Test with faculty, but missing id
        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Must include a nested faculty in the request with an associated facultyId.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testValidateSubjectPayloadNoAssessments() throws Exception {
        // Test that you can't edit a subject if you supply assessments
        Subject subject = subjectRepository.findOne(1L);

        subject.getAssessments().add(new Assessment());
        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Cannot specify assessments in payload.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testValidateSubjectPayloadNoComments() throws Exception {
        // Test that you can't edit a subject if you supply comments
        Subject subject = subjectRepository.findOne(1L);
        subject.getAssessments().clear();

        subject.getComments().add(new SubjectComment());
        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Cannot specify comments in payload.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testValidateSubjectPayloadNoTags() throws Exception {
        // Test that you can't edit a subject if you supply tags
        Subject subject = subjectRepository.findOne(1L);

        subject.getTags().add(new Tag());
        mockMvc
            .perform(put(buildSubjectApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Cannot specify tags in payload.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateSubject() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        // Setting only the facultyId, so that we can verify that only the nested facultyId is
        // required when creating a Subject.

        Subject subject = new Subject();
        subject.setFaculty(faculty);
        subject.setName("Created Subject");
        subject.setCode("ABCDE");
        subject.setDescription("Desc");
        subject.setMinRequirements("Min req");
        subject.setCreditPoints(1);
        subject.setAutumn(true);
        subject.setSpring(true);
        subject.setSummer(false);
        subject.setUndergrad(true);
        subject.setPostgrad(true);

        MvcResult result = mockMvc
            .perform(post(buildSubjectsApiUrl(1L) + "/subject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Created Subject")))
            .andExpect(jsonPath("$.code", is("ABCDE")))
            .andExpect(jsonPath("$.description", is("Desc")))
            .andExpect(jsonPath("$.minRequirements", is("Min req")))
            .andExpect(jsonPath("$.creditPoints", is(1)))
            .andExpect(jsonPath("$.autumn", is(true)))
            .andExpect(jsonPath("$.spring", is(true)))
            .andExpect(jsonPath("$.summer", is(false)))
            .andExpect(jsonPath("$.undergrad", is(true)))
            .andExpect(jsonPath("$.postgrad", is(true)))
            .andExpect(jsonPath("$.faculty.id", is(1)))
            .andExpect(jsonPath("$.faculty.name", is("Faculty of Testing")))
            .andReturn();

        JsonNode jsonNode = TestUtils.fromString(result.getResponse().getContentAsString());
        Long newId = jsonNode.get("id").asLong();

        mockMvc.perform(get(buildSubjectsApiUrl(1L)))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].id", is(newId.intValue())))
            .andExpect(jsonPath("$[2].name", is("Created Subject")))
            .andExpect(jsonPath("$[2].code", is("ABCDE")))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateSubjectFailsWithIdSupplied() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);

        mockMvc
            .perform(post(buildSubjectsApiUrl(1L) + "/subject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(subject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Cannot specify id for subject creation.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantCreateSubject() throws Exception {
        mockMvc
            .perform(post(buildSubjectsApiUrl(1L) + "/subject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(new Subject())))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditSubjectDontLoseChildren() throws Exception {
        Subject subject = entityUtils.createSubject("Test Sub", "TSUB", testFaculty);
        Assessment a1 = entityUtils.createAssessment("Assessment 1", subject);
        Assessment a2 = entityUtils.createAssessment("Assessment 2", subject);
        subject.setAssessments(new ArrayList<>(Arrays.asList(a1, a2)));

        mockMvc.perform(get(buildSubjectApiUrl(1L, subject.getId())))
            .andExpect(jsonPath("$.name", is("Test Sub")))
            .andExpect(jsonPath("$.code", is("TSUB")))
            .andExpect(jsonPath("$.assessments[0].name", is("Assessment 1")))
            .andExpect(jsonPath("$.assessments[1].name", is("Assessment 2")))
            .andExpect(jsonPath("$.assessments", hasSize(2)));

        Subject copy = new Subject();
        BeanUtils.copyProperties(subject, copy);
        copy.setAssessments(null);
        copy.setComments(null);
        copy.setTags(null);
        copy.setName("Renamed Test Subject");

        mockMvc
            .perform(put(buildSubjectApiUrl(1L, subject.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(copy)))
            .andExpect(status().isOk())
            // Check name is different but other things still the same
            .andExpect(jsonPath("$.name", is("Renamed Test Subject")))
            .andExpect(jsonPath("$.id", is(subject.getId().intValue())))
            .andExpect(jsonPath("$.code", is("TSUB")))
            .andExpect(jsonPath("$.assessments[0].name", is("Assessment 1")))
            .andExpect(jsonPath("$.assessments[1].name", is("Assessment 2")))
            .andExpect(jsonPath("$.assessments", hasSize(2)));
    }

    /**
     * Util test method that handles extraneous params for creating subject objects.
     */
    private Subject createSubject(String name, String code) {
        return entityUtils.createSubject(name, code, testFaculty);
    }
}
