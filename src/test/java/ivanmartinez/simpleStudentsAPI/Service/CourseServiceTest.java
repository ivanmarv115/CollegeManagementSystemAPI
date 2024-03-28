package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Courses.CourseIdPrerequisiteIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Service.Implementations.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                .semester(1)
                .build();

        Course courseToCreate = Course.builder()
                .code("M101")
                .name("Math 101")
                .professors(new HashSet<>())
                .coursesPrerequisites(new HashSet<>())
                .requiredForDegree(new HashSet<>())
                .optionalForDegree(new HashSet<>())
                .students(new HashSet<>())
                .semester(1)
                .build();

        given(courseRepository.existsByCode(request.getCode())).willReturn(false);

        //when
        underTest.createCourse(request);

        //test
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());

        assertThat(courseArgumentCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(courseToCreate);
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
                .semester(1)
                .build();

        Optional<Course> foundCourse = Optional.ofNullable(Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .semester(1)
                .build());

        given(courseRepository.existsById(requestCourse.getId())).willReturn(true);
        given(courseRepository.findById(requestCourse.getId())).willReturn(foundCourse);
        //when
        underTest.updateCourse(requestCourse);

        //test
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(courseRepository).save(courseArgumentCaptor.capture());
        assertThat(courseArgumentCaptor.getValue().getName())
                .isEqualTo(requestCourse.getName());
    }

    @Test
    void shouldAddCoursePrerequisite() throws ResourceNotFoundException {
        CourseIdPrerequisiteIdRequest request = CourseIdPrerequisiteIdRequest.builder()
                .courseId(1L)
                .prerequisiteCourseId(2L)
                .build();

        Optional<Course> foundCourse = Optional.ofNullable(Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .semester(1)
                .coursesPrerequisites(new HashSet<>())
                .build());

        Optional<Course> foundPrerequisiteCourse = Optional.ofNullable(Course.builder()
                .id(2L)
                .code("M200")
                .name("Intermediate Maths")
                .semester(1)
                .build());

        when(courseRepository.findById(request.getCourseId())).thenReturn(
                foundCourse);
        when(courseRepository.findById(request.getPrerequisiteCourseId())).thenReturn(
                foundPrerequisiteCourse);

        //when
        underTest.addCoursePrerequisite(request);

        //test
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(courseRepository).save(courseArgumentCaptor.capture());
        assertThat(courseArgumentCaptor.getValue().getCoursesPrerequisites())
                .isEqualTo(Set.of(foundPrerequisiteCourse.get()));
    }

    @Test
    void shouldRemoveCoursePrerequisite() throws ResourceNotFoundException {
        CourseIdPrerequisiteIdRequest request = CourseIdPrerequisiteIdRequest.builder()
                .courseId(1L)
                .prerequisiteCourseId(2L)
                .build();

        Optional<Course> foundCourse = Optional.ofNullable(Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .semester(1)
                .coursesPrerequisites(new HashSet<>())
                .build());

        Optional<Course> foundPrerequisiteCourse = Optional.ofNullable(Course.builder()
                .id(2L)
                .code("M200")
                .name("Intermediate Maths")
                .semester(1)
                .build());

        foundCourse.get().getCoursesPrerequisites().add(foundPrerequisiteCourse.get());

        when(courseRepository.findById(request.getCourseId())).thenReturn(
                foundCourse);
        when(courseRepository.findById(request.getPrerequisiteCourseId())).thenReturn(
                foundPrerequisiteCourse);

        //when
        underTest.removeCoursePrerequisite(request);

        //test
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(courseRepository).save(courseArgumentCaptor.capture());
        assertThat(courseArgumentCaptor.getValue().getCoursesPrerequisites())
                .isEqualTo(Set.of());
        assertThat(courseArgumentCaptor.getValue().getId())
                .isEqualTo(request.getCourseId());
    }
}
