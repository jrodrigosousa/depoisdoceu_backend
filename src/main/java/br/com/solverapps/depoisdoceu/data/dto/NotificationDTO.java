package br.com.solverapps.depoisdoceu.data.dto;

import jakarta.validation.constraints.NotNull;

public record NotificationDTO(@NotNull Integer antecedence, String date) {
}
