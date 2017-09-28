package com.example.subjecthub;

import com.example.subjecthub.controller.FacultyController;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.repository.FacultyRepository;
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

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();
    }
    @Test
    public void testGetFaculties() throws Exception {
        Faculty f1 = createFaculty("test1", null);
        Faculty f2 = createFaculty( null, "chicken");

        mockMvc.perform(get("/api/universities/university/{university_id}/faculties?name=test1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn();

    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty f = createFaculty("Faculty of SpringKings", "spring");

        mockMvc.perform(get("/api/universities/university/{university_id}/faculties/faculty/" + f.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", Matchers.is("Faculty of SpringKings")))
            .andExpect(jsonPath("$.code", Matchers.is("spring")))
            .andReturn();
    }

    private Faculty createFaculty(String name, String code) {
        Faculty testFaculty = new Faculty();
        testFaculty.setName(name);
        testFaculty.setCode(code);
        return facultyRepository.save(testFaculty);
    }

    // Test behaviour of getFaculty() and getFaculties()
    // check different combinations of parameters to getFaculties i.e. where some might be null
    // check that providing ids for faculties that don't exist are empty

}
