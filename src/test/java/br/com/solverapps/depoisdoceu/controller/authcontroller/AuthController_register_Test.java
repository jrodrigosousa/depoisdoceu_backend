package br.com.solverapps.depoisdoceu.controller.authcontroller;

import br.com.solverapps.depoisdoceu.business.AuthService;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthController_register_Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    void itShouldReturnOkWhenUserIsRegistered() throws Exception {
        when(authService.register(any())).thenReturn(true);
        this.mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("""
                            {
                                "login":"rodrigo",
                                "password":"teste",
                                "email":"j.rodrigo.sousa@gmail.com",
                                "name":"joao rodrigo",
                                "role":"ADMIN"
                            }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldFailWhenUserIsAlreadyRegistered() throws Exception {
        when(authService.register(any())).thenReturn(false);
        this.mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("""
                            {
                                "login":"rodrigo",
                                "password":"teste",
                                "email":"j.rodrigo.sousa@gmail.com",
                                "name":"joao rodrigo",
                                "role":"ADMIN"
                            }
                        """))
                .andExpect(status().isBadRequest());
    }
}