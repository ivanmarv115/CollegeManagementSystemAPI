package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.AssignCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProfessorService {
    ResponseEntity<List<GetProfessorResponse>> getAllProfessors() throws CustomException;

    ResponseEntity<Professor> getProfessorById(Long id);

    ResponseEntity<Long> createProfessor(CreateProfessorRequest professor) throws CustomException;

    ResponseEntity<Professor> updateProfessor(UpdateProfessorRequest professor) throws CustomException;

    ResponseEntity<Void> deleteProfessor(LongIdRequest id);

    ResponseEntity<String> assignCourse(AssignCourseRequest request) throws CustomException;
}
