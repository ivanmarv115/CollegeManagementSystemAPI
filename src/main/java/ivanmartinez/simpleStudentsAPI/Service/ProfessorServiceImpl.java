package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.*;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.ProfessorRepository;
import ivanmartinez.simpleStudentsAPI.Utils.ProfessorUtils;
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
public class ProfessorServiceImpl implements ProfessorService{

    private final ProfessorRepository professorRepository;
    private final ProfessorUtils professorUtils;
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(ProfessorService.class);

    @Override
    public ResponseEntity<List<GetProfessorResponse>> getAllProfessors() {
        logger.info("***** GET PROFESSORS CALLED *****");
        List<Professor> professors = professorRepository.findAll();
        List<GetProfessorResponse> responses = new ArrayList<>();

        for (Professor professor : professors){
            responses.add(professorUtils.
                    professorToGetProfessorResponse(professor));
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Professor> getProfessorById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Professor> createProfessor(CreateProfessorRequest createProfessorRequest) throws CustomException {
        try {
            logger.info("***** CREATE PROFESSOR BEGIN *****");
            logger.info("Request: " + createProfessorRequest);
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

            professorRepository.save(professor);
            logger.info("New professor saved");
            logger.info("***** CREATE PROFESSOR END *****");
            return new ResponseEntity<>(professor, HttpStatus.CREATED);
        }catch (RuntimeException ex){
            logger.error("ERROR WHEN TRYING TO CREATE PROFESSOR");
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Professor> updateProfessor(UpdateProfessorRequest updateProfessorRequest) throws CustomException {
        try {
            logger.info("***** UPDATE PROFESSOR BEGIN *****");
            logger.info("New professor: " + updateProfessorRequest);
            Optional<Professor> professorOptional = professorRepository.findById(
                    updateProfessorRequest.getId()
            );
            if (professorOptional.isEmpty()) {
                logger.warn("Professor with requested id does not exist");
                throw new CustomException("Professor not found", HttpStatus.NOT_FOUND);
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
        }catch (RuntimeException ex){
            logger.error("ERROR WHEN TRYING TO UPDATE PROFESSOR");
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> deleteProfessor(IdRequest id) {
        return null;
    }
}
