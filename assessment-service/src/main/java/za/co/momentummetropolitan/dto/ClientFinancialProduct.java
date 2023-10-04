package za.co.momentummetropolitan.dto;

import java.math.BigDecimal;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;

public record ClientFinancialProduct(Long id, String productName,
        FinancialProductsEnum type, BigDecimal balance) {

}
