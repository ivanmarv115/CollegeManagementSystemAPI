package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl underTest;

    @Test
    void shouldCreateCourse() throws CustomException {
        //given
        CreateCourseRequest request = CreateCourseRequest.builder()
                .code("M101")
                .name("Math 101")
                .degree("Degree")
                .year("1")
                .build();

        Course courseToCreate = Course.builder()
                .code("M101")
                .name("Math 101")
                .degree("Degree")
                .year("1")
                .build();

        Course courseToReturn = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .degree("Degree")
                .year("1")
                .build();

        given(courseRepository.save(courseToCreate)).willReturn(
                courseToReturn
        );
        //when
        ResponseEntity<Long> createCourseResponse = underTest.createCourse(request);

        //test
        ArgumentCaptor<Course> courseArgumentCaptor =
                ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());

        assertThat(courseArgumentCaptor.getValue()).isEqualTo(courseToCreate);
        assertThat(createCourseResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCourseResponse.getBody()).isNotNull();
    }

    @Test
    void shouldGetAllCourses() throws CustomException {
        //when
        underTest.getAllCourses();

        //test
        verify(courseRepository).findAll();
    }

    @Test
    void shouldDeleteCourse() throws CustomException {
        //given
        LongIdRequest longIdRequest = LongIdRequest.builder()
                .longId(1L)
                .build();

        given(courseRepository.existsById(1L)).willReturn(true);

        //when
        underTest.deleteCourse(longIdRequest);

        //test
        verify(courseRepository).deleteById(longIdRequest.getLongId());

    }

    @Test
    void shouldUpdateCourse() throws CustomException {
        //given
        UpdateCourseRequest requestCourse = UpdateCourseRequest.builder()
                .id(1L)
                .code("M101")
                .name("Mathematics 101")
                .degree("Degree")
                .year("1")
                .build();

        Course courseToSave = Course.builder()
                .id(1L)
                .code("M101")
                .name("Mathematics 101")
                .degree("Degree")
                .year("1")
                .build();

        Optional<Course> foundCourse = Optional.ofNullable(Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .degree("Degree")
                .year("1")
                .build());

        given(courseRepository.existsById(requestCourse.getId())).willReturn(true);
        given(courseRepository.findById(requestCourse.getId())).willReturn(foundCourse);
        //when
        underTest.updateCourse(requestCourse);

        //test
        verify(courseRepository).save(courseToSave);

    }
}
