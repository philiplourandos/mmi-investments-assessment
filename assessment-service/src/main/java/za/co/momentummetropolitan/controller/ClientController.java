package za.co.momentummetropolitan.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.momentummetropolitan.dto.ClientFinancialProduct;
import za.co.momentummetropolitan.dto.ClientInfoResponse;
import za.co.momentummetropolitan.dto.WithdrawFundsRequest;
import za.co.momentummetropolitan.service.ClientService;
import za.co.momentummetropolitan.service.WithdrawlService;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final WithdrawlService withdrawService;

    public ClientController(final ClientService clientService,
            final WithdrawlService withdrawService) {
        this.clientService = clientService;
        this.withdrawService = withdrawService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientInfoResponse> getClientInfo(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(clientService.retrieveClientInfo(id));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getClientInvestments(@PathVariable("id") final Long id) {
        final List<ClientFinancialProduct> clientProducts = clientService.retrieveClientProducts(id);

        if (clientProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clientProducts);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity withdrawFunds(@Validated @RequestBody final WithdrawFundsRequest request) {
        withdrawService.withdrawl(request.clientProductId(), request.amount());

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
