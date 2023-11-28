package br.com.solverapps.depoisdoceu.data.repository;

import br.com.solverapps.depoisdoceu.data.model.Message;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppDAO {

    @Autowired
    EntityManager entityManager;

    public List<Message> getAllMessagesByUserId(int userId) {
        String query = """
                select m from Message m 
                inner join m.user u 
                where u.id = :id 
                """;

        return entityManager.createQuery(query, Message.class)
                .setParameter("id", userId)
                .getResultList();
    }
}
