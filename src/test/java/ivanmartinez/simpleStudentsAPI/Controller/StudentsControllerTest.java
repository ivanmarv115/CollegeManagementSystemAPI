package ivanmartinez.simpleStudentsAPI.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Students.*;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentsController.class)
class StudentsControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private StudentService studentService;
    @BeforeEach
    public void startUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldGetAllStudents() throws Exception {
        //given
        List<GetStudentsResponse> response = new ArrayList<>();
        response.add(GetStudentsResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build());

        given(studentService.getAllStudents()).willReturn(
                ResponseEntity.status(HttpStatus.OK).body(response));

        //test
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(response)));

    }

    @Test
    void shouldGetStudentsContaining() throws Exception {
        //given
        GetByRequest request = GetByRequest.builder()
                .param("Ivan")
                .build();

        List<GetStudentsResponse> response = new ArrayList<>();
        response.add(GetStudentsResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build());

        given(studentService.getStudentsContaining(request)).willReturn(
                ResponseEntity.status(HttpStatus.OK).body(response));

        //test
        mockMvc.perform(get("/students/by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(response)));

    }

    @Test
    void shouldCreateStudent() throws Exception {
        //given
        CreateStudentRequest request = CreateStudentRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build();

        String response = "Student created successfully";

        given(studentService.createStudent(request)).willReturn(
                ResponseEntity.status(HttpStatus.CREATED).body(response));

        //test
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(response))
                .andDo(print());
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        //given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        String response = "Student deleted";

        given(studentService.deleteStudent(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response));

        String stringRequest = objectMapper.writeValueAsString(request);
        //test
        ResultActions result = mockMvc.perform(delete("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringRequest))
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        //given
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .dateOfBirth("23/08/2001")
                .build();

        String response = "Student updated";

        given(studentService.updateStudent(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response));

        //test
        mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }

    @Test
    void shouldEnrollToCourse() throws Exception {
        //given
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        String response = "Enrolled successfully";

        given(studentService.enrollToCourse(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
        );

        //test
        mockMvc.perform(patch("/students/enrollToCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }

    @Test
    void shouldUnrollToCourse() throws Exception {
        //given
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        String response = "Unrolled successfully";

        given(studentService.unrollToCourse(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
        );

        //test
        mockMvc.perform(patch("/students/unrollToCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }

    @Test
    void shouldAddPassedCourse() throws Exception {
        //given
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        String response = "Passed course added successfully";

        given(studentService.addPassedCourse(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
        );

        //test
        mockMvc.perform(patch("/students/addPassed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }

    @Test
    void shouldEnrollToDegree() throws Exception {
        //given
        StudentIdDegreeIdRequest request = StudentIdDegreeIdRequest.builder()
                .degreeId(1L)
                .studentId(1L)
                .build();

        String response = "Enrolled successfully";

        given(studentService.enrollToDegree(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
        );

        //test
        mockMvc.perform(patch("/students/enrollToDegree")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(response));
    }

}