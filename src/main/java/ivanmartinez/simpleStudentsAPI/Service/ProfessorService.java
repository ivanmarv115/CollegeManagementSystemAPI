package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.IdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProfessorService {
    ResponseEntity<List<GetProfessorResponse>> getAllProfessors();

    ResponseEntity<Professor> getProfessorById(Long id);

    ResponseEntity<Professor> createProfessor(CreateProfessorRequest professor) throws CustomException;

    ResponseEntity<Professor> updateProfessor(UpdateProfessorRequest professor) throws CustomException;

    ResponseEntity<Void> deleteProfessor(IdRequest id);
}
