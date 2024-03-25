package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.UpdateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.DegreeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DegreeServiceTest {
    @Mock
    private DegreeRepository degreeRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private DegreeServiceImpl underTest;

    @Test
    void shouldCreateDegree() throws ResourceAlreadyExistsException {
        //given
        CreateDegreeRequest request = CreateDegreeRequest.builder()
                .name("Computer Science Engineering")
                .build();

        Degree degreeToSave = Degree.builder()
                .name(request.getName())
                .build();

        given(degreeRepository.findByName(request.getName())).willReturn(
                Optional.empty());

        //when
        underTest.createDegree(request);

        //test
        verify(degreeRepository).save(degreeToSave);
    }

    @Test
    void shouldGetAllDegrees() {
        //given
        List<Degree> degrees = new ArrayList<>();
        degrees.add(Degree.builder()
                .id(1L).name("Computer Science Engineering").build());
        degrees.add(Degree.builder()
                .id(2L).name("Industrial Engineering").build());

        given(degreeRepository.findAll()).willReturn(degrees);

        //when
        underTest.getAllDegrees();

        //test
        verify(degreeRepository).findAll();
    }

    @Test
    void shouldAddRequiredCourseToDegree() throws ResourceNotFoundException, InvalidRequestException {
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(1L)
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .name("Computer Science Engineering")
                .requiredCourses(new HashSet<>())
                .build();

        Course course = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .build();

        given(degreeRepository.findById(request.getDegreeId())).willReturn(Optional.of(degree));
        given(courseRepository.findById(request.getCourseId())).willReturn(Optional.of(course));

        //when
        underTest.addRequiredCourse(request);

        //test
        ArgumentCaptor<Degree> degreeArgumentCaptor = ArgumentCaptor.forClass(Degree.class);

        verify(degreeRepository).save(degreeArgumentCaptor.capture());
        Degree capturedDegree = degreeArgumentCaptor.getValue();
        assertThat(capturedDegree.getRequiredCourses()).isEqualTo(Set.of(course));
    }

    @Test
    void shouldAddOptionalCourseToDegree() throws ResourceNotFoundException, InvalidRequestException {
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(1L)
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .name("Computer Science Engineering")
                .optionalCourses(new HashSet<>())
                .build();

        Course course = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .build();

        given(degreeRepository.findById(request.getDegreeId())).willReturn(Optional.of(degree));
        given(courseRepository.findById(request.getCourseId())).willReturn(Optional.of(course));

        //when
        underTest.addOptionalCourse(request);

        //test
        ArgumentCaptor<Degree> degreeArgumentCaptor = ArgumentCaptor.forClass(Degree.class);

        verify(degreeRepository).save(degreeArgumentCaptor.capture());
        Degree capturedDegree = degreeArgumentCaptor.getValue();
        assertThat(capturedDegree.getOptionalCourses()).isEqualTo(Set.of(course));
    }

    @Test
    void shouldDeleteRequiredCourse() throws InvalidRequestException, ResourceNotFoundException {
        //given
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(1L)
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .name("Computer Science Engineering")
                .requiredCourses(new HashSet<>())
                .build();

        Course course0 = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .build();

        degree.getRequiredCourses().add(course0);

        given(degreeRepository.findById(request.getDegreeId())).willReturn(Optional.of(degree));

        //when
        underTest.deleteRequiredCourse(request);

        //test
        ArgumentCaptor<Degree> degreeArgumentCaptor = ArgumentCaptor.forClass(Degree.class);

        verify(degreeRepository).save(degreeArgumentCaptor.capture());
        Degree capturedDegree = degreeArgumentCaptor.getValue();
        assertThat(capturedDegree.getRequiredCourses()).isEqualTo(Set.of());
    }

    @Test
    void shouldDeleteOptionalCourse() throws ResourceNotFoundException, InvalidRequestException {
        DegreeIdCourseIdRequest request = DegreeIdCourseIdRequest.builder()
                .degreeId(1L)
                .courseId(1L)
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .name("Computer Science Engineering")
                .optionalCourses(new HashSet<>())
                .build();

        Course course = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .build();

        degree.getOptionalCourses().add(course);

        given(degreeRepository.findById(request.getDegreeId())).willReturn(Optional.of(degree));
        given(courseRepository.findById(request.getCourseId())).willReturn(Optional.of(course));

        //when
        underTest.deleteOptionalCourse(request);

        //test
        ArgumentCaptor<Degree> degreeArgumentCaptor = ArgumentCaptor.forClass(Degree.class);

        verify(degreeRepository).save(degreeArgumentCaptor.capture());
        Degree capturedDegree = degreeArgumentCaptor.getValue();
        assertThat(capturedDegree.getOptionalCourses()).isEqualTo(Set.of());
    }

    @Test
    void shouldDeleteDegree() throws ResourceNotFoundException {
        //given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .build();

        given(degreeRepository.findById(request.getLongId())).willReturn(Optional.of(degree));

        //when
        underTest.deleteDegree(request);

        //test
        verify(degreeRepository).delete(degree);
    }

    @Test
    void shouldUpdateDegree() throws ResourceNotFoundException {
        //given
        UpdateDegreeRequest request = UpdateDegreeRequest.builder()
                .degreeId(1L)
                .name("Informatics Engineering")
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .name("Computer Science")
                .build();

        given(degreeRepository.findById(request.getDegreeId())).willReturn(
                Optional.ofNullable(degree));

        //when
        underTest.updateDegree(request);

        //test
        ArgumentCaptor<Degree> degreeArgumentCaptor = ArgumentCaptor.forClass(Degree.class);

        verify(degreeRepository).save(degreeArgumentCaptor.capture());
        Degree capturedDegree = degreeArgumentCaptor.getValue();
        assertThat(capturedDegree.getName()).isEqualTo(request.getName());
    }
}