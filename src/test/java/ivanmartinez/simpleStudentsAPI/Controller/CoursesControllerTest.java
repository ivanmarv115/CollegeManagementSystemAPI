package ivanmartinez.simpleStudentsAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CourseIdPrerequisiteIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.GetCourseResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.Service.CourseService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoursesController.class)
class CoursesControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private CourseService courseService;

    @BeforeEach
    public void startUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldCreateCourse() throws Exception {
        //given
        CreateCourseRequest request = CreateCourseRequest.builder()
                .code("CS101")
                .name("Introduction to Computer Science")
                .semester(1)
                .degree("CS")
                .build();

        given(courseService.createCourse(request))
                .willReturn(ResponseEntity.status(HttpStatus.CREATED)
                        .body(1L));
        //test
        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(1L)));
    }

    @Test
    void shouldGetAllCourses() throws Exception {
        //given
        List<GetCourseResponse> courses = new ArrayList<>();

        courses.add(GetCourseResponse.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .semester(1)
                .build());

        given(courseService.getAllCourses())
                .willReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(courses));

        //test
        mockMvc.perform(get("/courses/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(courses)));
    }

    @Test
    void shouldGetCoursesContaining() throws Exception {
        //given
        GetByRequest request = GetByRequest.builder()
                .param("Math")
                .build();

        List<GetCourseResponse> courses = new ArrayList<>();

        courses.add(GetCourseResponse.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .semester(1)
                .build());

        given(courseService.getCoursesContaining(request))
                .willReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(courses));

        //test
        mockMvc.perform(get("/courses/by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(courses)));
    }

    @Test
    void shouldUpdateCourse() throws Exception {
        //given
        UpdateCourseRequest request = UpdateCourseRequest.builder()
                .id(1L)
                .code("M101")
                .name("Mathematics 101")
                .semester(1)
                .build();

        given(courseService.updateCourse(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Course updated"));

        // test
        mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Course updated"));
    }

    @Test
    void shouldAddPrerequisite() throws Exception {
        //given
        CourseIdPrerequisiteIdRequest request = CourseIdPrerequisiteIdRequest.builder()
                .courseId(1L)
                .prerequisiteCourseId(2L)
                .build();

        given(courseService.addCoursePrerequisite(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully added prerequisite"));

        //test
        mockMvc.perform(patch("/courses/addPrerequisite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Successfully added prerequisite"));
    }

    @Test
    void shouldRemovePrerequisite() throws Exception {
        //given
        CourseIdPrerequisiteIdRequest request = CourseIdPrerequisiteIdRequest.builder()
                .courseId(1L)
                .prerequisiteCourseId(2L)
                .build();

        given(courseService.removeCoursePrerequisite(request)).willReturn(
                ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully removed prerequisite"));

        //test
        mockMvc.perform(patch("/courses/removePrerequisite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Successfully removed prerequisite"));
    }

}