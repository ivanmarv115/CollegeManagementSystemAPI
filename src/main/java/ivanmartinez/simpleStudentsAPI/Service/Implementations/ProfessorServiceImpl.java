package ivanmartinez.simpleStudentsAPI.Service.Implementations;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.GetCourseResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.AssignCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.CreateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.UpdateProfessorRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.ProfessorRepository;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Service.ProfessorService;
import ivanmartinez.simpleStudentsAPI.Service.UserService;
import ivanmartinez.simpleStudentsAPI.Utils.ProfessorUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ProfessorUtils professorUtils;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    Logger logger = LoggerFactory.getLogger(ProfessorService.class);

    @Override
    public ResponseEntity<List<GetProfessorResponse>> getAllProfessors(){
        logger.info("***** GET PROFESSORS CALLED *****");
        List<Professor> professors = professorRepository.findAll();
        List<GetProfessorResponse> responses = new ArrayList<>();

        for (Professor professor : professors) {
            responses.add(professorUtils.
                    professorToGetProfessorResponse(professor));
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GetProfessorResponse>> getProfessorsContaining(GetByRequest request)
            throws ResourceNotFoundException {
        logger.info("***** GET PROFESSORS CONTAINING *****");
        logger.info("Request: " + request);

        List<GetProfessorResponse> response = new ArrayList<>();
        if(request.getId() != null){
            Optional<Professor> professorOptional = professorRepository.findById(request.getId());
            if (professorOptional.isEmpty()) {
                throw new ResourceNotFoundException("Professor not found");
            }
            response.add(professorUtils.professorToGetProfessorResponse(professorOptional.get()));
        } else if (request.getParam() != null) {
            List<Professor> professors = professorRepository.getAllBy(request.getParam());
            for(Professor professor : professors){
                GetProfessorResponse responseElement = professorUtils.professorToGetProfessorResponse(professor);
                response.add(responseElement);
            }
        }

        logger.info("***** REQUEST OK *****");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Long> createProfessor(CreateProfessorRequest createProfessorRequest)
            throws ResourceAlreadyExistsException {
            logger.info("***** CREATE PROFESSOR BEGIN *****");
            logger.info("Request: " + createProfessorRequest);
            if(userRepository.findByUsername(createProfessorRequest.getUsername())
                    .isPresent()){
                logger.warn("Username already exists");
                throw new ResourceAlreadyExistsException("Username already exists");
            }

            Professor professor = professorUtils
                    .createProfessorRequestToProfessorEntity(
                            createProfessorRequest
                    );
            logger.info("Creating new user");
            CreateUserRequest createUserRequest = professorUtils
                    .createProfessorRequestToCreateUserRequest(
                            createProfessorRequest
                    );
            User user = userService.createUser(createUserRequest);
            professor.setUser(user);

            professor = professorRepository.save(professor);
            logger.info("New professor saved");
            logger.info("***** CREATE PROFESSOR END *****");
            return new ResponseEntity<>(professor.getId(), HttpStatus.CREATED);
        }

        @Override
    public ResponseEntity<Professor> updateProfessor(UpdateProfessorRequest updateProfessorRequest) throws ResourceNotFoundException {
        logger.info("***** UPDATE PROFESSOR BEGIN *****");
        logger.info("New professor: " + updateProfessorRequest);
        Optional<Professor> professorOptional = professorRepository.findById(
                updateProfessorRequest.getId()
        );
        if (professorOptional.isEmpty()) {
            logger.warn("Professor with requested id does not exist");
            throw new ResourceNotFoundException("Professor not found");
        }
        Professor professor = professorOptional.get();
        logger.info("Previous professor: " + professor);
        professor.setFirstName(updateProfessorRequest.getFirstName());
        professor.setLastName(updateProfessorRequest.getLastName());
        professor.getUser().setUsername(
                updateProfessorRequest.getUsername()
        );
        professorRepository.save(professor);
        logger.info("PROFESSOR UPDATED CORRECTLY");
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<String> deleteProfessor(LongIdRequest request) throws ResourceNotFoundException {
        logger.info("***** DELETE PROFESSOR *****");
        logger.info("Request: " + request);
        Optional<Professor> professorOptional = professorRepository.findById(request.getLongId());
        if(professorOptional.isEmpty()){
            logger.warn("***** PROFESSOR DOES NOT FOUND *****");
            throw new ResourceNotFoundException("Professor not found");
        }
        professorRepository.delete(professorOptional.get());
        logger.info("***** PROFESSOR DELETED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Professor deleted");
    }

    @Override
    public ResponseEntity<String> assignCourse(AssignCourseRequest request)
            throws ResourceNotFoundException {
        logger.info("***** ASSIGN COURSE *****");
        logger.info("Request: " + request);
        Optional<Professor> professorOptional = professorRepository.findById(request.getProfessorId());
        if(professorOptional.isEmpty()){
            logger.warn("Professor not found");
            throw new ResourceNotFoundException("Professor not found");
        }

        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("Course not found");
            throw new ResourceNotFoundException("Course not found");
        }

        Professor professor = professorOptional.get();
        Course course = courseOptional.get();

        professor.getCoursesTaught().add(course);
        course.getProfessors().add(professor);
        professorRepository.save(professor);
        courseRepository.save(course);

        logger.info("SUCCESS");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Assigned successfully");
    }

}
