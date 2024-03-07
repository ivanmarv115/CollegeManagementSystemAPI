package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.DTO.IdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import ivanmartinez.simpleStudentsAPI.Utils.StudentUtils;
import org.junit.jupiter.api.BeforeEach;
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
class StudentServiceTest {

    @Mock
    private StudentsRepository studentsRepository;
    @Mock
    private UserService userService;
    @Mock
    private StudentUtils studentUtils;
    private StudentServiceImpl underTest;

    @Value("${default.user.password}")
    private String defaultPassword;


    @BeforeEach
    void setUp() {
        underTest = new StudentServiceImpl(studentsRepository, studentUtils,userService);
    }

    @Test
    void shouldCreateStudent() throws CustomException {
        //given
        CreateStudentRequest createStudentRequest = CreateStudentRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build();

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("imartinez")
                .password(defaultPassword)
                .role(Role.STUDENT)
                .build();

        User user = User.builder()
                .username("imartinez")
                .role(Role.STUDENT)
                .build();

        Student student = Student.builder()
            .firstName("Ivan")
            .lastName("Martinez")
            .degree("Undergraduate")
            .dateOfBirth("23/08/2001")
            .user(user)
            .build();

        given(studentUtils.createStudentRequestToStudentEntity(createStudentRequest))
                .willReturn(student);
        given(userService.createUser(createUserRequest)).willReturn(user);
        given(studentUtils.createStudentRequestToCreateUserRequest(createStudentRequest))
                .willReturn(createUserRequest);

        //when
        underTest.createStudent(createStudentRequest);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    void shouldGetAllStudents() {
        //given
        User user = User.builder()
                .username("imartinez")
                .role(Role.STUDENT)
                .build();

        ArrayList<Student> students = new ArrayList<>();
        students.add(
                Student.builder()
                        .firstName("Ivan")
                        .lastName("Martinez")
                        .degree("Undergraduate")
                        .dateOfBirth("23/08/2001")
                        .user(user)
                        .build()
        );

        ArrayList<GetStudentsResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(GetStudentsResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build());

        given(studentsRepository.findAll()).willReturn(students);
        given(studentUtils.studentEntityToStudentResponse(students.get(0))).willReturn(
                expectedResponse.get(0)
        );
        //when
        ResponseEntity<List<GetStudentsResponse>> response = underTest.getAllStudents();

        //test
        verify(studentsRepository).findAll();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void shouldDeleteStudent() throws CustomException {
        //given
        IdRequest idRequest = IdRequest.builder()
                .id(1L)
                .build();

        Optional<Student> studentOptional = Optional.ofNullable(Student.builder()
                .id(1L)
                .build());

        given(studentsRepository.findById(idRequest.getId())).willReturn(studentOptional);

        //when
        underTest.deleteStudent(idRequest);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentsRepository).delete(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(studentOptional.get());

    }

    @Test
    void shouldThrow404OnDelete(){
        //given
        IdRequest idRequest = IdRequest.builder()
                .id(2L)
                .build();

        given(studentsRepository.findById(idRequest.getId())).willReturn(Optional.empty());

        //test
        assertThatThrownBy(() -> underTest.deleteStudent(idRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Student with idRequest IdRequest(id=2) does not exist");

        verify(studentsRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateStudent() throws CustomException {
        //given
        Student student = Student.builder()
                .id(1L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .build();

        Optional<Student> studentOptional = Optional.ofNullable(Student.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .build());

        given(studentsRepository.findById(student.getId())).willReturn(studentOptional);

        //when
        underTest.updateStudent(student);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void shouldThrow404OnUpdate() {
        //given
        Student student = Student.builder()
                .id(2L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .build();

        given(studentsRepository.findById(student.getId())).willReturn(Optional.empty());

        //test
        assertThatThrownBy(() -> underTest.updateStudent(student))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Student with id 2 not found");

        verify(studentsRepository, never()).delete(any());
    }

}