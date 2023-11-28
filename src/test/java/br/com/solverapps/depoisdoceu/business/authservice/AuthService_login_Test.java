package br.com.solverapps.depoisdoceu.business.authservice;

import br.com.solverapps.depoisdoceu.business.AuthService;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.dto.AuthenticationDTO;
import br.com.solverapps.depoisdoceu.data.dto.LoginResponseDTO;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import br.com.solverapps.depoisdoceu.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthService_login_Test {
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;

    AuthService underTest;

    @BeforeEach
    void setUp(){
        underTest = new AuthService(authenticationManager, userRepository, jwtService);
    }

    @Test
    void itVerifiesIfTheLoginWorksInNormalSituation() {
        //GIVEN
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("rodrigo", "123");
        User user = new User("rodrigo", "rodrigo", "123", "email@gmail.com", Role.ADMIN);
        TestingAuthenticationToken authToken = new TestingAuthenticationToken(user, null);

        //WHEN
        when(authenticationManager.authenticate(any())).thenReturn(authToken);
        when(jwtService.generateToken(user)).thenReturn("token");
        LoginResponseDTO response = underTest.login(authenticationDTO);

        //THEN
        assertThat(response).isEqualTo(new LoginResponseDTO("token"));
    }

    @Test
    void itVerifiesIfTheLoginFailWhenTheCredentialsAreWrong() {
        //GIVEN
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("rodrigo", "123");
        User user = new User("rodrigo", "rodrigo", "123", "email@gmail.com", Role.ADMIN);
        TestingAuthenticationToken authToken = new TestingAuthenticationToken(user, null);

        //WHEN
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("error"));
        assertThatThrownBy(()->underTest.login(authenticationDTO))
                .isInstanceOf(AuthenticationException.class);
    }
}