package br.com.solverapps.depoisdoceu.data.mapper;

import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.data.dto.MessageDTO;
import br.com.solverapps.depoisdoceu.data.dto.NewMessageDTO;
import br.com.solverapps.depoisdoceu.data.dto.NotificationDTO;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.repository.MessageRepository;
import br.com.solverapps.depoisdoceu.data.repository.RecipientRepository;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RecipientRepository recipientRepository;

    @Autowired NotificationMapper notificationMapper;

    public Message toMessage(MessageDTO messageDTO, int loggedUserId) {
        final List<Recipient> recipients = new ArrayList<>();
        if(messageDTO.recipientIds()!=null) {
            messageDTO.recipientIds().stream().forEach((id) -> {
                recipients.add(recipientRepository.getReferenceById(id));
            });
        }

        Message message = Message.builder()
                .id(messageDTO.id())
                .title(messageDTO.title())
                .text(messageDTO.text())
                .toSendDelayInHours(messageDTO.toSendDelayInHours())
                .active(messageDTO.active())
                .sentDate(messageDTO.sentDate())
                .user(userRepository.getReferenceById(loggedUserId))
                .recipients(recipients)
                .build();

        message.setToSendDate(LocalDateTime.now().plusHours(message.getToSendDelayInHours()));

        message.setNotifications(notificationMapper.toNotificationList(messageDTO.notifications()));

        return message;
    }

    public Message toMessage(NewMessageDTO messageDTO, int loggedUserId) {
        final List<Recipient> recipients = new ArrayList<>();
        if(messageDTO.recipientIds()!=null) {
            messageDTO.recipientIds().stream().forEach((id) -> {
                recipients.add(recipientRepository.getReferenceById(id));
            });
        }

        return new Message(
                messageDTO.title(),
                messageDTO.text(),
                messageDTO.toSendDate(),
                messageDTO.toSendDelayInHours(),
                messageDTO.active(),
                userRepository.getReferenceById(loggedUserId),
                recipients
        );
    }

    public MessageDTO toMessageDTO(Message message) {
        return new MessageDTO(message.getId(),
                message.getTitle(),
                message.getText(),
                message.getToSendDate(),
                message.getToSendDelayInHours(),
                message.getActive(),
                message.getSentDate(),
                message.wasSent(),
                message.getRecipients().stream().map((r)->r.getId()).toList(),
                notificationMapper.toNotificationDTOList(message.getNotifications())
        );
    }

}
