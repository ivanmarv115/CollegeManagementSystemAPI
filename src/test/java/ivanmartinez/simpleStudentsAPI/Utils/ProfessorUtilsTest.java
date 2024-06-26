package ivanmartinez.simpleStudentsAPI.Utils;

import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfessorUtilsTest {

    private final ProfessorUtils underTest = new ProfessorUtils();
    @Value("${default.user.password}")
    private String defaultPassword;

    @Test
    void createProfessorRequestToProfessorEntity() {
        // Given
        CreateProfessorRequest request = CreateProfessorRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .build();

        Professor expectedProfessor = Professor.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .coursesTaught(new HashSet<>())
                .build();

        // When
        Professor professor = underTest.createProfessorRequestToProfessorEntity(request);

        // Then
        assertEquals(expectedProfessor, professor);
    }

    @Test
    void professorToGetProfessorResponse() {
        // Given
        Set<Course> coursesTaught = new HashSet<>();

        Professor professor = new Professor();
        professor.setId(1L);
        professor.setUser(User.builder().username("imartinez").build());
        professor.setFirstName("Ivan");
        professor.setLastName("Martinez");
        professor.setCoursesTaught(coursesTaught);

        GetProfessorResponse expectedResponse = GetProfessorResponse.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinez")
                .coursesTaught(coursesTaught)
                .build();

        // When
        GetProfessorResponse response = underTest.professorToGetProfessorResponse(professor);

        // Then
        assertEquals(response, expectedResponse);

    }

    @Test
    void createProfessorRequestToCreateUserRequest() {
        // Given
        CreateProfessorRequest request = CreateProfessorRequest.builder()
                .firstName("Ivan")
                .lastName("Martinez")
                .username("imartinez")
                .build();

        CreateUserRequest expectedRequest = CreateUserRequest.builder()
                .username("imartinez")
                .role(Role.PROFESSOR)
                .password(defaultPassword)
                .build();

        // When
        CreateUserRequest createUserRequest = underTest.createProfessorRequestToCreateUserRequest(request);

        // Then
        assertEquals(createUserRequest, expectedRequest);
    }
}
