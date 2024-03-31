package ivanmartinez.simpleStudentsAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.UpdateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Service.DegreeService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DegreeController.class)
class DegreeControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private DegreeService degreeService;

    @BeforeEach
    public void startUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldCreateDegree() throws Exception {
        // given
        CreateDegreeRequest request = CreateDegreeRequest.builder()
                .name("Computer Science")
                .build();

        given(degreeService.createDegree(request))
                .willReturn(ResponseEntity.status(HttpStatus.CREATED)
                        .body("Degree created successfully"));

        // test
        mockMvc.perform(post("/degrees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Degree created successfully"));
    }

    @Test
    void shouldGetAllDegrees() throws Exception {
        // given
        List<Degree> degrees = new ArrayList<>();

        degrees.add(Degree.builder()
                .id(1L)
                .name("Computer Science")
                .build());

        given(degreeService.getAllDegrees())
                .willReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(degrees));

        // test
        mockMvc.perform(get("/degrees/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(degrees)));
    }

    @Test
    void shouldGetDegreesContaining() throws Exception {
        // given
        GetByRequest request = GetByRequest.builder()
                .param("Comp")
                .build();

        List<Degree> degrees = new ArrayList<>();

        degrees.add(Degree.builder()
                .id(1L)
                .name("Computer Science")
                .build());

        given(degreeService.getDegreesContaining(request))
                .willReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(degrees));

        // test
        mockMvc.perform(get("/degrees/by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(degrees)));
    }

    @Test
    void shouldAddRequiredCourse() throws Exception {
        // given
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(2L)
                .build();

        given(degreeService.addRequiredCourse(request))
                .willReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Required course added successfully"));

        // test
        mockMvc.perform(patch("/degrees/addRequiredCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Required course added successfully"));
    }

    @Test
    void shouldAddOptionalCourse() throws Exception {
        // given
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(3L)
                .build();

        given(degreeService.addOptionalCourse(request))
                .willReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Optional course added successfully"));

        // test
        mockMvc.perform(patch("/degrees/addOptionalCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Optional course added successfully"));
    }

    @Test
    void shouldDeleteRequiredCourse() throws Exception {
        // given
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(2L)
                .build();

        given(degreeService.deleteRequiredCourse(request))
                .willReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Required course deleted successfully"));

        // test
        mockMvc.perform(patch("/degrees/deleteRequiredCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Required course deleted successfully"));
    }

    @Test
    void shouldDeleteOptionalCourse() throws Exception {
        // given
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(3L)
                .build();

        given(degreeService.deleteOptionalCourse(request))
                .willReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Optional course deleted successfully"));

        // test
        mockMvc.perform(patch("/degrees/deleteOptionalCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Optional course deleted successfully"));
    }

    @Test
    void shouldDeleteDegree() throws Exception {
        // given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        given(degreeService.deleteDegree(request))
                .willReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Degree deleted successfully"));

        // test
        mockMvc.perform(delete("/degrees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Degree deleted successfully"));
    }

    @Test
    void shouldUpdateDegree() throws Exception {
        // given
        UpdateDegreeRequest request = UpdateDegreeRequest.builder()
                .degreeId(1L)
                .name("Updated Degree")
                .build();

        given(degreeService.updateDegree(request))
                .willReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Degree updated successfully"));

        // test
        mockMvc.perform(put("/degrees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Degree updated successfully"));
    }
}