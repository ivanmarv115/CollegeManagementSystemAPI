package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.AssignCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProfessorService {
    ResponseEntity<List<GetProfessorResponse>> getAllProfessors();

    ResponseEntity<Long> createProfessor(CreateProfessorRequest professor) throws ResourceAlreadyExistsException;

    ResponseEntity<Professor> updateProfessor(UpdateProfessorRequest professor) throws ResourceNotFoundException;

    ResponseEntity<String> deleteProfessor(LongIdRequest id) throws ResourceNotFoundException;

    ResponseEntity<String> assignCourse(AssignCourseRequest request) throws ResourceNotFoundException;
}
