package br.com.solverapps.depoisdoceu.controller.recipientcontroller;

import br.com.solverapps.Verifyer;
import br.com.solverapps.depoisdoceu.TestIncludingDBData;
import br.com.solverapps.depoisdoceu.Util;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipientController_updateRecipient_Test extends TestIncludingDBData{
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    Util util;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipientRepository recipientRepository;

    @BeforeEach
    void init(){
        observeTables(List.of("recipient"));
    }

    @AfterEach
    void tearDown(){
        util.resetDB();
    }
    
    @Test
    void updates_a_recipient_in_a_normal_situation() throws Exception {
        //givin
        User registeredUser = new User("rodrigo", "rodrigo", "123456", "email", Role.USER);
        userRepository.save(registeredUser);
        Recipient recipient = new Recipient(1,"recipient","email","whatsapp",registeredUser);
        recipientRepository.save(recipient);

        //when + then
        mockMvc.perform(put("/recipients")
                        .content("""
                            {
                                "id": 1,
                                "name": "brena",
                                "email": "brenakbs@gmail.com",
                                "userId": 1
                            }
                            """)
                        .contentType("application/json")
                        .with(user(registeredUser)))
                .andExpect(status().isOk());

        assertThatTable("recipient")
                .row(0)
                .column("name").value().isEqualTo("brena");
    }


    @Test
    void returns_error_when_tries_to_update_a_inexistent_recipient() throws Exception {
        //givin
        User registeredUser = new User("rodrigo", "rodrigo", "123456", "email", Role.USER);
        userRepository.save(registeredUser);

        //when + then
        ResultActions result = mockMvc.perform(put("/recipients")
                        .content("""
                                {
                                    "id": 1,
                                    "name": "brena",
                                    "email": "brenakbs@gmail.com",
                                    "userId": 1
                                }
                                """)
                        .contentType("application/json")
                        .with(user(registeredUser)))
                .andExpect(status().isBadRequest());

        Verifyer.verifyContent(result, Map.of(
                "statusCode", 400,
                "message", "There is no recipient with id 1"
        ));
    }

}