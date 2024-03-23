package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Repository.DegreeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DegreeServiceTest {
    @Mock
    private DegreeRepository degreeRepository;
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
    void shouldAddRequiredCourseToDegree(){
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
    }
}