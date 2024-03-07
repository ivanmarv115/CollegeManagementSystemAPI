package ivanmartinez.simpleStudentsAPI.Utils;

import ivanmartinez.simpleStudentsAPI.DTO.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Value;

class StudentUtilsTest {

    private final StudentUtils underTest = new StudentUtils();
    @Value("${default.user.password}")
    private String defaultPassword;

    @Test
    void shouldConvertToCreateUserRequest() {
        // Given
        CreateStudentRequest createStudentRequest = CreateStudentRequest
                .builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build();

        CreateUserRequest expectedCreateUserRequest = CreateUserRequest.builder()
                .username("imartinez")
                .password(defaultPassword)
                .role(Role.STUDENT)
                .build();

        // When
        CreateUserRequest userRequest = underTest
                .createStudentRequestToCreateUserRequest(createStudentRequest);

        // Then
        assertThat(userRequest).isEqualTo(expectedCreateUserRequest);
    }

    @Test
    void shouldConvertToStudentEntity() {
        // Given
        CreateStudentRequest createStudentRequest = CreateStudentRequest
                .builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .username("imartinez")
                .build();

        Student expectedStudent = Student.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .build();

        // When
        Student student = underTest.createStudentRequestToStudentEntity(createStudentRequest);

        // Then
        assertThat(student).isEqualTo(expectedStudent);
    }

    @Test
    void shouldConvertToStudentResponse() {
        // Given
        Student student = Student.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .course("Computer Science")
                .user(User.builder().username("imartinez").build())
                .build();

        GetStudentsResponse expectedResponse = GetStudentsResponse.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .degree("Undergraduate")
                .dateOfBirth("23/08/2001")
                .course("Computer Science")
                .username("imartinez")
                .build();

        // When
        GetStudentsResponse studentResponse = underTest.studentEntityToStudentResponse(student);

        // Then
        assertThat(studentResponse).isEqualTo(expectedResponse);
    }
}
