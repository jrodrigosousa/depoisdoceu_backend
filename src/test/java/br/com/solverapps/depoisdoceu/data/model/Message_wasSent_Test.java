package br.com.solverapps.depoisdoceu.data.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class Message_wasSent_Test {

    @Mock
    User user;

    @Test
    void itVerifiesThatTheMessageIsCreatedInANotSentState() {
        //given
        Message message = Message.builder()
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();

        //then
        assertThat(message.wasSent()).isFalse();
    }

    @Test
    void itVerifiesThatTheMessageIsConsideredNotSentIfTheSentDateIsNull() {
        //given
        Message message = Message.builder()
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();
        message.sentDate = null;

        //then
        assertThat(message.wasSent()).isFalse();
    }

    @Test
    void itVerifiesThatTheMessageIsConsideredSentIfTheSentDateIsNotNull() {
        //given
        Message message = Message.builder()
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();
        message.sentDate = LocalDateTime.now();

        //then
        assertThat(message.wasSent()).isTrue();
    }
}