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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.example.subjecthub.testutils.UrlUtils.*;
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

    private University testUniversity;
    private Faculty testFaculty;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(new ExceptionAdvice())
            .build();

        this.testUniversity = universityRepository.findOne(1L);
        this.testFaculty = facultyRepository.findByUniversityId(testUniversity.getId()).get(0);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddNewTagToSubjectThatExists() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(
                post(buildSubjectApiUrl(1L, 1L) + "/addTag")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"BAD_AUTHORITY"})
    public void testAddNewTagWithoutAuthority() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(
                post(buildSubjectApiUrl(1L, 1L) + "/addTag")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddNewTagToSubjectThatDoesNotExist() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 10000L) + "/addTag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddExistingTagToSubjectThatExists() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 1L) + "/addTag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(get(buildSubjectApiUrl(1L, 1L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tags[0].name", is("Test Tag")));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddExistingTagToSubjectThatDoesNotExist() throws Exception {
        Tag tag = new Tag("Test Tag");

        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 1L) +"/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post(buildSubjectApiUrl(1L, 10000L) +"/addTag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddExistingTagToSubjectThatAlreadyHasTag() throws Exception {
        Tag tag = new Tag("Test Tag");

        String subjectUrl = buildSubjectApiUrl(1L, 1L);
        String requestJson = TestUtils.asJson(tag);
        mockMvc
            .perform(post(subjectUrl + "/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
        mockMvc
            .perform(post(subjectUrl + "/addTag" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message", is("Tag already exists for subject.")));
    }

    @Test
    public void testAddSubject() throws Exception {
        String subjectsUrl = buildSubjectsApiUrl(1L);
        mockMvc.perform(get(subjectsUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        createSubject("Test", "abcxyz");

        // Checks that new subject appears in list
        mockMvc.perform(get(subjectsUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Test")))
            .andReturn();
    }

    @Test
    public void testOtherUni() throws Exception {
        // Tests that data for university2 exists.
        mockMvc.perform(get(buildSubjectsApiUrl(2L)))
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

        mockMvc.perform(get(buildSubjectApiUrl(1L, s.getId())))
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

        String subjectsUrl = buildSubjectsApiUrl(1L);
        for(String name : names) {
            mockMvc.perform(get(subjectsUrl + "?name=" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(s1Name)))
                .andExpect(jsonPath("$[1].name", is(s2Name)))
                .andReturn();
        }

        // Check that a non existent subject doesn't match.
        mockMvc.perform(get(subjectsUrl + "?name=samsepiol"))
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
        Long userId = 1L;
        createComment(userId, s.getId(), "yumyum1");
        createComment(userId, s.getId(), "yumyum2");
        createComment(userId, s.getId(), "yumyum3");
        createComment(userId, s.getId(), "yumyum4");
        createComment(userId, s.getId(), "yumyum5");


        mockMvc.perform(get(buildCommentsApiUrl(1L, s.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].post", is("yumyum1")))
            .andExpect(jsonPath("$[4].post", is("yumyum5")))
            .andReturn();
    }

    @Test
    public void testGetSingleComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        SubjectComment c = createComment(1L, s.getId(), "yumyum");

        //checks it can grabbed
        mockMvc.perform(get(buildCommentApiUrl(1L, s.getId(), c.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddThumbUpComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        SubjectComment c = createComment(1L, s.getId(), "yumyum");

        //checks it can be thumbed up
        mockMvc.perform(get(buildCommentApiUrl(1L, s.getId(), c.getId()) + "/addThumbUp"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(1)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testAddThumbDownComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        SubjectComment c = createComment(1L, s.getId(), "yumyum");

        //checks it can be thumbed down
        mockMvc.perform(get( buildCommentApiUrl(1L, s.getId(), c.getId()) + "/addThumbDown"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(1)))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testFlagComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        SubjectComment c = createComment(1L, s.getId(), "yumyum");

        //checks it can be flagged
        String flagUrl = buildCommentApiUrl(1L, s.getId(), c.getId()) + "/flag";
        mockMvc.perform(get(flagUrl))
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
    @WithMockUser(authorities = {"USER"})
    public void testUnflagComment() throws Exception {
        //creates subject with comment
        Subject s = createSubject("test", "ABC");
        SubjectComment c = createComment(1L, s.getId(), "yumyum");

        String unflagUrl = buildCommentApiUrl(1L, s.getId(), c.getId()) + "/unflag";
        //checks it can be unflagged
        mockMvc.perform(get(unflagUrl))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post",is("yumyum")))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andExpect(jsonPath("$.flagged", is(false)))
            .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testAdminCanDeleteSubject() throws Exception {
        Subject s = createSubject("Subject To Delete", "S2D");
        String subjectsUrl = buildSubjectsApiUrl(testUniversity.getId());

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Subject To Delete")))
            .andReturn();

        mockMvc
            .perform(delete(buildSubjectApiUrl(testUniversity.getId(), s.getId())))
            .andExpect(status().isOk());

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser
    public void testStudentCantDeleteSubject() throws Exception {
        String subjectsUrl = buildSubjectsApiUrl(testUniversity.getId());
        Subject s = createSubject("Subject To Delete", "S2D");

        mockMvc.perform(get(subjectsUrl))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[2].name", is("Subject To Delete")))
            .andReturn();

        mockMvc
            .perform(delete(subjectsUrl + "/subject/" + s.getId()))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    public void testGetSubject404() throws Exception {
        mockMvc.perform(get(buildSubjectApiUrl(1L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Subject not found.")))
            .andReturn();
    }

    @Test
    public void testGetComments404() throws Exception {
        mockMvc.perform(get(buildCommentsApiUrl(1L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Subject not found.")))
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
        testSubject.setFaculty(testFaculty);
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
    private SubjectComment createComment(Long userId, Long subjectId, String message){
        Subject s = subjectRepository.findOne(subjectId);

        SubjectComment newComment = new SubjectComment();
        newComment.setPostTimeNow();
        newComment.setUser(subjectHubUserRepository.findOne(userId));
        newComment.setSubject(s);
        newComment.setPost(message);
        newComment = subjectCommentRepository.save(newComment);

        // Have to save comment via parent as well for some strange reason...
        s.getComments().add(newComment);
        subjectRepository.save(s);
        return newComment;
    }
}
