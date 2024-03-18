package ivanmartinez.simpleStudentsAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationRequest;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup()
    {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void authenticate() throws Exception {
        //given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("adminpred")
                .password("adminPredPass")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .role(Role.ADMIN)
                .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbnByZWQiLCJpYXQiOjE3MTAyMDMxMTUsImV4cCI6MTcxMDI4OTUxNX0.S446vcMtjt-lVPkaIixDgYTO-AFo6Wlqhm9uT19rpWI")
                .build();

        given(authenticationService.authenticate(request))
                .willReturn(ResponseEntity.status(HttpStatus.OK).body(response));

        //test
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }
}