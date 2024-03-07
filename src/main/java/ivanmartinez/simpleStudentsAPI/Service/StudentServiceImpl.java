package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.DTO.IdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import ivanmartinez.simpleStudentsAPI.Utils.StudentUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentsRepository studentsRepository;
    Logger logger  = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentUtils studentUtils;
    private final UserService userService;

    @Override
    public ResponseEntity<String> createStudent(CreateStudentRequest createStudentRequest) throws CustomException {
        try {
            logger.info("***** BEGINNING CREATE STUDENT *****");
            logger.info("New Student: " + createStudentRequest);
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
            logger.error("ERROR WHEN TRYING TO CREATE STUDENT");
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<GetStudentsResponse>> getAllStudents() {
        logger.info("***** GET ALL STUDENTS CALLED *****");
        List<Student> students = studentsRepository.findAll();

        if(!students.isEmpty()){
            logger.info("***** FOUND STUDENTS *****");

            List<GetStudentsResponse> response = new ArrayList<>();

            for(Student student : students){
                response.add(studentUtils.studentEntityToStudentResponse(student));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        logger.warn("***** NO STUDENTS FOUND *****");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
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
    public ResponseEntity<String> deleteStudent(IdRequest idRequest) throws CustomException {
        logger.info("***** DELETE STUDENT CALLED *****");
        logger.info("Student idRequest: " + idRequest);
        Optional<Student> student = studentsRepository.findById(idRequest.getId());

        if(student.isPresent()){
            studentsRepository.delete(student.get());
            logger.info("***** STUDENT DELETED *****");
            return new ResponseEntity<>("Student with idRequest " + idRequest + " deleted", HttpStatus.ACCEPTED);
        }
        else{
            logger.info("***** STUDENT DOES NOT EXIST *****");
            throw new CustomException("Student with idRequest " + idRequest + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateStudent(Student student) throws CustomException {
        try {
            logger.info("***** UPDATE STUDENT CALLED *****");
            logger.info("New Student: " + student);
            Optional<Student> studentById = studentsRepository.findById(student.getId());


            if (studentById.isEmpty()) {
                logger.warn("Student not found");
                throw new CustomException("Student with id " + student.getId() + " not found",
                        HttpStatus.NOT_FOUND);
            }

            Student getStudentById = studentById.get();
            logger.info("Old Student: " + getStudentById);

            getStudentById.setFirstName(student.getFirstName());
            getStudentById.setLastName(student.getLastName());
            getStudentById.setCourse(student.getCourse());
            getStudentById.setDateOfBirth(student.getDateOfBirth());
            studentsRepository.save(getStudentById);
            logger.info("***** END UDPATE STUDENT *****");
            return new ResponseEntity<>("Student Updated",
                    HttpStatus.ACCEPTED);
        }catch (RuntimeException ex){
            logger.error(ex.getMessage());
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
