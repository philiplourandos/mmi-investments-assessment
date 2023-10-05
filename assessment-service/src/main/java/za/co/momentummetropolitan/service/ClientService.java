package za.co.momentummetropolitan.service;

import java.util.List;
import org.springframework.stereotype.Service;
import za.co.momentummetropolitan.dto.ClientFinancialProduct;
import za.co.momentummetropolitan.dto.ClientInfoResponse;
import za.co.momentummetropolitan.exceptions.ClientIdlNotFoundException;
import za.co.momentummetropolitan.repository.ClientFinancialProductRepository;
import za.co.momentummetropolitan.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepo;
    private final ClientFinancialProductRepository clientProductsRepo;

    public ClientService(final ClientRepository clientRepo, final ClientFinancialProductRepository clientProductsRepo) {
        this.clientRepo = clientRepo;
        this.clientProductsRepo = clientProductsRepo;
    }

    public ClientInfoResponse retrieveClientInfo(final Long clientId) {
        return clientRepo.findById(clientId)
                .map(m -> new ClientInfoResponse(m.getClientName(), m.getAddress(), 
                        m.getEmail(), m.getMobileNumber(), m.getDateOfBirth()))
                .orElseThrow(() -> new ClientIdlNotFoundException(clientId));
    }

    public List<ClientFinancialProduct> retrieveClientProducts(final Long clientId) {
        clientRepo.findById(clientId).orElseThrow(() -> new ClientIdlNotFoundException(clientId));

        return clientProductsRepo.findClientProductsByClientId(clientId);
    }
}
