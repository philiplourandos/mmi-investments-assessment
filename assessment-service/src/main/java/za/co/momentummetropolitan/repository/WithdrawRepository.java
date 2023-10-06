package za.co.momentummetropolitan.repository;

import org.springframework.data.repository.CrudRepository;
import za.co.momentummetropolitan.entities.Withdraw;

public interface WithdrawRepository extends CrudRepository<Withdraw, Long> {
    
}
