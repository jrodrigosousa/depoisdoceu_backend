package br.com.solverapps.depoisdoceu.controller;

import br.com.solverapps.depoisdoceu.business.MessageService;
import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.business.exception.IncorrectEndpointUsageException;
import br.com.solverapps.depoisdoceu.data.dto.MessageDTO;
import br.com.solverapps.depoisdoceu.data.dto.NewMessageDTO;
import br.com.solverapps.depoisdoceu.data.mapper.MessageMapper;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    final MessageService messageService;
    final MessageMapper messageMapper;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getAllMessages(@Autowired Principal principal) throws EntityNotFoundException {
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        List<Message> messages = messageService.getAllMessages(userId);
        List<MessageDTO> response = messages.stream().map((m)->messageMapper.toMessageDTO(m)).toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable int id) throws EntityNotFoundException {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.ok().body(messageMapper.toMessageDTO(message));
    }

    @DeleteMapping("/messages/{id}")
    public Boolean deleteMessage(@PathVariable int id) throws EntityNotFoundException {
        messageService.deleteMessageById(id);
        return true;
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> createMessage(@RequestBody NewMessageDTO messageDTO, @Autowired Principal principal) throws IncorrectEndpointUsageException {
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        Message message = messageMapper.toMessage(messageDTO, userId);
        MessageDTO savedMessage = messageMapper.toMessageDTO(messageService.create(message));
        return ResponseEntity.ok().body(savedMessage);
    }

    @PutMapping("/messages")
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody MessageDTO messageDTO, @Autowired Principal principal) throws IncorrectEndpointUsageException, EntityNotFoundException {
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        Message message = messageMapper.toMessage(messageDTO, userId);
        MessageDTO savedMessage = messageMapper.toMessageDTO(messageService.update(message));
        return ResponseEntity.ok().body(savedMessage);
    }

    @PutMapping("/messages/reset/{id}")
    public ResponseEntity<MessageDTO> resetMessage(@PathVariable int id, @Autowired Principal principal) throws IncorrectEndpointUsageException, EntityNotFoundException {
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        Message message = messageService.resetMessageById(id, userId);
        return ResponseEntity.ok().body(messageMapper.toMessageDTO(message));
    }
}
