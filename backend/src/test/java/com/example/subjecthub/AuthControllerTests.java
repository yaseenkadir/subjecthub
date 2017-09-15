package com.example.subjecthub;

import com.example.subjecthub.controller.AuthenticationController;
import com.example.subjecthub.dto.AuthenticationRequest;
import com.example.subjecthub.dto.RegisterRequest;
import com.example.subjecthub.security.JwtTokenFilter;
import com.example.subjecthub.security.JwtTokenUtils;
import com.example.subjecthub.testutils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Before;
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

import javax.validation.constraints.Null;

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

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(authController)
            .build();
    }

    @Test
    public void testRegister() throws Exception {
        // TODO: Add email to registration request.
        RegisterRequest registerRequest = new RegisterRequest("__test", "test123", "a@test.com");

        String requestJson = TestUtils.asJson(registerRequest);
        mockMvc
            .perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticate() throws Exception {
        authenticate()
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andReturn();
    }

    @Test
    public void testBadAuthenticate() throws Exception {
        AuthenticationRequest badAuthRequest = new AuthenticationRequest("abc", "def");

        authenticate(badAuthRequest)
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void testSelfWithToken() throws Exception {
        MvcResult result = authenticate().andReturn();
        String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
        Assert.assertNotNull(jwt);

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
    public void testSelfWithoutToken() throws Exception {
        mockMvc
            .perform(get("/api/auth/self"))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    /**
     * Util method used by both testAuthenticate and testSelfWithToken.
     */
    private ResultActions authenticate() throws Exception {
        return authenticate(new AuthenticationRequest("testuser", "testpassword"));
    }

    private ResultActions authenticate(AuthenticationRequest authRequest) throws Exception {
        String authRequestJson = TestUtils.asJson(authRequest);
        return mockMvc
            .perform(post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson));
    }
}
