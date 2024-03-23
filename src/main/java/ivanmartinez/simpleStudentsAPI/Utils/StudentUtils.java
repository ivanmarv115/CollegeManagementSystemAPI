package ivanmartinez.simpleStudentsAPI.Utils;

import ivanmartinez.simpleStudentsAPI.DTO.Students.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class StudentUtils {
    @Value("${default.user.password}")
    private String defaultPassword;

    public Student createStudentRequestToStudentEntity(CreateStudentRequest createStudentRequest){

        return Student.builder()
                .dateOfBirth(createStudentRequest.getDateOfBirth())
                .lastName(createStudentRequest.getLastName())
                .firstName(createStudentRequest.getFirstName())
                .currentCourses(new HashSet<>())
                .passedCourses(new HashSet<>())
                .build();
    }



    public CreateUserRequest createStudentRequestToCreateUserRequest(
            CreateStudentRequest createStudentRequest
    ){
        return CreateUserRequest.builder()
                .username(createStudentRequest.getUsername())
                .password(defaultPassword)
                .role(Role.STUDENT)
                .build();
    }
}
