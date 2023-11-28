package br.com.solverapps.depoisdoceu.business;

import br.com.solverapps.depoisdoceu.data.model.Message;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class SendMessage implements Runnable{

    Message message;

    public SendMessage(Message message){
        this.message = message;
    }

    Logger logger = LoggerFactory.getLogger(SendMessage.class);

    @Override
    public void run() {
        EmailService emailService = new EmailService();
        List recipientsEmails = new ArrayList<>();
        message.getRecipients().stream().forEach((r)->{
            recipientsEmails.add(r.getEmail());
        });
        emailService.sendEmail(
                message.getUser().getEmail(),
                recipientsEmails,
                message.getTitle(),
                message.getText()
        );
    }

}
