package br.com.solverapps.depoisdoceu.data.mapper;

import br.com.solverapps.depoisdoceu.business.RecipientService;
import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.data.dto.MessageDTO;
import br.com.solverapps.depoisdoceu.data.dto.NewMessageDTO;
import br.com.solverapps.depoisdoceu.data.dto.NewRecipientDTO;
import br.com.solverapps.depoisdoceu.data.dto.RecipientDTO;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipientMapper {

    @Autowired
    RecipientRepository recipientRepository;

    @Autowired
    UserRepository userRepository;

    public Recipient toRecipient(RecipientDTO recipientDTO, int loggedUserId) {
        Recipient recipient = recipientRepository.findById(recipientDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("There is no recipient with id "+recipientDTO.id()));
        List<Message> messages = recipient.getMessages();
        User user = userRepository.findById(loggedUserId)
                .orElseThrow(() ->new EntityNotFoundException("There is no user with id "+loggedUserId));

        return new Recipient(
                recipientDTO.id(),
                recipientDTO.name(),
                recipientDTO.email(),
                recipientDTO.whatsapp(),
                user
        );
    }

    public Recipient toRecipient(NewRecipientDTO recipientDTO, int loggedUserId) {
        User user = userRepository.findById(loggedUserId)
                .orElseThrow(() ->new EntityNotFoundException("There is no user with id "+loggedUserId));

        return new Recipient(
                null,
                recipientDTO.name(),
                recipientDTO.email(),
                recipientDTO.whatsapp(),
                user
        );
    }

    public RecipientDTO toRecipientDTO(Recipient recipient) {
        return new RecipientDTO(
                recipient.getId(),
                recipient.getName(),
                recipient.getEmail(),
                recipient.getWhatsapp()
        );
    }
}
