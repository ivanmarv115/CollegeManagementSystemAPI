package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.AssignCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.ProfessorRepository;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Utils.ProfessorUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @InjectMocks
    private ProfessorServiceImpl underTest;
    @Mock
    private UserService userService;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private ProfessorUtils professorUtils;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Value("${default.user.password}")
    private String defaultPassword;

    @Test
    void shouldGetAllProfessors() throws CustomException {
        //given
        User user = User.builder()
                .username("imartinezprof")
                .role(Role.PROFESSOR)
                .build();

        List<Professor> professors = new ArrayList<>();
        professors.add(
                Professor.builder()
                        .firstName("Ivan")
                        .lastName("Martinez")
                        .user(user)
                        .build()
        );

        List<GetProfessorResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(GetProfessorResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinezprof")
                .build());

        given(professorRepository.findAll()).willReturn(professors);
        given(professorUtils.professorToGetProfessorResponse(professors.get(0))).willReturn(
                expectedResponse.get(0)
        );

        //when
        ResponseEntity<List<GetProfessorResponse>> response = underTest.getAllProfessors();

        //test
        verify(professorRepository).findAll();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void shouldCreateProfessor() throws CustomException, CustomException {
        //given
        CreateProfessorRequest createProfessorRequest = CreateProfessorRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinezprof")
                .build();

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("imartinezprof")
                .password(defaultPassword)
                .role(Role.PROFESSOR)
                .build();

        User user = User.builder()
                .username("imartinezprof")
                .role(Role.PROFESSOR)
                .build();

        Professor professor = Professor.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .user(user)
                .build();

        Professor returnedProfessor = Professor.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .user(user)
                .build();

        given(userRepository.findByUsername(createProfessorRequest.getUsername())).willReturn(
                Optional.empty()
        );
        given(professorUtils.createProfessorRequestToProfessorEntity(createProfessorRequest))
                .willReturn(professor);
        given(userService.createUser(createUserRequest)).willReturn(user);
        given(professorUtils.createProfessorRequestToCreateUserRequest(createProfessorRequest))
                .willReturn(createUserRequest);
        given(professorRepository.save(professor)).willReturn(returnedProfessor);

        //when
        underTest.createProfessor(createProfessorRequest);

        //test
        verify(professorRepository).save(professor);
    }

    @Test
    void shouldThrowAlreadyExistsOnCreate() throws CustomException {
        //given
        CreateProfessorRequest createProfessorRequest = CreateProfessorRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinezprof")
                .build();

        User userFound = User.builder()
                .username("imartinezprof")
                .build();

        given(userRepository.findByUsername(createProfessorRequest.getUsername()))
                .willReturn(Optional.of(userFound));

        //test
        assertThatThrownBy(() -> underTest.createProfessor(createProfessorRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");

        verify(professorRepository, never()).save(any());
    }

    @Test
    void shouldUpdateProfessor() throws CustomException {
        //given
        User user = User.builder()
                .username("imartinezprof")
                .role(Role.PROFESSOR)
                .build();

        UpdateProfessorRequest updateProfessorRequest = UpdateProfessorRequest.builder()
                .id(1L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .username("imartinez")
                .build();

        Professor expectedProfessor = Professor.builder()
                .id(1L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .user(User.builder()
                        .username("imartinez")
                        .role(Role.PROFESSOR)
                        .build())
                .build();

        Optional<Professor> professorOptional = Optional.ofNullable(Professor.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .user(user)
                .build());

        given(professorRepository.findById(updateProfessorRequest.getId()))
                .willReturn(professorOptional);

        //when
        underTest.updateProfessor(updateProfessorRequest);

        //test
        verify(professorRepository).save(expectedProfessor);
    }

    @Test
    void shouldThrow404OnUpdate() {
        //given
        UpdateProfessorRequest professorRequest = UpdateProfessorRequest.builder()
                .id(1L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .username("imartinez")
                .build();

        given(professorRepository.findById(professorRequest.getId())).willReturn(Optional.empty());

        //test
        assertThatThrownBy(() -> underTest.updateProfessor(professorRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Professor not found");

        verify(professorRepository, never()).save(any());
    }


    @Test
    @Disabled
    void shouldDeleteProfessor() {
    }

    @Test
    void shouldAssignCourse() throws CustomException {
        //given
        AssignCourseRequest request = AssignCourseRequest.builder()
                .professorId(1L)
                .courseId(1L)
                .build();

        Professor professorFound = Professor.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .coursesTaught(new HashSet<>())
                .build();

        Course courseFound = Course.builder()
                .id(1L)
                .semester(1)
                .professors(new HashSet<>())
                .build();


        given(professorRepository.findById(request.getProfessorId()))
                .willReturn(Optional.of(professorFound));
        given(courseRepository.findById(request.getCourseId()))
                .willReturn(Optional.of(courseFound));

        //when
        underTest.assignCourse(request);

        //test
        ArgumentCaptor<Professor> professorArgumentCaptor = ArgumentCaptor.forClass(Professor.class);
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(professorRepository).save(professorArgumentCaptor.capture());
        assertThat(professorArgumentCaptor.getValue().getCoursesTaught())
                .isEqualTo(Set.of(courseFound));

        verify(courseRepository).save(courseArgumentCaptor.capture());
        assertThat(courseArgumentCaptor.getValue().getProfessors())
                .isEqualTo(Set.of(professorFound));
    }

    @ParameterizedTest
    @CsvSource({
            "true, false, Course not found",
            "false, true, Professor not found"
    })
    void should404OnAssign(boolean isProfessorFound, boolean isCourseFound, String expectedMsg) {
        // given
        AssignCourseRequest request = AssignCourseRequest.builder()
                .professorId(1L)
                .courseId(1L)
                .build();

        Optional<Professor> professorFound = isProfessorFound ? Optional.of(new Professor()) : Optional.empty();
        Optional<Course> courseFound = isCourseFound ? Optional.of(new Course()) : Optional.empty();

        lenient().when(professorRepository.findById(request.getProfessorId()))
                .thenReturn(professorFound);
        lenient().when(courseRepository.findById(request.getCourseId()))
                .thenReturn(courseFound);

        // test
        assertThatThrownBy(() -> underTest.assignCourse(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(expectedMsg);

        verify(professorRepository, never()).save(any());
    }
}