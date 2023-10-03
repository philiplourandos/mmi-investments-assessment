package za.co.momentummetropolitan.exceptions;

public class EmailNotFoundException extends RuntimeException {

    private final String email;
    
    public EmailNotFoundException(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
