package my.jmangeruga.recipes.adapter.rest;

import my.jmangeruga.recipes.domain.DomainValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
final class ErrorHandling {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandling.class);

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<SimpleErrorRs> handle(DomainValidationException exception) {
        LOGGER.error("Domain validation problem:", exception);
        return ResponseEntity.badRequest().body(
            new SimpleErrorRs("Incorrect input", exception.getMessage())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<SingleMessageRs> handle(AccessDeniedException exception) {
        LOGGER.error("Improper access to resources:", exception);
        throw exception;
    }

}
