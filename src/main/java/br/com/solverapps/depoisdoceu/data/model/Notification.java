package br.com.solverapps.depoisdoceu.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    Integer minutesBefore;

    @Column(nullable = false)
    LocalDateTime toSendDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    Message message;

    public Notification(Integer minutesBefore, LocalDateTime toSendDate, Message message) {
        this.minutesBefore = minutesBefore;
        this.toSendDate = toSendDate;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }
}
