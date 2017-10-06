package com.example.subjecthub;

import com.example.subjecthub.controller.AuthenticationController;
import com.example.subjecthub.dto.AuthenticationRequest;
import com.example.subjecthub.dto.RegisterRequest;
import com.example.subjecthub.security.JwtTokenFilter;
import com.example.subjecthub.security.JwtTokenUtils;
import com.example.subjecthub.testutils.TestUtils;
import com.example.subjecthub.exception.ExceptionAdvice;
import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AuthControllerTests {

    @Autowired
    private AuthenticationController authController;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private MockMvc mockMvc;

    @BeforeClass
    public static void timezoneSetup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(authController)
            .setControllerAdvice(new ExceptionAdvice())
            .build();
    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("tester", "test123", "a@test.com");

        String requestJson = TestUtils.asJson(registerRequest);
        mockMvc
            .perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("tester")))
            .andExpect(jsonPath("$.password").doesNotExist())
            .andExpect(jsonPath("$.email", is("a@test.com")));
    }

    @Test
    public void testRegisterInvalidEmail() throws Exception {
        String[] invalidEmails = {"test", "test@localhost", "@@@@@"};

        for (String email : invalidEmails) {
            RegisterRequest registerRequest = new RegisterRequest("tester", "test123", email);

            String requestJson = TestUtils.asJson(registerRequest);
            mockMvc
                .perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid email address.")));
        }
    }

    @Test
    public void testRegisterInvalidUsername() throws Exception {
        String[] invalidUsernames = {"test", "test@localhost", "@@@@@", "123456", "__abc", "a23456789012345678901"};

        for (String username : invalidUsernames) {
            RegisterRequest registerRequest = new RegisterRequest(username, "test123", "a@b.com");

            String requestJson = TestUtils.asJson(registerRequest);
            mockMvc
                .perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message",
                    is("Username is invalid. Must start with letters and be between 5 and 20 characters.")));
        }
    }

    @Test
    public void testRegisterUsernameAlreadyExists() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("exists", "exists", "a@b.com");
        String requestJson = TestUtils.asJson(registerRequest);
        mockMvc
            .perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        RegisterRequest usernameExistsRequest = new RegisterRequest("exists", "exists", "b@c.com");
        String usernameExistsRequestJson = TestUtils.asJson(usernameExistsRequest);
        mockMvc
            .perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usernameExistsRequestJson))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.message", is("Username already exists.")));
    }

    @Test
    public void testRegisterEmailAlreadyExists() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("exists", "exists", "a@b.com");
        String requestJson = TestUtils.asJson(registerRequest);
        mockMvc
            .perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Test that registering a user with the same email fails (we change the
        RegisterRequest emailExistsRequest = new RegisterRequest("exists2", "exists", "a@b.com");
        String emailExistsRequestJson = TestUtils.asJson(emailExistsRequest);
        mockMvc
            .perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emailExistsRequestJson))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.message", is("Email already exists.")));
    }

    @Test
    public void testAuthenticate() throws Exception {
        // Authenticates and checks that the token is valid and expires in seven days.
        String token = getTokenFor("testuser", "testpassword");
        Claims c = jwtTokenUtils.getClaimsFromToken(token);

        // THIS TEST MIGHT BREAK
        // It might break in the rare case when a request is sent just before midnight and the token
        // response is received just after midnight. The plusDays will be different.
        ZonedDateTime expiry = ZonedDateTime.ofInstant(c.getExpiration().toInstant(),
            ZoneId.systemDefault());
        ZonedDateTime expected = ZonedDateTime.now().plusDays(7);
        Assert.assertTrue(expected.toLocalDate().equals(expiry.toLocalDate()));
    }

    @Test
    public void testBadAuthenticate() throws Exception {
        AuthenticationRequest badAuthRequest = new AuthenticationRequest("abc", "def");

        authenticate(badAuthRequest)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.message", is("Invalid credentials.")))
            .andReturn();
    }

    @Test
    public void testSelfWithTokenAsStudent() throws Exception {
        String jwt = getTokenFor("testuser", "testpassword");

        mockMvc
            .perform(get("/api/auth/self")
                .header(JwtTokenFilter.AUTHORIZATION_HEADER, jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("testuser")))
            .andExpect(jsonPath("$.email", is("test@example.com")))
            // Password should never be returned to user!
            .andExpect(jsonPath("$.password").doesNotExist())
            .andReturn();
    }

    @Test
    public void testSelfWithTokenAsAdmin() throws Exception {
        String jwt = getTokenForAdmin();

        mockMvc
            .perform(get("/api/auth/self")
                .header(JwtTokenFilter.AUTHORIZATION_HEADER, jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("admin")))
            .andExpect(jsonPath("$.email", is("admin@example.com")))
            .andExpect(jsonPath("$.password").doesNotExist())
            .andExpect(jsonPath("$.admin", is(true)))
            .andReturn();
    }

    @Test
    public void testSelfWithoutToken() throws Exception {
        mockMvc
            .perform(get("/api/auth/self"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.message", is("Not logged in.")))
            .andReturn();
    }

    @Test
    public void testExpiredToken() throws Exception {
        String token = jwtTokenUtils.generateToken("testuser", null,
            Date.from(ZonedDateTime.now().minusMinutes(1).toInstant()));

        mockMvc
            .perform(get("/api/auth/self")
                .header(JwtTokenFilter.AUTHORIZATION_HEADER, token))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.message", is("Not logged in.")))
            .andReturn();
    }

    private ResultActions authenticate(AuthenticationRequest authRequest) throws Exception {
        String authRequestJson = TestUtils.asJson(authRequest);
        return mockMvc
            .perform(post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson));
    }

    private String getTokenForAdmin() throws Exception {
        return getTokenFor("admin", "adminpassword");
    }

    private String getTokenFor(String username, String password) throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        String authRequestJson = TestUtils.asJson(request);
        MvcResult result =  mockMvc
            .perform(post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson))
            .andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }
}
