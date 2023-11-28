package br.com.solverapps.depoisdoceu.data.repository;

import br.com.solverapps.depoisdoceu.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findUserByLogin(String login);
}
