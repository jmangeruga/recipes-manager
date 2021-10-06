package my.jmangeruga.recipes.domain;

import java.util.List;
import java.util.function.Consumer;

final class SimpleDomainValidation {

    private SimpleDomainValidation() {
    }

    static <T> T validated(Consumer<T> rule, T elementToValidate) {
        rule.accept(elementToValidate);
        return elementToValidate;
    }

    static <T> List<T> validated(Consumer<List<?>> rule, List<T> elementToValidate) {
        rule.accept(elementToValidate);
        return elementToValidate;
    }

    static Consumer<List<?>> notNullEmptyList(String exceptionMessage) {
        return list -> {
            if (list == null || list.isEmpty()) {
                throw new DomainValidationException(exceptionMessage);
            }
        };
    }

    static Consumer<String> notNullOrEmpty(String exceptionMessage) {
        return string -> {
            if (string == null || string.isEmpty()) {
                throw new DomainValidationException(exceptionMessage);
            }
        };
    }

    static <T> Consumer<T> notNull(String exceptionMessage) {
        return object -> {
            if (object == null) {
                throw new DomainValidationException(exceptionMessage);
            }
        };
    }

    static Consumer<String> withMaxSize(int maxExpectedSize, String exceptionMessage) {
        return string -> {
            if (string.length() > maxExpectedSize) {
                throw new DomainValidationException(exceptionMessage);
            }
        };
    }

    static Consumer<Short> withMinValue(short minValue, String exceptionMessage) {
        return integer -> {
            if (integer < minValue) {
                throw new DomainValidationException(exceptionMessage);
            }
        };
    }

}
