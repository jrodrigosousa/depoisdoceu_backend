package br.com.solverapps.depoisdoceu.business;

import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.business.exception.IncorrectEndpointUsageException;
import br.com.solverapps.depoisdoceu.data.dto.MessageDTO;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.repository.AppDAO;
import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final AppDAO appDAO;
    private final Scheduler scheduler;

    public Message getMessageById(int id) throws EntityNotFoundException {
        Message message = messageRepository.findById(id)
                .orElseThrow(() ->new EntityNotFoundException("There is no message with id "+id));
        return message;
    }

    private Message save(Message message) {
        scheduler.cancel(message);
        scheduler.schedule(message);
        return messageRepository.save(message);
    }

    public Message create(Message message) throws IncorrectEndpointUsageException {
        if(message.getId()==null)
            return save(message);
        else
            throw new IncorrectEndpointUsageException("It is not possible create a new message with a informed Id. Let it null.");
    }

    public Message update(Message message) throws EntityNotFoundException {
        if(messageRepository.existsById(message.getId()))
            return save(message);
        else
            throw new EntityNotFoundException("There is no message with id "+message.getId());
    }

    public void deleteMessageById(int id) throws EntityNotFoundException {
        if(messageRepository.existsById(id))
            messageRepository.deleteById(id);
        else
            throw new EntityNotFoundException("There is no message with id "+id);
    }

    public List<Message> getAllMessages(int userId) {
        return appDAO.getAllMessagesByUserId(userId);
    }

    public Message resetMessageById(int id, int loggedUserId) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() ->new EntityNotFoundException("There is no message with id "+id));

        message.setToSendDate(LocalDateTime.now().plusHours(message.getToSendDelayInHours()));

        message.setNotifications(message.getNotifications());

        return save(message);
    }
}
