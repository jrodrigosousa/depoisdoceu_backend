package br.com.solverapps.depoisdoceu.data.repository;

import br.com.solverapps.depoisdoceu.data.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
