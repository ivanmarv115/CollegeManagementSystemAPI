package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Utils.StudentUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.util.*;

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
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private StudentServiceImpl underTest;

    @Value("${default.user.password}")
    private String defaultPassword;

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
            .courses(new HashSet<>())
            .user(user)
            .build();

        given(studentUtils.createStudentRequestToStudentEntity(createStudentRequest))
                .willReturn(student);
        given(userService.createUser(createUserRequest)).willReturn(user);
        given(studentUtils.createStudentRequestToCreateUserRequest(createStudentRequest))
                .willReturn(createUserRequest);
        given(userRepository.findByUsername(createStudentRequest.getUsername())).willReturn(
                Optional.empty()
        );

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
    void shouldGetAllStudents() throws CustomException {
        //given
        User user = User.builder()
                .username("imartinez")
                .role(Role.STUDENT)
                .build();

        Course course = Course.builder()
                .name("Course 1")
                .build();

        Student student = Student.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .user(user)
                .courses(Set.of(course))
                .build();

        course.setStudents(Set.of(student));

        ArrayList<Student> students = new ArrayList<>();
        students.add(student);

        ArrayList<GetStudentsResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(GetStudentsResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .courses(Set.of(course))
                .build());

        given(studentsRepository.findAll()).willReturn(students);
        //when
        ResponseEntity<List<GetStudentsResponse>> response = underTest.getAllStudents();

        //test
        verify(studentsRepository).findAll();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void shouldDeleteStudent() throws CustomException {
        //given
        LongIdRequest longIdRequest = LongIdRequest.builder()
                .longId(1L)
                .build();

        Optional<Student> studentOptional = Optional.ofNullable(Student.builder()
                .id(1L)
                .build());

        given(studentsRepository.findById(longIdRequest.getLongId())).willReturn(studentOptional);

        //when
        underTest.deleteStudent(longIdRequest);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentsRepository).delete(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(studentOptional.get());

    }

    @Test
    void shouldThrow404OnDelete(){
        //given
        LongIdRequest longIdRequest = LongIdRequest.builder()
                .longId(2L)
                .build();

        given(studentsRepository.findById(longIdRequest.getLongId())).willReturn(Optional.empty());

        //test
        assertThatThrownBy(() -> underTest.deleteStudent(longIdRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Student does not exist");

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

    @Test
    void shouldEnrollToCourse() throws CustomException {
        //given
        StudentCourseEnrollRequest request = StudentCourseEnrollRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        Student studentFound = Student.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .courses(new HashSet<>())
                .build();

        Course courseFound = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .degree("Degree")
                .year("1")
                .students(new HashSet<>())
                .build();

        Student expectedNewStudent = Student.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .courses(Set.of(courseFound))
                .build();

        Course expectedNewCourse = Course.builder()
                .id(1L)
                .code("M101")
                .name("Math 101")
                .degree("Degree")
                .year("1")
                .students(Set.of(studentFound))
                .build();

        given(studentsRepository.findById(request.getStudentId())).willReturn(
                Optional.of(studentFound)
        );
        given(courseRepository.findById(request.getCourseId()))
                .willReturn(Optional.of(courseFound));

        //when
        underTest.enrollToCourse(request);

        //test
        verify(studentsRepository).save(expectedNewStudent);
        verify(courseRepository).save(expectedNewCourse);

    }

}