package br.com.solverapps.depoisdoceu.data.dto;

import br.com.solverapps.depoisdoceu.constants.Role;

public record RegisterDTO(String name, String login, String password, String email, Role role) {
}
