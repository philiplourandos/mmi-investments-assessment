package za.co.momentummetropolitan.exceptions;

import java.time.LocalDate;

public class RetirementAgeNotAttainedException extends RuntimeException {
    private final long yearsDifference;
    private final LocalDate dateOfBirth;

    public RetirementAgeNotAttainedException(long yearsDifference, LocalDate dateOfBirth) {
        this.yearsDifference = yearsDifference;
        this.dateOfBirth = dateOfBirth;
    }

    public long getYearsDifference() {
        return yearsDifference;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
