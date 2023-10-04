package za.co.momentummetropolitan.exceptions;

import java.math.BigDecimal;

public class WithdrawPercentageExceedsThresholdException extends RuntimeException {
    private final BigDecimal maxWithdrawPercentage;
    private final BigDecimal requestedWithDrawPercentage;

    public WithdrawPercentageExceedsThresholdException(BigDecimal maxWithdrawPercentage, BigDecimal requestedWithDrawPercentage) {
        this.maxWithdrawPercentage = maxWithdrawPercentage;
        this.requestedWithDrawPercentage = requestedWithDrawPercentage;
    }

    public BigDecimal getMaxWithdrawPercentage() {
        return maxWithdrawPercentage;
    }

    public BigDecimal getRequestedWithDrawPercentage() {
        return requestedWithDrawPercentage;
    }
}
