package pl.janekosa.nn.homework.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.janekosa.nn.homework.exception.BadRequestException;
import pl.janekosa.nn.homework.exception.NotFoundException;
import pl.janekosa.nn.homework.exception.TechnicalException;


@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    public ErrorHandler() {
        super();
    }

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<Object> handleBusinessException(final RuntimeException ex, final WebRequest request) {
        ErrorResponse responseError = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage());

        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {TechnicalException.class})
    protected ResponseEntity<Object> handleTechnicalException(final RuntimeException ex, final WebRequest request) {
        ErrorResponse responseError = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage());

        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        ErrorResponse responseError = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage());

        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
