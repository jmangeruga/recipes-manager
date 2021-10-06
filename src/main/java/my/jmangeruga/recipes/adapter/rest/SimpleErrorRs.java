package my.jmangeruga.recipes.adapter.rest;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class SimpleErrorRs {
    private final String message;
    private final String description;

    SimpleErrorRs(String message, String description) {
        this.message = Objects.requireNonNull(message);
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    Map<String, Object> asMap() {
        return Stream.of(
                new AbstractMap.SimpleEntry<>("message", message),
                new AbstractMap.SimpleEntry<>("description", description)
            ).filter(entry -> entry.getValue() != null)
            .collect(Collectors.toUnmodifiableMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

}
