package br.com.solverapps.depoisdoceu.controller.recipientcontroller;

import br.com.solverapps.Verifyer;
import br.com.solverapps.depoisdoceu.Util;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipientController_createRecipient_Test {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    Util util;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown(){
        util.resetDB();
    }
    
    @Test
    void creates_a_new_recipient_in_a_normal_situation() throws Exception {
        //givin
        User registeredUser = new User("rodrigo", "rodrigo", "123456", "email", Role.USER);
        userRepository.save(registeredUser);

        //when + then
        mockMvc.perform(post("/recipients")
                        .content("""
                            {
                                "name": "brena",
                                "email": "brenakbs@gmail.com",
                                "userId": 1
                            }
                            """)
                        .contentType("application/json")
                        .with(user(registeredUser)))
                .andExpect(status().isOk());
    }



}