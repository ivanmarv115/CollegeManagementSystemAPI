package ivanmartinez.simpleStudentsAPI.Service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl underTest;

    //TODO: test lots of cases with just this method
    @Test
    public void shouldCreateCourse() throws CustomException {
        //given
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
        ResponseEntity<Long> createCourseResponse = underTest.createCourse(courseToCreate);

        //test
        ArgumentCaptor<Course> courseArgumentCaptor =
                ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());

        assertThat(courseArgumentCaptor.getValue()).isEqualTo(courseToCreate);
        assertThat(createCourseResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCourseResponse.getBody()).isNotNull();
    }
}
