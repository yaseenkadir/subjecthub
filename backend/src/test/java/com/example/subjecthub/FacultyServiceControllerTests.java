package com.example.subjecthub;

import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.UniversityRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
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

import static com.example.subjecthub.testutils.UrlUtils.buildFacultyApiUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FacultyServiceControllerTests {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private University university;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();

        this.university = universityRepository.findAll().get(0);

    }
    @Test
    public void testGetFacultiesWithoutParams() throws Exception {
        // test getFaculties when no params are provided
        makeTestUniversity();

        mockMvc.perform(get("/api/universities/university/"
            + makeTestUniversity().getId() + "/faculties"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();
    }
    @Test
    public void testGetFacultiesWithName() throws Exception {
        // test getFaculties when name param is provided
            mockMvc.perform(get("/api/universities/university/1/faculties?name=" +
            "faculty of testing"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();
    }
    @Test
    public void testGetFacultiesWithNullCode() throws Exception {
        // test getFaculties when faculty has a code that is null
       makeTestUniversity();

        mockMvc.perform(get("/api/universities/university/" + makeTestUniversity().getId() + "/faculties"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();
    }

    @Test
    public void testGetFacultiesWithCode() throws Exception {
        // test getFaculties when code param is provided
        mockMvc.perform(get("/api/universities/university/1/faculties?code=FoT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();
    }
    @Test
    public void testGetFacultiesWithNameAndCode() throws Exception {
        // test getFaculties when both params are provided
        mockMvc.perform(get("/api/universities/university/1/faculties?code=FoT&name=" +
            "faculty of testing"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();
    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty f = createFaculty("Faculty of SpringKings", "spr");

        mockMvc.perform(get("/api/universities/university/1/faculties/faculty/" + f.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", Matchers.is("Faculty of SpringKings")))
            .andExpect(jsonPath("$.code", Matchers.is("spr")))
            .andReturn();
    }

    @Test
    public void testGetFaculty404() throws Exception {
        mockMvc.perform(get(buildFacultyApiUrl(1L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Faculty not found.")));
    }

    @Test
    public void testGetFacultyNoUniversity404() throws Exception {
        mockMvc.perform(get(buildFacultyApiUrl(10000L, 1L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("University not found.")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testDeleteFaculty() throws Exception {
        Faculty f = createFaculty("delete faculty", "DF");

        String facultyUrl = buildFacultyApiUrl(f.getUniversity().getId(), f.getId());
        mockMvc.perform(get(facultyUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("delete faculty")))
            .andExpect(jsonPath("$.code", is("DF")));

        mockMvc.perform(delete(facultyUrl))
            .andExpect(status().isOk());

        Assert.assertNull(facultyRepository.findOne(f.getId()));
        mockMvc.perform(get(facultyUrl))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Faculty not found.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testStudentCantDeleteUniversity() throws Exception {
        mockMvc.perform(delete(buildFacultyApiUrl(1L, 1L)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    private University makeTestUniversity() {
        University testUniversity = new University("University of No Codes", "NoCo");
        testUniversity = universityRepository.save(testUniversity);

        Faculty facultyWithoutACode = new Faculty("Faculty without a code", null, testUniversity);
        facultyWithoutACode = facultyRepository.save(facultyWithoutACode);

        Faculty facultyWithACode = new Faculty("Faculty with a code", "yes", testUniversity);
        facultyWithACode = facultyRepository.save(facultyWithACode);

        return testUniversity;
    }

    private Faculty createFaculty(String name, String code) {
        Faculty testFaculty = new Faculty();

        testFaculty.setName(name);
        testFaculty.setCode(code);
        testFaculty.setUniversity(this.university);
        return facultyRepository.save(testFaculty);
    }
}
