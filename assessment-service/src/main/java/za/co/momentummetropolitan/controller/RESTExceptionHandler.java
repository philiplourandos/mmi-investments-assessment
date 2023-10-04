package za.co.momentummetropolitan.controller;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import za.co.momentummetropolitan.exceptions.ClientIdlNotFoundException;
import za.co.momentummetropolitan.exceptions.ClientProductIdNotFoundException;
import za.co.momentummetropolitan.exceptions.RetirementAgeNotAttainedException;
import za.co.momentummetropolitan.exceptions.WithdrawAmountExceedsBalanceException;
import za.co.momentummetropolitan.exceptions.WithdrawPercentageExceedsThresholdException;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientIdlNotFoundException.class})
    public ProblemDetail handleClientIdNotFound(final ClientIdlNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
                format("Id: [%d] does not exsit", exception.getId()));
    }

    @ExceptionHandler({ClientProductIdNotFoundException.class})
    public ProblemDetail handleClientProductIdNotFound(
            final ClientProductIdNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                format("Client Product Id: [%d] does not exist",
                        exception.getClientProductId()));
    }

    @ExceptionHandler({RetirementAgeNotAttainedException.class})
    public ProblemDetail handleNotAtRetirementAge(
            final RetirementAgeNotAttainedException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, 
                format("CLient's birth date is: [%s], they are [%d] years before retirement age.",
                        exception.getDateOfBirth(), exception.getYearsDifference()));
    }

    @ExceptionHandler({WithdrawAmountExceedsBalanceException.class})
    public ProblemDetail handleWithDrawExceedsBalance(
            final WithdrawAmountExceedsBalanceException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                format("Client's product balance is: [%d] and the withdraw amount: [%d] exceeds it.",
                        exception.getProductBalance(), exception.getWithdrawAmount()));
    }

    @ExceptionHandler({WithdrawPercentageExceedsThresholdException.class})
    public ProblemDetail handlePercentageWithdrawExceeded(
            final WithdrawPercentageExceedsThresholdException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, 
                format("Max withdraw percentage is: [%d]. The amount requested to be withdrawn: [%d] exceeds it.",
                        exception.getMaxWithdrawPercentage(), exception.getRequestedWithDrawPercentage()));
    }
}
