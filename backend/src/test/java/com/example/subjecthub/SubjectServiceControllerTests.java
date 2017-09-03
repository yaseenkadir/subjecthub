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

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();
    }

    @Test
    public void testAddSubject() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/universities/1/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        University u1 = universityRepository.findAll().get(0);
        Faculty u1f1 = facultyRepository.findByUniversityId(u1.getId()).get(0);
        createSubject(u1f1, "Test", "abcxyz");

        // Checks that new subject appears in list
        result = mockMvc.perform(get("/api/universities/1/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Test")))
            .andReturn();
    }

    @Test
    public void testOtherUni() throws Exception {

        // Tests that data for university2 exists.
        MvcResult result = mockMvc.perform(get("/api/universities/2/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Accounting or something")))
            .andExpect(jsonPath("$[0].code", is("B1003")))
            .andReturn();
    }

    /**
     * Util test method that handles extraneous params for creating subject objects.
     */
    private void createSubject(Faculty faculty, String name, String code) {
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
        subjectRepository.save(testSubject);
    }
}
