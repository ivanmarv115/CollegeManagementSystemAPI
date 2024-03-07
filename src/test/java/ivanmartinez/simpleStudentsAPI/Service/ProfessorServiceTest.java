package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.ProfessorRepository;
import ivanmartinez.simpleStudentsAPI.Utils.ProfessorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    private ProfessorServiceImpl underTest;
    @Mock
    private UserService userService;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    ProfessorUtils professorUtils;
    @Value("${default.user.password}")
    private String defaultPassword;

    @BeforeEach
    void setUp(){
        underTest = new ProfessorServiceImpl(professorRepository,  professorUtils, userService);
    }

    @Test
    void shouldGetAllProfessors() {
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

        given(professorUtils.createProfessorRequestToProfessorEntity(createProfessorRequest))
                .willReturn(professor);
        given(userService.createUser(createUserRequest)).willReturn(user);
        given(professorUtils.createProfessorRequestToCreateUserRequest(createProfessorRequest))
                .willReturn(createUserRequest);

        //when
        underTest.createProfessor(createProfessorRequest);

        //test
        ArgumentCaptor<Professor> professorArgumentCaptor =
                ArgumentCaptor.forClass(Professor.class);

        verify(professorRepository).save(professorArgumentCaptor.capture());
        Professor capturedProfessor = professorArgumentCaptor.getValue();
        assertThat(capturedProfessor).isEqualTo(professor);
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
        ArgumentCaptor<Professor> professorArgumentCaptor =
                ArgumentCaptor.forClass(Professor.class);

        verify(professorRepository).save(professorArgumentCaptor.capture());
        Professor capturedProfessor = professorArgumentCaptor.getValue();
        assertThat(capturedProfessor).isEqualTo(expectedProfessor);
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

        verify(professorRepository, never()).delete(any());
    }


    @Test
    @Disabled
    void shouldDeleteProfessor() {
    }
}