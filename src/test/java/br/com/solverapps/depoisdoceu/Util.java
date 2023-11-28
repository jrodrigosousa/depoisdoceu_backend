package br.com.solverapps.depoisdoceu;

import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Util {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    RecipientRepository recipientRepository;

    @Transactional
    public void resetDB(){
        recipientRepository.deleteAll();
        messageRepository.deleteAll();
        userRepository.deleteAll();

        entityManager.createNativeQuery("ALTER TABLE message AUTO_INCREMENT = 1")
                .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1")
                .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE recipient AUTO_INCREMENT = 1")
                .executeUpdate();
    }
}
