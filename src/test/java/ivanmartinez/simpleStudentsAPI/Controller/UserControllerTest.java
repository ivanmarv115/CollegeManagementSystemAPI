package ivanmartinez.simpleStudentsAPI.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.ChangePasswordRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.ProfessorService;
import ivanmartinez.simpleStudentsAPI.Service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    @BeforeEach
    public void startUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldAddUser() throws Exception {
        //Given
        CreateUserRequest request = CreateUserRequest.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .build();

        given(userService.addUser(request)).willReturn(
                ResponseEntity.status(HttpStatus.CREATED).body("User created")
        );

        //Test
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created"));
    }

    @Test
    void lockUser() throws Exception {
        //Given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        given(userService.lockUser(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body("User locked")
        );

        //Test
        mockMvc.perform(patch("/users/lockUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User locked"));
    }

    @Test
    void unlockUser() throws Exception {
        //Given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        given(userService.unlockUser(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body("User unlocked")
        );

        //Test
        mockMvc.perform(patch("/users/unlockUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User unlocked"));
    }

    @Test
    void changePassword() throws Exception {
        //Given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPwd("currentPass")
                .newPwd("newPass")
                .build();

        given(userService.changePassword(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body("Password changed")
        );

        //Test
        mockMvc.perform(patch("/users/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Password changed"));
    }
}