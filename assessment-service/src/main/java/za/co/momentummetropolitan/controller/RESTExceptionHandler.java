package za.co.momentummetropolitan.controller;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import za.co.momentummetropolitan.exceptions.ClientIdlNotFoundException;
import za.co.momentummetropolitan.exceptions.ClientProductIdNotFoundException;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientIdlNotFoundException.class})
    public ProblemDetail handleClientIdNotFound(final ClientIdlNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
                format("Id: [%d] does not exsit", exception.getId()));
    }

    @ExceptionHandler({ClientProductIdNotFoundException.class})
    public ProblemDetail handleClientProductIdNotFound(final ClientProductIdNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                format("Client Product Id: [%d] does not exist", exception.getClientProductId()));
    }
}
