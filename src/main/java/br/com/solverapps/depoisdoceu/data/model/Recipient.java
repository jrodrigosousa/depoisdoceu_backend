package br.com.solverapps.depoisdoceu.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.IdGeneratorType;

import java.util.List;

@Entity
@Table(name = "recipient")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    String email;

    String whatsapp;

    @ManyToMany(mappedBy = "recipients", fetch = FetchType.LAZY)
    List<Message> messages;

    @ManyToOne
    User user;

    public Recipient(Integer id, String name, String email, String whatsapp, User user) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.whatsapp = whatsapp;
        this.user = user;
    }

    public static final class RecipientBuilder {
        private Recipient recipient;

        private RecipientBuilder() {
            recipient = new Recipient();
        }

        public static RecipientBuilder aRecipient() {
            return new RecipientBuilder();
        }

        public RecipientBuilder withId(Integer id) {
            recipient.setId(id);
            return this;
        }

        public RecipientBuilder withName(String name) {
            recipient.setName(name);
            return this;
        }

        public RecipientBuilder withEmail(String email) {
            recipient.setEmail(email);
            return this;
        }

        public RecipientBuilder withWhatsapp(String whatsapp) {
            recipient.setWhatsapp(whatsapp);
            return this;
        }

        public RecipientBuilder withMessages(List<Message> messages) {
            recipient.setMessages(messages);
            return this;
        }

        public RecipientBuilder withUser(User user) {
            recipient.setUser(user);
            return this;
        }

        public RecipientBuilder but() {
            return aRecipient().withId(recipient.getId()).withName(recipient.getName()).withEmail(recipient.getEmail()).withWhatsapp(recipient.getWhatsapp()).withMessages(recipient.getMessages()).withUser(recipient.getUser());
        }

        public Recipient build() {
            return recipient;
        }
    }
}
