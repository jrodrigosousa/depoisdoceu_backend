package br.com.solverapps.depoisdoceu.data.repository;

import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.User;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageRepository_save_Test {

    @Value("${spring.datasource.url}")
    String databaseUrl;
    @Value("${spring.datasource.username}")
    String databaseUser;
    @Value("${spring.datasource.password}")
    String databasePassword;
    Source source;
    Table messageTable;
    @Autowired
    MessageRepository underTest;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init(){
        source = new Source(databaseUrl, databaseUser, databasePassword);
        messageTable = new Table(source, "message");
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    void test_if_the_relation_between_a_messagem_and_its_user_is_correctly_saved(){
        //given
        User user = userRepository.save(new User("name", "login", "password", "email", Role.ADMIN));
        Message message = Message.builder()
                .title("title")
                .text("text")
                .toSendDate(LocalDateTime.now())
                .toSendDelayInHours(1)
                .active(true)
                .user(user)
                .build();

        //when
        underTest.save(message);

        //then
        org.assertj.db.api.Assertions.assertThat(messageTable).hasNumberOfRows(1);
        org.assertj.db.api.Assertions.assertThat(messageTable).
                row(0)
                .column("title").value().isEqualTo("title")
                .column("user_id").value().isEqualTo(user.getId());
    }
}