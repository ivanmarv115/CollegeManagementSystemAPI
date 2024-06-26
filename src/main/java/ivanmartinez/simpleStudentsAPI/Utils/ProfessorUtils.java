package ivanmartinez.simpleStudentsAPI.Utils;

import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ProfessorUtils {

    @Value("${default.user.password}")
    private String defaultPassword;

    public Professor createProfessorRequestToProfessorEntity(
            CreateProfessorRequest createProfessorRequest){
        return Professor.builder()
                .firstName(createProfessorRequest.getFirstName())
                .lastName(createProfessorRequest.getLastName())
                .coursesTaught(new HashSet<>())
                .build();
    }

    public GetProfessorResponse professorToGetProfessorResponse(
            Professor professor
    ){
        return GetProfessorResponse.builder()
                .id(professor.getId())
                .username(professor.getUser().getUsername())
                .firstName(professor.getFirstName())
                .lastName(professor.getLastName())
                .coursesTaught(professor.getCoursesTaught())
                .build();
    }

    public CreateUserRequest createProfessorRequestToCreateUserRequest(
            CreateProfessorRequest createProfessorRequest){
        return CreateUserRequest.builder()
                .username(createProfessorRequest.getUsername())
                .password(defaultPassword)
                .role(Role.PROFESSOR)
                .build();
    }

}
