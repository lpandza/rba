package com.lpandza.rba.request;

import com.lpandza.rba.model.enums.CardStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "OIB is required") @Size(min = 11, max = 11, message = "OIB must be exactly 11 characters")
        String oib,

        @NotNull(message = "Card status is required")
        CardStatus cardStatus
) {
}
