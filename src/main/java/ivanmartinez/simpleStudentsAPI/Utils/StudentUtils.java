package ivanmartinez.simpleStudentsAPI.Utils;

import ivanmartinez.simpleStudentsAPI.DTO.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StudentUtils {
    @Value("${default.user.password}")
    private String defaultPassword;

    public Student createStudentRequestToStudentEntity(CreateStudentRequest createStudentRequest){

        return Student.builder()
                .course(createStudentRequest.getCourse())
                .dateOfBirth(createStudentRequest.getDateOfBirth())
                .degree(createStudentRequest.getDegree())
                .lastName(createStudentRequest.getLastName())
                .firstName(createStudentRequest.getFirstName())
                .build();
    }

    public GetStudentsResponse studentEntityToStudentResponse(Student student){
        return GetStudentsResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .degree(student.getDegree())
                .course(student.getCourse())
                .username(student.getUser().getUsername())
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
