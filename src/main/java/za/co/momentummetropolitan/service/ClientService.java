package za.co.momentummetropolitan.service;

import org.springframework.stereotype.Service;
import za.co.momentummetropolitan.dto.ClientInfoResponse;
import za.co.momentummetropolitan.exceptions.ClientIdlNotFoundException;
import za.co.momentummetropolitan.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepo;

    public ClientService(final ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public ClientInfoResponse retrieveClientInfo(final Long id) {
        return clientRepo.findById(id)
                .map(m -> new ClientInfoResponse(m.getClientName(), m.getAddress(), 
                        m.getEmail(), m.getMobileNumber(), m.getDateOfBirth()))
                .orElseThrow(() -> new ClientIdlNotFoundException(id));
    }
}
