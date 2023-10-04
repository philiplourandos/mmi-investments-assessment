package za.co.momentummetropolitan.entities;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("client_products")
public class ClientProduct {
    @Id
    private Long id;
    
    @Column("client_id")
    private Long clientId;
    @Column("financial_product_id")
    private Long financialProductId;
    private BigDecimal balance;

    public ClientProduct() {
    }

    public ClientProduct(Long id, Long clientId, Long financialProductId, BigDecimal balance) {
        this.id = id;
        this.clientId = clientId;
        this.financialProductId = financialProductId;
        this.balance = balance;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getFinancialProductId() {
        return financialProductId;
    }

    public void setFinancialProductId(Long financialProductId) {
        this.financialProductId = financialProductId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
