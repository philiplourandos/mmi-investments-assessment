package za.co.momentummetropolitan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import za.co.momentummetropolitan.exceptions.ClientIdlNotFoundException;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientIdlNotFoundException.class})
    public ProblemDetail handleEmailNotFound(final ClientIdlNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
                String.format("Id: %d does not exsit", exception.getId()));
    }
}
