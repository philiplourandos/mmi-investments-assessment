package za.co.momentummetropolitan.dto;

import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public record WithdrawFundsRequest(@NotEmpty Long clientProductId, @NotEmpty BigDecimal amount) {
}
