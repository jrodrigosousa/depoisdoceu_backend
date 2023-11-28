package br.com.solverapps.depoisdoceu.data.repository;

import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.User;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserRepository_save_Test {

    @Value("${spring.datasource.url}")
    String databaseUrl;
    @Value("${spring.datasource.username}")
    String databaseUser;
    @Value("${spring.datasource.password}")
    String databasePassword;
    Source source;
    Table userTable;
    @Autowired
    UserRepository underTest;

    @BeforeEach
    void init(){
        source = new Source(databaseUrl, databaseUser, databasePassword);
        userTable = new Table(source, "users");
        underTest.deleteAll();
    }

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void verifyesIfTheUserIsCorrectlySaved(){
        //GIVEN
        User user = new User("name", "login", "password", "email", Role.ADMIN);

        //WHEN
        underTest.save(user);

        //THEN
        org.assertj.db.api.Assertions.assertThat(userTable).hasNumberOfRows(1);
        org.assertj.db.api.Assertions.assertThat(userTable).
                row(0)
                .column("name").value().isEqualTo("name");
    }

    @Test
    void verifyesThatAnotherUserCantBeSavedWithSameEmail(){
        //GIVEN
        User previousUser = new User("name", "login", "password", "email", Role.ADMIN);
        underTest.save(previousUser);
        User newUser = new User("name2", "login2", "password2", "email", Role.ADMIN);

        //WHEN + THEN
        assertThatThrownBy(()->underTest.save(newUser))
                .isInstanceOf(DataIntegrityViolationException.class);
        org.assertj.db.api.Assertions.assertThat(userTable).hasNumberOfRows(1);
        org.assertj.db.api.Assertions.assertThat(userTable).
                row(0)
                .column("name").value().isEqualTo("name");
    }

}