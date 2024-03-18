package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Utils.StudentUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentsRepository studentsRepository;
    Logger logger  = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentUtils studentUtils;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public ResponseEntity<String> createStudent(CreateStudentRequest createStudentRequest) throws CustomException {
        try {
            logger.info("***** BEGINNING CREATE STUDENT *****");
            logger.info("New Student: " + createStudentRequest);
            if(userRepository.findByUsername(createStudentRequest.getUsername()).isPresent()){
                throw new ResourceAlreadyExistsException("Username already exists");
            }
            Student student = studentUtils.createStudentRequestToStudentEntity(createStudentRequest);
            logger.info("Creating new user");
            CreateUserRequest createUserRequest = studentUtils
                    .createStudentRequestToCreateUserRequest(createStudentRequest);
            User user = userService.createUser(createUserRequest);
            student.setUser(user);

            studentsRepository.save(student);
            logger.info("Student saved");
            logger.info("***** ENDING CREATE STUDENT *****");
            return new ResponseEntity<>("Student created successfully", HttpStatus.CREATED);
        }catch(RuntimeException ex){
            logger.error(ex.getMessage());
            throw new CustomException("Error: could not create student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<GetStudentsResponse>> getAllStudents() throws CustomException {
        try {
            logger.info("***** GET ALL STUDENTS CALLED *****");
            List<Student> students = studentsRepository.findAll();

            if (!students.isEmpty()) {
                logger.info("***** FOUND STUDENTS *****");

                List<GetStudentsResponse> response = new ArrayList<>();

                for (Student student : students) {
                    response.add(studentEntityToStudentResponse(student));
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            logger.warn("***** NO STUDENTS FOUND *****");
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }catch (RuntimeException exception){
            logger.error(exception.getMessage());
            throw new CustomException("Error when trying to get students",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private GetStudentsResponse studentEntityToStudentResponse(Student student){
        return GetStudentsResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .degree(student.getDegree())
                .courses(student.getCourses())
                .username(student.getUser().getUsername())
                .build();
    }

    @Override
    public ResponseEntity<List<Student>> getFilteredStudents(Optional<String> firstNameParam,
                                                             Optional<String> lastNameParam,
                                                             Optional<String> courseParam) {
        logger.info("***** GET STUDENTS BY CALLED *****");
        String firstName = firstNameParam.orElse("");
        String lastName = lastNameParam.orElse("");
        String course = courseParam.orElse("");

        logger.info(" FM: "+ firstName
                + " LM: " + lastName + " C: " + course);

        List<Student> students = studentsRepository
                .findByFirstNameContainingAndLastNameContainingAndCourseContaining
                        (firstName, lastName, course);

        if(!students.isEmpty()){
            logger.info("***** FOUND STUDENTS *****");
            return new ResponseEntity<>(students, HttpStatus.OK);
        }

        logger.info("***** NO STUDENTS FOUND *****");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteStudent(LongIdRequest longIdRequest) throws CustomException {
        try {
            logger.info("***** DELETE STUDENT CALLED *****");
            logger.info("Student idRequest: " + longIdRequest);
            Optional<Student> student = studentsRepository.findById(longIdRequest.getLongId());

            if (student.isPresent()) {
                studentsRepository.delete(student.get());
                logger.info("***** STUDENT DELETED *****");
                return new ResponseEntity<>("Student deleted", HttpStatus.ACCEPTED);
            } else {
                logger.warn("***** STUDENT DOES NOT EXIST *****");
                throw new ResourceNotFoundException("Student does not exist");
            }
        }catch (RuntimeException ex){
            throw new CustomException("Could not delete the student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateStudent(Student request) throws CustomException {
        try {
            logger.info("***** UPDATE STUDENT CALLED *****");
            logger.info("New Student: " + request);
            Optional<Student> studentById = studentsRepository.findById(request.getId());


            if (studentById.isEmpty()) {
                logger.warn("STUDENT NOT FOUND");
                throw new ResourceNotFoundException("Student with id " + request.getId() +
                        " not found");
            }
            Student newStudent = studentById.get();
            logger.info("Old Student: " + newStudent);

            newStudent.setFirstName(request.getFirstName());
            newStudent.setLastName(request.getLastName());
            newStudent.setDateOfBirth(request.getDateOfBirth());
            studentsRepository.save(newStudent);
            logger.info("UPDATED");
            logger.info("***** END UDPATE STUDENT *****");
            return new ResponseEntity<>("Student Updated",
                    HttpStatus.ACCEPTED);
        }catch (RuntimeException ex){
            logger.error(ex.getMessage());
            throw new CustomException("Error when trying to update student",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> enrollToCourse(StudentCourseEnrollRequest request) throws CustomException {
        try{
            logger.info("***** STUDENT ENROLL *****");
            logger.info("Request: " + request);
            Optional<Student> studentOptional = studentsRepository.findById(request.getStudentId());
            if(studentOptional.isEmpty()){
                logger.warn("Student not found");
                throw new ResourceNotFoundException("Student not found");
            }
            Student student = studentOptional.get();

            Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
            if (courseOptional.isEmpty()){
                logger.warn("Course not found");
                throw new ResourceNotFoundException("Course not found");
            }
            Course course = courseOptional.get();

            student.getCourses().add(course);
            course.getStudents().add(student);
            studentsRepository.save(student);
            courseRepository.save(course);
            logger.info("SUCCESS");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enrolled successfully");
        }catch (Exception exception){
            logger.error(exception.getMessage());
            throw new CustomException("Could not enroll student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
