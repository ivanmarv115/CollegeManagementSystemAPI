package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.Courses.CourseIdPrerequisiteIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.GetCourseResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Long> createCourse(@RequestBody CreateCourseRequest request)
            throws ResourceAlreadyExistsException {
        return courseService.createCourse(request);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetCourseResponse>> getAllCourses()
            throws ResourceNotFoundException {
        return courseService.getAllCourses();
    }

    @GetMapping("/by")
    public ResponseEntity<List<GetCourseResponse>> getCoursesContaining(
            @RequestBody GetByRequest request
            )
            throws ResourceNotFoundException {
        return courseService.getCoursesContaining(request);
    }

    @PutMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateCourse(@RequestBody UpdateCourseRequest request)
            throws CustomException {
        return courseService.updateCourse(request);
    }

    @PatchMapping("/addPrerequisite")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addPrerequisite(@RequestBody CourseIdPrerequisiteIdRequest request)
            throws ResourceNotFoundException {
        return courseService.addCoursePrerequisite(request);
    }

    @PatchMapping("/removePrerequisite")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> removePrerequisite(@RequestBody CourseIdPrerequisiteIdRequest request)
            throws ResourceNotFoundException {
        return courseService.removeCoursePrerequisite(request);
    }
}
