package com.example.subjecthub;

import com.example.subjecthub.controller.UniversityController;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.testutils.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.subjecthub.testutils.UrlUtils.buildFacultiesApiUrl;
import static com.example.subjecthub.testutils.UrlUtils.buildSubjectsApiUrl;
import static com.example.subjecthub.testutils.UrlUtils.buildUniApiUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Data needed for tests is initialised in {@link DbInitialiser#run}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // this is used so that database is rolled back between tests.
public class UniversityControllerTests {

    @Autowired
    private UniversityController controller;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    EntityUtils entityUtils;

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    public void testAddUniversity() throws Exception {
        mockMvc.perform(get("/api/universities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        createUniversity("Test", "UOT");

        mockMvc.perform(get("/api/universities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Test")))
            .andExpect(jsonPath("$[2].abbreviation", is("UOT")))
            .andReturn();
    }

    @Test
    public void testGetUniversities() throws Exception {
        mockMvc.perform(get("/api/universities"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("University of Testing")));
    }

    @Test
    public void testGetSingleUniversityWithId() throws Exception {
        // Creates a university and checks that it can be fetched.
        University u = createUniversity("testUni", "TU");

        String universityUri = "/api/universities/university/" + u.getId();
        mockMvc.perform(get(universityUri))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("testUni")))
            .andExpect(jsonPath("$.abbreviation", is("TU")))
            .andReturn();
    }

    @Test
    public void testGetSingleUniversityWithNameAndAbbreviationCaseInsensitive() throws Exception {
        mockMvc.perform(get("/api/universities?name=cOlLegE OF TEStIng&abbreviation=coT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("College of Testing")))
            .andExpect(jsonPath("$[0].abbreviation", is("CoT")))
            .andReturn();
    }

    @Test
    public void testGetNoUniversitiesWithNameAndAbbreviation() throws Exception {
        mockMvc.perform(get("/api/universities?name=NonExistant&abbreviation=NonEx"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }


    @Test
    public void testGetUniversitiesWithNameCaseInsensitive() throws Exception {

        String[] names =
            {"UnIveRsiTY of teStiNg", "university of testing", "UNIVERSITY OF TESTING"};

        for (String universityName : names) {
            mockMvc.perform(get("/api/universities?name=" + universityName))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("University of Testing")))
                .andReturn();
        }
    }

    @Test
    public void testGetSingleUniversityWithAbbreviation() throws Exception {
        mockMvc.perform(get("/api/universities?abbreviation=CoT"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("College of Testing")));
    }

    @Test
    public void testGetUniversity404() throws Exception {
        mockMvc.perform(get(buildUniApiUrl(10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("University not found.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testDeleteUniversity() throws Exception {
        University u = createUniversity("Delete University", "DT");

        Faculty f1 = new Faculty("Faculty 1", "F1", u);
        Faculty f2 = new Faculty("Faculty 2", "F2", u);

        Subject f1s1 = entityUtils.createSubject("f1 sub1", "F1S1", f1);
        Subject f1s2 = entityUtils.createSubject("f1 sub2", "F1S2", f1);
        Subject f2s1 = entityUtils.createSubject("f2 sub1", "F2S1", f2);

        List<Subject> faculty1Subjects = new ArrayList<>(Arrays.asList(f1s1, f1s2));
        List<Subject> faculty2Subjects = new ArrayList<>(Collections.singletonList(f2s1));

        f1.setSubjects(faculty1Subjects);
        f2.setSubjects(faculty2Subjects);

        List<Faculty> testFaculties = new ArrayList<>(Arrays.asList(f1, f2));

        facultyRepository.save(testFaculties);
        u.setFaculties(testFaculties);
        u = universityRepository.save(u);

        String uniUrl = buildUniApiUrl(u.getId());
        String subjectsUrl = buildSubjectsApiUrl(u.getId());

        // Test that the uni we created appears in the universities list
        mockMvc.perform(get("/api/universities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Delete University")));

        // Test that three 3 subjects we created appear in the list
        mockMvc.perform(get(subjectsUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("f1 sub1")))
            .andExpect(jsonPath("$[1].name", is("f1 sub2")))
            .andExpect(jsonPath("$[2].name", is("f2 sub1")));

        mockMvc.perform(delete(uniUrl))
            .andExpect(status().isOk());

        // Test can't get university
        mockMvc.perform(get(uniUrl))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("University not found.")));

        // Test can't get faculties for university
        mockMvc.perform(get(buildFacultiesApiUrl(u.getId())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("University not found.")));

        // Test can't get subjects for university
        mockMvc.perform(get(subjectsUrl))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("University not found.")));

        mockMvc.perform(get("/api/universities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantDeleteUniversity() throws Exception {
        mockMvc.perform(delete(buildUniApiUrl(1L)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    /**
     * Util test method that handles extraneous params for creating university objects.
     */
    private University createUniversity(String name, String abbreviation) {
        University testUniversity = new University();
        testUniversity.setName(name);
        testUniversity.setAbbreviation(abbreviation);
        return universityRepository.save(testUniversity);
    }
}
