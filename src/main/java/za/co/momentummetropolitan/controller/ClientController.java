package za.co.momentummetropolitan.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.momentummetropolitan.dto.ClientFinancialProducts;
import za.co.momentummetropolitan.dto.ClientInfoResponse;
import za.co.momentummetropolitan.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientInfoResponse> getClientInfo(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(clientService.retrieveClientInfo(id));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getClientInvestments(@PathVariable("id") final Long id) {
        final List<ClientFinancialProducts> clientProducts = clientService.retrieveClientProducts(id);

        if (clientProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clientProducts);
        }
    }
}
