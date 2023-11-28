package br.com.solverapps.depoisdoceu.data.model;

import br.com.solverapps.depoisdoceu.constants.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Message_user_Test {

    @Test
    void it_verifies_that_the_user_of_a_message_can_be_defined_and_consulted() {
        //given
        User user = new User("name", "login", "password", "email", Role.ADMIN);
        Message message = Message.builder()
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();
        //when

        //then
        
    }

}