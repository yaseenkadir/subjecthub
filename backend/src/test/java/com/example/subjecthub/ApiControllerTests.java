package com.example.subjecthub;

import com.example.subjecthub.controller.ApiController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTests {

    @Autowired
    private ApiController apiController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(apiController)
            .build();
    }

    @Test
    public void testGetStudentYaseen() throws Exception {
        mockMvc.perform(get("/api/students/student/12020525"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']", is(12020525)))
            .andExpect(jsonPath("$['firstName']", is("Yaseen")))
            .andExpect(jsonPath("$['lastName']", is("Kadir")))
            .andExpect(jsonPath("$['courseCode']", is("C1000")))
            .andExpect(jsonPath("$['faculty']", is("FDAB")))
            .andReturn();

    }

    @Test
    public void testStudentGetMiles() throws Exception {
        mockMvc.perform(get("/api/students/student/75048524"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']", is(75048524)))
            .andExpect(jsonPath("$['firstName']", is("Miles")))
            .andExpect(jsonPath("$['lastName']", is("Johnson")))
            .andExpect(jsonPath("$['courseCode']", is("C1234")))
            .andExpect(jsonPath("$['faculty']", is("FEIT")))
            .andReturn();

    }

    @Test
    public void testStudentGetAndre() throws Exception {
        mockMvc.perform(get("/api/students/student/98084294"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']", is(98084294)))
            .andExpect(jsonPath("$['firstName']", is("Andre")))
            .andExpect(jsonPath("$['lastName']", is("Farah")))
            .andExpect(jsonPath("$['courseCode']", is("C1234")))
            .andExpect(jsonPath("$['faculty']", is("FEITChanged")))
            .andReturn();

    }

    @Test
    public void testStudentGetSirman() throws Exception {
        mockMvc.perform(get("/api/students/student/12544110"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']", is(12544110)))
            .andExpect(jsonPath("$['firstName']", is("Simran")))
            .andExpect(jsonPath("$['lastName']", is("Bagga")))
            .andExpect(jsonPath("$['courseCode']", is("C1234")))
            .andExpect(jsonPath("$['faculty']", is("FEIT")))
            .andReturn();

    }

    @Test
    public void testStudentGetAnuj() throws Exception {
        mockMvc.perform(get("/api/students/student/12564011"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']", is(12564011)))
            .andExpect(jsonPath("$['firstName']", is("Anuj")))
            .andExpect(jsonPath("$['lastName']", is("Paudel")))
            .andExpect(jsonPath("$['courseCode']", is("C1234")))
            .andExpect(jsonPath("$['faculty']", is("FEIT")))
            .andReturn();

    }

    @Test
    public void testStudentGetJet() throws Exception {
        mockMvc.perform(get("/api/students/student/11338896"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']", is(11338896)))
            .andExpect(jsonPath("$['firstName']", is("Jet")))
            .andExpect(jsonPath("$['lastName']", is("Zhu")))
            .andExpect(jsonPath("$['courseCode']", is("C1234")))
            .andExpect(jsonPath("$['faculty']", is("FEIT")))
            .andReturn();

    }

}
