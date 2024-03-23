package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.AssignCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professors")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<GetProfessorResponse>> getAllProfessors() throws CustomException {
        return professorService.getAllProfessors();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return professorService.getProfessorById(id);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Long> createProfessor
            (@RequestBody CreateProfessorRequest createProfessorRequest) throws CustomException {
        return professorService.createProfessor(createProfessorRequest);
    }

    @PutMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Professor> updateProfessor
            (@RequestBody UpdateProfessorRequest request) throws CustomException {
        return professorService.updateProfessor(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProfessor(@RequestBody LongIdRequest longIdRequest) {
        return professorService.deleteProfessor(longIdRequest);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/assign")
    public ResponseEntity<String> assignCourse(@RequestBody AssignCourseRequest request) throws CustomException {
        return professorService.assignCourse(request);
    }
}
