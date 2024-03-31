package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.UpdateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DegreeService {
    ResponseEntity<String> createDegree(CreateDegreeRequest request) throws ResourceAlreadyExistsException;

    ResponseEntity<List<Degree>> getAllDegrees();

    ResponseEntity<String> addRequiredCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;

    ResponseEntity<String> addOptionalCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;

    ResponseEntity<String> deleteRequiredCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;

    ResponseEntity<String> deleteOptionalCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;

    ResponseEntity<String> deleteDegree(LongIdRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> updateDegree(UpdateDegreeRequest request) throws ResourceNotFoundException;

    ResponseEntity<List<Degree>> getDegreesContaining(GetByRequest request) throws ResourceNotFoundException;
}
