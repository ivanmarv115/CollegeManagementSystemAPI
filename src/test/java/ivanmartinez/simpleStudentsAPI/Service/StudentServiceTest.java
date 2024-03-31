package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Students.*;
import ivanmartinez.simpleStudentsAPI.Entity.*;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.DegreeRepository;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Service.Implementations.StudentServiceImpl;
import ivanmartinez.simpleStudentsAPI.Utils.StudentUtils;
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
import static org.mockito.Mockito.lenient;

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
    @Mock
    private DegreeRepository degreeRepository;
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
            .dateOfBirth("23/08/2001")
            .currentCourses(new HashSet<>())
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
                .dateOfBirth("23/08/2001")
                .user(user)
                .currentCourses(Set.of(course))
                .build();

        course.setStudents(Set.of(student));

        ArrayList<Student> students = new ArrayList<>();
        students.add(student);

        ArrayList<GetStudentsResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(GetStudentsResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .currentCourses(Set.of(course))
                .build());

        given(studentsRepository.findAll()).willReturn(students);
        //when
        ResponseEntity<List<GetStudentsResponse>> response = underTest.getAllStudents();

        //test
        verify(studentsRepository).findAll();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void shouldGetStudentsContaining() throws ResourceNotFoundException {
        //given
        GetByRequest request = GetByRequest.builder()
                .param("i")
                .build();

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
                .dateOfBirth("23/08/2001")
                .user(user)
                .currentCourses(Set.of(course))
                .build();

        course.setStudents(Set.of(student));

        ArrayList<Student> students = new ArrayList<>();
        students.add(student);

        ArrayList<GetStudentsResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(GetStudentsResponse.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .currentCourses(Set.of(course))
                .build());

        given(studentsRepository.getAllBy(request.getParam())).willReturn(students);
        //when
        ResponseEntity<List<GetStudentsResponse>> response = underTest.getStudentsContaining(request);

        //test
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
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .id(1L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .dateOfBirth("23/08/2001")
                .build();

        Optional<Student> studentOptional = Optional.ofNullable(Student.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .dateOfBirth("23/08/2001")
                .build());

        given(studentsRepository.findById(request.getId())).willReturn(studentOptional);

        //when
        underTest.updateStudent(request);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent.getId()).isEqualTo(request.getId());
        assertThat(capturedStudent.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(capturedStudent.getLastName()).isEqualTo(request.getLastName());
        assertThat(capturedStudent.getDateOfBirth()).isEqualTo(request.getDateOfBirth());
    }

    @Test
    void shouldThrow404OnUpdate() {
        //given
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .id(1L)
                .firstName("Ivan Mod")
                .lastName("Martinez")
                .dateOfBirth("23/08/2001")
                .build();

        given(studentsRepository.findById(request.getId())).willReturn(Optional.empty());

        //test
        assertThatThrownBy(() -> underTest.updateStudent(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student with id 1 not found");

        verify(studentsRepository, never()).delete(any());
    }

    @Test
    void shouldEnrollToCourse() throws CustomException {
        // given
        Degree degree = Degree.builder()
                .id(1L)
                .build();

        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        Student studentFound = Student.builder()
                .id(1L)
                .semester(1)
                .currentCourses(new HashSet<>())
                .passedCourses(new HashSet<>())
                .degree(degree)
                .build();

        Course courseFound = Course.builder()
                .id(1L)
                .semester(1)
                .students(new HashSet<>())
                .coursesPrerequisites(new HashSet<>())
                .optionalForDegree(new HashSet<>())
                .requiredForDegree(Set.of(degree))
                .build();

        given(studentsRepository.findById(request.getStudentId())).willReturn(
                Optional.of(studentFound)
        );
        given(courseRepository.findById(request.getCourseId()))
                .willReturn(Optional.of(courseFound));

        // when
        underTest.enrollToCourse(request);

        // test
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getCurrentCourses()).isEqualTo(Set.of(courseFound));

        verify(courseRepository).save(courseArgumentCaptor.capture());
        Course capturedCourse = courseArgumentCaptor.getValue();
        assertThat(capturedCourse.getStudents()).isEqualTo(Set.of(studentFound));
    }

    @ParameterizedTest
    @CsvSource({
            "true, false, Course not found",
            "false, true, Student not found"
    })
    void should404OnEnroll(boolean isStudentFound, boolean isCourseFound, String expectedMsg) {
        // given
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .studentId(1L)
                .courseId(1L)
                .build();

        Optional<Student> studentFound = isStudentFound ? Optional.of(new Student()) : Optional.empty();
        Optional<Course> courseFound = isCourseFound ? Optional.of(new Course()) : Optional.empty();

        lenient().when(studentsRepository.findById(request.getStudentId()))
                .thenReturn(studentFound);
        lenient().when(courseRepository.findById(request.getCourseId()))
                .thenReturn(courseFound);

        // test
        assertThatThrownBy(() -> underTest.enrollToCourse(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(expectedMsg);

        verify(studentsRepository, never()).save(any());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void should400OnEnroll() {
        // given
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        Student studentFound = Student.builder()
                .id(1L)
                .semester(1)
                .currentCourses(new HashSet<>())
                .build();

        Course courseFound = Course.builder()
                .id(1L)
                .semester(2)
                .students(new HashSet<>())
                .build();

        given(studentsRepository.findById(request.getStudentId())).willReturn(
                Optional.of(studentFound));
        given(courseRepository.findById(request.getCourseId())).willReturn(
                Optional.of(courseFound));

        // test
        assertThatThrownBy(() -> underTest.enrollToCourse(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Student needs more semesters!");

    }

    @Test
    void shouldUnrollToCourse() throws InvalidRequestException, ResourceNotFoundException {
        //given
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        Student studentFound = Student.builder()
                .id(1L)
                .semester(1)
                .currentCourses(new HashSet<>())
                .passedCourses(new HashSet<>())
                .build();

        Course courseFound = Course.builder()
                .id(1L)
                .semester(1)
                .students(new HashSet<>())
                .build();

        courseFound.getStudents().add(studentFound);
        studentFound.getCurrentCourses().add(courseFound);

        given(studentsRepository.findById(request.getStudentId())).willReturn(
                Optional.of(studentFound));
        given(courseRepository.findById(request.getCourseId())).willReturn(
                Optional.of(courseFound));

        //when
        underTest.unrollToCourse(request);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getPassedCourses()).isEqualTo(Set.of());
    }

    @Test
    void shouldAddPassedCourse() throws InvalidRequestException, ResourceNotFoundException {
        StudentIdCourseIdRequest request = StudentIdCourseIdRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .build();

        Student studentFound = Student.builder()
                .id(1L)
                .semester(1)
                .currentCourses(new HashSet<>())
                .passedCourses(new HashSet<>())
                .build();

        Course courseFound = Course.builder()
                .id(1L)
                .semester(1)
                .students(new HashSet<>())
                .build();

        courseFound.getStudents().add(studentFound);
        studentFound.getCurrentCourses().add(courseFound);

        given(studentsRepository.findById(request.getStudentId())).willReturn(
                Optional.of(studentFound));
        given(courseRepository.findById(request.getCourseId())).willReturn(
                Optional.of(courseFound));

        //when
        underTest.addPassedCourse(request);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getPassedCourses()).isEqualTo(Set.of(courseFound));
    }

    @Test
    void shouldEnrollToDegree() throws ResourceNotFoundException {
        //given
        StudentIdDegreeIdRequest request = StudentIdDegreeIdRequest.builder()
                .degreeId(1L)
                .studentId(1L)
                .build();

        Student student = Student.builder()
                .id(1L)
                .build();

        Degree degree = Degree.builder()
                .id(1L)
                .build();

        given(studentsRepository.findById(request.getStudentId())).willReturn(
                Optional.of(student));
        given(degreeRepository.findById(request.getDegreeId())).willReturn(
                Optional.of(degree));

        //when
        underTest.enrollToDegree(request);

        //test
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentsRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getDegree()).isEqualTo(degree);
    }

}