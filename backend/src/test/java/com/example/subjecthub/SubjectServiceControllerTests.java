package com.example.subjecthub;

import com.example.subjecthub.controller.SubjectServiceController;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.UniversityRepository;
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
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;

    private MockMvc mockMvc;

    private University university;
    private Faculty faculty;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        this.university = universityRepository.findAll().get(0);
        this.faculty = facultyRepository.findByUniversityId(university.getId()).get(0);
    }

    @Test
    public void testAddSubject() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/universities/university/1/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        createSubject("Test", "abcxyz");

        // Checks that new subject appears in list
        mockMvc.perform(get("/api/universities/university/1/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Test")))
            .andReturn();
    }

    @Test
    public void testOtherUni() throws Exception {
        // Tests that data for university2 exists.
        mockMvc.perform(get("/api/universities/university/2/subjects"))
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

        String subjectUri = "/api/universities/university/1/subjects/subject/" + s.getId();
        MvcResult result = mockMvc.perform(get(subjectUri))
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

        for(String name : names) {
            mockMvc.perform(get("/api/universities/university/1/subjects?name=" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(s1Name)))
                .andExpect(jsonPath("$[1].name", is(s2Name)))
                .andReturn();
        }

        // Check that a non existent subject doesn't match.
        mockMvc.perform(get("/api/universities/university/1/subjects?name=samsepiol"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }

    /**
     * Util test method that handles extraneous params for creating subject objects.
     */
    private Subject createSubject(String name, String code) {
        Subject testSubject = new Subject();
        testSubject.setName(name);
        testSubject.setCode(code);
        testSubject.setDescription("desc");
        testSubject.setFaculty(faculty);
        testSubject.setSummer(true);
        testSubject.setSpring(false);
        testSubject.setAutumn(true);
        testSubject.setMinRequirements("min requirements");
        testSubject.setRating(1);
        testSubject.setNumRatings(1);
        testSubject.setCreditPoints(1);
        testSubject.setUndergrad(true);
        testSubject.setPostgrad(true);
        return subjectRepository.save(testSubject);
    }
}
