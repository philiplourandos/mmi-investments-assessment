package za.co.momentummetropolitan.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import za.co.momentummetropolitan.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
