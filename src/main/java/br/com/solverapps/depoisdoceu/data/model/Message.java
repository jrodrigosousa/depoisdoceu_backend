package br.com.solverapps.depoisdoceu.data.model;

import br.com.solverapps.depoisdoceu.business.UserService;
import br.com.solverapps.depoisdoceu.data.dto.MessageDTO;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "message")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String text;

    @Column(nullable = false)
    LocalDateTime toSendDate;

    @Column(nullable = false)
    Integer toSendDelayInHours;

    @Column(nullable = false)
    Boolean active;

    LocalDateTime sentDate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable=false)
    User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "message_recipient",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    List<Recipient> recipients;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "message",orphanRemoval = true)
    @Builder.Default
    List<Notification> notifications = new ArrayList<>();

    public Message(String title, String text, LocalDateTime toSendDate, Integer toSendDelayInHours, Boolean active, User user, List<Recipient> recipients) {
        this.title = title;
        this.text = text;
        this.toSendDate = toSendDate;
        this.toSendDelayInHours = toSendDelayInHours;
        this.active = active;
        this.user = user;
        this.recipients = recipients;
    }

    public boolean wasSent(){
        return sentDate != null;
    }

    public void setNotifications(List<Notification> newNotifications) {
        this.notifications.clear();
        newNotifications.stream().forEach((notification -> {
            addNotification(notification);
        }));
    }

    public void addNotification(Notification notification){
        notification.message = this;
        notification.toSendDate = this.toSendDate.minusMinutes(notification.minutesBefore);
        notifications.add(notification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}