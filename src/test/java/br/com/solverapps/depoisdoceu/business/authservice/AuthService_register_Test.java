package br.com.solverapps.depoisdoceu.business.authservice;

import br.com.solverapps.depoisdoceu.business.AuthService;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.dto.RegisterDTO;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import br.com.solverapps.depoisdoceu.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthService_register_Test {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;
    private AuthService underTest;
    private AutoCloseable autocloseble;

    @BeforeEach
    public void setUp(){
        underTest = new AuthService(authenticationManager, userRepository, jwtService);
    }

    @Test
    public void itRegisterAnUserInNormalSituation(){
        //GIVEN
        RegisterDTO dto = new RegisterDTO("rodrigo", "rodrigo", "123", "email@gmail.com", Role.ADMIN);

        //WHEN
        when(userRepository.findUserByLogin("rodrigo")).thenReturn(Optional.empty());
        Boolean registered = underTest.register(dto);

        //THEN
        assertThat(registered).isTrue();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertThat(savedUser.getName()).isEqualTo("rodrigo");
        assertThat(savedUser.getLogin()).isEqualTo("rodrigo");
        assertThat(savedUser.getEmail()).isEqualTo("email@gmail.com");
        assertThat(savedUser.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    public void itTriesToRegisterAnExistingUserAndFail(){
        //GIVEN
        RegisterDTO dto = new RegisterDTO("rodrigo", "rodrigo", "123", "email@gmail.com", Role.ADMIN);
        User existingUser = new User("rodrigo", "rodrigo", "123", "email@gmail.com", Role.ADMIN);

        //WHEN
        when(userRepository.findUserByLogin("rodrigo")).thenReturn(Optional.of(existingUser));
        Boolean registered = underTest.register(dto);

        //THEN
        assertThat(registered).isFalse();
    }

    @Test
    public void itTriesToRegisterAnUserWithAnAlreadyTakenEmailAndFail(){
        //GIVEN
        RegisterDTO dto = new RegisterDTO("rodrigo", "rodrigo", "123", "email@gmail.com", Role.ADMIN);
        User existingUser = new User("rodrigo", "rodrigo2", "123", "email@gmail.com", Role.ADMIN);

        //WHEN + THEN
        when(userRepository.findUserByLogin("rodrigo")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(()->underTest.register(dto))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void itTriesToRegisterAnUserWithEmptyNameAndFail(){
        //GIVEN
        RegisterDTO dto = new RegisterDTO("", "rodrigo", "123", "email@gmail.com", Role.ADMIN);

        //WHEN
        Boolean registered = underTest.register(dto);

        //THEN
        assertThat(registered).isFalse();
    }

    @Test
    public void itTriesToRegisterAnUserWithNullNameAndFail(){
        //GIVEN
        RegisterDTO dto = new RegisterDTO(null, "rodrigo", "123", "email@gmail.com", Role.ADMIN);

        //WHEN
        Boolean registered = underTest.register(dto);

        //THEN
        assertThat(registered).isFalse();
    }

}