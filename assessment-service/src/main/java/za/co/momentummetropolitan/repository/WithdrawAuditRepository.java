package za.co.momentummetropolitan.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import za.co.momentummetropolitan.entities.WithdrawAuditTracking;

public interface WithdrawAuditRepository extends CrudRepository<WithdrawAuditTracking, Long> {
    List<WithdrawAuditTracking> findByWithdrawIdOrderByEventCreatedAsc(Long withdrawId);
}
