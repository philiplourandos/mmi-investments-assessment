package za.co.momentummetropolitan.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record WithdrawFundsRequest(@NotNull Long clientProductId, @NotNull BigDecimal amount) {
}
