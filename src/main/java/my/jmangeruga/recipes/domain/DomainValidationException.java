package my.jmangeruga.recipes.domain;

public class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }

}
