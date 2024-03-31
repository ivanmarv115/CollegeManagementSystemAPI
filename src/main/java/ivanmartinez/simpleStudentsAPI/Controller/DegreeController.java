package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.UpdateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/degrees")
public class DegreeController {
    @Autowired
    private DegreeService degreeService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> createDegree(@RequestBody CreateDegreeRequest request)
            throws ResourceAlreadyExistsException {
        return degreeService.createDegree(request);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Degree>> getAllDegrees(){
        return degreeService.getAllDegrees();
    }

    @GetMapping("/by")
    public ResponseEntity<List<Degree>> getDegreesContaining(
            @RequestBody GetByRequest request
            ) throws ResourceNotFoundException {
        return degreeService.getDegreesContaining(request);
    }

    @PatchMapping("/addRequiredCourse")
    public ResponseEntity<String> addRequiredCourse(@RequestBody DegreeIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return degreeService.addRequiredCourse(request);
    }

    @PatchMapping("/addOptionalCourse")
    public ResponseEntity<String> addOptionalCourse(@RequestBody DegreeIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return degreeService.addOptionalCourse(request);
    }

    @PatchMapping("/deleteRequiredCourse")
    public ResponseEntity<String> deleteRequiredCourse(@RequestBody DegreeIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return degreeService.deleteRequiredCourse(request);
    }

    @PatchMapping("/deleteOptionalCourse")
    public ResponseEntity<String> deleteOptionalCourse(@RequestBody DegreeIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return degreeService.deleteOptionalCourse(request);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDegree(@RequestBody LongIdRequest request)
            throws ResourceNotFoundException {
        return degreeService.deleteDegree(request);
    }

    @PutMapping
    public ResponseEntity<String> updateDegree(@RequestBody UpdateDegreeRequest request)
            throws ResourceNotFoundException {
        return degreeService.updateDegree(request);
    }
}
