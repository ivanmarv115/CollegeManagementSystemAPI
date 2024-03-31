package ivanmartinez.simpleStudentsAPI.Service.Implementations;

import ivanmartinez.simpleStudentsAPI.DTO.Courses.CourseIdPrerequisiteIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.*;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Students.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Override
    public ResponseEntity<Long> createCourse(CreateCourseRequest request) throws ResourceAlreadyExistsException {
        logger.info("***** CREATE COURSE *****");
        logger.info("New course: " + request);

        if(courseRepository.existsByCode(request.getCode())){
            logger.warn("Course with code " + request.getCode() + " already exists");
            throw new ResourceAlreadyExistsException("Course with code " +
                    request.getCode() + " already exists");
        }

        Course course = createCourseRequestToCourseEntity(request);
        courseRepository.save(course);
        logger.info("COURSE CREATED");
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    private Course createCourseRequestToCourseEntity(CreateCourseRequest request){
        return Course.builder()
                .name(request.getName())
                .code(request.getCode())
                .semester(request.getSemester())
                .professors(new HashSet<>())
                .students(new HashSet<>())
                .coursesPrerequisites(new HashSet<>())
                .optionalForDegree(new HashSet<>())
                .requiredForDegree(new HashSet<>())
                .build();
    }

    @Override
    public ResponseEntity<List<GetCourseResponse>> getAllCourses(){
        logger.info("***** GET ALL COURSES *****");
        List<Course> courses = courseRepository.findAll();

        List<GetCourseResponse> response = new ArrayList<>();
        for(Course course : courses){
            GetCourseResponse getCourseResponse = courseEntityToResponse(course);
            response.add(getCourseResponse);
        }

        logger.info("***** SUCCESS *****");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private GetCourseResponse courseEntityToResponse(Course course){
        GetCourseResponse getCourseResponse = GetCourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .build();

        getCourseResponse.setRequiredForDegree(new ArrayList<>());
        for(Degree degree : course.getRequiredForDegree()){
            getCourseResponse.getRequiredForDegree().add(degree.getName());
        }
        getCourseResponse.setOptionalForDegree(new ArrayList<>());
        for(Degree degree : course.getOptionalForDegree()){
            getCourseResponse.getOptionalForDegree().add(degree.getName());
        }
        getCourseResponse.setStudents(new ArrayList<>());
        for (Student student : course.getStudents()){
            getCourseResponse.getStudents().add(EnrolledStudent.builder()
                    .id(student.getId())
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .degree(student.getDegree() != null ? student.getDegree().getName() : "Not yet enrolled")
                    .semester(student.getSemester())
                    .build());
        }
        getCourseResponse.setProfessors(new ArrayList<>());
        for (Professor professor : course.getProfessors()){
            getCourseResponse.getProfessors().add(AssignedProfessor.builder()
                    .id(professor.getId())
                    .firstName(professor.getFirstName())
                    .lastName(professor.getLastName())
                    .build());
        }

        return getCourseResponse;
    }

    @Override
    public ResponseEntity<List<GetCourseResponse>> getCoursesContaining(GetByRequest request) throws ResourceNotFoundException {
        logger.info("***** GET COURSES CONTAINING *****");
        logger.info("Request: " + request);

        List<GetCourseResponse> response = new ArrayList<>();
        if(request.getId() != null){
            Optional<Course> courseOptional = courseRepository.findById(request.getId());
            if (courseOptional.isEmpty()) {
                throw new ResourceNotFoundException("Course not found");
            }
            response.add(courseEntityToResponse(courseOptional.get()));
        } else if (request.getParam() != null) {
            List<Course> courses = courseRepository.getAllBy(request.getParam());
            for(Course course : courses){
                GetCourseResponse responseElement = courseEntityToResponse(course);
                response.add(responseElement);
            }
        }

        logger.info("***** REQUEST OK *****");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<String> deleteCourse(LongIdRequest longIdRequest) throws ResourceNotFoundException {
        logger.info("***** DELETE COURSE *****");
        logger.info("id: " + longIdRequest.getLongId());
        if(!courseRepository.existsById(longIdRequest.getLongId())){
            logger.warn("Course does not exists");
            throw new ResourceNotFoundException("Course does not exists");
        }
        courseRepository.deleteById(longIdRequest.getLongId());
        logger.info("***** DELETED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course Deleted");
    }

    @Override
    public ResponseEntity<String> updateCourse(UpdateCourseRequest requestCourse) throws ResourceNotFoundException {
        logger.info("***** UPDATE COURSE *****");
        logger.info("New Course: " + requestCourse);
        if(!courseRepository.existsById(requestCourse.getId())){
            logger.warn("Course does not exists");
            throw new ResourceNotFoundException("Course does not exists");
        }
        Course courseToUpdate = courseRepository.findById(requestCourse.getId()).get();
        logger.info("Course to udpate: " + courseToUpdate);

        courseToUpdate.setName(requestCourse.getName());
        courseToUpdate.setCode(requestCourse.getCode());
        courseToUpdate.setSemester(requestCourse.getSemester());
        courseRepository.save(courseToUpdate);
        logger.info("***** UPDATED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course updated");
    }

    @Override
    public ResponseEntity<String> addCoursePrerequisite(CourseIdPrerequisiteIdRequest request) throws ResourceNotFoundException {
        logger.info("***** ADD PREREQUISITE COURSE *****");
        logger.info("Request: " + request);
        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("Course not found");
            throw new ResourceNotFoundException("Course not found");
        }
        Course course = courseOptional.get();

        Optional<Course> coursePrerequisiteOptional = courseRepository.findById(request.getPrerequisiteCourseId());
        if (coursePrerequisiteOptional.isEmpty()){
            logger.warn("Prerequisite course not found");
            throw new ResourceNotFoundException("Prerequisite course not found");
        }
        logger.info("Resources found");

        course.getCoursesPrerequisites().add(coursePrerequisiteOptional.get());
        courseRepository.save(course);
        logger.info("***** SUCCESS *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully added prerequisite");
    }

    @Override
    public ResponseEntity<String> removeCoursePrerequisite(CourseIdPrerequisiteIdRequest request) throws ResourceNotFoundException {
        logger.info("***** REMOVE PREREQUISITE COURSE *****");
        logger.info("Request: " + request);
        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("Course not found");
            throw new ResourceNotFoundException("Course not found");
        }
        Course course = courseOptional.get();

        Optional<Course> coursePrerequisiteOptional = courseRepository.findById(request.getPrerequisiteCourseId());
        if (coursePrerequisiteOptional.isEmpty()){
            logger.warn("Prerequisite course not found");
            throw new ResourceNotFoundException("Prerequisite course not found");
        }
        logger.info("Resources found");

        course.getCoursesPrerequisites().remove(coursePrerequisiteOptional.get());
        courseRepository.save(course);
        logger.info("***** REMOVED SUCCESSFULLY *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully removed prerequisite");
    }

}
