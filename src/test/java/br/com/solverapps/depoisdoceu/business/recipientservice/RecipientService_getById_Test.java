package br.com.solverapps.depoisdoceu.business.recipientservice;

import br.com.solverapps.depoisdoceu.business.RecipientService;
import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecipientService_getById_Test {


    @Mock
    UserRepository userRepository;

    @Mock
    RecipientRepository recipientRepository;

    RecipientService underTest;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void init(){
        underTest = new RecipientService(recipientRepository, userRepository, entityManager);
    }

    @Test
    void testInTheCaseOfANormalAndSuccessfulGet(){
        //givin
        User registeredUser = new User(1, "rodrigo", "rodrigo", "123456", "email", Role.USER, null);
        Recipient recipient = new Recipient(1, "recipient", "email", null, registeredUser);

        //when
        when(recipientRepository.findById(1)).thenReturn(Optional.of(recipient));
        Recipient result = underTest.getById(1, 1);

        //then
        assertThat(result.getName()).isEqualTo("recipient");
    }

    @Test
    void itTriesToGetARecipientFromAnotherUser(){
        //givin
        User registeredUser = new User(1, "rodrigo", "rodrigo", "123456", "email", Role.USER, null);
        Recipient recipient = new Recipient(1, "recipient", "email", null, registeredUser);

        //when
        when(recipientRepository.findById(1)).thenReturn(Optional.of(recipient));
        assertThatThrownBy(() -> underTest.getById(1, 2))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void itTriesToGetANonexistentRecipient(){
        //when + then
        when(recipientRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getById(1, 1))
                .isInstanceOf(EntityNotFoundException.class);
    }
}