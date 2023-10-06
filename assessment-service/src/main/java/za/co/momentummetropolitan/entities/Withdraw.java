package za.co.momentummetropolitan.entities;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import za.co.momentummetropolitan.enums.WithdrawStatusEnum;

@Table("withdraw")
public class Withdraw {
    @Id
    private Long id;
    
    @Column("client_product_id")
    private Long clientProductId;

    private WithdrawStatusEnum status;

    private BigDecimal amount;

    public Withdraw() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientProductId() {
        return clientProductId;
    }

    public void setClientProductId(Long clientProductId) {
        this.clientProductId = clientProductId;
    }

    public WithdrawStatusEnum getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatusEnum status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
