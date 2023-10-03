package za.co.momentummetropolitan.exceptions;

public class ClientIdlNotFoundException extends RuntimeException {

    private final Long id;

    public ClientIdlNotFoundException(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
