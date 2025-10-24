package at.fhtw.paperlessrest.presentation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { IllegalArgumentException.class, NullPointerException.class })
    public ResponseEntity<Object> returnBadArgumentResponse(
            RuntimeException ex, WebRequest request) {
        log.warn("Encountered validation exception while processing request:"
                .concat(" Type: {} - Msg: {}"), ex.getClass().getSimpleName(), ex.getMessage());

        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
}
