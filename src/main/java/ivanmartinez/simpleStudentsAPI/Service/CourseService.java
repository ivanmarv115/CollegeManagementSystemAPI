package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.AddCoursePrerequisiteRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {
    ResponseEntity<Long> createCourse(CreateCourseRequest request) throws CustomException;

    ResponseEntity<List<Course>> getAllCourses() throws CustomException;

    ResponseEntity<String> deleteCourse(LongIdRequest longIdRequest) throws CustomException;

    ResponseEntity<String> updateCourse(UpdateCourseRequest requestCourse) throws CustomException;

    ResponseEntity<String> addCoursePrerequisite(AddCoursePrerequisiteRequest request) throws ResourceNotFoundException;
}
