package br.com.solverapps.depoisdoceu.business.recipientservice;

import br.com.solverapps.depoisdoceu.business.RecipientService;
import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.constants.Role;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RecipientService_delete_Test {

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
    void deletesTheRecipientInNormalSituation(){
        //givin
        User registeredUser = new User(1, "rodrigo", "rodrigo", "123456", "email", Role.USER, null);
        Recipient recipient = new Recipient(1, "recipient", "email", null, registeredUser);

        //when
        when(recipientRepository.getReferenceById(1)).thenReturn(recipient);
        when(recipientRepository.existsById(1)).thenReturn(true);
        underTest.delete(1, 1);

        //then
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(recipientRepository).deleteById(captor.capture());
        Integer deletedId = captor.getValue();
        assertThat(deletedId).isEqualTo(1);
    }

    @Test
    void triesToDeleteARecipientFromAnotherUserAndDoesntWork()
    {
        //givin
        User anotherUser = new User(1, "rodrigo", "rodrigo", "123456", "email", Role.USER, null);
        Recipient recipient = new Recipient(1, "recipient", "email", null, anotherUser);

        //when
        when(recipientRepository.getReferenceById(1)).thenReturn(recipient);
        when(recipientRepository.existsById(1)).thenReturn(true);
        assertThatThrownBy(() -> underTest.delete(1, 2))
                .isInstanceOf(AccessDeniedException.class);

        //then
        verify(recipientRepository, never()).deleteById(null);
    }

    @Test
    void triesToDeleteAnInexistingRecipientAndDoesntWork()
    {
        //givin
        User anotherUser = new User(1, "rodrigo", "rodrigo", "123456", "email", Role.USER, null);

        //when
        assertThatThrownBy(() -> underTest.delete(1, 2))
                .isInstanceOf(EntityNotFoundException.class);

        //then
        verify(recipientRepository, never()).deleteById(null);
    }

}