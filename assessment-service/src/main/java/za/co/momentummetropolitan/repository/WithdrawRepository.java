package za.co.momentummetropolitan.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import za.co.momentummetropolitan.entities.Withdraw;

public interface WithdrawRepository extends CrudRepository<Withdraw, Long> {
    Optional<Withdraw> findByClientProductId(Long clientProductId);
}
