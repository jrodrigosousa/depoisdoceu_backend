package br.com.solverapps.depoisdoceu.controller.messagecontroller;

import br.com.solverapps.Verifyer;
import br.com.solverapps.depoisdoceu.Util;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageController_deleteMessage_Test {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    Util util;

    @AfterEach
    void tearDown(){
        util.resetDB();
    }
    
    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void delete_an_existing_message_in_normal_situation() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);
        Message message = Message.builder()
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();
        messageRepository.save(message);

        //when + then
        mockMvc.perform(delete("/messages/1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("true"));
    }

    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void delete_an_inexisting_message_and_throws_an_error() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);

        //when + then
        ResultActions result = mockMvc.perform(delete("/messages/1"))
                .andExpect(status().isBadRequest());


        Verifyer.verifyContent(result, Map.of("message", "There is no message with id 1",
                "statusCode",400
        ));
    }

}