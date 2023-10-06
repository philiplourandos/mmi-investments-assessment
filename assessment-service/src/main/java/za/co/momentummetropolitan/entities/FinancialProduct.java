package za.co.momentummetropolitan.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;

@Table("financial_products")
public class FinancialProduct {
    @Id
    private Long id;
    private String name;
    @Column("type")
    private FinancialProductsEnum productType;

    public FinancialProduct() {
    }

    public FinancialProduct(Long id, String name, FinancialProductsEnum productType) {
        this.id = id;
        this.name = name;
        this.productType = productType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FinancialProductsEnum getProductType() {
        return productType;
    }

    public void setProductType(FinancialProductsEnum productType) {
        this.productType = productType;
    }
}
