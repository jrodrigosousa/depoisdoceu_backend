package br.com.solverapps.depoisdoceu.business;

import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.data.dto.RecipientDTO;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipientService {

    final RecipientRepository recipientRepository;

    final UserRepository userRepository;

    final EntityManager entityManager;

    public RecipientService(RecipientRepository recipientRepository, UserRepository userRepository, EntityManager entityManager
    ) {
        this.recipientRepository = recipientRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    public Recipient create(Recipient recipient, int loggedUserId) {
        if(recipient.getUser()==null)
            recipient.setUser(userRepository.getReferenceById(loggedUserId));
        else if(recipient.getUser().getId() != loggedUserId)
            throw new AccessDeniedException("It is only possible to add recipients to the current user.");
        return recipientRepository.save(recipient);
    }

    public Recipient update(Recipient recipient, int loggedUserId) {
        if(recipient.getUser().getId() != loggedUserId)
            throw new AccessDeniedException("It is only possible to update recipients from the current user.");
        return recipientRepository.save(recipient);
    }

    public Boolean delete(Integer recipientId, int loggedUserId) {
        if(!recipientRepository.existsById(recipientId))
            throw new EntityNotFoundException("There is no recipient with id "+recipientId);
        Recipient recipient = recipientRepository.getReferenceById(recipientId);
        if(recipient.getUser().getId() != loggedUserId)
            throw new AccessDeniedException("It is only possible to delete recipients from the current user.");
        recipientRepository.deleteById(recipientId);
        return true;
    }

    public Recipient getById(Integer recipientId, int loggedUserId) {
        EntityNotFoundException entityNotFoundException =
                new EntityNotFoundException("There is no recipient with id " + recipientId);
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> entityNotFoundException);
        if(recipient.getUser().getId() != loggedUserId)
            throw entityNotFoundException;

        return recipient;
    }

    public List<Recipient> getAllRecipients(int loggedUserId){
        return entityManager.createQuery("select r from Recipient r inner join r.user u where u.id = :id", Recipient.class)
                .setParameter("id", loggedUserId)
                .getResultList();
    }
}
