package br.com.solverapps.depoisdoceu.controller.messagecontroller;

import br.com.solverapps.depoisdoceu.Util;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageController_createMessage_Test {

    @Value("${spring.datasource.url}")
    String databaseUrl;
    @Value("${spring.datasource.username}")
    String databaseUser;
    @Value("${spring.datasource.password}")
    String databasePassword;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    Source source;

    Table messageTable;

    @Autowired
    Util util;

    @BeforeEach
    void init(){
        source = new Source(databaseUrl, databaseUser, databasePassword);
        messageTable = new Table(source, "message");
    }

    @AfterEach
    void tearDown(){
        util.resetDB();
    }
    
    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void creates_a_new_message_in_a_normal_situation() throws Exception {
        //givin
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);

        //when + Then
        mockMvc.perform(post("/messages")
                .content("""
                        {
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
                .andExpect(status().isOk());

        org.assertj.db.api.Assertions.assertThat(messageTable).hasNumberOfRows(1);
    }

    @Test
    @WithMockUser(username = "rodrigo", roles = {"USER"})
    void on_the_new_message_creation_completely_ignores_the_given_id() throws Exception {
        //givin
        User user = new User("rodrigo", "rodrigo", "password", "email", Role.USER);
        user = userRepository.save(user);

        //when + Then
        mockMvc.perform(post("/messages")
                        .content("""
                        {
                            "id": 5,
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
                .andExpect(status().isOk());

        org.assertj.db.api.Assertions.assertThat(messageTable)
                .hasNumberOfRows(1)
                .row(0).column("id").equals(1);
    }
}
