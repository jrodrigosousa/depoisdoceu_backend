package br.com.solverapps.depoisdoceu.controller.recipientcontroller;

import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipientController_deleteRecipient_Test {

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenTriesToDeleteAnInexistingUserDoesntWork() throws Exception {
        //givin
        User registeredUser = new User(1, "rodrigo", "rodrigo", "123456", "email", Role.USER, null);

        //when + then
        mockMvc.perform(delete("/recipients/1")
                        .with(user(registeredUser)))
                .andExpect(status().isBadRequest());
    }
}
