package za.co.momentummetropolitan.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.momentummetropolitan.dto.ClientInfoResponse;
import za.co.momentummetropolitan.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<ClientInfoResponse> getClientInfo(@NotBlank @PathVariable("email") final String email) {
        return ResponseEntity.ok(clientService.retrieveClientInfo(email));
    }
}
