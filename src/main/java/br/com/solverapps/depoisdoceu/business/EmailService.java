package br.com.solverapps.depoisdoceu.business;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EmailService {

    @Value("${sendgrid.api.key}")
    String key;

    Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String emailFrom, String emailTo, String title, String text) {
        Email from = new Email(emailFrom);
        String subject = title;
        Email to = new Email(emailTo);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(key);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error("Não foi possível enviar o email");
        }
        logger.info("Enviando e-mail com título: "+title);
    }

    public void sendEmail(String emailFrom, List<String> emailToList, String title, String text) {
        for(String emailTo: emailToList){
            sendEmail(emailFrom, emailTo, title, text);
        }
    }
}
