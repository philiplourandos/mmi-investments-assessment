package za.co.momentummetropolitan.exceptions;

import java.math.BigDecimal;

public class WithdrawAmountExceedsBalanceException extends RuntimeException {
    private final BigDecimal productBalance;
    private final BigDecimal withdrawAmount;

    public WithdrawAmountExceedsBalanceException(BigDecimal productBalance, BigDecimal withdrawAmount) {
        this.productBalance = productBalance;
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getProductBalance() {
        return productBalance;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }
}
