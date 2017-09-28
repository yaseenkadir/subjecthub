package com.example.subjecthub;

import com.example.subjecthub.api.UniversityServiceApi;
import com.example.subjecthub.controller.FacultyController;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.UniversityRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FacultyControllerTests {

    @Autowired
    private FacultyController controller;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UniversityRepository universityRepository;


    private MockMvc mockMvc;
    private University university;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        this.university = universityRepository.findAll().get(0);
    }
    @Test
    public void testGetFaculties() throws Exception {
        // test getFaculties when no params are provided
        mockMvc.perform(get("/api/universities/university/1/faculties"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andReturn();

        // test getFaculties when name param is provided
        mockMvc.perform(get("/api/universities/university/1/faculties?name=" +
            "faculty of engineering and information technology"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();

        // test getFaculties when code param is provided
        mockMvc.perform(get("/api/universities/university/1/faculties?code=feit"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();

        // test getFaculties when both params are provided
        mockMvc.perform(get("/api/universities/university/1/faculties?code=feit&name=" +
            "faculty of engineering and Information Technology"))
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

    private Faculty createFaculty(String name, String code) {
        Faculty testFaculty = new Faculty();

        testFaculty.setName(name);
        testFaculty.setCode(code);
        testFaculty.setUniversity(this.university);
        return facultyRepository.save(testFaculty);
    }

    // Test behaviour of getFaculty() and getFaculties()
    // check different combinations of parameters to getFaculties i.e. where some might be null
    // check that providing ids for faculties that don't exist are empty

}
