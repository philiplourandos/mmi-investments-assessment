package za.co.momentummetropolitan.exceptions;

public class ClientProductIdNotFoundException extends RuntimeException {
    private final Long clientProductId;

    public ClientProductIdNotFoundException(final Long clientProductId) {
        this.clientProductId = clientProductId;
    }

    public Long getClientProductId() {
        return clientProductId;
    }
}
