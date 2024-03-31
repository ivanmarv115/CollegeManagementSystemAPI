package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.ChangePasswordRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Service.Implementations.UserServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private UserServiceImpl underTest;

    @Test
    void shouldCreateUser() throws ResourceAlreadyExistsException {
        //given
        CreateUserRequest request = CreateUserRequest.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .build();

        User user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .isNonLocked(true)
                .build();

        given(userRepository.findByUsername(request.getUsername())).willReturn(
                Optional.empty());

        //when
        underTest.addUser(request);

        //test
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        assertThat(userArgumentCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldGetAllUsers(){
        //given
        User user = User.builder()
                .username("imartinezprof")
                .role(Role.PROFESSOR)
                .build();

        List<User> expectedResponse = new ArrayList<>();
        expectedResponse.add(user);

        given(userRepository.findAll()).willReturn(expectedResponse);

        //when
        ResponseEntity<List<User>> response = underTest.getAllUsers();

        //test
        verify(userRepository).findAll();
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    void shouldGetUsersContaining() throws ResourceNotFoundException {
        //given
        GetByRequest request = GetByRequest.builder()
                .param("imar")
                .build();

        User user = User.builder()
                .username("imartinezprof")
                .role(Role.PROFESSOR)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        given(userRepository.getAllBy(request.getParam())).willReturn(users);

        //when
        ResponseEntity<List<User>> response = underTest.getUsersContaining(request);

        //test
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(users);
    }

    @Test
    void shouldLockUser() throws ResourceNotFoundException {
        //given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        User user = User.builder()
                .username("admin")
                .isNonLocked(false)
                .role(Role.ADMIN)
                .build();

        given(userRepository.findById(request.getLongId())).willReturn(
                Optional.of(user));

        //when
        underTest.lockUser(request);

        //test
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        assertThat(userArgumentCaptor.getValue().getIsNonLocked())
                .isEqualTo(true);
    }

    @Test
    void shouldUnlockUser() throws ResourceNotFoundException {
        //given
        LongIdRequest request = LongIdRequest.builder()
                .longId(1L)
                .build();

        User user = User.builder()
                .username("admin")
                .isNonLocked(true)
                .role(Role.ADMIN)
                .build();

        given(userRepository.findById(request.getLongId())).willReturn(
                Optional.of(user));

        //when
        underTest.unlockUser(request);

        //test
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        assertThat(userArgumentCaptor.getValue().getIsNonLocked())
                .isEqualTo(false);
    }

    @Test
    void shouldChangePassword() throws InvalidRequestException, ResourceNotFoundException {
        //given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPwd("pass")
                .newPwd("newPass")
                .build();

        User user = User.builder()
                .id(1L)
                .username("imartinez")
                .password("pass")
                .build();

        String encodedPwd = "encodedPwd";

        given(authenticationService.getUser()).willReturn(user);
        given(passwordEncoder.encode(request.getNewPwd())).willReturn(encodedPwd);

        //when
        underTest.changePassword(request);

        //test
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        AssertionsForClassTypes.assertThat(userArgumentCaptor.getValue().getPassword())
                .isEqualTo(encodedPwd);
    }
}