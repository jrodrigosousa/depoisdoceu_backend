package br.com.solverapps.depoisdoceu.data.repository;

import br.com.solverapps.depoisdoceu.data.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepository extends JpaRepository<Recipient, Integer> {
}
