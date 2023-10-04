package za.co.momentummetropolitan.dto;

import java.math.BigDecimal;

public record WithdrawFundsRequest(Long clientProductId, BigDecimal amount) {
}
