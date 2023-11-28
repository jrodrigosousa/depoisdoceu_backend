package br.com.solverapps.depoisdoceu.controller;

import br.com.solverapps.depoisdoceu.business.RecipientService;
import br.com.solverapps.depoisdoceu.data.dto.NewRecipientDTO;
import br.com.solverapps.depoisdoceu.data.dto.RecipientDTO;
import br.com.solverapps.depoisdoceu.data.mapper.RecipientMapper;
import br.com.solverapps.depoisdoceu.data.model.Recipient;
import br.com.solverapps.depoisdoceu.data.model.User;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class RecipientController {

    @Autowired
    RecipientService recipientService;

    @Autowired
    RecipientMapper recipientMapper;

    @PostMapping("/recipients")
    public ResponseEntity<RecipientDTO> createRecipient(@RequestBody NewRecipientDTO recipientDTO, @Autowired Principal principal){
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        Recipient recipient = recipientMapper.toRecipient(recipientDTO, userId);
        Recipient newRecipient = recipientService.create(recipient, userId);
        return ResponseEntity.ok().body(recipientMapper.toRecipientDTO(newRecipient));
    }

    @PutMapping("/recipients")
    public ResponseEntity<RecipientDTO> updateRecipient(@RequestBody RecipientDTO recipientDTO, @Autowired Principal principal){
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        Recipient recipient = recipientMapper.toRecipient(recipientDTO, userId);
        Recipient savedRecipient = recipientService.update(recipient, userId);
        return ResponseEntity.ok().body(recipientMapper.toRecipientDTO(savedRecipient));
    }

    @DeleteMapping("/recipients/{id}")
    public ResponseEntity<Boolean> deleteRecipient(@PathVariable Integer id, @Autowired Principal principal){
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        return ResponseEntity.ok().body(recipientService.delete(id, userId));
    }

    @GetMapping("/recipients/{id}")
    public ResponseEntity<RecipientDTO> getRecipient(@PathVariable Integer id, @Autowired Principal principal){
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        Recipient recipient = recipientService.getById(id, userId);
        return ResponseEntity.ok().body(recipientMapper.toRecipientDTO(recipient));
    }

    @GetMapping("/recipients")
    public ResponseEntity<List<RecipientDTO>> getRecipients(@Autowired Principal principal){
        int userId = ((User)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        List<Recipient> recipients = recipientService.getAllRecipients(userId);
        List<RecipientDTO> recipientDTOs = recipients.stream().map((r)->recipientMapper.toRecipientDTO(r)).toList();
        return ResponseEntity.ok().body(recipientDTOs);
    }
}
