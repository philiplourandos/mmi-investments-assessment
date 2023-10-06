package za.co.momentummetropolitan.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import za.co.momentummetropolitan.enums.WithdrawStatusEnum;

@Table("withdraw_audit_tracking")
public class WithdrawAuditTracking {
    @Id
    public Long id;

    @Column("withdraw_id")
    private Long withdrawId;

    @Column("status")
    private WithdrawStatusEnum withdrawStatus;

    @Column("event_created")
    private LocalDateTime eventCreated;
    
    @Column("previous_balance")
    private BigDecimal previousBalance;

    public WithdrawAuditTracking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(Long withdrawId) {
        this.withdrawId = withdrawId;
    }

    public WithdrawStatusEnum getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(WithdrawStatusEnum withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public LocalDateTime getEventCreated() {
        return eventCreated;
    }

    public void setEventCreated(LocalDateTime eventCreated) {
        this.eventCreated = eventCreated;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }
}
