package com.example.subjecthub;

import com.example.subjecthub.controller.CommentsServiceController;
import com.example.subjecthub.dto.AddCommentRequest;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.SubjectComment;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.exception.ExceptionAdvice;
import com.example.subjecthub.repository.*;
import com.example.subjecthub.testutils.EntityUtils;
import com.example.subjecthub.testutils.TestUtils;
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

import static com.example.subjecthub.testutils.UrlUtils.buildCommentApiUrl;
import static com.example.subjecthub.testutils.UrlUtils.buildCommentsApiUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Data needed for tests is initialised in {@link DbInitialiser#run}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CommentsServiceControllerTests {

    @Autowired
    private CommentsServiceController controller;

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

    @Autowired
    EntityUtils entityUtils;

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
    @WithMockUser(username = "testuser", authorities = {"USER"})
    public void testAddComment() throws Exception {
        // creates dud subject then adds a comment
        Subject s = createTestSubject();
        String m = "tasty";

        AddCommentRequest addCommentRequest = new AddCommentRequest(m);

        // Checks that new subject appears in list
        mockMvc.perform(
            post(buildCommentsApiUrl(1L, s.getId()) + "/comment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJson(addCommentRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subject.name", is(s.getName())))
            .andExpect(jsonPath("$.post", is(m)))
            .andExpect(jsonPath("$.thumbsUp", is(0)))
            .andExpect(jsonPath("$.thumbsDown", is(0)))
            .andExpect(jsonPath("$.flagged", is(false)))
            .andReturn();
    }

    @Test
    public void testGetComments() throws Exception {
        //test that a list of comments can be pulled for a subject
        Subject s = createTestSubject();
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
        Subject s = createTestSubject();
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
        Subject s = createTestSubject();
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
        Subject s = createTestSubject();
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
        Subject s = createTestSubject();
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
        Subject s = createTestSubject();
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
    public void testGetComment404() throws Exception {
        mockMvc.perform(get(buildCommentsApiUrl(1L, 10000L)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Subject not found.")))
            .andReturn();
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

    /**
     * Util test method that handles extraneous params for creating subject objects.
     */
    private Subject createTestSubject() {
        return entityUtils.createSubject("test", "ABC", testFaculty);
    }
}
