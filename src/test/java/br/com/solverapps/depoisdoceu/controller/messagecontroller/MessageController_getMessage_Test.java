package br.com.solverapps.depoisdoceu.controller.messagecontroller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.solverapps.Verifyer;
import br.com.solverapps.VerifyOptions;

@SpringBootTest
@AutoConfigureMockMvc
class MessageController_getMessage_Test {
    
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
    @WithMockUser(username="rodrigo",roles={"USER"})
    void test_if_the_details_of_a_registered_message_can_be_retrieved() throws Exception {
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
        ResultActions result = mockMvc.perform(get("/messages/1"))
                .andExpect(status().isOk());

        Verifyer.verifyContent(result, Map.of("id",1,
                                            "title", "title",
                                            "text", "text",
                                            "active", true,
                                            "sentDate", VerifyOptions.NULL,
                                            "alreadySent",false
                                ));

    }


}