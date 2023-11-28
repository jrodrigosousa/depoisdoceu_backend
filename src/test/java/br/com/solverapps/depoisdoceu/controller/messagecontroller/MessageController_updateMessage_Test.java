package br.com.solverapps.depoisdoceu.controller.messagecontroller;

import br.com.solverapps.VerifyOptions;
import br.com.solverapps.Verifyer;
import br.com.solverapps.depoisdoceu.TestIncludingDBData;
import br.com.solverapps.depoisdoceu.Util;
import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.Notification;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageController_updateMessage_Test  extends TestIncludingDBData {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    Util util;

    @BeforeEach
    void init(){
        observeTables(List.of("notification"));
    }

    @AfterEach
    void tearDown(){
        util.resetDB();
    }
    
    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void it_verifies_if_the_controller_really_doesnt_accept_to_update_a_message_with_null_id() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);

        //when + then
        ResultActions result = mockMvc.perform(put("/messages")
                .content("""
                        {
                            "id": null,
                            "title": "teste",
                            "text": "teste",
                            "toSendDate": "2023-12-12T12:00:00",
                            "toSendDelayInHours": 1,
                            "active": true,
                            "sentDate": null,
                            "alreadySent": false,
                            "userId": 1
                        }""")
                        .contentType("application/json")
                        .with(user(user)))
                .andExpect(status().is(400));

        Verifyer.verifyContent(result, Map.of("message", "The given id must not be null",
                                              "statusCode",400
                ));
    }

    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void it_verifyes_the_normal_situation_when_the_update_should_occur_and_succeed() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);
        int userId = user.getId();
        Message message = Message.builder()
                .id(1)
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();

        int messageId = messageRepository.save(message).getId();

        //when + then
        ResultActions result = mockMvc.perform(put("/messages")
                .content("""
                        {
                            "id": """+messageId+"""
                            ,
                            "title": "teste",
                            "text": "teste",
                            "toSendDate": "2023-12-12T12:00:00",
                            "toSendDelayInHours": 1,
                            "active": true,
                            "sentDate": null,
                            "alreadySent": false,
                            "userId": """+userId+"""
                        }""")
                .contentType("application/json")
                .with(user(user)))
                .andExpect(status().isOk());

        Verifyer.verifyContent(result, Map.of(
                "id", messageId,
                "title", "teste",
                "text", "teste",
                "toSendDelayInHours", 1,
                "active", true,
                "sentDate", VerifyOptions.NULL,
                "alreadySent", false
        ));
    }

    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void it_verifyes_that_is_not_possible_to_update_a_message_that_wasnt_registered_previously() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);

        //when + then
        ResultActions result = mockMvc.perform(put("/messages")
                .content("""
                        {
                            "id": 1,
                            "title": "teste",
                            "text": "teste",
                            "toSendDate": "2023-12-12T12:00:00",
                            "toSendDelayInHours": 1,
                            "active": true,
                            "sentDate": null,
                            "alreadySent": false,
                            "userId": 1
                        }""")
                .contentType("application/json")
                .with(user(user)))
                .andExpect(result1 -> assertThat(result1.getResolvedException()).isInstanceOf(EntityNotFoundException.class));

        Verifyer.verifyContent(result, Map.of("message", "There is no message with id 1",
                "statusCode",400
        ));
    }

    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void verifiesIfCorrectlyUpdateAMessageInsertingSomeNotifications() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);
        int userId = user.getId();
        Message message = Message.builder()
                .title("title prev")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();
        int messageId = messageRepository.save(message).getId();

        //when + then
        ResultActions result = mockMvc.perform(put("/messages")
                        .content("""
                        {
                            "id": 1,
                            "title": "teste post",
                            "text": "teste2",
                            "toSendDate": "2023-12-12T12:00:00",
                            "toSendDelayInHours": 1,
                            "active": true,
                            "sentDate": null,
                            "alreadySent": false,
                            "recipientIds": [],
                            "notifications": [
                                {
                                    "antecedence": 5,
                                    "date": "2023-12-01T05:00"
                                }
                            ]
                        }""")
                        .contentType("application/json")
                        .with(user(user)))
                .andExpect(status().isOk());

        Verifyer.verifyContent(result, Map.of(
                "notifications[0].antecedence", 5,
                "notifications[0].date", "2023-12-01T05:00"
        ));
    }

    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void verifiesIfCorrectlyUpdateAMessageThatAlreadyHadNotificationsToChangeItsNotifications() throws Exception {
        //given
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);
        int userId = user.getId();
        Message message = Message.builder()
                .title("title prev")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();
        message.setNotifications(List.of(new Notification(1, LocalDateTime.now(), message)));
        messageRepository.save(message).getId();

        //when + then
        ResultActions result = mockMvc.perform(put("/messages")
                        .content("""
                        {
                            "id": 1,
                            "title": "teste post",
                            "text": "teste2",
                            "toSendDate": "2023-12-12T12:00:00",
                            "toSendDelayInHours": 1,
                            "active": true,
                            "sentDate": null,
                            "alreadySent": false,
                            "recipientIds": [],
                            "notifications": [
                                {
                                    "antecedence": 5,
                                    "date": "2023-12-01T05:00"
                                }
                            ]
                        }""")
                        .contentType("application/json")
                        .with(user(user)))
                .andExpect(status().isOk());

        Verifyer.verifyContent(result, Map.of(
                "notifications[0].antecedence", 5,
                "notifications[0].date", "2023-12-01T05:00"
        ));

        assertThatTable("notification")
                .row(0)
                .column("id").value().isEqualTo(2);
    }

}