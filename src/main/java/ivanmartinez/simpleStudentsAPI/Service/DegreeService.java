package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DegreeService {
    ResponseEntity<String> createDegree(CreateDegreeRequest request) throws ResourceAlreadyExistsException;

    ResponseEntity<List<Degree>> getAllDegrees();

    ResponseEntity<String> addRequiredCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> addOptionalCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException;
}
