package com.example.subjecthub;

import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.testutils.TestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Matchers;
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
import org.springframework.web.context.WebApplicationContext;

import static com.example.subjecthub.testutils.UrlUtils.buildFacultiesApiUrl;
import static com.example.subjecthub.testutils.UrlUtils.buildFacultyApiUrl;
import static com.example.subjecthub.testutils.UrlUtils.buildUniApiUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditFaculty() throws Exception {
        // Renaming 'Faculty of Testing' to 'Edited Faculty'.
        Faculty editedFaculty = facultyRepository.findOne(1L);
        editedFaculty.setName("Edited Faculty");

        mockMvc
            .perform(put(buildFacultyApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(editedFaculty)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Edited Faculty")))
            .andExpect(jsonPath("$.code", is("FoT")));

        mockMvc.perform(get(buildFacultiesApiUrl(1L)))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Edited Faculty")))
            .andExpect(jsonPath("$[0].code", is("FoT")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testEditFacultyIdsDontMatch() throws Exception {
        Faculty editedFaculty = new Faculty("Edit Uni", "EU", null);
        editedFaculty.setId(2L);

        mockMvc
            .perform(put(buildFacultyApiUrl(1L, 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(editedFaculty)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Payload facultyId and URL path facultyId do not match.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantEditFaculty() throws Exception {
        Faculty editFaculty = new Faculty("Edit Uni", "EU",  null);

        mockMvc
            .perform(put(buildUniApiUrl(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(editFaculty)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateFaculty() throws Exception {
        // Go land crabs!
        Faculty faculty = new Faculty("Test Faculty", "FoT2", null);

        MvcResult result = mockMvc
            .perform(post(buildFacultiesApiUrl(1L) + "/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(faculty)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Test Faculty")))
            .andExpect(jsonPath("$.code", is("FoT2")))
            .andReturn();

        JsonNode jsonNode = TestUtils.fromString(result.getResponse().getContentAsString());
        Long newId = jsonNode.get("id").asLong();

        mockMvc.perform(get(buildFacultiesApiUrl(1L)))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[1].id", is(newId.intValue())))
            .andExpect(jsonPath("$[1].name", is("Test Faculty")))
            .andExpect(jsonPath("$[1].code", is("FoT2")))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateFacultyFailsWithIdSupplied() throws Exception {
        Faculty faculty = new Faculty("Test Faculty", "FoT2", null);
        faculty.setId(1L);

        mockMvc
            .perform(post(buildFacultiesApiUrl(1L) + "/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(faculty)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message",
                is("Cannot specify id for faculty creation.")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUserCantCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Test Faculty", "FoT2", null);

        mockMvc
            .perform(post("/api/universities/university")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(faculty)))
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
