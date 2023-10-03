package za.co.momentummetropolitan.service;

import org.springframework.stereotype.Service;
import za.co.momentummetropolitan.dto.ClientInfoResponse;
import za.co.momentummetropolitan.exceptions.EmailNotFoundException;
import za.co.momentummetropolitan.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepo;

    public ClientService(final ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public ClientInfoResponse retrieveClientInfo(final String email) {
        return clientRepo.findByEmail(email)
                .map(m -> new ClientInfoResponse(m.getClientName(), m.getAddress(), 
                        email, m.getMobileNumber(), m.getDateOfBirth()))
                .orElseThrow(() -> new EmailNotFoundException(email));
    }
}
