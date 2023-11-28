package br.com.solverapps.depoisdoceu.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecipientDTO(@NotNull Integer id, @NotBlank String name, String email, String whatsapp) {
}
