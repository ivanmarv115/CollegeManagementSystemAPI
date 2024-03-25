package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CourseIdPrerequisiteIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {
    ResponseEntity<Long> createCourse(CreateCourseRequest request) throws ResourceAlreadyExistsException;

    ResponseEntity<List<Course>> getAllCourses();

    ResponseEntity<String> deleteCourse(LongIdRequest longIdRequest) throws ResourceNotFoundException;

    ResponseEntity<String> updateCourse(UpdateCourseRequest requestCourse) throws ResourceNotFoundException;

    ResponseEntity<String> addCoursePrerequisite(CourseIdPrerequisiteIdRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> removeCoursePrerequisite(CourseIdPrerequisiteIdRequest request) throws ResourceNotFoundException;
}
