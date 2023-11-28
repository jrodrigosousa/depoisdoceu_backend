package br.com.solverapps.depoisdoceu.data.mapper;

import br.com.solverapps.depoisdoceu.data.dto.NotificationDTO;
import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationMapper {
    public Notification toNotification(NotificationDTO notificationDTO){
        LocalDateTime date = null;
        if(notificationDTO.date()!=null)
            date = LocalDateTime.parse(notificationDTO.date());
        return new Notification(
                notificationDTO.antecedence(),
                date,
                null);
    }

    public NotificationDTO toNotificationDTO(Notification notification){
        return new NotificationDTO(
                notification.getMinutesBefore(),
                notification.getToSendDate().toString());
    }

    public List<NotificationDTO> toNotificationDTOList(List<Notification> notifications){
        if(notifications==null)
            return new ArrayList<>();
        return notifications.stream().map((n)->{
            return this.toNotificationDTO(n);
        }).toList();
    }

    public List<Notification> toNotificationList(List<NotificationDTO> notificationDTOs){
        if(notificationDTOs==null)
            return new ArrayList<>();
        return notificationDTOs.stream().map((n)->{
            return this.toNotification(n);
        }).toList();
    }
}
