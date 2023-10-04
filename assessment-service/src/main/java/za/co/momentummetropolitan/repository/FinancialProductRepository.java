package za.co.momentummetropolitan.repository;

import org.springframework.data.repository.CrudRepository;
import za.co.momentummetropolitan.entities.FinancialProduct;

public interface FinancialProductRepository extends CrudRepository<FinancialProduct, Long> {
    
}
