package com.example.subjecthub;

import com.example.subjecthub.controller.UniversityController;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.FacultyRepository;
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

import java.util.List;

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
public class UniversityControllerTests {

    @Autowired
    private UniversityController controller;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    FacultyRepository facultyRepository;

    private MockMvc mockMvc;

    private University university;
    private List<Faculty> faculties;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        this.university = universityRepository.findAll().get(0);
        this.faculties = facultyRepository.findByUniversityId(university.getId());
    }

    @Test
    public void testAddUniversity() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/universities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        createUniversity("Test", "UOT");

        // Checks that new university appears in list
        testSizeAndOtherProperty("/api/universities", "$", 3,
            "$[2].name", "Test");
    }

    @Test
    public void testGetUniversities() throws Exception {
        MvcResult result = testSizeAndOtherProperty("/api/universities", "$", 2,
            "$[0].name", "University of Testing");
    }

    @Test
    public void testGetSingleUniversityWithId() throws Exception {
        // Creates a university and checks that it can be fetched.
        University u = createUniversity("testUni", "TU");

        String universityUri = "/api/universities/university/" + u.getId();
        MvcResult result = mockMvc.perform(get(universityUri))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("testUni")))
            .andExpect(jsonPath("$.abbreviation", is("TU")))
            .andExpect(jsonPath("$.faculties[0].name", is("Faculty of Testing")))
            .andReturn();
    }

    @Test
    public void testGetSingleUniversityWithNameAndAbbreviation() throws Exception {
        // case-insensitive tests
        String universityName = "cOlLegE OF TEStIng";
        String universityAbbreviation = "coT";

        mockMvc.perform(get("/api/universities?name=" + universityName + "&abbreviation=" + universityAbbreviation))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("College of Testing")))
            .andExpect(jsonPath("$[0].abbreviation", is("CoT")))
            .andReturn();
    }

    @Test
    public void testGetNoUniversitiesWithNameAndAbbreviation() throws Exception {
        mockMvc.perform(get("/api/universities?name=NonExistent&abbreviation=NonEx"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)))
            .andReturn();
    }

    /**
     * Util test method that handles extraneous params for creating university objects.
     */
    private University createUniversity(String name, String abbreviation) {
        University testUniversity  = new University();
        testUniversity.setName(name);
        testUniversity.setAbbreviation(abbreviation);
        testUniversity.setFaculties(faculties);
        return universityRepository.save(testUniversity);
    }

    /**
     * Util method that tests size and name of university
     */
    private MvcResult testSizeAndOtherProperty(String urlTemplate, String sizeExpr, int expectedSize, String otherPropertyExpr, String expectedName) throws Exception {
        MvcResult result = mockMvc.perform(get(urlTemplate))
            .andExpect(status().isOk())
            .andExpect(jsonPath(sizeExpr, hasSize(expectedSize)))
            .andExpect(jsonPath(otherPropertyExpr, is(expectedName)))
            .andReturn();
        return result;
    }
}
