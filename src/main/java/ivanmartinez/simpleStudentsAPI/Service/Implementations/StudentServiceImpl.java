package ivanmartinez.simpleStudentsAPI.Service.Implementations;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Students.*;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.DegreeRepository;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Service.StudentService;
import ivanmartinez.simpleStudentsAPI.Service.UserService;
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
public class StudentServiceImpl implements StudentService {

    private final StudentsRepository studentsRepository;
    private final StudentUtils studentUtils;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final DegreeRepository degreeRepository;
    Logger logger  = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public ResponseEntity<String> createStudent(CreateStudentRequest createStudentRequest) throws ResourceAlreadyExistsException {
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
    }

    @Override
    public ResponseEntity<List<GetStudentsResponse>> getAllStudents(){
        logger.info("***** GET ALL STUDENTS CALLED *****");
        List<Student> students = studentsRepository.findAll();

        if (!students.isEmpty()) {
            logger.info("***** FOUND STUDENTS *****");

            List<GetStudentsResponse> response = new ArrayList<>();

            for (Student student : students) {
                GetStudentsResponse studentsResponse = studentEntityToStudentResponse(student);
                if(student.getDegree() != null){
                    studentsResponse.setDegree(student.getDegree().getName());
                }
                response.add(studentsResponse);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        logger.warn("***** NO STUDENTS FOUND *****");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    private GetStudentsResponse studentEntityToStudentResponse(Student student){
        return GetStudentsResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .semester(student.getSemester())
                .currentCourses(student.getCurrentCourses())
                .passedCourses(student.getPassedCourses())
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
                .findByFirstNameContainingAndLastNameContaining(firstName, lastName);

        if(!students.isEmpty()){
            logger.info("***** FOUND STUDENTS *****");
            return new ResponseEntity<>(students, HttpStatus.OK);
        }

        logger.info("***** NO STUDENTS FOUND *****");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteStudent(LongIdRequest longIdRequest) throws ResourceNotFoundException {
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
    }

    @Override
    public ResponseEntity<String> updateStudent(UpdateStudentRequest request) throws ResourceNotFoundException {
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
        newStudent.setSemester(request.getSemester());
        studentsRepository.save(newStudent);
        logger.info("UPDATED");
        logger.info("***** END UDPATE STUDENT *****");
        return new ResponseEntity<>("Student Updated",
                HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<String> enrollToCourse(StudentIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException {
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
        logger.info("Resources found");

        if(student.getSemester() < course.getSemester()){
            logger.warn("Student needs more semesters!");
            throw new InvalidRequestException("Student needs more semesters!");
        }

        if(!student.getPassedCourses().containsAll(course.getCoursesPrerequisites())){
            logger.warn("Student does not meets course prerequisites");
            throw new InvalidRequestException("Student does not meets course prerequisites");
        }

        if(!course.getOptionalForDegree().contains(student.getDegree()) &&
                !course.getRequiredForDegree().contains(student.getDegree())){
            logger.warn("Course is not teach for student's degree");
            throw new InvalidRequestException("Course is not teach for student's degree");
        }
        logger.info("Student meets demands");

        student.getCurrentCourses().add(course);
        course.getStudents().add(student);
        studentsRepository.save(student);
        courseRepository.save(course);
        logger.info("***** SUCCESSFULLY ENROLLED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enrolled successfully");
    }

    @Override
    public ResponseEntity<String> unrollToCourse(StudentIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException {
        logger.info("***** UNROLL COURSE *****");
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
        logger.info("Resources found");

        if(!student.getCurrentCourses().contains(course)){
            logger.warn("Student is not enrolled to this course");
            throw new InvalidRequestException("Student is not enrolled to this course");
        }

        student.getCurrentCourses().remove(course);
        studentsRepository.save(student);
        logger.info("***** UNROLLED COURSE SUCCESSFULLY *****");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Unrolled successfully");
    }


    @Override
    public ResponseEntity<String> addPassedCourse(StudentIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        logger.info("***** ADD PASSED COURSE *****");
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
        logger.info("Resources found");

        if(!student.getCurrentCourses().contains(course)){
            logger.warn("Student is not enrolled to this course");
            throw new InvalidRequestException("Student is not enrolled to this course");
        }

        student.getPassedCourses().add(course);
        student.getCurrentCourses().remove(course);
        studentsRepository.save(student);
        logger.info("***** ADDED PASSED COURSE SUCCESSFULLY *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Added successfully");
    }

    @Override
    public ResponseEntity<String> enrollToDegree(StudentIdDegreeIdRequest request)
            throws ResourceNotFoundException {
        logger.info("***** STUDENT ENROLL *****");
        logger.info("Request: " + request);
        Optional<Student> studentOptional = studentsRepository.findById(request.getStudentId());
        if(studentOptional.isEmpty()){
            logger.warn("Student not found");
            throw new ResourceNotFoundException("Student not found");
        }
        Student student = studentOptional.get();

        Optional<Degree> degreeOptional = degreeRepository.findById(request.getDegreeId());
        if (degreeOptional.isEmpty()){
            logger.warn("Degree not found");
            throw new ResourceNotFoundException("Degree not found");
        }
        Degree degree = degreeOptional.get();
        logger.info("Resources found");

        student.setDegree(degree);
        studentsRepository.save(student);
        logger.info("***** SUCCESSFULLY ENROLLED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enrolled successfully");
    }


}
