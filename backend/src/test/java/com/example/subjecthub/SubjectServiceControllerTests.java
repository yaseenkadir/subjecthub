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
import com.example.subjecthub.testutils.TestUtils;


import com.example.subjecthub.entity.*;
import com.example.subjecthub.repository.*;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private MockMvc mockMvc;

    private University university;
    private Faculty faculty;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(new ExceptionAdvice())
            .build();

        this.university = universityRepository.findAll().get(0);
        this.faculty = facultyRepository.findByUniversityId(university.getId()).get(0);
    }
    @Test
    public void testAddNewTagToSubjectThatExists() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/1/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());

    }

    @Test
    public void testAddNewTagToSubjectThatDoesNotExist() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/400/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testAddExistingTagToSubjectThatExists() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/1/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/2/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
    }

    @Test
    public void testAddExistingTagToSubjectThatDoesNotExist() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/1/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/400/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testAddExistingTagToSubjectThatAlreadyHasTag() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/1/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post("/api/universities/university/1/subjects/subject/1/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message", is("Tag already exists for subject.")));
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
/*
    @Test
    public void testAddComment() throws Exception {
        // creates dud subject then adds a comment
        Subject s = createSubject("test", "ABCDEF");
        Long s_id = s.getId();
        String m = "tasty";

        AddCommentRequest addCommentRequest = new AddCommentRequest(m);

        // Checks that new subject appears in list
        mockMvc.perform(post("/api/universities/university/1/subjects/subject/"+s_id+"/comments/comment/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"comment\":\""+addCommentRequest.getComment()+"\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post", is(m)))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andReturn();
    }
*/

    @Test
    public void testGetComments() throws Exception {
        //test that a list of comments can be pulled for a subject
        Subject s = createSubject("test", "ABC");
        Long u_id = Long.parseLong("1");
        SubjectComment c1 = createComment(u_id, s.getId(), "yumyum1");
        SubjectComment c2 = createComment(u_id, s.getId(), "yumyum2");
        SubjectComment c3 = createComment(u_id, s.getId(), "yumyum3");
        SubjectComment c4 = createComment(u_id, s.getId(), "yumyum4");
        SubjectComment c5 = createComment(u_id, s.getId(), "yumyum5");

        mockMvc.perform(get("/api/universities/university/1/subjects/subject/"+s.getId()+"/comments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..post", hasSize(5)))
            .andReturn();
    }

    @Test
    public void testGetSingleComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        Long u_id = Long.parseLong("1");
        SubjectComment c = createComment(u_id, s.getId(), "yumyum");

        //checks it can be grabbed
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/"+s.getId()+
            "/comments/comment/"+c.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andReturn();
    }

    @Test
    public void testAddThumbUpComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        Long u_id = Long.parseLong("1");
        SubjectComment c = createComment(u_id, s.getId(), "yumyum");

        //checks it can be thumbed up
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/"+s.getId()+
            "/comments/comment/"+c.getId()+"/addThumbUp"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(1)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andReturn();
    }

    @Test
    public void testAddThumbDownComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        Long u_id = Long.parseLong("1");
        SubjectComment c = createComment(u_id, s.getId(), "yumyum");

        //checks it can be thumbed down
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/"+s.getId()+
            "/comments/comment/"+c.getId()+"/addThumbDown"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(1)))
            .andReturn();
    }

    @Test
    public void testFlagComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        Long u_id = Long.parseLong("1");
        SubjectComment c = createComment(u_id, s.getId(), "yumyum");

        //checks it can be flagged
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/"+s.getId()+
            "/comments/comment/"+c.getId()+"/flag"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andExpect(jsonPath("$.flagged", is(true)))
            .andReturn();
    }

    @Test
    public void testUnflagComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        Long u_id = Long.parseLong("1");
        SubjectComment c = createComment(u_id, s.getId(), "yumyum");

        //checks it can be unflagged
        mockMvc.perform(get("/api/universities/university/1/subjects/subject/"+s.getId()+
            "/comments/comment/"+c.getId()+"/unflag"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andExpect(jsonPath("$.flagged", is(false)))
            .andReturn();
    }

    // TODO: Move these subject controller tests to SubjectServiceControllerTests
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testAdminCanDeleteSubject() throws Exception {
        Subject s = createSubject("Subject To Delete", "S2D");

        String subjectsUrl = String.format("/api/universities/university/%s/subjects",
            university.getId());

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Subject To Delete")))
            .andReturn();

        mockMvc
            .perform(delete(subjectsUrl + "/subject/" + s.getId()))
            .andExpect(status().isOk());

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser
    public void testStudentCantDeleteSubject() throws Exception {
        University u = universityRepository.findAll().get(0);
        Subject s = createSubject("Subject To Delete", "S2D");

        String subjectsUrl = String.format("/api/universities/university/%s/subjects", u.getId());
        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Subject To Delete")))
            .andReturn();

        mockMvc
            .perform(delete(subjectsUrl + "/subject/" + s.getId()))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
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

    /**
     * Util test method that handles extraneous params for creating comment objects.
     */
    private SubjectComment createComment(Long userid, Long subjectid, String message){
        SubjectComment newComment = new SubjectComment();
        newComment.setPostTimeNow();
        newComment.setUser(subjectHubUserRepository.findOne(userid));
        newComment.setSubject(subjectRepository.findOne(subjectid));
        newComment.setPost(message);
        return subjectCommentRepository.save(newComment);
    }
}
