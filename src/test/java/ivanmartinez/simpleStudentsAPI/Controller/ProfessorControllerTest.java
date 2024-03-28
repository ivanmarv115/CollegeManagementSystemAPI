package ivanmartinez.simpleStudentsAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.AssignCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfessorController.class)
class ProfessorControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private ProfessorService professorService;

    @BeforeEach
    public void startUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldGetAllProfessors() throws Exception {
        // Given
        List<GetProfessorResponse> response = new ArrayList<>();
        response.add(GetProfessorResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinez")
                .build());

        given(professorService.getAllProfessors()).willReturn(
                ResponseEntity.ok(response));

        // Test
        mockMvc.perform(get("/professors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void shouldCreateProfessor() throws Exception {
        // Given
        CreateProfessorRequest request = CreateProfessorRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinez")
                .build();

        Long idResponse = 1L;

        given(professorService.createProfessor(request)).willReturn(
                ResponseEntity.status(HttpStatus.CREATED).body(idResponse));

        System.out.println(objectMapper.writeValueAsString(request));
        // Test
        mockMvc.perform(post("/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(idResponse)));
    }

    @Test
    void shouldUpdateProfessor() throws Exception {
        // Given
        UpdateProfessorRequest request = UpdateProfessorRequest.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .build();

        Professor professor = Professor.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .build();

        given(professorService.updateProfessor(request)).willReturn(
                ResponseEntity.ok(professor));

        // Test
        mockMvc.perform(put("/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(professor)));
    }

    @Test
    void shouldDeleteProfessor() throws Exception {
        // Given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        given(professorService.deleteProfessor(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body("Professor deleted"));

        // Test
        mockMvc.perform(delete("/professors", request.getLongId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Professor deleted"));
    }

    @Test
    void shouldAssignCourse() throws Exception {
        //Given
        AssignCourseRequest request = AssignCourseRequest.builder()
                .courseId(1L)
                .professorId(1L)
                .build();

        String response = "Assigned successfully";

        given(professorService.assignCourse(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response));
        //Test
        mockMvc.perform(patch("/professors/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }
}