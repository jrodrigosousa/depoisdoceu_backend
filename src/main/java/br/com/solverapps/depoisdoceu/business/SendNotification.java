package br.com.solverapps.depoisdoceu.business;

import br.com.solverapps.depoisdoceu.data.model.Notification;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class SendNotification implements Runnable{


    Notification notification;

    public SendNotification(Notification notification){
        this.notification = notification;
    }

    Logger logger = LoggerFactory.getLogger(SendNotification.class);

    @Value("api.baseurl")
    String baseurl;

    @Override
    public void run() {
        EmailService emailService = new EmailService();
        emailService.sendEmail(
                "depoisdoceu.email@gmail.com",
                notification.getMessage().getUser().getEmail(),
                "Alerta de proximidade de envio",
                "Informo que a mensagem '"
                        +notification.getMessage().getTitle()
                        +"' está com envio programado para '"
                        +notification.getMessage().getToSendDate()
                        +"'.\nA qualquer momento será possível adiar o envio da mensagem acessando o seguinte link:"
                        +baseurl+"messages/reset/"+ notification.getMessage().getId()
        );
    }
}
