package br.com.solverapps.depoisdoceu.data.dto;

import br.com.solverapps.depoisdoceu.data.model.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record MessageDTO(@NotNull
                         Integer id,
                         @NotBlank String title,
                         String text,
                         @NotNull LocalDateTime toSendDate,
                         @NotNull Integer toSendDelayInHours,
                         @NotNull Boolean active,
                         LocalDateTime sentDate,
                         Boolean alreadySent,
                         List<Integer> recipientIds,
                         List<NotificationDTO> notifications) {


}
