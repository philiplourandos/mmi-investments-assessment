package za.co.momentummetropolitan.repository;

import org.springframework.data.repository.CrudRepository;
import za.co.momentummetropolitan.entities.WithdrawAuditTracking;

public interface WithdrawAuditRepository extends CrudRepository<WithdrawAuditTracking, Long> {
    
}
